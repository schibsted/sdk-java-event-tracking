package no.spt.sdk.serializers;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import no.spt.sdk.models.ASObject;
import no.spt.sdk.models.Link;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

public class GsonASJsonConverter implements ASJsonConverter {

    private final Gson gson;

    public GsonASJsonConverter() {
        this.gson = new GsonBuilder().registerTypeAdapter(ASObject.class, new ASObjectTypeConverter())
                .registerTypeAdapter(Link.class, new LinkTypeConverter())
                .setFieldNamingStrategy(new ActivityNamingStrategy())
                .create();
    }

    @Override
    public String serialize(Object object) {
        return gson.toJson(object);
    }

    @Override
    public Map<String, Object> deSerialize(String json) {
        Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(json, stringStringMap);
    }

    private static class ASObjectTypeConverter implements JsonSerializer<ASObject> {

        @Override
        public JsonElement serialize(ASObject src, Type srcType, JsonSerializationContext context) {
            return context.serialize(src.getMap());
        }

    }

    private static class LinkTypeConverter implements JsonSerializer<Link> {

        @Override
        public JsonElement serialize(Link src, Type srcType, JsonSerializationContext context) {
            return context.serialize(src.getMap());
        }

    }

    private static class ActivityNamingStrategy implements FieldNamingStrategy {

        public String translateName(Field field) {
            String name = field.getName();
            if("context".equals(name)) {
                return "@context";
            }
            if("type".equals(name)) {
                return "@type";
            }
            if("id".equals(name)) {
                return "@id";
            }
            return name;
        }
    }

}
