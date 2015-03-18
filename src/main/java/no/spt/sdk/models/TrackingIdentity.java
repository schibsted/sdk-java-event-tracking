package no.spt.sdk.models;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that holds tracking IDs from the Central Identification Service (CIS)
 */
public class TrackingIdentity {

    private Map<String, Object> data;

    /**
     * Creates a new TrackingIdentity object
     */
    public TrackingIdentity() {
        data = new HashMap<String, Object>();
    }

    /**
     * Creates a new TrackingIdentity object with the provided data
     *
     * @param data Data from the CIS
     */
    public TrackingIdentity(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * Returns the sessionId for the tracking identity
     *
     * @return The sessionId for the tracking identity. If sessionId doesn't exist an empty string is returned
     */
    public String getSessionId() {
        if(data == null || data.get("sessionId") == null) {
            return "";
        }
        return data.get("sessionId").toString();
    }

    /**
     * Returns the environmentId for the tracking identity
     *
     * @return The environmentId for the tracking identity. If environmentId doesn't exist an empty string is returned
     */
    public String getEnvironmentId() {
        if(data == null || data.get("environmentId") == null) {
            return "";
        }
        return data.get("environmentId").toString();
    }

    /**
     * Returns the userId for the tracking identity
     *
     * @return The userId for the tracking identity. If userId doesn't exist an empty string is returned
     */
    public String getUserId() {
        if(data == null || data.get("userId") == null) {
            return "";
        }
        return data.get("userId").toString();
    }

    /**
     * Returns the visitorId for the tracking identity
     *
     * @return The visitorId for the tracking identity. If visitorId doesn't exist an empty string is returned
     */
    public String getVisitorId() {
        if(data == null || data.get("visitorId") == null) {
            return "";
        }
        return data.get("visitorId").toString();
    }

    /**
     * Returns all data that the CIS responded with
     *
     * @return All data that the CIS responded with
     */
    public Map<String, Object> getData() {
        return data;
    }


}
