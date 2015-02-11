package no.spt.sdk.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class Link {

    private final Map<String,Object> map;

    public Link(Link.Builder builder) {
        this.map = ImmutableMap.copyOf(builder.map);
    }

    public Map getMap() {
        return this.map;
    }

    public static class Builder {

        private final Map<String, Object> map = newLinkedHashMap();

        public Builder(String href) {
            type("Link");
            href(href);
        }

        public Builder set(String key, Object value) {
            if (value == null) {
                return this;
            } else {
                map.put(key, value);
            }
            return this;
        }

        private Builder type(String type) {
            return set("@type", type);
        }

        public Builder href(String href) {
            return set("href", href);
        }

        public Builder title(String title) {
            return set("title", title);
        }

        public Builder displayName(String displayName) {
            return set("displayName", displayName);
        }

        public Builder mediaType(String mediaType) {
            return set("mediaType", mediaType);
        }

        public Link build() {
            return new Link(this);
        }
    }

}
