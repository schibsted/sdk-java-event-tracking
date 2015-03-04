package no.spt.sdk.batch;


import no.spt.sdk.Constants;
import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.error.ActivitySendingError;
import no.spt.sdk.models.Activity;
import no.spt.sdk.serializers.ASJsonConverter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A sender that you manually have to flush to send activities from the queue to the data collector
 */
public class ManualBatchSender implements Sender {

    private LinkedBlockingQueue<Activity> activityQueue;
    private HttpConnection client;
    private Options options;
    private ASJsonConverter jsonConverter;

    /**
     * @param options options used to configure the behaviour of the sender
     * @param client  an http client wrapper that handles http connections with data collector
     */
    public ManualBatchSender(Options options, HttpConnection client, ASJsonConverter jsonConverter) {
        this.client = client;
        this.activityQueue = new LinkedBlockingQueue<Activity>();
        this.options = options;
        this.jsonConverter = jsonConverter;
    }

    /**
     * Sends all activities in the queue to the data collector.
     * Blocks until all activities are sent.
     *
     * @throws DataTrackingException If something goes wrong while sending activities
     */
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
                    throw new DataTrackingException(e, ActivitySendingError.INTERRUPTED_ERROR);
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
                        DataTrackingResponse response = client.send(new DataTrackingPostRequest(options
                                .getDataCollectorUrl(), null, jsonConverter.serialize(current)));
                        current = new LinkedList<Activity>();
                        if (response.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
                            throw new DataTrackingException("Response from Data Collector was not OK", response,
                                    ActivitySendingError.BAD_REQUEST);
                        } else if (response.getResponseCode() == HttpStatus.SC_MULTI_STATUS) {
                            throw new DataTrackingException("Some of the activities could not be validated by Data " +
                                    "Collector", response, ActivitySendingError.VALIDATION_ERROR);
                        } else if (response.getResponseCode() != HttpStatus.SC_OK) {
                            throw new DataTrackingException("Unexpected response from Data Collector", response,
                                    ActivitySendingError.UNEXPECTED_RESPONSE);
                        }
                    }
                    success = true;
                } catch (IOException e) {
                    retryCount++;
                    success = false;
                }
            } while (!success && retryCount < options.getRetries());

            if (!success) {
                throw new DataTrackingException(String.format("Unable to send batch after %s tries. Giving up on " +
                        "this" + " batch.", retryCount), ActivitySendingError.HTTP_CONNECTION_ERROR);
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
            throw new DataTrackingException("Queue has reached maxSize, dropping activity.", ActivitySendingError
                    .QUEUE_MAX_SIZE_REACHED);
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
        // Nothing has to be initiated
    }

}
