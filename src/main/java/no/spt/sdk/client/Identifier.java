package no.spt.sdk.client;

import no.spt.sdk.exceptions.DataTrackingException;

import java.util.Map;

public interface Identifier {

    void identifyActorAsync(Map<String, String> identifiers, IdentityCallback callback);

    void close() throws DataTrackingException;
}
