package no.spt.sdk.batch;

import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;

/**
 * A sender used to send activities to the data collector
 */
public interface ISender {

    /**
     * A method that sends all activities in the queue to the data collector
     * @throws DataTrackingException
     */
    public void flush() throws DataTrackingException;

    /**
     * Enqueue an activity to be sent to the data collector.
     * If the queue is full, the activity will be dropped
     * @param activity an activity to enqueue
     * @throws DataTrackingException if the queue has reached it's max size
     */
    public void enqueue(Activity activity) throws DataTrackingException;

    /**
     * This method closes the sender
     * @throws DataTrackingException if http client cannot be closed
     */
    public void close() throws DataTrackingException;

    /**
     * This method returns the current queue depth
     * @return the current queue depth
     */
    public int getQueueDepth();

    /**
     * Initializes the sender
     */
    public void init();
}
