package no.spt.sdk.exceptions;

import java.util.concurrent.LinkedBlockingQueue;

public class ErrorCollector {

    private LinkedBlockingQueue<DataTrackingException> errorQueue;

    public ErrorCollector() {
        this.errorQueue = new LinkedBlockingQueue<DataTrackingException>();
    }

    public void collect(DataTrackingException e) {
        errorQueue.add(e);
        // TODO Send to backend
    }
}
