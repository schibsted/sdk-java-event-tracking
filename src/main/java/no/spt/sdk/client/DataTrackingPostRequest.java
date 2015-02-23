package no.spt.sdk.client;

import java.util.Map;

/**
 * An HTTP POST request representation
 */
public class DataTrackingPostRequest {

    private final String url;
    private final Map<String, String> headers;
    private final String rawBody;

    /**
     * Constructs a new DataTrackingPostRequest
     *
     * @param url The URL that the request should be sent to
     * @param headers The HTTP request headers to include
     * @param rawBody The request body
     */
    public DataTrackingPostRequest(String url, Map<String, String> headers, String rawBody) {
        this.url = url;
        this.headers = headers;
        this.rawBody = rawBody;
    }

    /**
     * Returns the URL
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the HTTP headers of the request
     * @return the HTTP headers of the request
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Returns the body of the request
     * @return the body of the request
     */
    public String getRawBody() {
        return rawBody;
    }
}
