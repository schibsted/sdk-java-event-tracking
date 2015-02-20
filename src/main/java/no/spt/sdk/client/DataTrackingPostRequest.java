package no.spt.sdk.client;

import java.util.Map;

public class DataTrackingPostRequest {

    private final String url;
    private final Map<String, String> headers;
    private final String rawBody;

    public DataTrackingPostRequest(String url, Map<String, String> headers, String rawBody) {
        this.url = url;
        this.headers = headers;
        this.rawBody = rawBody;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRawBody() {
        return rawBody;
    }
}
