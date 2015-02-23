package no.spt.sdk.serializers;


import java.io.IOException;
import java.util.Map;

public interface ASJsonConverter {

    public String serialize(Object object) throws IOException;

    public Map<String, Object> deSerialize(String json) throws IOException ;

}
