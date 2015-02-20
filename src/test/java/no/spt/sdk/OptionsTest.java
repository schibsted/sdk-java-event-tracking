package no.spt.sdk;

import org.junit.Test;

public class OptionsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSetDataCollectorUrlEmptyString() throws Exception {
        Options options = new Options("", Defaults.ANONYMOUS_ID_SERVICE_URL, Defaults.MAX_QUEUE_SIZE, Defaults.TIMEOUT, Defaults.RETRIES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAnonymousIdServiceUrlEmptyString() throws Exception {
        Options options = new Options(Defaults.DATA_COLLECTOR_URL, "", Defaults.MAX_QUEUE_SIZE, Defaults.TIMEOUT, Defaults.RETRIES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxQueueSizeToNegativeValue() throws Exception {
        Options options = new Options(Defaults.DATA_COLLECTOR_URL, Defaults.ANONYMOUS_ID_SERVICE_URL, -1, Defaults.TIMEOUT, Defaults.RETRIES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeoutToNegativeValue() throws Exception {
        Options options = new Options(Defaults.DATA_COLLECTOR_URL, Defaults.ANONYMOUS_ID_SERVICE_URL, Defaults.MAX_QUEUE_SIZE, -1, Defaults.RETRIES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRetriesToNegativeValue() throws Exception {
        Options options = new Options(Defaults.DATA_COLLECTOR_URL, Defaults.ANONYMOUS_ID_SERVICE_URL, Defaults.MAX_QUEUE_SIZE, Defaults.TIMEOUT, -1);
    }
}