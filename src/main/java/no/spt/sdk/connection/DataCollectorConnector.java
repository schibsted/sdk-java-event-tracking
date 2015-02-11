package no.spt.sdk.connection;

import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The DataCollectorConnector wraps an HTTP client and is responsible for the HTTP connections with the data collector.
 */
public class DataCollectorConnector {

    private CloseableHttpClient httpClient;
    private Options options;
    private ASJsonConverter jsonConverter;

    /**
     * Constructs a DataCollectorConnector using the provided options
     * @param options used to configure the connector
     */
    public DataCollectorConnector(Options options) {
        this.httpClient = HttpClients.createDefault();
        this.options = options;
        this.jsonConverter = new GsonASJsonConverter();
    }

    /**
     * Method used to send a batch of activities to the data collector
     * @param batch a batch to send to the data collector
     * @return the response from the data collector
     * @throws DataTrackingException if the response HTTP status is not 200
     * @throws IOException if writing to stream fail
     */
    public DataTrackingResponse send(List<Activity> batch) throws DataTrackingException, IOException {
        HttpPost post = new HttpPost(options.getDataCollectorUrl());
        post.addHeader("Content-Type", "application/json; charset=utf-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(jsonConverter.serialize(batch)
                               .getBytes());
        post.setEntity(new ByteArrayEntity(out.toByteArray()));

        RequestConfig config = RequestConfig.custom()
                                            .setSocketTimeout(options.getTimeout())
                                            .setConnectTimeout(options.getTimeout())
                                            .setConnectionRequestTimeout(options.getTimeout())
                                            .build();

        post.setConfig(config);

        ResponseHandler<DataTrackingResponse> responseHandler = new DataCollectorResponseHandler();
        DataTrackingResponse response = httpClient.execute(post, responseHandler);
        if(response.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
            throw new DataTrackingException("Response from Data Collector was not OK", response);
        } else if(response.getResponseCode() == HttpStatus.SC_MULTI_STATUS) {
            // TODO Handle that some activities didn't validate
            throw new DataTrackingException("Some of the activities could not be validated by Data Collector", response);
        } else if(response.getResponseCode() != HttpStatus.SC_OK) {
            throw new DataTrackingException("Unexpected response from Data Collector", response);
        }
        return response;
    }

    /**
     * This class handles responses from the data collector
     */
    private class DataCollectorResponseHandler implements ResponseHandler<DataTrackingResponse> {

        /**
         * This method handles responses from the data collector
         * @param response the HTTP response from the data collector
         * @return a DataTrackingResponse
         * @throws IOException if the HTTP entity cannot be converted to a String
         */
        public DataTrackingResponse handleResponse(final HttpResponse response) throws IOException {
            int status = response.getStatusLine()
                                 .getStatusCode();
            HttpEntity entity = response.getEntity();
            return new DataTrackingResponse(status, null, EntityUtils.toString(entity));
        }
    }

    /**
     * This method closes the DataCollectorConnector
     * @throws DataTrackingException if the HTTP client cannot be closed
     */
    public void close() throws DataTrackingException {
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new DataTrackingException(e);
        }
    }

}