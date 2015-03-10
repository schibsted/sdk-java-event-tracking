package no.spt.sdk.identity;

import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.CommunicationDataTrackingException;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.error.AnonymousIdentityError;
import no.spt.sdk.models.AnonymousIdentity;
import no.spt.sdk.serializers.ASJsonConverter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.Map;

/**
 * A connection to the Anonymous Identity Service used to get anonymous IDs.
 */
public class SimpleIdentityConnector implements IdentityConnector {

    private Options options;
    private HttpConnection httpConnection;
    private ASJsonConverter jsonConverter;


    public SimpleIdentityConnector(Options options, HttpConnection httpConnection, ASJsonConverter jsonConverter) {
        this.options = options;
        this.httpConnection = httpConnection;
        this.jsonConverter = jsonConverter;
    }

    @Override
    public AnonymousIdentity getAnonymousId(Map<String, String> identifiers) throws DataTrackingException {
        return getIdFromService(identifiers);
    }

    private AnonymousIdentity getIdFromService(Map<String, String> identifiers) throws DataTrackingException {
        DataTrackingResponse response;
        DataTrackingPostRequest request;
        try {
            request = new DataTrackingPostRequest(options.getAnonymousIdUrl(), null,
                    jsonConverter.serialize(identifiers));
            response = httpConnection.send(request);
        } catch (IOException e) {
            throw new DataTrackingException(e, AnonymousIdentityError.HTTP_CONNECTION_ERROR);
        }
        if (response.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
            throw new CommunicationDataTrackingException("Response from Anonymous Identity Service was not OK", response,
                    request, AnonymousIdentityError.HTTP_CONNECTION_ERROR);
        } else if (response.getResponseCode() != HttpStatus.SC_OK) {
            throw new CommunicationDataTrackingException("Unexpected response from Anonymous Identity Service", response,
                    request, AnonymousIdentityError.HTTP_CONNECTION_ERROR);
        }
        AnonymousIdentity anonymousIdentity;
        try {
            anonymousIdentity = jsonConverter.deSerializeAnonymousIdentity(response.getRawBody());
        } catch (Exception e) {
            throw new DataTrackingException(e, AnonymousIdentityError.JSON_CONVERTING_ERROR);
        }
        if (anonymousIdentity.getData()
                .isEmpty()) {
            throw new CommunicationDataTrackingException("Unexpected data from Anonymous Identity Service", response,
                    request, AnonymousIdentityError.HTTP_CONNECTION_ERROR);
        }
        return anonymousIdentity;
    }
}
