package no.spt.sdk.serializers;


import com.google.gson.*;
import no.spt.sdk.models.ASObject;
import no.spt.sdk.models.Link;

import java.lang.reflect.Type;

public class GsonASJsonConverter implements ASJsonConverter {

    private Gson gson;

    public GsonASJsonConverter() {
        this.gson = new GsonBuilder().registerTypeAdapter(ASObject.class, new ASObjectTypeConverter())
                                     .registerTypeAdapter(Link.class, new LinkTypeConverter())
                                     .create();
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }

    public String deSerialize(Object object) {
        return gson.toJsonTree(object).getAsJsonObject();
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

}
