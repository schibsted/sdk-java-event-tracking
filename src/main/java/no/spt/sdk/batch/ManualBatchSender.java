package no.spt.sdk.batch;


import no.spt.sdk.connection.DataCollectorConnector;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ManualBatchSender implements ISender {

    private LinkedBlockingQueue<Activity> activityQueue;

    private DataCollectorConnector client;

    private Options options;

    public ManualBatchSender(Options options, DataCollectorConnector client) {
        this.client = client;
        this.activityQueue = new LinkedBlockingQueue<Activity>();
        this.options = options;
    }

    @Override
    public void flush() throws DataTrackingException {
        do {
            List<Activity> current = new LinkedList<Activity>();
            do {
                if (activityQueue.size() == 0) {
                    break;
                }

                Activity activity = null;
                try {
                    activity = activityQueue.poll(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new DataTrackingException(e);
                }

                if (activity != null) {
                    current.add(activity);
                }
            } while (activityQueue.size() > 0 && current.size() < options.getMaxBatchSize());
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
            } while (!success && retryCount < options.getRetries());

            if (!success) {
                throw new DataTrackingException("Unable to send batch. Giving up on this batch.");
            }
        } while (activityQueue.size() > 0);
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
        flush();
        client.close();
    }

    @Override
    public int getQueueDepth() {
        return activityQueue.size();
    }

    @Override
    public void init() {
        // TODO
    }

}
