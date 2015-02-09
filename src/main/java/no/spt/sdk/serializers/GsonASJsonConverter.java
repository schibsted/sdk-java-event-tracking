package no.spt.sdk.serializers;


import com.google.gson.*;
import no.spt.sdk.models.ASObject;

import java.lang.reflect.Type;

public class GsonASJsonConverter implements ASJsonConverter {

    private Gson gson;

    public GsonASJsonConverter() {
        this.gson = new GsonBuilder().registerTypeAdapter(ASObject.class, new ASObjectTypeConverter())
                                     .create();
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }

    private static class ASObjectTypeConverter implements JsonSerializer<ASObject> {

        @Override
        public JsonElement serialize(ASObject src, Type srcType, JsonSerializationContext context) {
            return context.serialize(src.getMap());
        }

    }


}
