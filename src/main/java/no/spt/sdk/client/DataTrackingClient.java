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
import no.spt.sdk.identity.*;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.TrackingIdentity;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.JacksonASJsonConverter;
import no.spt.sdk.stats.DataTrackingClientStats;
import no.spt.sdk.stats.DataTrackingStats;

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
    private final Identifier identifier;
    private final HttpConnection httpConnection;
    private final DataTrackingStats stats;

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
        private Identifier identifier;
        private DataTrackingStats stats;

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
         * Set the {@link no.spt.sdk.identity.IdentityConnector} to use for getting tracking IDs from the CIS
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
         * getting tracking IDs from the CIS
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
            stats = new DataTrackingStats();
            if (options == null) {
                throw new IllegalArgumentException("Data-collector-sdk#DataTrackingClient#options cannot be null.");
            }
            if(httpConnection == null) {
                httpConnection = new HttpClientConnection(options);
            }
            if(jsonConverter == null) {
                this.jsonConverter = new JacksonASJsonConverter();
            }
            if(errorCollector == null) {
                errorCollector = new ReportingErrorCollector(options, httpConnection, jsonConverter, stats);
            }
            if(identityConnector == null) {
                this.identityConnector = new SimpleIdentityConnector(options, httpConnection, jsonConverter);
            }
            if(ActivitySenderType.MANUAL_ACTIVITY_SENDER.equals(activitySenderType)) {
                activitySender = new ManualBatchSender(options, httpConnection, jsonConverter, stats);
            } else if (activitySender == null || ActivitySenderType.AUTOMATIC_ACTIVITY_SENDER.equals(activitySenderType)) {
                activitySender = new AutomaticBatchSender(options, httpConnection, errorCollector, jsonConverter, stats);
            }
            this.activitySender.init();
            this.identifier = new AsynchronousIdentifier(options, identityConnector, errorCollector);
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
        this.identifier = builder.identifier;
        this.httpConnection = builder.httpConnection;
        this.stats = builder.stats;
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
     * Close the client. This will block while internal queues are flushed.
     */
    public void close() {
        try {
            this.identifier.close();
        } catch (DataTrackingException e) {
            handleError(e);
        }
        try {
            this.activitySender.close();
        } catch (DataTrackingException e) {
            handleError(e);
        }
        try {
            this.errorCollector.close();
        } catch (DataTrackingException e) {
            // Ignore
        }
        try {
            this.httpConnection.close();
        } catch (DataTrackingException e) {
            // Ignore
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
     * Takes a {@link java.util.Map} of identifiers and returns a TrackingIdentity object based on those identifiers
     * that contains sessionId and environmentId
     *
     * @param identifiers A Map of identifiers
     * @return A TrackingIdentity object that contains sessionId and environmentId
     * @throws DataTrackingException If an ID could not be created
     */
    public TrackingIdentity getTrackingId(Map<String, String> identifiers) throws DataTrackingException {
        try {
            return identityConnector.getTrackingId(identifiers);
        } catch (DataTrackingException e) {
            handleError(e);
            throw e;
        }
    }

    /**
     * Takes a {@link java.util.Map} of identifiers used to get a TrackingIdentity, and an IdentityCallback which has
     * an onSuccess method which will be called with the TrackingIdentity when it is returned from CIS
     *
     * @param identifiers A Map of identifiers
     * @param callback A callback which will be used when the TrackingIdentity is returned from CIS
     */
    public void identifyActorAsync(Map<String, String> identifiers, IdentityCallback callback) {
        identifier.identifyActorAsync(identifiers, callback);
    }

    /**
     * Returns a DataTrackingClientStats objects which contains counters for events that occur in the tracking client
     *
     * @return A DataTrackingClientStats objects which contains counters for events that occur in the tracking client
     */
    public DataTrackingClientStats getStats() {
        return stats;
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