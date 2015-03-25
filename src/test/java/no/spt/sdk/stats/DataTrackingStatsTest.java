package no.spt.sdk.stats;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataTrackingStatsTest {

    DataTrackingStats stats = new DataTrackingStats();
    private final long DELTA = 42;

    @Test
    public void testGetQueuedActivitiesCount() throws Exception {
        assertEquals(0, stats.getQueuedActivitiesCount());
    }

    @Test
    public void testIncrementQueuedActivities() throws Exception {
        stats.incrementQueuedActivities();
        assertEquals(1, stats.getQueuedActivitiesCount());
    }

    @Test
    public void testGetSentBatchesCount() throws Exception {
        assertEquals(0, stats.getSentBatchesCount());
    }

    @Test
    public void testIncrementSentBatches() throws Exception {
        stats.incrementSentBatches();
        assertEquals(1, stats.getSentBatchesCount());
    }

    @Test
    public void testGetSuccessfulCount() throws Exception {
        assertEquals(0, stats.getSuccessfulCount());
    }

    @Test
    public void testIncrementSuccessful() throws Exception {
        stats.incrementSuccessful();
        assertEquals(1, stats.getSuccessfulCount());
    }
    @Test
    public void testAddToSuccessful() throws Exception {
        stats.addToSuccessful(DELTA);
        assertEquals(DELTA, stats.getSuccessfulCount());
    }

    @Test
    public void testGetValidationFailedCount() throws Exception {
        assertEquals(0, stats.getValidationFailedCount());
    }

    @Test
    public void testIncrementValidationFailed() throws Exception {
        stats.incrementValidationFailed();
        assertEquals(1, stats.getValidationFailedCount());
    }

    @Test
    public void testAddToValidationFailed() throws Exception {
        stats.addToValidationFailed(DELTA);
        assertEquals(DELTA, stats.getValidationFailedCount());
    }

    @Test
    public void testGetSendingFailedCount() throws Exception {
        assertEquals(0, stats.getSendingFailedCount());
    }

    @Test
    public void testIncrementSendingFailed() throws Exception {
        stats.incrementSendingFailed();
        assertEquals(1, stats.getSendingFailedCount());
    }

    @Test
    public void testAddToSendingFailed() throws Exception {
        stats.addToSendingFailed(DELTA);
        assertEquals(DELTA, stats.getSendingFailedCount());
    }

    @Test
    public void testGetDroppedCount() throws Exception {
        assertEquals(0, stats.getDroppedCount());
    }

    @Test
    public void testIncrementDropped() throws Exception {
        stats.incrementDropped();
        assertEquals(1, stats.getDroppedCount());
    }

    @Test
    public void testAddToDropped() throws Exception {
        stats.addToDropped(DELTA);
        assertEquals(DELTA, stats.getDroppedCount());
    }

    @Test
    public void testGetErrorReportsCount() throws Exception {
        assertEquals(0, stats.getErrorReportsCount());
    }

    @Test
    public void testIncrementErrorReports() throws Exception {
        stats.incrementErrorReports();
        assertEquals(1, stats.getErrorReportsCount());
    }

    @Test
    public void testAddToErrorReports() throws Exception {
        stats.addToErrorReports(DELTA);
        assertEquals(DELTA, stats.getErrorReportsCount());
    }
}