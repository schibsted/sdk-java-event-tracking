package no.spt.sdk.client;


import no.spt.sdk.Options;
import no.spt.sdk.batch.AutomaticBatchSender;
import no.spt.sdk.batch.ISender;
import no.spt.sdk.batch.ManualBatchSender;
import no.spt.sdk.connection.HttpClientDataCollectorConnection;
import no.spt.sdk.connection.IDataCollectorConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.IErrorCollector;
import no.spt.sdk.exceptions.LoggingErrorCollector;
import no.spt.sdk.models.Activity;

import static no.spt.sdk.Defaults.*;

/**
 * The DataTrackingClient can be used to track activities. The client is an HTTP wrapper over a data collector API.
 * It allows you to consume the API without making any HTTP requests yourself.
 */
public class DataTrackingClient {

    private final Options options;
    private final ISender activitySender;
    private final IErrorCollector errorCollector;

    /**
     * Builder for instantiating DataTrackingClients.
     *
     * The default implementation uses a {@link no.spt.sdk.batch.AutomaticBatchSender} with a
     * {@link HttpClientDataCollectorConnection} to automatically send activitiesto the data collector on a separate
     * thread.
     * It uses a {@link no.spt.sdk.exceptions.LoggingErrorCollector} to collect errors that occur while tracking
     * activities.
     */
    public static class Builder {

        private Options options;
        private ISender activitySender;
        private IDataCollectorConnection dataCollectorConnection;
        private IErrorCollector errorCollector;
        private ActivitySenderType activitySenderType;

        /**
         * Sets the {@link no.spt.sdk.Options} to use for the client
         *
         * @param options the {@link no.spt.sdk.Options} to use
         */
        public void setOptions(Options options){
            this.options = options;
        }

        /**
         * Sets the {@link no.spt.sdk.Options} to use for the client
         *
         * @param options The {@link no.spt.sdk.Options} to use
         * @return This instance (for method chaining)
         */
        public Builder withOptions(Options options) {
            setOptions(options);
            return this;
        }

        /**
         * Gets the {@link no.spt.sdk.Options} this builder is currently configured to use. If null,
         * {@link no.spt.sdk.Defaults} values will be used instead.
         *
         * @return The {@link no.spt.sdk.Options} to use
         */
        public Options getOptions() {
            return this.options;
        }

        /**
         * Sets the {@link no.spt.sdk.batch.ISender} that will use to send activities to the data collector.
         *
         * @param activitySender The {@link no.spt.sdk.batch.ISender} to use
         */
        public void setActivitySender(ISender activitySender){
            this.activitySender = activitySender;
        }

        /**
         * Sets the {@link no.spt.sdk.batch.ISender} that will use to send activities to the data collector to
         * {@link no.spt.sdk.batch.ManualBatchSender}.
         *
         * @return This instance (for method chaining)
         */
        public Builder withManualActivitySender() {
            activitySenderType = ActivitySenderType.MANUAL_ACTIVITY_SENDER;
            return this;
        }

        /**
         * Sets the {@link no.spt.sdk.batch.ISender} that will use to send activities to the data collector to
         * {@link no.spt.sdk.batch.AutomaticBatchSender}.
         *
         * @return This instance (for method chaining)
         */
        public Builder withAutomaticActivitySender() {
            activitySenderType = ActivitySenderType.AUTOMATIC_ACTIVITY_SENDER;
            return this;
        }

        /**
         * Sets the {@link no.spt.sdk.connection.IDataCollectorConnection} that the {@link no.spt.sdk.batch.ISender}
         * will use to send activities to the data collector.
         *
         * @param dataCollectorConnection The {@link no.spt.sdk.connection.IDataCollectorConnection} to use
         */
        public void setDataCollectorConnection(IDataCollectorConnection dataCollectorConnection){
            this.dataCollectorConnection = dataCollectorConnection;
        }

        /**
         * Sets the {@link no.spt.sdk.connection.IDataCollectorConnection} that the {@link no.spt.sdk.batch.ISender}
         * will use to send activities to the data collector.
         *
         * @param dataCollectorConnection The {@link no.spt.sdk.connection.IDataCollectorConnection} to use
         * @return This instance (for method chaining)
         */
        public Builder withDataCollectorConnection(IDataCollectorConnection dataCollectorConnection) {
            setDataCollectorConnection(dataCollectorConnection);
            return this;
        }

        /**
         * Gets the {@link no.spt.sdk.connection.IDataCollectorConnection} this builder is currently configured to use
         * when creating the {@link no.spt.sdk.batch.ISender}.
         * If null, {@link no.spt.sdk.connection.HttpClientDataCollectorConnection} will be used instead.
         *
         * @return The {@link no.spt.sdk.connection.IDataCollectorConnection} to use
         */
        public IDataCollectorConnection getDataCollectorConnection() {
            return this.dataCollectorConnection;
        }

        /**
         * Sets the {@link no.spt.sdk.exceptions.IErrorCollector} to use for collecting errors that occur when sending
         * activities to the data collector.
         *
         * @param errorCollector The {@link no.spt.sdk.exceptions.IErrorCollector} to use.
         */
        public void setErrorCollector(IErrorCollector errorCollector){
            this.errorCollector = errorCollector;
        }

        /**
         * Sets the {@link no.spt.sdk.exceptions.IErrorCollector} to use for collecting errors that occur while sending
         * activities to the data collector.
         *
         * @param errorCollector The {@link no.spt.sdk.exceptions.IErrorCollector} to use.
         * @return This instance (for method chaining)
         */
        public Builder withErrorCollector(IErrorCollector errorCollector) {
            setErrorCollector(errorCollector);
            return this;
        }

        /**
         * Gets the {@link no.spt.sdk.exceptions.IErrorCollector} this builder is currently configured to use for
         * collecting errors that occur while sending activities to the data collector.
         *
         * @return The {@link no.spt.sdk.exceptions.IErrorCollector} to use.
         */
        public IErrorCollector getErrorCollector() {
            return this.errorCollector;
        }

        /**
         * Builds a new DataTrackingClient using the interfaces which have been specified on this builder instance.
         * If none have been specified the default interfaces will be used.
         *
         * @return A newly constructed DataTrackingClient
         */
        public DataTrackingClient build() {
            if (options == null) {
                options = new Options(DATA_COLLECTOR_URL, MAX_QUEUE_SIZE, TIMEOUT, RETRIES);
            }
            if (dataCollectorConnection == null) {
                dataCollectorConnection = new HttpClientDataCollectorConnection(options);
            }
            if (errorCollector == null) {
                errorCollector = new LoggingErrorCollector();
            }
            if(ActivitySenderType.MANUAL_ACTIVITY_SENDER.equals(activitySenderType)) {
                activitySender = new ManualBatchSender(options, dataCollectorConnection);
            } else if (activitySender == null || ActivitySenderType.AUTOMATIC_ACTIVITY_SENDER.equals(activitySenderType)) {
                activitySender = new AutomaticBatchSender(options, dataCollectorConnection, errorCollector);
            }
            this.activitySender.init();

            return new DataTrackingClient(this);
        }

        private enum ActivitySenderType {
            AUTOMATIC_ACTIVITY_SENDER,
            MANUAL_ACTIVITY_SENDER
        }
    }

    /**
     * Constructs a DataTrackingClient using the provided builder
     *
     * @param builder the builder from which to get the DataTrackingClient's settings
     */
    private DataTrackingClient(Builder builder) {
        this.errorCollector = builder.errorCollector;
        this.activitySender = builder.activitySender;
        this.options = builder.options;
    }

    /**
     * A method that enqueues an activity to be sent to the data collector
     *
     * @param activity the activity to track
     */
    public void track(Activity activity) {
        try {
            this.activitySender.enqueue(activity);
        } catch (DataTrackingException e) {
            handleError(e);
        }
    }

    /**
     * Force the enqueued activities to be sent to the data collector
     */
    public void send() {
        try {
            this.activitySender.flush();
        } catch (DataTrackingException e) {
            handleError(e);
        }
    }

    /**
     * Enqueue an activity and force it to be send to the data collector
     *
     * @param activity an activity to enqueue
     */
    public void send(Activity activity) {
        track(activity);
        send();
    }

    /**
     * Close the client
     */
    public void close() {
        try {
            this.activitySender.close();
        } catch (DataTrackingException e) {
            handleError(e);
        }
    }

    /**
     * Get the number of activities currently waiting in queue to be sent to the data collector
     *
     * @return the number number of activities currently waiting in queue to be sent to the data collector
     */
    public int getQueueDepth() {
        return this.activitySender.getQueueDepth();
    }

    /**
     * Handle errors that occur in the client
     *
     * @param e an exception to handle
     */
    private void handleError(DataTrackingException e) {
        errorCollector.collect(e);
    }

}