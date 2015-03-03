package no.spt.sdk.exceptions.error;

public enum AnonymousIdentityError implements DataTrackingError {

    GENERAL_ANONYMOUS_IDENTITY_ERROR(20000),
    CACHE_ERROR(20050),
    HTTP_CONNECTION_ERROR(20100),
    JSON_CONVERTING_ERROR(20200);

    private final int errorCode;

    private AnonymousIdentityError(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}
