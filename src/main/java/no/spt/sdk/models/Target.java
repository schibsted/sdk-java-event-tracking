package no.spt.sdk.models;

/**
 * Represents an Activity Streams 2.0 Target
 *
 */
public class Target extends ASObject {

    private Target(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.AbstractBuilder<Target, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }

        public Target build() {
            return new Target(this);
        }
    }
}
