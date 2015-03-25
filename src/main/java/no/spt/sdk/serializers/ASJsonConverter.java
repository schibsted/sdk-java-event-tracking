package no.spt.sdk.serializers;


import no.spt.sdk.client.DataCollectorResponse.DataCollectorResponse;
import no.spt.sdk.models.TrackingIdentity;

import java.io.IOException;

/**
 * A JSON converter that can serialize/deserialize Activity Stream objects
 */
public interface ASJsonConverter {

    /**
     * Serialize an object to JSON
     *
     * @param object The object to serialize
     * @return The serialized object as JSON
     * @throws IOException If the object cannot be serialized
     */
    public String serialize(Object object) throws IOException;

    /**
     * Deserialize a JSON String from the CIS to a {@link no.spt.sdk.models.TrackingIdentity}
     * object
     *
     * @param json The response from the CIS
     * @return A TrackingIdentity object that contains sessionId and environmentId
     * @throws IOException If the JSON String could not be deserialized
     */
    public TrackingIdentity deserializeTrackingIdentity(String json)  throws IOException;

    /**
     * Deserialize a JSON String from the data collector to a
     * {@link no.spt.sdk.client.DataCollectorResponse.DataCollectorResponse} object
     *
     * @param json The response from the data collector
     * @return A DataCollectorResponse object
     * @throws IOException If the JSON String could not be deserialized
     */
    public DataCollectorResponse deserializeDataCollectorResponse(String json) throws IOException;
}
