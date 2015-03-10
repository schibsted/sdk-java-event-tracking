package no.spt.sdk.identity;

import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.TrackingIdentity;

import java.util.Map;

/**
 * A connection to the Central Identification Service used to get tracking IDs
 */
public interface IdentityConnector {

    /**
     * Takes a {@link java.util.Map} of identifiers and returns a TrackingIdentity object based on those identifiers
     * that contains sessionId and environmentId
     *
     * @param identifiers A Map of identifiers
     * @return A TrackingIdentity object that contains sessionId and environmentId
     * @throws DataTrackingException If an ID could not be created
     */
    TrackingIdentity getTrackingId(Map<String, String> identifiers) throws DataTrackingException;

}
