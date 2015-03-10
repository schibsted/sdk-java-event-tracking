package no.spt.sdk.batch;

import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.CommunicationDataTrackingException;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.exceptions.error.ActivitySendingError;
import no.spt.sdk.models.Activity;
import no.spt.sdk.serializers.ASJsonConverter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A sender that automatically sends activities from the queue to the data collector.
 * It sends batches of activities to the data collector on a separate thread.
 */
public class AutomaticBatchSender implements Runnable, Sender {

    private static final Object fileLock = new Object();
    private final Thread thread;
    private LinkedBlockingQueue<Activity> activityQueue;
    private volatile boolean shouldSend;
    private HttpConnection client;
    private volatile CountDownLatch latch;
    private Options options;
    private ErrorCollector errorCollector;
    private ASJsonConverter jsonConverter;

    /**
     * @param options        options used to configure the behaviour of the sender
     * @param client         an http client wrapper that handles http connections with data collector
     * @param errorCollector an error collector that collects all exceptions
     */
    public AutomaticBatchSender(Options options, HttpConnection client, ErrorCollector errorCollector,
                                ASJsonConverter jsonConverter) {
        this.client = client;
        this.errorCollector = errorCollector;
        this.activityQueue = new LinkedBlockingQueue<Activity>();
        this.shouldSend = true;
        this.latch = new CountDownLatch(0);
        this.options = options;
        this.thread = new Thread(this);
        this.jsonConverter = jsonConverter;
    }

    /**
     * Initializes the sender and starts the thread that is responsible for polling activities from the queue
     * and sending them to the data collector
     */
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
                    errorCollector.collect(new DataTrackingException("Interrupted while trying to flush activity " +
                            "queue.", e, ActivitySendingError.INTERRUPTED_ERROR));
                }

                if (activity != null) {
                    synchronized (fileLock) {
                        if (latch.getCount() == 0) {
                            latch = new CountDownLatch(1);
                        }
                    }
                    current.add(activity);
                }
            } while (shouldSend && activityQueue.size() > 0 && current.size() < options.getMaxActivityBatchSize());

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
                    DataTrackingPostRequest request = new DataTrackingPostRequest(options.getDataCollectorUrl(),
                            null, jsonConverter.serialize(current));
                    DataTrackingResponse response = client.send(request);
                    current = new LinkedList<Activity>();
                    if (response.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
                        throw new CommunicationDataTrackingException("Response from Data Collector was not OK",
                                response, request, ActivitySendingError.BAD_REQUEST);
                    } else if (response.getResponseCode() == HttpStatus.SC_MULTI_STATUS) {
                        throw new CommunicationDataTrackingException("Some of the activities could not be validated " +
                                "by Data Collector", response, request, ActivitySendingError.VALIDATION_ERROR);
                    } else if (response.getResponseCode() != HttpStatus.SC_OK) {
                        throw new CommunicationDataTrackingException("Unexpected response from Data Collector",
                                response, request, ActivitySendingError.UNEXPECTED_RESPONSE);
                    }
                }
                success = true;
            } catch (IOException e) {
                retryCount++;
                success = false;
            }
        } while (!success && retryCount <= options.getRetries());

        if (!success) {
            throw new DataTrackingException(String.format("Unable to send batch after %s tries. Giving up on this" +
                    " batch.", retryCount), ActivitySendingError.HTTP_CONNECTION_ERROR);
        }

    }

    /**
     * Sends all activities in the queue to the data collector.
     * Blocks until all activities are sent, maximum 2 minutes.
     *
     * @throws DataTrackingException If interrupted while waiting
     */
    @Override
    public void flush() throws DataTrackingException {
        try {
            latch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new DataTrackingException(e, ActivitySendingError.INTERRUPTED_ERROR);
        }
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
        if (activityQueue.size() <= options.getMaxQueueSize()) {
            synchronized (fileLock) {
                if (latch.getCount() == 0) {
                    latch = new CountDownLatch(1);
                }
                this.activityQueue.add(activity);
            }
        } else {
            throw new DataTrackingException("Queue has reached maxSize, dropping activity.", ActivitySendingError
                    .QUEUE_MAX_SIZE_REACHED);
        }
    }

    /**
     * This method closes the sender after flushing and clearing the queue.
     *
     * @throws DataTrackingException if http client cannot be closed
     */
    @Override
    public void close() throws DataTrackingException {
        flush();
        shouldSend = false;
        activityQueue.clear();
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

}
