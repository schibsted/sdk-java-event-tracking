package no.spt.sdk.identity;

import no.spt.sdk.exceptions.DataTrackingException;

import java.util.Map;

public interface IIdentityConnector {

    String getAnonymousId(Map<String, String> identifiers) throws DataTrackingException;

}
