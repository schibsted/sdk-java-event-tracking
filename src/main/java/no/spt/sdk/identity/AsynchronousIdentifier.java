package no.spt.sdk.identity;

import no.spt.sdk.Options;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.exceptions.error.ActivitySendingError;
import no.spt.sdk.exceptions.error.TrackingIdentityError;
import no.spt.sdk.models.TrackingIdentity;

import java.util.Map;
import java.util.concurrent.*;

public class AsynchronousIdentifier implements Identifier {

    private IdentityConnector identityConnector;
    private ErrorCollector errorCollector;
    private ExecutorService executor;

    public AsynchronousIdentifier(Options options, IdentityConnector identityConnector, ErrorCollector errorCollector) {
        this.identityConnector = identityConnector;
        this.errorCollector = errorCollector;
        this.executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>
            (options.getMaxQueueSize()));
    }

    @Override
    public void identifyActorAsync(Map<String, String> identifiers, IdentityCallback callback) {
        try {
            executor.execute(new IdentifyingCallbackTask(identifiers, callback));
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

    private class IdentifyingCallbackTask implements Runnable {

        private Map<String, String> identifiers;
        private IdentityCallback callback;

        public IdentifyingCallbackTask(Map<String, String> identifiers, IdentityCallback callback) {
            this.identifiers = identifiers;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                TrackingIdentity trackingId = identityConnector.getTrackingId(identifiers);
                callback.onSuccess(trackingId);
            } catch (DataTrackingException e) {
                errorCollector.collect(e);
            }
        }
    }
}
