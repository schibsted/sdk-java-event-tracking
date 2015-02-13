package no.spt.sdk.exceptions;

public interface IErrorCollector {

    void collect(DataTrackingException e);

}
