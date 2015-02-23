package no.spt.sdk.client;

import java.util.Map;

/**
 * An HTTP response representation
 */
public class DataTrackingResponse {

    private final Map<String, String> headers;
    private final int responseCode;
    private final String rawBody;

    /**
     * Constructs a new DataTrackingResponse
     *
     * @param responseCode the HTTP status code of the response
     * @param headers the HTTP headers of the response
     * @param rawBody the HTTP body of the response
     */
    public DataTrackingResponse(int responseCode, Map<String, String> headers, String rawBody) {
        this.responseCode = responseCode;
        this.headers = headers;
        this.rawBody = rawBody;
    }

    /**
     * Returns the HTTP headers of the response
     * @return the HTTP headers of the response
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Return the HTTP status code of the response
     * @return the HTTP status code of the response
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Return the body of the request response
     * @return the body of the response
     */
    public String getRawBody() {
        return rawBody;
    }


}
