package no.spt.sdk.exceptions;

import no.spt.sdk.Options;
import no.spt.sdk.Util;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.models.*;
import no.spt.sdk.serializers.ASJsonConverter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static no.spt.sdk.models.Makers.*;

public class ReportingErrorCollector implements ErrorCollector {

    private static final int MAX_ERROR_QUEUE_SIZE = 1000;

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
        if(errorQueue.size() >= options.getMaxErrorBatchSize()) {
            List<DataTrackingException> current = new LinkedList<DataTrackingException>();
            errorQueue.drainTo(current, options.getMaxErrorBatchSize());
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
                                        jsonConverter.serialize(convertToErrorReports(errors))));
                    }
                    success = true;
                } catch (IOException e) {
                    retryCount++;
                    success = false;
                }
            } while (!success && retryCount < options.getRetries());
            // If errors cannot be sent they are dropped
        }

        private List<Activity> convertToErrorReports(List<DataTrackingException> errors) {
            List<Activity> errorReports = new LinkedList<Activity>();
            for(DataTrackingException error : errors) {
                if(error instanceof CommunicationDataTrackingException) {
                    errorReports.add(convertToErrorReport((CommunicationDataTrackingException)error));
                } else {
                    errorReports.add(convertToErrorReport(error));
                }
            }
            return errorReports;
        }


        private Activity convertToErrorReport(DataTrackingException error) {
            return new Activity.Builder("Create")
                    .actor(getSdkActor(options))
                    .object(object("spt:error", "urn:spt.no:error:" + String.valueOf(error.getErrorCode()))
                            .set("spt:errorCode", String.valueOf(error.getErrorCode()))
                            .set("spt:errorMessage", error.getMessage())
                            .set("spt:stackTrace", stackTracesToStringList(error.getStackTrace())))
                    .provider(getProvider(options))
                    .build();
        }


        private Activity convertToErrorReport(CommunicationDataTrackingException error) {
            return new Activity.Builder("Accept")
                    .actor(getSdkActor(options))
                    .object(object("spt:errorResponse", "urn:spt.no:error:" + String.valueOf(error.getErrorCode()))
                            .set("spt:errorCode", String.valueOf(error.getErrorCode()))
                            .set("spt:errorMessage", error.getMessage())
                            .set("spt:httpStatusCode", String.valueOf(error.getResponseCode()))
                            .set("spt:responseBody", new JsonString(error.getResponseBody()))
                            .set("inReplyTo", object("spt:request", null).set("spt:requestBody", new JsonString(error
                                    .getRequestBody()))))
                            .provider(getProvider(options))
                            .target(target("Service", error.getRequestUrl()))
                            .build();
        }

        private List<String> stackTracesToStringList(StackTraceElement[] stackTrace) {
            List<String> list = new LinkedList<String>();
            for(StackTraceElement elem : stackTrace) {
                list.add(elem.toString());
            }
            return list;
        }

        private Provider getProvider(Options options) {
            return provider("Organization", "urn:spid.no:" + options.getClientId()).build();
        }

        private Actor getSdkActor(Options options) {
            return actor("Application", "urn:spt.no:sdk:java:" + Util.getSdkVersion()).set("using", getOptionsObject
                    (options)).build();
        }

        private ASObject getOptionsObject(Options options) {
            return object("spt:options", "urn:spt.no:options:" +
                    options.getClientId()).set("spt:dataCollectorUrl", options.getDataCollectorUrl())
                    .set("spt:anonymousIdUrl", options.getAnonymousIdUrl())
                    .set("spt:errorReportingUrl", options.getErrorReportingUrl())
                    .set("spt:maxQueueSize", String.valueOf(options.getMaxQueueSize()))
                    .set("spt:timeout", String.valueOf(options.getTimeout()))
                    .set("spt:retries", String.valueOf(options.getRetries())).build();
        }

    }

}
