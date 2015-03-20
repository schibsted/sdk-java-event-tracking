package no.spt.sdk.client;

import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;

import java.util.Map;

public interface IdentifyingDataTracker {

    void identifyActorAndTrack(Map<String, String> identifiers, Activity activity);

    void close() throws DataTrackingException;
}
