package no.spt.sdk.exceptions;

import no.spt.sdk.client.DataTrackingResponse;

public class DataTrackingException extends Exception {

    private Integer responseCode;
    private String responseBody;

    public DataTrackingException(String message) {
        super(message);
    }

    public DataTrackingException(Throwable throwable) {
        super(throwable);
    }

    public DataTrackingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DataTrackingException(String s, DataTrackingResponse dataTrackingResponse) {
        super(s);
        this.responseCode = dataTrackingResponse.getResponseCode();
        this.responseBody = dataTrackingResponse.getRawBody();
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

}
