package no.spt.sdk.connection;

import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;

import java.io.IOException;
import java.util.List;

public interface IDataCollectorConnection {
    DataTrackingResponse send(List<Activity> batch) throws DataTrackingException, IOException;

    void close() throws DataTrackingException;
}
