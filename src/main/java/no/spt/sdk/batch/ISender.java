package no.spt.sdk.batch;

import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;

public interface ISender {

    public void flush() throws DataTrackingException;

    public void enqueue(Activity activity) throws DataTrackingException;

    public void close() throws DataTrackingException;

    public int getQueueDepth();

    public void init();
}
