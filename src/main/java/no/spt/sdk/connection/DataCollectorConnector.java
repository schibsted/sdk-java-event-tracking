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

public class DataCollectorConnector {

    private CloseableHttpClient httpClient;
    private Options options;
    private ASJsonConverter jsonConverter;

    public DataCollectorConnector() {
        this(Options.getDefault());
    }

    public DataCollectorConnector(Options options) {
        this.httpClient = HttpClients.createDefault();
        this.options = options;
        this.jsonConverter = new GsonASJsonConverter();
    }

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

    private class DataCollectorResponseHandler implements ResponseHandler<DataTrackingResponse> {

        public DataTrackingResponse handleResponse(final HttpResponse response) throws IOException {
            int status = response.getStatusLine()
                                 .getStatusCode();
            HttpEntity entity = response.getEntity();
            return new DataTrackingResponse(status, null, EntityUtils.toString(entity));
        }
    }

    public void close() throws DataTrackingException {
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new DataTrackingException(e);
        }
    }

}