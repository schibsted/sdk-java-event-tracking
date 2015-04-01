package no.spt.sdk.identity;

import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.CommunicationDataTrackingException;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.error.TrackingIdentityError;
import no.spt.sdk.models.TrackingIdentity;
import no.spt.sdk.serializers.ASJsonConverter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.Map;

/**
 * A connection to the Central Identification Service used to get tracking IDs.
 */
public class SimpleIdentityConnector implements IdentityConnector {

    Options options;
    HttpConnection httpConnection;
    ASJsonConverter jsonConverter;


    public SimpleIdentityConnector(Options options, HttpConnection httpConnection, ASJsonConverter jsonConverter) {
        this.options = options;
        this.httpConnection = httpConnection;
        this.jsonConverter = jsonConverter;
    }

    @Override
    public TrackingIdentity getTrackingId(Map<String, String> identifiers) throws DataTrackingException {
        return getIdFromService(identifiers);
    }

    TrackingIdentity getIdFromService(Map<String, String> identifiers) throws DataTrackingException {
        DataTrackingResponse response;
        DataTrackingPostRequest request;
        try {
            request = new DataTrackingPostRequest(options.getCISUrl(), null,
                    jsonConverter.serialize(identifiers));
            response = httpConnection.send(request);
        } catch (IOException e) {
            throw new DataTrackingException(e, TrackingIdentityError.HTTP_CONNECTION_ERROR);
        }
        if (response.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
            throw new CommunicationDataTrackingException("Response from CIS was not OK", response,
                    request, TrackingIdentityError.HTTP_CONNECTION_ERROR);
        } else if (response.getResponseCode() != HttpStatus.SC_OK) {
            throw new CommunicationDataTrackingException("Unexpected response from CIS", response,
                    request, TrackingIdentityError.HTTP_CONNECTION_ERROR);
        }
        TrackingIdentity trackingIdentity;
        try {
            trackingIdentity = jsonConverter.deserializeTrackingIdentity(response.getRawBody());
        } catch (Exception e) {
            throw new DataTrackingException(e, TrackingIdentityError.JSON_CONVERTING_ERROR);
        }
        if (trackingIdentity.getData()
                .isEmpty()) {
            throw new CommunicationDataTrackingException("Unexpected data from CIS", response,
                    request, TrackingIdentityError.HTTP_CONNECTION_ERROR);
        }
        return trackingIdentity;
    }
}
