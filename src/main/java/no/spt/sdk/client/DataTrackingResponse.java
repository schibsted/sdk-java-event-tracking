package no.spt.sdk.client;

import java.util.Map;

public class DataTrackingResponse {

    private Map<String, String> headers;
    private int responseCode;
    private String rawBody;

    public DataTrackingResponse(int responseCode, Map<String, String> headers, String rawBody) {
        this.responseCode = responseCode;
        this.headers = headers;
        this.rawBody = rawBody;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getRawBody() {
        return rawBody;
    }


}
