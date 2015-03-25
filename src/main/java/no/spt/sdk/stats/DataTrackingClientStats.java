package no.spt.sdk.stats;

/**
 * DataTrackingClientStats keeps counters for events that occur in the DataTrackingClient
 */
public interface DataTrackingClientStats {

    /**
     * Returns the number of activities that has been added to the tracking queue
     *
     * @return The number of activities that has been added to the tracking queue
     */
    long getQueuedActivitiesCount();

    /**
     * Returns the number of batches that has been sent to the data collector
     *
     * @return The number of batches that has been sent to the data collector
     */
    long getSentBatchesCount();

    /**
     * Returns the number activities that has been successfully sent to the data collector
     *
     * @return The number activities that has been successfully sent to the data collector
     */
    long getSuccessfulCount();

    /**
     * Returns the number of activities that has been rejected by the data collector due to validation errors
     *
     * @return The number of activities that has been rejected by the data collector due to validation errors
     */
    long getValidationFailedCount();

    /**
     * Returns the number of activities that could not be sent to the data collector due to an error
     *
     * @return The number of activities that could not be sent to the data collector due to an error
     */
    long getSendingFailedCount();

    /**
     * Returns the number of activities that has been dropped because the activity queue was full
     *
     * @return The number of activities that has been dropped because the activity queue was full
     */
    long getDroppedCount();

    /**
     * Returns the number of error reports that has been sent to the error reporting service
     *
     * @return The number of error reports that has been sent to the error reporting service
     */
    long getErrorReportsCount();
}
