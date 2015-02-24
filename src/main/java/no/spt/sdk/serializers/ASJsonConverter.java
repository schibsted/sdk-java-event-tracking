package no.spt.sdk.serializers;


import java.io.IOException;
import java.util.Map;

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
     * Deserialize a JSON String to a Map
     *
     * @param json The JSON String to deserialize
     * @return A Map representing the JSON String
     * @throws IOException If the JSON String cannot be deserialized
     */
    public Map<String, Object> deSerialize(String json) throws IOException ;

}
