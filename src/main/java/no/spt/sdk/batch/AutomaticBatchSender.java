package no.spt.sdk.batch;

import no.spt.sdk.connection.DataCollectorConnector;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AutomaticBatchSender implements Runnable, ISender {

    private LinkedBlockingQueue<Activity> activityQueue;

    private volatile boolean shouldSend;

    private DataCollectorConnector client;

    private volatile CountDownLatch latch;

    private Options options;

    private ErrorCollector errorCollector;

    private final Thread thread;

    private final Integer mutex;

    public AutomaticBatchSender(Options options, DataCollectorConnector client, ErrorCollector errorCollector) {
        this.client = client;
        this.errorCollector = errorCollector;
        this.activityQueue = new LinkedBlockingQueue<Activity>();
        this.shouldSend = true;
        this.latch = new CountDownLatch(0);
        this.options = options;
        this.thread = new Thread(this);
        this.mutex = new Integer(-1);
    }

    @Override
    public void init() {
        thread.start();
    }

    @Override
    public void run() {
        while (shouldSend) {
            List<Activity> current = new LinkedList<Activity>();

            do {
                if (activityQueue.size() == 0) {
                    latch.countDown();
                }

                Activity activity = null;
                try {
                    activity = activityQueue.poll(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    errorCollector.collect(new DataTrackingException("Interrupted while trying to flush activity queue.", e));
                }

                if (activity != null) {
                    synchronized (mutex) {
                        if (latch.getCount() == 0) {
                            latch = new CountDownLatch(1);
                        }
                    }
                    current.add(activity);
                }
            }
            while (shouldSend && activityQueue.size() > 0 && current.size() < options.getMaxBatchSize());

            try {
                sendBatch(current);
            } catch (DataTrackingException e) {
                errorCollector.collect(e);
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                // Interrupted while sleeping flushing thread
            }
        }
    }

    private void sendBatch(List<Activity> current) throws DataTrackingException {
        boolean success = true;
        int retryCount = 0;
        do {
            try {
                if (current.size() > 0) {
                    client.send(current);
                    current = new LinkedList<Activity>();
                }
                success = true;
            } catch (IOException e) {
                retryCount++;
                success = false;
            }
        }
        while (!success && retryCount < options.getRetries());

        if (!success) {
            throw new DataTrackingException("Unable to send batch. Giving up on this batch.");
        }

    }

    @Override
    public void flush() throws DataTrackingException {
        try {
            latch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new DataTrackingException(e);
        }
    }

    @Override
    public void enqueue(Activity activity) throws DataTrackingException {
        int maxQueueSize = options.getMaxQueueSize();
        int currentQueueSize = activityQueue.size();

        if (currentQueueSize <= maxQueueSize) {
            this.activityQueue.add(activity);
        } else {
            throw new DataTrackingException("Queue has reached maxSize, dropping activity.");
        }
    }

    @Override
    public void close() throws DataTrackingException {
        shouldSend = false;
        activityQueue.clear();
        client.close();
    }

    @Override
    public int getQueueDepth() {
        return activityQueue.size();
    }

}
