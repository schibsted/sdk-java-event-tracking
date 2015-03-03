package no.spt.sdk;

/**
 * Options contains settings used to configure the behaviour of the data collector SDK
 */
public class Options {

    private final String clientId;
    private final String dataCollectorUrl;
    private final String anonymousIdUrl;
    private final String errorReportingUrl;
    private final int maxQueueSize;
    private final int timeout;
    private final int retries;

    private Options(String clientId, String dataCollectorUrl, String anonymousIdUrl, String errorReportingUrl,
                    int maxQueueSize, int timeout, int retries) {
        this.clientId = clientId;
        this.dataCollectorUrl = dataCollectorUrl;
        this.anonymousIdUrl = anonymousIdUrl;
        this.errorReportingUrl = errorReportingUrl;
        this.maxQueueSize = maxQueueSize;
        this.timeout = timeout;
        this.retries = retries;
    }

    /**
     * Gets the client ID
     * @return The client ID
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Gets the data collector endpoint
     * @return The data collector endpoint
     */
    public String getDataCollectorUrl() {
        return dataCollectorUrl;
    }

    /**
     * Gets the anonymous identity service endpoint
     * @return The anonymous identity service endpoint
     */
    public String getAnonymousIdUrl() {
        return anonymousIdUrl;
    }

    /**
     * Gets the error reporting endpoint
     * @return The error reporting endpoint
     */
    public String getErrorReportingUrl() {
        return errorReportingUrl;
    }

    /**
     * Gets the maximum size of the activity queue waiting to be sent to the data collector.
     * If the queue has reached the maximum size it will no longer accept new activities and those will be dropped.
     * @return The maximum size of the activity queue waiting to be sent to the data collector
     */
    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    /**
     * Gets the amount of milliseconds before an HTTP request is marked as timed out
     * @return The amount of milliseconds before an HTTP request is marked as timed out
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Gets the amount of times to retry an HTTP request
     * @return The amount of times to retry an HTTP request
     */
    public int getRetries() {
        return retries;
    }

    /**
     * Builder used to constructs a new Option. It has some sensible defaults which can be overridden.
     */
    public static class Builder {

        private final String clientId;
        private String dataCollectorUrl = Defaults.DATA_COLLECTOR_URL;
        private String anonymousIdUrl = Defaults.ANONYMOUS_ID_SERVICE_URL;
        private String errorReportingUrl = Defaults.ERROR_REPORTING_URL;
        private int maxQueueSize = Defaults.MAX_QUEUE_SIZE;
        private int timeout = Defaults.TIMEOUT;
        private int retries = Defaults.RETRIES;

        public Builder(String clientId) {
            this.clientId = clientId;
        }

        /**
         * @param dataCollectorUrl The url to the data collector endpoint
         * @return This builder instance for chaining
         */
        public Builder setDataCollectorUrl(String dataCollectorUrl) {
            this.dataCollectorUrl = dataCollectorUrl;
            return this;
        }

        /**
         * @param anonymousIdUrl The url to the anonymous identity service endpoint
         * @return This builder instance for chaining
         */
        public Builder setAnonymousIdUrl(String anonymousIdUrl) {
            this.anonymousIdUrl = anonymousIdUrl;
            return this;
        }

        /**
         * @param errorReportingUrl The url to the error reporting endpoint
         * @return This builder instance for chaining
         */
        public Builder setErrorReportingUrl(String errorReportingUrl) {
            this.errorReportingUrl = errorReportingUrl;
            return this;
        }

        /**
         * @param maxQueueSize The maximum size of the activity queue waiting to be sent to the data collector
         * @return This builder instance for chaining
         */
        public Builder setMaxQueueSize(int maxQueueSize) {
            this.maxQueueSize = maxQueueSize;
            return this;
        }

        /**
         * @param timeout The amount of milliseconds before a request is marked as timed out
         * @return This builder instance for chaining
         */
        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * @param retries The amount of times to retry the request
         * @return This builder instance for chaining
         */
        public Builder setRetries(int retries) {
            this.retries = retries;
            return this;
        }

        private void validateClientId(String clientId) {
            if(clientId == null || clientId.equals("")) {
                throw new IllegalArgumentException("Data-collector-sdk#options#clientId must be a valid client ID.");
            }
        }

        private void validateRetries(int retries) {
            if(retries < 0) {
                throw new IllegalArgumentException("Data-collector-sdk#options#retries must be greater or equal to 0.");
            }
        }

        private void validateTimeout(int timeout) {
            if(timeout < 500) {
                throw new IllegalArgumentException("Data-collector-sdk#options#timeout must be at least 500 milliseconds.");
            }
        }

        private void validateMaxQueueSize(int maxQueueSize) {
            if(maxQueueSize < 1) {
                throw new IllegalArgumentException("Data-collector-sdk#options#maxQueueSize must be greater than 0.");
            }
        }

        private void validateErrorReportingUrl(String errorReportingUrl) {
            if(errorReportingUrl == null || errorReportingUrl.equals("")) {
                throw new IllegalArgumentException("Data-collector-sdk#options#errorReportingUrl must be a valid url.");
            }
        }

        private void validateAnonymousIdUrl(String anonymousIdUrl) {
            if(anonymousIdUrl == null || anonymousIdUrl.equals("")) {
                throw new IllegalArgumentException("Data-collector-sdk#options#anonymousIdUrl must be a valid url.");
            }
        }

        private void validateDataCollectorUrl(String dataCollectorUrl) {
            if(dataCollectorUrl == null || dataCollectorUrl.equals("")) {
                throw new IllegalArgumentException("Data-collector-sdk#options#dataCollectorUrl must be a valid url.");
            }
        }

        public Options build() {
            validateClientId(clientId);
            validateDataCollectorUrl(dataCollectorUrl);
            validateAnonymousIdUrl(anonymousIdUrl);
            validateErrorReportingUrl(errorReportingUrl);
            validateMaxQueueSize(maxQueueSize);
            validateTimeout(timeout);
            validateRetries(retries);
            return new Options(clientId, dataCollectorUrl, anonymousIdUrl, errorReportingUrl, maxQueueSize, timeout, retries);
        }
    }

}
