package no.spt.sdk.exceptions;

import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.error.DataTrackingError;

public class CommunicationDataTrackingException extends DataTrackingException {

    DataTrackingResponse response;
    DataTrackingPostRequest request;

    /**
     * Constructs a CommunicationDataTrackingException
     * @param errorMessage
     * @param response
     * @param request
     * @param errorCode
     */
    public CommunicationDataTrackingException(String errorMessage, DataTrackingResponse response,
                                              DataTrackingPostRequest request, DataTrackingError errorCode) {
        super(errorMessage, errorCode);
        this.request = request;
        this.response = response;
    }

    /**
     * Returns the HTTP response status code for the response that led to this exception
     *
     * @return
     */
    public Integer getResponseCode() {
        return response.getResponseCode();
    }

    /**
     * Returns the HTTP response body for the response that led to this exception
     *
     * @return
     */
    public String getResponseBody() {
        return response.getRawBody();
    }


    /**
     * Returns the HTTP request body for the request that led to this exception
     *
     * @return
     */
    public String getRequestBody() {
        return request.getRawBody();
    }

    /**
     * Returns the URL for the request that led to this exception
     * @return
     */
    public String getRequestUrl() {
        return request.getUrl();
    }
}
