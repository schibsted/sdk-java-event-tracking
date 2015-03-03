package no.spt.sdk.exceptions;

import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.error.DataTrackingError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * An SDK specific exception
 */
public class DataTrackingException extends Exception {

    private static final DateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);

    private Integer responseCode;
    private String responseBody;
    private DataTrackingError errorCode;
    private String timestamp;


    /**
     * Constructs a DataTrackingException
     *
     * @param message
     */
    public DataTrackingException(String message, DataTrackingError errorCode) {
        super(message);
        this.errorCode = errorCode;
        setTimestampNow();
    }

    /**
     * Constructs a DataTrackingException
     *
     * @param throwable
     */
    public DataTrackingException(Throwable throwable, DataTrackingError errorCode) {
        super(throwable);
        this.errorCode = errorCode;
        setTimestampNow();
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
        setTimestampNow();
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
        setTimestampNow();
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

    public String getTimestamp() {
        return timestamp;
    }

    private void setTimestampNow() {
        Calendar currentTime = Calendar.getInstance();
        timestamp = ISO_8601_FORMAT.format(currentTime.getTime());
    }

}
