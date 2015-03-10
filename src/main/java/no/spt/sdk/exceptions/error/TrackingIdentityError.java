package no.spt.sdk.exceptions.error;

public enum TrackingIdentityError implements DataTrackingError {

    GENERAL_TRACKING_IDENTITY_ERROR(20000),
    CACHE_ERROR(20050),
    HTTP_CONNECTION_ERROR(20100),
    JSON_CONVERTING_ERROR(20200);

    private final int errorCode;

    private TrackingIdentityError(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}
