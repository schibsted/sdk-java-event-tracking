package no.spt.sdk;

/**
 * Options contains settings used to configure the behaviour of the data collector SDK
 */
public class Options {

    private String dataCollectorUrl;
    private String anonymousIdUrl;
    private int maxQueueSize;
    private int timeout;
    private int retries;

    /**
     * Constructs an Option with the provided settings
     *
     * @param dataCollectorUrl the url to the data collector endpoint
     * @param maxQueueSize     the maximum size of the activity queue waiting to be sent to the data collector
     * @param timeout          the amount of milliseconds before a request is marked as timed out
     * @param retries          the amount of times to retry the request
     */
    public Options(final String dataCollectorUrl, final String anonymousIdUrl, final int maxQueueSize, final int timeout, final int retries) {
        setDataCollectorUrl(dataCollectorUrl);
        setAnonymousIdUrl(anonymousIdUrl);
        setMaxQueueSize(maxQueueSize);
        setTimeout(timeout);
        setRetries(retries);
    }

    public String getDataCollectorUrl() {
        return dataCollectorUrl;
    }

    /**
     * Sets the data collector endpoint
     *
     * @param dataCollectorUrl
     */
    public void setDataCollectorUrl(String dataCollectorUrl) {
        if(dataCollectorUrl == null || dataCollectorUrl.equals("")) {
            throw new IllegalArgumentException("Data-collector-sdk#options#dataCollectorUrl must be a valid url.");
        }
        this.dataCollectorUrl = dataCollectorUrl;
    }

    public String getAnonymousIdUrl() {
        return anonymousIdUrl;
    }

    public void setAnonymousIdUrl(String anonymousIdUrl) {
        if(anonymousIdUrl == null || anonymousIdUrl.equals("")) {
            throw new IllegalArgumentException("Data-collector-sdk#options#anonymousIdUrl must be a valid url.");
        }
        this.anonymousIdUrl = anonymousIdUrl;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    /**
     * Sets the maximum size of the activity queue waiting to be sent to the data collector.
     * If the queue has reached the maximum size it will no longer accept new activities and those will be dropped.
     *
     * @param maxQueueSize
     */
    public void setMaxQueueSize(int maxQueueSize) {
        if(maxQueueSize < 1) {
            throw new IllegalArgumentException("Data-collector-sdk#options#maxQueueSize must be greater than 0.");
        }
        this.maxQueueSize = maxQueueSize;
    }

    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the amount of milliseconds before a request is marked as timed out
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        if(timeout < 500) {
            throw new IllegalArgumentException("Data-collector-sdk#options#timeout must be at least 500 milliseconds.");
        }
        this.timeout = timeout;
    }

    public int getRetries() {
        return retries;
    }

    /**
     * Sets the amount of times to retry the request
     *
     * @param retries
     */
    public void setRetries(int retries) {
        if(retries < 0) {
            throw new IllegalArgumentException("Data-collector-sdk#options#retries must be greater or equal to 0.");
        }
        this.retries = retries;
    }

}
