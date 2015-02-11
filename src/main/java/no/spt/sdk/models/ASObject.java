package no.spt.sdk.models;

import com.google.common.collect.ImmutableMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class ASObject {

    private static final DateFormat ISO_8601_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);

    protected final Map<String,Object> map;

    public ASObject(ASObject.AbstractBuilder<?,?> builder) {
        this.map = ImmutableMap.copyOf(builder.map);
    }

    public Map getMap() {
        return this.map;
    }

    public static class Builder extends AbstractBuilder<ASObject, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }
    }

    public static abstract class AbstractBuilder<A extends ASObject, B extends AbstractBuilder<A,B>> {
        private final Map<String, Object> map = newLinkedHashMap();

        public AbstractBuilder(String type, String id) {
            type(type);
            id(id);
        }

        public B set(String key, Object value) {
            if (value == null) {
                return (B)this;
            } else {
                map.put(key, value);
            }
            return (B)this;
        }

        public B id(String id) {
            return set("@id", id);
        }

        public B type(String type) {
            return set("@type", type);
        }

        public B title(String title) {
            return set("title", title);
        }

        public B provider(String provider) {
            return set("provider", provider);
        }

        public B provider(ASObject.Builder builder) {
            return set("provider", builder.build());
        }

        public B url(String url) {
            return set("url", url);
        }

        public B displayName(String displayName) {
            return set("displayName", displayName);
        }

        public B content(String content) {
            return set("content", content);
        }

        public B published(String published) {
            return set("published", published);
        }

        public B published(Date published) {
            return published(ISO_8601_FORMAT.format(published));
        }

        public B publishedNow() {
            Calendar currentTime = Calendar.getInstance();
            return published(ISO_8601_FORMAT.format(currentTime.getTime()));
        }

        public ASObject build() {
            return new ASObject(this);
        }
    }

}
