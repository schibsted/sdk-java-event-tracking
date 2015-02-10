package no.spt.sdk.models;

public class Provider extends ASObject {

    public Provider(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.AbstractBuilder {

        public Builder(String type, String id) {
            super(type, id);
        }

        public ASObject.AbstractBuilder userAgent(String userAgent) {
            return set("userAgent", userAgent);
        }

        public Provider build() {
            return new Provider(this);
        }
    }

}
