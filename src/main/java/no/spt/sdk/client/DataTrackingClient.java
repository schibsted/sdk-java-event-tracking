package no.spt.sdk.client;


import no.spt.sdk.Options;
import no.spt.sdk.batch.AutomaticBatchSender;
import no.spt.sdk.batch.ManualBatchSender;
import no.spt.sdk.batch.Sender;
import no.spt.sdk.connection.HttpClientConnection;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.exceptions.ReportingErrorCollector;
import no.spt.sdk.identity.CachingIdentityConnector;
import no.spt.sdk.identity.IdentityConnector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.AnonymousIdentity;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.JacksonASJsonConverter;

import java.util.Map;

/**
 * The DataTrackingClient can be used to track activities. The client is an HTTP wrapper over a data collector API.
 * It allows you to consume the API without making any HTTP requests yourself.
 *
 */
public class DataTrackingClient {

    private final Options options;
    private final Sender activitySender;
    private final ErrorCollector errorCollector;
    private final IdentityConnector identityConnector;

    /**
     * Builder for instantiating DataTrackingClients.
     *
     * The default implementation uses a {@link no.spt.sdk.batch.AutomaticBatchSender} with a
     * {@link no.spt.sdk.connection.HttpClientConnection} to automatically send activities to the data collector on a separate
     * thread.
     * It uses a {@link no.spt.sdk.exceptions.ReportingErrorCollector} to collect errors that occur while tracking
     * activities and sending error reports to a central error collector.
     */
    public static class Builder {

        private Options options;
        private Sender activitySender;
        private HttpConnection httpConnection;
        private ErrorCollector errorCollector;
        private ActivitySenderType activitySenderType;
        private IdentityConnector identityConnector;
        private ASJsonConverter jsonConverter;

        /**
         * Sets the {@link no.spt.sdk.Options} to use for the client
         *
         * @param options The {@link no.spt.sdk.Options} to use
         * @return This instance (for method chaining)
         */
        public Builder withOptions(Options options) {
            this.options = options;
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
         * Sets the {@link no.spt.sdk.batch.Sender} that will use to send activities to the data collector.
         *
         * @param activitySender The {@link no.spt.sdk.batch.Sender} to use
         * @return This instance (for method chaining)
         */
        public Builder withActivitySender(Sender activitySender){
            this.activitySender = activitySender;
            return this;
        }

        /**
         * Sets the {@link no.spt.sdk.batch.Sender} that will use to send activities to the data collector to
         * {@link no.spt.sdk.batch.ManualBatchSender}.
         *
         * @return This instance (for method chaining)
         */
        public Builder withManualActivitySender() {
            activitySenderType = ActivitySenderType.MANUAL_ACTIVITY_SENDER;
            return this;
        }

        /**
         * Sets the {@link no.spt.sdk.batch.Sender} that will use to send activities to the data collector to
         * {@link no.spt.sdk.batch.AutomaticBatchSender}.
         *
         * @return This instance (for method chaining)
         */
        public Builder withAutomaticActivitySender() {
            activitySenderType = ActivitySenderType.AUTOMATIC_ACTIVITY_SENDER;
            return this;
        }

        /**
         * Sets the {@link no.spt.sdk.connection.HttpConnection} that will be used for HTTP communication.
         *
         * @param httpConnection The {@link no.spt.sdk.connection.HttpConnection} to use
         * @return This instance (for method chaining)
         */
        public Builder withHttpConnection(HttpConnection httpConnection) {
            this.httpConnection = httpConnection;
            return this;
        }

        /**
         * Gets the {@link no.spt.sdk.connection.HttpConnection} this builder is currently configured to use
         * for HTTP communication
         * If null, {@link no.spt.sdk.connection.HttpClientConnection} will be used instead.
         *
         * @return The {@link no.spt.sdk.connection.HttpConnection} to use
         */
        public HttpConnection getHttpConnection() {
            return this.httpConnection;
        }

        /**
         * Sets the {@link no.spt.sdk.exceptions.ErrorCollector} to use for collecting errors that occur while sending
         * activities to the data collector.
         *
         * @param errorCollector The {@link no.spt.sdk.exceptions.ErrorCollector} to use.
         * @return This instance (for method chaining)
         */
        public Builder withErrorCollector(ErrorCollector errorCollector) {
            this.errorCollector = errorCollector;
            return this;
        }

        /**
         * Gets the {@link no.spt.sdk.exceptions.ErrorCollector} this builder is currently configured to use for
         * collecting errors that occur while sending activities to the data collector.
         *
         * @return The {@link no.spt.sdk.exceptions.ErrorCollector} to use.
         */
        public ErrorCollector getErrorCollector() {
            return this.errorCollector;
        }

        /**
         * Set the {@link no.spt.sdk.identity.IdentityConnector} to use for getting anonymous IDs from the Anonymous
         * Identity Service
         *
         * @param identityConnector The {@link no.spt.sdk.identity.IdentityConnector} to use
         * @return This instance (for method chaining)
         */
        public Builder withIdentityConnector(IdentityConnector identityConnector) {
            this.identityConnector = identityConnector;
            return this;
        }

        /**
         * Gets the {@link no.spt.sdk.identity.IdentityConnector} this builder is currently configured to use for
         * getting anonymous IDs from the Anonymous Identity Service
         *
         * @return The {@link no.spt.sdk.identity.IdentityConnector} to use
         */
        public IdentityConnector getIdentityConnector(){
            return identityConnector;
        }

        /**
         * Set the {@link no.spt.sdk.serializers.ASJsonConverter} to use for serializing/deserializing json
         *
         * @param jsonConverter The {@link no.spt.sdk.serializers.ASJsonConverter} to use
         * @return This instance (for method chaining)
         */
        public Builder withJsonConverter(ASJsonConverter jsonConverter) {
            this.jsonConverter = jsonConverter;
            return this;
        }

        /**
         * Gets the {@link no.spt.sdk.serializers.ASJsonConverter} this builder is currently configured to use for
         * serializing/deserializing json
         *
         * @return The {@link no.spt.sdk.serializers.ASJsonConverter} to use
         */
        public ASJsonConverter getJsonConverter() {
            return jsonConverter;
        }

        /**
         * Builds a new DataTrackingClient using the interfaces which have been specified on this builder instance.
         * If none have been specified the default interfaces will be used.
         *
         * @return A newly constructed DataTrackingClient
         */
        public DataTrackingClient build() {
            if (options == null) {
                throw new IllegalArgumentException("Data-collector-sdk#DataTrackingClient#options cannot be null.");
            }
            if (httpConnection == null) {
                httpConnection = new HttpClientConnection(options);
            }
            if(jsonConverter == null) {
                this.jsonConverter = new JacksonASJsonConverter();
            }
            if (errorCollector == null) {
                errorCollector = new ReportingErrorCollector(options, httpConnection, jsonConverter);
            }
            if(identityConnector == null) {
                this.identityConnector = new CachingIdentityConnector(options, httpConnection, jsonConverter);
            }
            if(ActivitySenderType.MANUAL_ACTIVITY_SENDER.equals(activitySenderType)) {
                activitySender = new ManualBatchSender(options, httpConnection, jsonConverter);
            } else if (activitySender == null || ActivitySenderType.AUTOMATIC_ACTIVITY_SENDER.equals(activitySenderType)) {
                activitySender = new AutomaticBatchSender(options, httpConnection, errorCollector, jsonConverter);
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
        this.identityConnector = builder.identityConnector;
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
     * Enqueue an activity and force it to be sent to the data collector
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
     * Takes a {@link java.util.Map} of identifiers and returns an AnonymousIdentity object based on those identifiers
     * that contains sessionId and environmentId
     *
     * @param identifiers A Map of identifiers
     * @return An AnonymousIdentity object that contains sessionId and environmentId
     * @throws DataTrackingException If an ID could not be created
     */
    public AnonymousIdentity getAnonymousId(Map<String, String> identifiers) throws DataTrackingException {
        return identityConnector.getAnonymousId(identifiers);
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