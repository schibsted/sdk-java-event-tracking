package no.spt.sdk;

/**
 * Defaults contains default values that can be used in the SDK
 */
public final class Defaults {

    private Defaults() {

    }

    /**
     * The URL to the data collector
     */
    public static final String DATA_COLLECTOR_URL = "http://localhost:8002/api/v1/track"; // TODO Correct URL

    /**
     * The URL to the CIS
     */
    public static final String CIS_URL = "http://localhost:8003/api/v1/identify"; // TODO Correct URL

    /**
     * The URL to the error reports collector
     */
    public static final String ERROR_REPORTING_URL = "http://localhost:8004/api/v1/error"; // TODO Correct URL

    /**
     * The maximum size of the activities queue
     */
    public static final int MAX_QUEUE_SIZE = 10000;

    /**
     * The timeout of a request to the data collector
     */
    public static final int TIMEOUT = 2000;

    /**
     * The number of times to retry a request to the data collector
     */
    public static final int RETRIES = 2;

    /**
     * The maximum size of a batch of activities
     */
    public static final int MAX_ACTIVITY_BATCH_SIZE = 20;

    /**
     * The maximum size of a batch of errors
     */
    public static final int MAX_ERROR_BATCH_SIZE = 5;
}
