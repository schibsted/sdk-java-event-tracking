package no.spt.sdk.models;


import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Activity {

    private static final DateFormat ISO_8601_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);

    @SerializedName("@context")
    private List<Object> context;
    @SerializedName("@type")
    private String type;
    private String published;
    private ASObject actor;
    private ASObject provider;
    private ASObject object;
    private ASObject target;
    private ASObject result;

    private Activity() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("spt", "http://www.spt.no/activityStreams");
        context = Arrays.asList("http://www.w3.org/ns/activitystreams", map);
    }

    public static class Builder {

        private Activity _temp = new Activity();

        public Builder(String type) {
            type(type);
        }

        public Builder actor(ASObject.AbstractBuilder builder) {
            _temp.actor = builder.build();
            return this;
        }

        public Builder provider(ASObject.AbstractBuilder builder) {
            _temp.provider = builder.build();
            return this;
        }

        public Builder target(ASObject.AbstractBuilder builder) {
            _temp.target = builder.build();
            return this;
        }

        public Builder type(String type) {
            _temp.type = type;
            return this;
        }

        public Builder published(String published) {
            _temp.published = published;
            return this;
        }

        public Builder published(Date published) {
            _temp.published = ISO_8601_FORMAT.format(published);
            return this;
        }

        public Builder publishedNow() {
            Calendar currentTime = Calendar.getInstance();
            _temp.published = ISO_8601_FORMAT.format(currentTime.getTime());
            return this;
        }

        public Builder object(ASObject.AbstractBuilder builder) {
            _temp.object = builder.build();
            return this;
        }

        public Activity build() {
            return _temp;
        }
    }


}
