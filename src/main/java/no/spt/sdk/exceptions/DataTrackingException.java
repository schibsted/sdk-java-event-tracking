package no.spt.sdk.exceptions;

import no.spt.sdk.exceptions.error.DataTrackingError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * An SDK specific exception
 */
public class DataTrackingException extends Exception {

    private DataTrackingError error;
    private String timestamp;


    /**
     * Constructs a DataTrackingException
     *
     * @param message
     */
    public DataTrackingException(String message, DataTrackingError error) {
        super(message);
        this.error = error;
        setTimestampNow();
    }

    /**
     * Constructs a DataTrackingException
     *
     * @param throwable
     */
    public DataTrackingException(Throwable throwable, DataTrackingError error) {
        super(throwable);
        this.error = error;
        setTimestampNow();
    }


    /**
     * Constructs a DataTrackingException
     *
     * @param message
     * @param throwable
     */
    public DataTrackingException(String message, Throwable throwable, DataTrackingError error) {
        super(message, throwable);
        this.error = error;
        setTimestampNow();
    }

    /**
     * Returns the error for this exception
     *
     * @return
     */
    public DataTrackingError getError() {
        return error;
    }

    /**
     * Returns the error code for this exception
     *
     * @return
     */
    public int getErrorCode() {
        return error.getErrorCode();
    }

    public String getTimestamp() {
        return timestamp;
    }

    private void setTimestampNow() {
        timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).format(new Date());
    }

}
