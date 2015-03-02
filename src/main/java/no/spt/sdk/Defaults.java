package no.spt.sdk;

/**
 * Defaults contains default values that can be used in the SDK
 */
public class Defaults {

    /**
     * The URL to the data collector
     */
    public static final String DATA_COLLECTOR_URL = "http://localhost:8002/api/v1/track"; // TODO Correct URL

    /**
     * The URL to the anonymous id service
     */
    public static final String ANONYMOUS_ID_SERVICE_URL = "http://localhost:8003/api/v1/identify"; // TODO Correct URL

    /**
     * The URL to the error reports collector
     */
    public static final String ERROR_REPORTING_URL = "http://localhost:8085/api"; // TODO Correct URL

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
}
