package no.spt.sdk.exceptions.error;

public enum ActivitySendingError implements DataTrackingError {

    GENERAL_ACTIVITY_SENDING_ERROR(10000),
    INTERRUPTED_ERROR(10020),
    HTTP_CONNECTION_ERROR(10050),
    BAD_REQUEST(10051),
    VALIDATION_ERROR(10052),
    UNEXPECTED_RESPONSE(10053),
    QUEUE_MAX_SIZE_REACHED(10070),
    CLOSING_CLIENT_ERROR(10100);


    private final int errorCode;

    private ActivitySendingError(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}
