package no.spt.sdk.models;

/**
 * Represents an Activity Streams 2.0 Provider
 *
 */
public class Provider extends ASObject {

    private Provider(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.AbstractBuilder<Provider, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }

        public Provider build() {
            return new Provider(this);
        }
    }

}
