package no.spt.sdk.connection;

import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.DataTrackingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * The HttpClientConnection wraps a {@link org.apache.http.impl.client.CloseableHttpClient} and is responsible for
 * HTTP connections.
 */
public class HttpClientConnection implements HttpConnection {

    private CloseableHttpClient httpClient;
    private Options options;

    /**
     * Constructs a DataCollectorConnector using the provided options
     *
     * @param options The options used to configure the connector
     */
    public HttpClientConnection(Options options) {
        this(options, HttpClients.createDefault());
    }

    /**
     * Constructor used primarily for testing
     *
     * @param options    The options used to configure the connector
     * @param httpClient The httpClient to use for communication with the data collector
     */
    protected HttpClientConnection(Options options, CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.options = options;
    }

    /**
     * Sends a {@link no.spt.sdk.client.DataTrackingPostRequest}
     *
     * @return a {@link no.spt.sdk.client.DataTrackingResponse}
     * @throws IOException If sending fails
     */
    @Override
    public DataTrackingResponse send(DataTrackingPostRequest request) throws IOException {
        HttpPost post = new HttpPost(request.getUrl());
        post.addHeader("Content-Type", "application/json; charset=utf-8");
        if (request.getHeaders() != null) {
            for (Map.Entry<String, String> entry : request.getHeaders()
                    .entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(request.getRawBody()
                .getBytes(Charset.forName("UTF-8")));
        post.setEntity(new ByteArrayEntity(out.toByteArray()));

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(options.getTimeout())
                .setConnectTimeout(options.getTimeout())
                .setConnectionRequestTimeout(options.getTimeout())
                .build();

        post.setConfig(config);

        ResponseHandler<DataTrackingResponse> responseHandler = new DataTrackingResponseHandler();
        return httpClient.execute(post, responseHandler);
    }

    /**
     * Closes the CloseableHttpClient
     *
     * @throws DataTrackingException if the CloseableHttpClient cannot be closed
     */
    @Override
    public void close() throws DataTrackingException {
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new DataTrackingException(e);
        }
    }

    /**
     * Response handler for the HTTP client
     */
    private static class DataTrackingResponseHandler implements ResponseHandler<DataTrackingResponse> {

        /**
         * Response handler for the HTTP client
         *
         * @param response the HTTP response
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

}