package no.spt.sdk.models;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that holds anonymous IDs from the Anonymous Identity Service
 */
public class AnonymousIdentity {

    private Map<String, Object> data;

    /**
     * Creates a new AnonymousIdentity object
     */
    public AnonymousIdentity() {
        data = new HashMap<String, Object>();
    }

    /**
     * Creates a new AnonymousIdentity object with the provided data
     *
     * @param data Data from the Anonymous Identity Service
     */
    public AnonymousIdentity(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * Returns the sessionId for the anonymous user
     *
     * @return The sessionId for the anonymous user. If sessionId doesn't exist an empty string is returned
     */
    public String getSessionId() {
        if(data == null || data.get("sessionId") == null) {
            return "";
        }
        return data.get("sessionId").toString();
    }

    /**
     * Returns the environmentId for the anonymous user
     *
     * @return The environmentId for the anonymous user. If environmentId doesn't exist an empty string is returned
     */
    public String getEnvironmentId() {
        if(data == null || data.get("environmentId") == null) {
            return "";
        }
        return data.get("environmentId").toString();
    }

    /**
     * Returns all data that the Anonymous Identity Service responded with
     *
     * @return All data that the Anonymous Identity Service responded with
     */
    public Map<String, Object> getData() {
        return data;
    }


}
