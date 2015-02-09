package no.spt.sdk.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class ASObject {

    protected String id;
    protected String objectType;
    protected String url;
    protected String displayName;
    protected String content;

    protected final Map<String,Object> map;

    public ASObject(ASObject.AbstractBuilder<?,?> builder) {
        this.map = ImmutableMap.copyOf(builder.map);
    }

    public Map getMap() {
        return this.map;
    }

    public static class Builder extends AbstractBuilder<ASObject, Builder> {

        public Builder() {

        }

        public Builder(String objectType) {
            objectType(objectType);
        }

    }

    public static abstract class AbstractBuilder<A extends ASObject, B extends AbstractBuilder<A,B>> {
        private final Map<String, Object> map = newLinkedHashMap();

        public B set(String key, Object value) {
            if (value == null) {
                return (B)this;
            } else {
                map.put(key, value);
            }
            return (B)this;
        }

        public B ip(String ip) {
            return set("ip", ip);
        }

        public B deviceLanguage(String deviceLanguage) {
            return set("deviceLanguage", deviceLanguage);
        }

        public B userAgent(String userAgent) {
            return set("userAgent", userAgent);
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

        public B referrer(String referrer) {
            return set("referrer", referrer);
        }

        public B referrer(ASObject.Builder builder) {
            return set("referrer", builder.build());
        }

        public B objectType(String objectType) {
            return set("objectType", objectType);
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

        public B id(String id) {
            return set("id", id);
        }

        public ASObject build() {
            return new ASObject(this);
        }
    }

}
