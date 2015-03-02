package no.spt.sdk.exceptions;

import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.serializers.ASJsonConverter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ReportingErrorCollector implements ErrorCollector {

    private static final int MAX_ERROR_QUEUE_SIZE = 10000;
    private static final int MAX_ERROR_BATCH_SIZE = 20;

    private Options options;
    private HttpConnection httpConnection;
    private ASJsonConverter jsonConverter;
    private LinkedBlockingQueue<DataTrackingException> errorQueue;
    private Executor executor;

    public ReportingErrorCollector(Options options, HttpConnection httpConnection,
                                   ASJsonConverter jsonConverter) {
        this.options = options;
        this.httpConnection = httpConnection;
        this.jsonConverter = jsonConverter;
        this.executor = Executors.newSingleThreadExecutor();
        this.errorQueue = new LinkedBlockingQueue<DataTrackingException>();
    }

    @Override
    public void collect(DataTrackingException e) {
        if(errorQueue.size() < MAX_ERROR_QUEUE_SIZE) {
            errorQueue.add(e);
        }
        if(errorQueue.size() >= MAX_ERROR_BATCH_SIZE) {
            List<DataTrackingException> current = new LinkedList<DataTrackingException>();
            errorQueue.drainTo(current, MAX_ERROR_BATCH_SIZE);
            executor.execute(new ErrorBatchSender(options, httpConnection, jsonConverter, current));
        }
    }

    private static class ErrorBatchSender implements Runnable {

        private Options options;
        private HttpConnection httpConnection;
        private ASJsonConverter jsonConverter;
        private List<DataTrackingException> errors;

        public ErrorBatchSender(Options options, HttpConnection httpConnection,
                                ASJsonConverter jsonConverter, List<DataTrackingException> errors) {
            this.options = options;
            this.httpConnection = httpConnection;
            this.jsonConverter = jsonConverter;
            this.errors = errors;
        }

        @Override
        public void run() {
            boolean success = true;
            int retryCount = 0;

            do {
                try {
                    if (errors.size() > 0) {
                        DataTrackingResponse response = httpConnection.send(
                                new DataTrackingPostRequest(options.getErrorReportingUrl(), null,
                                        jsonConverter.serialize(errors)));
                    }
                    success = true;
                } catch (IOException e) {
                    retryCount++;
                    success = false;
                }
            } while (!success && retryCount < options.getRetries()); // TODO If errors cannot be sent they are dropped
        }
    }

}
