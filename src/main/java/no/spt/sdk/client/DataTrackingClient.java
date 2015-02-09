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
    private DataCollectorConnector pixelPongConnection;
    private ErrorCollector errorCollector;

    /**
     *
     * @param options options to configure the behaviour of the client
     */
    public DataTrackingClient(Options options) {
        this.errorCollector = new ErrorCollector();
        this.pixelPongConnection = new DataCollectorConnector(options);
        if(options.isSendAutomatic()) {
            this.activitySender = new AutomaticBatchSender(options, pixelPongConnection, errorCollector);
            this.activitySender.init();
        } else {
            this.activitySender = new ManualBatchSender(options, pixelPongConnection);
        }

    }

    public void track(Activity activity) {
        try {
            this.activitySender.enqueue(activity);
        } catch (DataTrackingException e) {
            handleError(e);
        }
    }

    public void send() {
        try {
            this.activitySender.flush();
        } catch (DataTrackingException e) {
            handleError(e);
        }
    }

    public void send(Activity activity) {
        track(activity);
        send();
    }

    public void close() {
        try {
            this.activitySender.close();
        } catch (DataTrackingException e) {
            handleError(e);
        }
    }

    public int getQueueDepth() {
        return this.activitySender.getQueueDepth();
    }

    private void handleError(DataTrackingException e) {
        errorCollector.collect(e);
    }

}