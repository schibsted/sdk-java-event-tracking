package no.spt.sdk.client;


import no.spt.sdk.batch.AutomaticBatchSender;
import no.spt.sdk.batch.ISender;
import no.spt.sdk.batch.ManualBatchSender;
import no.spt.sdk.connection.DataCollectorConnector;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;

/**
 * The DataTrackingClient can be used to track activities. The client is an HTTP wrapper over a data collector API.
 * It allows you to consume the API without making any HTTP requests yourself.
 */
public class DataTrackingClient {

    private ISender activitySender;
    private DataCollectorConnector dataCollectorConnection;
    private ErrorCollector errorCollector;

    /**
     * @param options options to configure the behaviour of the client
     */
    public DataTrackingClient(Options options) {
        this.errorCollector = new ErrorCollector();
        this.dataCollectorConnection = new DataCollectorConnector(options);
        if (options.isSendAutomatic()) {
            this.activitySender = new AutomaticBatchSender(options, dataCollectorConnection, errorCollector);
            this.activitySender.init();
        } else {
            this.activitySender = new ManualBatchSender(options, dataCollectorConnection);
        }

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