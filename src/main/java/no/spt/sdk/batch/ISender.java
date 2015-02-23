package no.spt.sdk.batch;

import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;

/**
 * A sender used to send activities to the data collector
 */
public interface ISender {

    /**
     * A method that sends all activities in the queue to the data collector
     *
     * @throws DataTrackingException
     */
    public void flush() throws DataTrackingException;

    /**
     * Enqueue an activity to be sent to the data collector.
     * If the queue is full, the activity will be dropped
     *
     * @param activity An activity to enqueue
     * @throws DataTrackingException If the queue has reached it's max size
     */
    public void enqueue(Activity activity) throws DataTrackingException;

    /**
     * Closes the sender
     *
     * @throws DataTrackingException If http client cannot be closed
     */
    public void close() throws DataTrackingException;

    /**
     * Returns the current queue depth
     *
     * @return The current queue depth
     */
    public int getQueueDepth();

    /**
     * Initializes the sender
     */
    public void init();
}
