package no.spt.sdk.models;

import com.google.common.collect.ImmutableMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.common.collect.Maps.newLinkedHashMap;

/**
 * Represents an Activity Streams 2.0 Object
 *
 * @see <a href="http://www.w3.org/TR/activitystreams-core/#object">http://www.w3.org/TR/activitystreams-core/#object</a>
 */
public class ASObject {

    public static final ASObject EMPTY = new ASObject(new Builder(null, null));

    private static final DateFormat ISO_8601_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);

    protected final Map<String,Object> map;

    protected ASObject(ASObject.AbstractBuilder<?,?> builder) {
        this.map = ImmutableMap.copyOf(builder.map);
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public static class Builder extends AbstractBuilder<ASObject, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }

        public Builder(String type) {
            super(type);
        }

        public ASObject build() {
            return new ASObject(this);
        }
    }

    public static abstract class AbstractBuilder<A extends ASObject, B extends AbstractBuilder<A,B>> {
        private final Map<String, Object> map;

        public AbstractBuilder(String type, String id) {
            map = newLinkedHashMap();
            type(type);
            id(id);
        }

        public AbstractBuilder(String type) {
            this(type, null);
        }

        public abstract A build();

        B set(String key, Object value) {
            if (value == null || EMPTY.equals(value)) {
                return (B)this;
            } else {
                map.put(key, value);
            }
            return (B)this;
        }

        public B set(String key, ASObject value) {
            if (value == null || EMPTY.equals(value)) {
                return (B)this;
            } else {
                map.put(key, value);
            }
            return (B)this;
        }

        public B set(String key, AbstractBuilder value) {
            return set(key, value.build());
        }

        public B set(String key, Link value) {
            if (value == null) {
                return (B)this;
            } else {
                map.put(key, value);
            }
            return (B)this;
        }

        public B set(String key, Link.Builder value) {
            return set(key, value.build());
        }

        public B set(String key, String value) {
            if (value == null) {
                return (B)this;
            } else {
                map.put(key, value);
            }
            return (B)this;
        }

        public B set(String key, List<String> value) {
            if (value == null || value.isEmpty()) {
                return (B)this;
            } else {
                map.put(key, value);
            }
            return (B)this;
        }

        public B set(String key, JsonString jsonString) {
            if (jsonString == null) {
                return (B)this;
            } else {
                map.put(key, jsonString);
            }
            return (B)this;
        }

        public B set(String key, Collection collection) {
            if (collection == null) {
                return (B)this;
            } else {
                map.put(key, collection);
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

    }

}
