package no.spt.sdk;

/**
 * Options contains settings used to configure the behaviour of the data collector SDK
 */
public class Options {

    private final String dataCollectorUrl;
    private final String anonymousIdUrl;
    private final int maxQueueSize;
    private final int timeout;
    private final int retries;

    /**
     * Constructs an Option with the provided settings
     *
     * @param dataCollectorUrl the url to the data collector endpoint
     * @param anonymousIdUrl   the url to the anonymous identity service endpoint
     * @param maxQueueSize     the maximum size of the activity queue waiting to be sent to the data collector
     * @param timeout          the amount of milliseconds before a request is marked as timed out
     * @param retries          the amount of times to retry the request
     */
    public Options(String dataCollectorUrl, String anonymousIdUrl, int maxQueueSize, int timeout, int retries) {
        this.dataCollectorUrl = validateDataCollectorUrl(dataCollectorUrl);
        this.anonymousIdUrl = validateAnonymousIdUrl(anonymousIdUrl);
        this.maxQueueSize =validateMaxQueueSize(maxQueueSize);
        this.timeout = validateTimeout(timeout);
        this.retries = validateRetries(retries);
    }

    /**
     * Gets the data collector endpoint
     * @return The data collector endpoint
     */
    public String getDataCollectorUrl() {
        return dataCollectorUrl;
    }

    private String validateDataCollectorUrl(String dataCollectorUrl) {
        if(dataCollectorUrl == null || dataCollectorUrl.equals("")) {
            throw new IllegalArgumentException("Data-collector-sdk#options#dataCollectorUrl must be a valid url.");
        }
        return dataCollectorUrl;
    }

    /**
     * Gets the anonymous identity service endpoint
     * @return The anonymous identity service endpoint
     */
    public String getAnonymousIdUrl() {
        return anonymousIdUrl;
    }

    private String validateAnonymousIdUrl(String anonymousIdUrl) {
        if(anonymousIdUrl == null || anonymousIdUrl.equals("")) {
            throw new IllegalArgumentException("Data-collector-sdk#options#anonymousIdUrl must be a valid url.");
        }
        return anonymousIdUrl;
    }

    /**
     * Gets the maximum size of the activity queue waiting to be sent to the data collector.
     * If the queue has reached the maximum size it will no longer accept new activities and those will be dropped.
     * @return The maximum size of the activity queue waiting to be sent to the data collector
     */
    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    private int validateMaxQueueSize(int maxQueueSize) {
        if(maxQueueSize < 1) {
            throw new IllegalArgumentException("Data-collector-sdk#options#maxQueueSize must be greater than 0.");
        }
        return maxQueueSize;
    }

    /**
     * Gets the amount of milliseconds before an HTTP request is marked as timed out
     * @return The amount of milliseconds before an HTTP request is marked as timed out
     */
    public int getTimeout() {
        return timeout;
    }

    private int validateTimeout(int timeout) {
        if(timeout < 500) {
            throw new IllegalArgumentException("Data-collector-sdk#options#timeout must be at least 500 milliseconds.");
        }
        return timeout;
    }

    /**
     * Gets the amount of times to retry an HTTP request
     * @return The amount of times to retry an HTTP request
     */
    public int getRetries() {
        return retries;
    }

    private int validateRetries(int retries) {
        if(retries < 0) {
            throw new IllegalArgumentException("Data-collector-sdk#options#retries must be greater or equal to 0.");
        }
        return retries;
    }

}
