package no.spt.sdk.models;

/**
 * Options contains settings used to configure the behaviour of the data collector SDK
 */
public class Options {

    private String dataCollectorUrl;
    private int maxQueueSize;
    private int maxBatchSize;

    private int timeout;

    private int retries;

    private boolean sendAutomatic;

    /**
     * Constructs an Option with the provided settings
     *
     * @param dataCollectorUrl the url to the data collector endpoint
     * @param maxQueueSize the maximum size of the activity queue waiting to be sent to the data collector
     * @param maxBatchSize the maximum size of each batch of activities sent to the data collector
     * @param timeout the amount of milliseconds before a request is marked as timed out
     * @param retries the amount of times to retry the request
     * @param sendAutomatic specifies if the client should send activities automatically
     */
    public Options(final String dataCollectorUrl, final int maxQueueSize, final int maxBatchSize, final int timeout, final
    int retries, final boolean sendAutomatic) {
        setDataCollectorUrl(dataCollectorUrl);
        setMaxQueueSize(maxQueueSize);
        setMaxBatchSize(maxBatchSize);
        setTimeout(timeout);
        setRetries(retries);
        setSendAutomatic(sendAutomatic);
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

    public int getMaxBatchSize() {
        return maxBatchSize;
    }


    /**
     * Sets the maximum size of each batch of activities sent to the data collector
     * @param maxBatchSize
     */
    public void setMaxBatchSize(int maxBatchSize) {
        if(maxBatchSize < 1) {
            throw new IllegalArgumentException("Data-collector-sdk#options#maxBatchSize must be greater than 0.");
        }
        this.maxBatchSize = maxBatchSize;
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

    public boolean isSendAutomatic() {
        return sendAutomatic;
    }

    /**
     * Sets if the client should send activities automatically
     *
     * @param sendAutomatic
     */
    public void setSendAutomatic(boolean sendAutomatic) {
        this.sendAutomatic = sendAutomatic;
    }
}
