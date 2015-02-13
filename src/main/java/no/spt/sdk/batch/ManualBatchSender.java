package no.spt.sdk.batch;


import no.spt.sdk.Constants;
import no.spt.sdk.Options;
import no.spt.sdk.connection.IDataCollectorConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A sender that you manually have to flush to send activities from the queue to the data collector
 */
public class ManualBatchSender implements ISender {

    private LinkedBlockingQueue<Activity> activityQueue;
    private IDataCollectorConnection client;
    private Options options;

    /**
     * @param options options used to configure the behaviour of the sender
     * @param client  an http client wrapper that handles http connections with data collector
     */
    public ManualBatchSender(Options options, IDataCollectorConnection client) {
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
            } while (activityQueue.size() > 0 && current.size() < Constants.MAX_BATCH_SIZE);
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
                throw new DataTrackingException(String.format("Unable to send batch after %s tries. Giving up on this" +
                        " batch.", retryCount));
            }
        } while (activityQueue.size() > 0);
    }

    /**
     * Enqueue an activity to be sent to the data collector.
     * If the queue is full, the activity will be dropped
     *
     * @param activity an activity to enqueue
     * @throws DataTrackingException if the queue has reached it's max size
     */
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

    /**
     * This method flushes the queue and closes the sender.
     *
     * @throws DataTrackingException if http client cannot be closed
     */
    @Override
    public void close() throws DataTrackingException {
        flush();
        client.close();
    }

    /**
     * This method returns the current queue depth
     *
     * @return the current queue depth
     */
    @Override
    public int getQueueDepth() {
        return activityQueue.size();
    }

    @Override
    public void init() {
        // TODO
    }

}
