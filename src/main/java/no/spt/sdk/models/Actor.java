package no.spt.sdk.models;

public class Actor extends ASObject {

    public Actor(Builder builder) {
        super(builder);
    }


    public static class Builder extends ASObject.Builder {

        public Builder() {
        }

        public Builder(String objectType) {
            super(objectType);
        }

        public Actor build() {
            return new Actor(this);
        }
    }

}
