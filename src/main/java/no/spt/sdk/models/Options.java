package no.spt.sdk.models;

public class Options {

    private static final String DEFAULT_DATA_COLLECTOR_URL = "http://localhost:8080/json";
    private static final int DEFAULT_MAX_QUEUE_SIZE = 10000;
    private static final int DEFAULT_MAX_BATCH_SIZE = 20;
    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int DEFAULT_RETRIES = 2;
    private static final int DEFAULT_BACKOFF = 1000;
    private static final boolean DEFAULT_SEND_AUTOMATIC = false;

    private String dataCollectorUrl;
    private int maxQueueSize;
    private int maxBatchSize;

    private int timeout;

    private int retries;

    private int backoff;

    private boolean sendAutomatic;

    public Options(final String dataCollectorUrl, final int maxQueueSize, final int maxBatchSize, final int timeout, final
    int retries, final int backoff, final boolean sendAutomatic) {
        this.dataCollectorUrl = dataCollectorUrl;
        this.maxQueueSize = maxQueueSize;
        this.maxBatchSize = maxBatchSize;
        this.timeout = timeout;
        this.retries = retries;
        this.backoff = backoff;
        this.sendAutomatic = sendAutomatic;
    }

    public static Options getDefault() {
        return new Options(DEFAULT_DATA_COLLECTOR_URL, DEFAULT_MAX_QUEUE_SIZE, DEFAULT_MAX_BATCH_SIZE, DEFAULT_TIMEOUT,
                DEFAULT_RETRIES, DEFAULT_BACKOFF, DEFAULT_SEND_AUTOMATIC);
    }

    public String getDataCollectorUrl() {
        return dataCollectorUrl;
    }

    public void setDataCollectorUrl(String dataCollectorUrl) {
        this.dataCollectorUrl = dataCollectorUrl;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getBackoff() {
        return backoff;
    }

    public void setBackoff(int backoff) {
        this.backoff = backoff;
    }

    public boolean isSendAutomatic() {
        return sendAutomatic;
    }

    public void setSendAutomatic(boolean sendAutomatic) {
        this.sendAutomatic = sendAutomatic;
    }
}
