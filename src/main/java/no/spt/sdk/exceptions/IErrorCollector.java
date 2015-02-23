package no.spt.sdk.exceptions;

/**
 * An Error Collector that collects and handles exceptions that occur in the SDK.
 */
public interface IErrorCollector {

    /**
     * Collects a {@link no.spt.sdk.exceptions.DataTrackingException}
     * @param e
     */
    void collect(DataTrackingException e);

}
