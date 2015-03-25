package no.spt.sdk.stats;

import com.google.common.util.concurrent.AtomicLongMap;

/**
 * DataTrackingStats keeps counters of events that occur in the client
 */
public class DataTrackingStats implements DataTrackingClientStats {

    private static final String QUEUED_KEY = "queued";
    private static final String BATCHES_SENT_KEY = "batches_sent";
    private static final String SUCCESSFUL_KEY = "successful";
    private static final String VALIDATION_FAILED_KEY = "validation_failed";
    private static final String SENDING_FAILED_KEY = "sending_failed";
    private static final String DROPPED_KEY = "dropped";
    private static final String ERROR_REPORTS_SENT_KEY = "error_reports_sent";

    private final AtomicLongMap<String> counters = AtomicLongMap.create();

    /**
     * {@inheritDoc}
     */
    @Override
    public long getQueuedActivitiesCount() {
        return counters.get(QUEUED_KEY);
    }

    public void incrementQueuedActivities() {
        counters.incrementAndGet(QUEUED_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSentBatchesCount() {
        return counters.get(BATCHES_SENT_KEY);
    }

    public void incrementSentBatches() {
        counters.incrementAndGet(BATCHES_SENT_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuccessfulCount() {
        return counters.get(SUCCESSFUL_KEY);
    }

    public void incrementSuccessful() {
        counters.incrementAndGet(SUCCESSFUL_KEY);
    }

    public void addToSuccessful(long count) {
        counters.addAndGet(SUCCESSFUL_KEY, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getValidationFailedCount() {
        return counters.get(VALIDATION_FAILED_KEY);
    }

    public void incrementValidationFailed() {
        counters.incrementAndGet(VALIDATION_FAILED_KEY);
    }

    public void addToValidationFailed(long count) {
        counters.addAndGet(VALIDATION_FAILED_KEY, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSendingFailedCount() {
        return counters.get(SENDING_FAILED_KEY);
    }

    public void incrementSendingFailed() {
        counters.incrementAndGet(SENDING_FAILED_KEY);
    }

    public void addToSendingFailed(long count) {
        counters.addAndGet(SENDING_FAILED_KEY, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDroppedCount() {
        return counters.get(DROPPED_KEY);
    }

    public void incrementDropped() {
        counters.incrementAndGet(DROPPED_KEY);
    }

    public void addToDropped(long count) {
        counters.addAndGet(DROPPED_KEY, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getErrorReportsCount() {
        return counters.get(ERROR_REPORTS_SENT_KEY);
    }

    public void incrementErrorReports() {
        counters.incrementAndGet(ERROR_REPORTS_SENT_KEY);
    }

    public void addToErrorReports(long count) {
        counters.addAndGet(ERROR_REPORTS_SENT_KEY, count);
    }
}
