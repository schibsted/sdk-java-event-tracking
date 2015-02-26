package no.spt.sdk.identity;

import no.spt.sdk.exceptions.DataTrackingException;

import java.util.Map;

/**
 * A connection to the Anonymous Identity Service used to get anonymous IDs
 */
public interface IdentityConnector {

    /**
     * Takes a {@link java.util.Map} of identifiers and returns a unique ID based on those identifiers
     *
     * @param identifiers A Map of identifiers
     * @return A unique ID based on the provided identifiers
     * @throws DataTrackingException If an ID could not be created
     */
    String getAnonymousId(Map<String, String> identifiers) throws DataTrackingException;

}
