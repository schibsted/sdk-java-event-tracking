package no.spt.sdk.models;

public class Actor extends ASObject {

    public Actor(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.AbstractBuilder<Actor, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }

        public Actor build() {
            return new Actor(this);
        }
    }

}
