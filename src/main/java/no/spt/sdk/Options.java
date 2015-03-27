package no.spt.sdk;

/**
 * Options contains settings used to configure the behaviour of the data collector SDK
 */
public class Options {

    private final String clientId;
    private final String dataCollectorUrl;
    private final String CISUrl;
    private final String errorReportingUrl;
    private final int maxQueueSize;
    private final int timeout;
    private final int retries;
    private final int maxActivityBatchSize;
    private final int maxErrorBatchSize;

    private Options(String clientId, String dataCollectorUrl, String CISUrl, String errorReportingUrl, int
            maxQueueSize, int timeout, int retries, int maxActivityBatchSize, int maxErrorBatchSize) {
        this.clientId = clientId;
        this.dataCollectorUrl = dataCollectorUrl;
        this.CISUrl = CISUrl;
        this.errorReportingUrl = errorReportingUrl;
        this.maxQueueSize = maxQueueSize;
        this.timeout = timeout;
        this.retries = retries;
        this.maxActivityBatchSize = maxActivityBatchSize;
        this.maxErrorBatchSize = maxErrorBatchSize;
    }

    /**
     * Gets the client ID
     *
     * @return The client ID
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Gets the data collector endpoint
     *
     * @return The data collector endpoint
     */
    public String getDataCollectorUrl() {
        return dataCollectorUrl;
    }

    /**
     * Gets the CIS endpoint
     *
     * @return The CIS endpoint
     */
    public String getCISUrl() {
        return CISUrl;
    }

    /**
     * Gets the error reporting endpoint
     *
     * @return The error reporting endpoint
     */
    public String getErrorReportingUrl() {
        return errorReportingUrl;
    }

    /**
     * Gets the maximum size of the activity queue waiting to be sent to the data collector.
     * If the queue has reached the maximum size it will no longer accept new activities and those will be dropped.
     *
     * @return The maximum size of the activity queue waiting to be sent to the data collector
     */
    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    /**
     * Gets the amount of milliseconds before an HTTP request is marked as timed out
     *
     * @return The amount of milliseconds before an HTTP request is marked as timed out
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Gets the amount of times to retry an HTTP request
     *
     * @return The amount of times to retry an HTTP request
     */
    public int getRetries() {
        return retries;
    }

    /**
     * Gets the maximum size of a batch of activities that is sent to the data collector.
     *
     * @return The maximum size of a batch of activities that is sent to the data collector.
     */
    public int getMaxActivityBatchSize() {
        return maxActivityBatchSize;
    }

    /**
     * Gets the maximum size of a batch of errors that is sent to the central error collector.
     *
     * @return The maximum size of a batch of errors that is sent to the central error collector.
     */
    public int getMaxErrorBatchSize() {
        return maxErrorBatchSize;
    }

    /**
     * Builder used to constructs a new Option. It has some sensible defaults which can be overridden.
     */
    public static class Builder {

        private final String clientId;
        private String dataCollectorUrl = Defaults.DATA_COLLECTOR_URL;
        private String CISUrl = Defaults.CIS_URL;
        private String errorReportingUrl = Defaults.ERROR_REPORTING_URL;
        private int maxQueueSize = Defaults.MAX_QUEUE_SIZE;
        private int timeout = Defaults.TIMEOUT;
        private int retries = Defaults.RETRIES;
        private int maxActivityBatchSize = Defaults.MAX_ACTIVITY_BATCH_SIZE;
        private int maxErrorBatchSize = Defaults.MAX_ERROR_BATCH_SIZE;

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
         * @param CISUrl The url to the CIS endpoint
         * @return This builder instance for chaining
         */
        public Builder setCISUrl(String CISUrl) {
            this.CISUrl = CISUrl;
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

        public Builder setMaxActivityBatchSize(int maxActivityBatchSize) {
            this.maxActivityBatchSize = maxActivityBatchSize;
            return this;
        }

        public Builder setMaxErrorBatchSize(int maxErrorBatchSize) {
            this.maxErrorBatchSize = maxErrorBatchSize;
            return this;
        }

        private void validateClientId(String clientId) {
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalArgumentException("Data-collector-sdk#options#clientId must be a valid client ID.");
            }
        }

        private void validateRetries(int retries) {
            if (retries < 0) {
                throw new IllegalArgumentException("Data-collector-sdk#options#retries must be greater or equal to 0.");
            }
        }

        private void validateTimeout(int timeout) {
            if (timeout < 500) {
                throw new IllegalArgumentException("Data-collector-sdk#options#timeout must be at least 500 " +
                        "milliseconds.");
            }
        }

        private void validateMaxQueueSize(int maxQueueSize) {
            if (maxQueueSize < 1) {
                throw new IllegalArgumentException("Data-collector-sdk#options#maxQueueSize must be greater than 0.");
            }
        }

        private void validateErrorReportingUrl(String errorReportingUrl) {
            if (errorReportingUrl == null || errorReportingUrl.isEmpty()) {
                throw new IllegalArgumentException("Data-collector-sdk#options#errorReportingUrl must be a valid url.");
            }
        }

        private void validateCISUrl(String CISUrl) {
            if (CISUrl == null || CISUrl.isEmpty()) {
                throw new IllegalArgumentException("Data-collector-sdk#options#CISUrl must be a valid url.");
            }
        }

        private void validateDataCollectorUrl(String dataCollectorUrl) {
            if (dataCollectorUrl == null || dataCollectorUrl.isEmpty()) {
                throw new IllegalArgumentException("Data-collector-sdk#options#dataCollectorUrl must be a valid url.");
            }
        }

        private void validateMaxActivityBatchSize(int maxActivityBatchSize) {
            if (maxActivityBatchSize < 1) {
                throw new IllegalArgumentException("Data-collector-sdk#options#maxActivityBatchSize must be greater " +
                        "than 0.");
            }
            if (maxActivityBatchSize > 20) {
                throw new IllegalArgumentException("Data-collector-sdk#options#maxActivityBatchSize must be less than" +
                        " 20.");
            }
        }

        private void validateMaxErrorBatchSize(int maxErrorBatchSize) {
            if (maxErrorBatchSize < 1) {
                throw new IllegalArgumentException("Data-collector-sdk#options#maxErrorBatchSize must be greater than" +
                        " 0.");
            }
            if (maxErrorBatchSize > 20) {
                throw new IllegalArgumentException("Data-collector-sdk#options#maxErrorBatchSize must be less than 20" +
                        ".");
            }
        }

        public Options build() {
            Options options = new Options(clientId, dataCollectorUrl, CISUrl, errorReportingUrl, maxQueueSize, timeout,
                retries, maxActivityBatchSize, maxErrorBatchSize);
            validateClientId(options.getClientId());
            validateDataCollectorUrl(options.getDataCollectorUrl());
            validateCISUrl(options.getCISUrl());
            validateErrorReportingUrl(options.getErrorReportingUrl());
            validateMaxQueueSize(options.getMaxQueueSize());
            validateTimeout(options.getTimeout());
            validateRetries(options.getRetries());
            validateMaxActivityBatchSize(options.getMaxActivityBatchSize());
            validateMaxErrorBatchSize(options.getMaxErrorBatchSize());
            return options;
        }
    }

}
