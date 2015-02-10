package no.spt.sdk.client;

import java.util.Map;

/**
 * A response from the data collector
 */
public class DataTrackingResponse {

    private Map<String, String> headers;
    private int responseCode;
    private String rawBody;

    /**
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
     * Return the HTTP body of the response
     * @return the HTTP body of the response
     */
    public String getRawBody() {
        return rawBody;
    }


}
