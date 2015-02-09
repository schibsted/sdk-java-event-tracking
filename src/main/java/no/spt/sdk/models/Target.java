package no.spt.sdk.models;

public class Target extends ASObject {

    public Target(AbstractBuilder<?, ?> builder) {
        super(builder);
    }

    public static class Builder extends ASObject.Builder {

        public Builder() {

        }

        public Builder(String objectType) {
            super(objectType);
        }


        public Target build() {
            return new Target(this);
        }
    }
}
