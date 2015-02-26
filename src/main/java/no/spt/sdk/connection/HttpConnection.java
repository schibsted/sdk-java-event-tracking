package no.spt.sdk.connection;

import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.DataTrackingException;

import java.io.IOException;

/**
 * The IHttpConnection is responsible for HTTP communication.
 */
public interface HttpConnection {

    /**
     * Sends a {@link no.spt.sdk.client.DataTrackingPostRequest}
     *
     * @param request The request to send
     * @return A {@link no.spt.sdk.client.DataTrackingResponse}
     * @throws IOException If sending fails
     */
    DataTrackingResponse send(DataTrackingPostRequest request) throws IOException;

    /**
     * Closes the connection
     *
     * @throws DataTrackingException If connection cannot be closed
     */
    void close() throws DataTrackingException;
}
