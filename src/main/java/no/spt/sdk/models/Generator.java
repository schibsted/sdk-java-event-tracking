package no.spt.sdk.models;

public class Generator extends ASObject {

    public Generator(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.Builder{

        public Generator build() {
            return new Generator(this);
        }
    }

}
