package no.spt.sdk.exceptions;

import no.spt.sdk.client.DataTrackingResponse;

/**
 * An SDK specific exception
 */
public class DataTrackingException extends Exception {

    private Integer responseCode;
    private String responseBody;

    /**
     * Constructs a DataTrackingException
     * @param message
     */
    public DataTrackingException(String message) {
        super(message);
    }

    /**
     * Constructs a DataTrackingException
     * @param throwable
     */
    public DataTrackingException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructs a DataTrackingException
     * @param message
     * @param throwable
     */
    public DataTrackingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a DataTrackingException
     * @param s
     * @param dataTrackingResponse
     */
    public DataTrackingException(String s, DataTrackingResponse dataTrackingResponse) {
        super(s);
        this.responseCode = dataTrackingResponse.getResponseCode();
        this.responseBody = dataTrackingResponse.getRawBody();
    }

    /**
     * Returns the HTTP response status code for the response that led to this exception
     * @return
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     * Returns the HTTP response body for the response that led to this exception
     * @return
     */
    public String getResponseBody() {
        return responseBody;
    }

}
