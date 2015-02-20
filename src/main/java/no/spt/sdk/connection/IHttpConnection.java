package no.spt.sdk.connection;

import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.DataTrackingException;

import java.io.IOException;

public interface IHttpConnection {
    DataTrackingResponse send(DataTrackingPostRequest request) throws DataTrackingException, IOException;

    void close() throws DataTrackingException;
}
