package no.spt.sdk.exceptions;

import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.error.DataTrackingError;

/**
 * An SDK specific exception
 */
public class DataTrackingException extends Exception {

    private Integer responseCode;
    private String responseBody;
    private DataTrackingError errorCode;


    /**
     * Constructs a DataTrackingException
     *
     * @param message
     */
    public DataTrackingException(String message, DataTrackingError errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a DataTrackingException
     *
     * @param throwable
     */
    public DataTrackingException(Throwable throwable, DataTrackingError errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }


    /**
     * Constructs a DataTrackingException
     *
     * @param message
     * @param throwable
     */
    public DataTrackingException(String message, Throwable throwable, DataTrackingError errorCode) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a DataTrackingException
     *
     * @param s
     * @param dataTrackingResponse
     */
    public DataTrackingException(String s, DataTrackingResponse dataTrackingResponse, DataTrackingError errorCode) {
        super(s);
        this.responseCode = dataTrackingResponse.getResponseCode();
        this.responseBody = dataTrackingResponse.getRawBody();
        this.errorCode = errorCode;
    }

    /**
     * Returns the HTTP response status code for the response that led to this exception
     *
     * @return
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     * Returns the HTTP response body for the response that led to this exception
     *
     * @return
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Returns the error code for this exception
     *
     * @return
     */
    public DataTrackingError getErrorCode() {
        return errorCode;
    }

}
