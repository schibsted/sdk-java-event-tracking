package no.spt.sdk.client;

import no.spt.sdk.Options;
import no.spt.sdk.batch.Sender;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.exceptions.error.ActivitySendingError;
import no.spt.sdk.exceptions.error.TrackingIdentityError;
import no.spt.sdk.identity.IdentityConnector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Actor;
import no.spt.sdk.models.TrackingIdentity;

import java.util.Map;
import java.util.concurrent.*;

public class AsynchronousIdentifyingDataTracker implements IdentifyingDataTracker {

    private Sender activitySender;
    private IdentityConnector identityConnector;
    private ErrorCollector errorCollector;
    private ExecutorService executor;

    public AsynchronousIdentifyingDataTracker(Options options, Sender activitySender, IdentityConnector
        identityConnector, ErrorCollector errorCollector) {
        this.activitySender = activitySender;
        this.identityConnector = identityConnector;
        this.errorCollector = errorCollector;
        this.executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>
            (options.getMaxQueueSize()));
    }

    @Override
    public void identifyActorAndTrack(Map<String, String> identifiers, Activity activity) {
        try {
            executor.execute(new IdentifyingDataTrackingTask(identifiers, activity));
        } catch (RejectedExecutionException e) {
            errorCollector.collect(new DataTrackingException("AsynchronousIdentityDataTracker queue is full", e,
                TrackingIdentityError.QUEUE_MAX_SIZE_REACHED));
        }
    }

    @Override
    public void close() throws DataTrackingException {
        executor.shutdown();
        try {
            if(!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            throw new DataTrackingException(e, ActivitySendingError.CLOSING_CLIENT_ERROR);
        }
    }

    private class IdentifyingDataTrackingTask implements Runnable {

        private Map<String, String> identifiers;
        private Activity activity;

        public IdentifyingDataTrackingTask(Map<String, String> identifiers, Activity activity) {
            this.identifiers = identifiers;
            this.activity = activity;
        }

        @Override
        public void run() {
            try {
                TrackingIdentity trackingId = identityConnector.getTrackingId(identifiers);
                activitySender.enqueue(new Activity.Builder(activity).actor(
                    activity.getActor() != null ?
                        new Actor.Builder(trackingId, activity.getActor()).build()
                    : new Actor.Builder(trackingId).build()
                ).build());
            } catch (DataTrackingException e) {
                errorCollector.collect(e);
            }
        }
    }
}
