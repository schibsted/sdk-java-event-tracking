package no.spt.sdk.identity;

import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.AnonymousIdentity;

import java.util.Map;

/**
 * A connection to the Anonymous Identity Service used to get anonymous IDs
 */
public interface IdentityConnector {

    /**
     * Takes a {@link java.util.Map} of identifiers and returns an AnonymousIdentity object based on those identifiers
     * that contains sessionId and environmentId
     *
     * @param identifiers A Map of identifiers
     * @return An AnonymousIdentity object that contains sessionId and environmentId
     * @throws DataTrackingException If an ID could not be created
     */
    AnonymousIdentity getAnonymousId(Map<String, String> identifiers) throws DataTrackingException;

}
