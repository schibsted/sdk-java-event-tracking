package no.spt.sdk.models;

public class Target extends ASObject {

    public Target(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.AbstractBuilder {

        public Builder(String type, String id) {
            super(type, id);
        }

        public Target build() {
            return new Target(this);
        }
    }
}
