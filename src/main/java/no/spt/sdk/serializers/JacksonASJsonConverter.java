package no.spt.sdk.serializers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import no.spt.sdk.models.ASObject;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.AnonymousIdentity;
import no.spt.sdk.models.Link;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A JSON converter that uses {@link com.fasterxml.jackson}
 */
public class JacksonASJsonConverter implements  ASJsonConverter {

    private final ObjectMapper mapper;

    private static final MapType MAP_TYPE =
            TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);

    private static final JavaType ANONYMOUS_IDENTITY_TYPE =
            TypeFactory.defaultInstance().constructType(AnonymousIdentity.class);

    public JacksonASJsonConverter() {
        this.mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule module = new SimpleModule();
        module.addSerializer(ASObject.class, new ASObjectSerializer());
        module.addSerializer(Link.class, new LinkSerializer());
        mapper.registerModule(module);
        mapper.addMixIn(Activity.class, ActivityMixIn.class);
        mapper.addMixIn(AnonymousIdentity.class, AnonymousIdentityMixIn.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String serialize(Object object) throws IOException{
        return mapper.writeValueAsString(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> deSerialize(String json) throws IOException {
        return mapper.readValue(json, MAP_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnonymousIdentity deSerializeAnonymousIdentity(String json) throws IOException {
        return mapper.readValue(json, ANONYMOUS_IDENTITY_TYPE);
    }

    private static class ASObjectSerializer extends JsonSerializer<ASObject> {
        @Override
        public void serialize(ASObject value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeObject(value.getMap());
        }
    }

    private static class LinkSerializer extends JsonSerializer<Link> {
        @Override
        public void serialize(Link value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeObject(value.getMap());
        }
    }

    private class ActivityMixIn {
        @JsonProperty("@context")
        private List<Object> context;
        @JsonProperty("@type")
        private String type;
        @JsonProperty("@id")
        private String id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class AnonymousIdentityMixIn {

    }
}
