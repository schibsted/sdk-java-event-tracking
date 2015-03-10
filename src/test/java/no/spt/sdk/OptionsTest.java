package no.spt.sdk;

import org.junit.Test;

public class OptionsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSetDataCollectorClientIdEmptyString() throws Exception {
        Options options = new Options.Builder("")
                .setDataCollectorUrl(Defaults.DATA_COLLECTOR_URL)
                .setCISUrl(Defaults.CIS_URL)
                .setMaxQueueSize(Defaults.MAX_QUEUE_SIZE)
                .setTimeout(Defaults.TIMEOUT)
                .setRetries(Defaults.RETRIES)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDataCollectorUrlEmptyString() throws Exception {
        Options options = new Options.Builder("abc123").setDataCollectorUrl("")
                .setCISUrl(Defaults.CIS_URL)
                .setMaxQueueSize(Defaults.MAX_QUEUE_SIZE)
                .setTimeout(Defaults.TIMEOUT)
                .setRetries(Defaults.RETRIES)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCISUrlEmptyString() throws Exception {
        Options options = new Options.Builder("abc123").setDataCollectorUrl(Defaults.DATA_COLLECTOR_URL)
                .setCISUrl("")
                .setMaxQueueSize(Defaults.MAX_QUEUE_SIZE)
                .setTimeout(Defaults.TIMEOUT)
                .setRetries(Defaults.RETRIES)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetErrorReportingUrlEmptyString() throws Exception {
        Options options = new Options.Builder("abc123").setDataCollectorUrl(Defaults.DATA_COLLECTOR_URL)
                .setErrorReportingUrl("")
                .setMaxQueueSize(Defaults.MAX_QUEUE_SIZE)
                .setTimeout(Defaults.TIMEOUT)
                .setRetries(Defaults.RETRIES)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxQueueSizeToNegativeValue() throws Exception {
        Options options = new Options.Builder("abc123").setDataCollectorUrl(Defaults.DATA_COLLECTOR_URL)
                .setCISUrl(Defaults.CIS_URL)
                .setMaxQueueSize(-1)
                .setTimeout(Defaults.TIMEOUT)
                .setRetries(Defaults.RETRIES)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeoutToNegativeValue() throws Exception {
        Options options = new Options.Builder("abc123").setDataCollectorUrl(Defaults.DATA_COLLECTOR_URL)
                .setCISUrl(Defaults.CIS_URL)
                .setMaxQueueSize(Defaults.MAX_QUEUE_SIZE)
                .setTimeout(-1)
                .setRetries(Defaults.RETRIES)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRetriesToNegativeValue() throws Exception {
        Options options = new Options.Builder("abc123").setDataCollectorUrl(Defaults.DATA_COLLECTOR_URL)
                .setCISUrl(Defaults.CIS_URL)
                .setMaxQueueSize(Defaults.MAX_QUEUE_SIZE)
                .setTimeout(Defaults.TIMEOUT)
                .setRetries(-1)
                .build();
    }
}