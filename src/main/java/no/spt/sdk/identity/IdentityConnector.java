package no.spt.sdk.identity;

import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.IHttpConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.Map;

public class IdentityConnector {

    private Options options;
    private IHttpConnection httpConnection;
    private ASJsonConverter jsonConverter;

    public IdentityConnector(Options options, IHttpConnection httpConnection) {
        this.options = options;
        this.httpConnection = httpConnection;
        this.jsonConverter = new GsonASJsonConverter();
    }

    public String getAnonymousId(Map<String, String> identifiers) throws DataTrackingException {
        DataTrackingResponse response;
        try {
            response = httpConnection.send(new DataTrackingPostRequest(options.getAnonymousIdUrl(), null, jsonConverter.serialize(identifiers)));
        } catch (IOException e) {
            throw new DataTrackingException(e);
        }
        if(response.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
            throw new DataTrackingException("Response from Anonymous Identity Service was not OK", response);
        } else if(response.getResponseCode() != HttpStatus.SC_OK) {
            throw new DataTrackingException("Unexpected response from  Anonymous Identity Service", response);
        }
        return response.getRawBody();
    }
}
