package no.spt.sdk.models;

public class Provider extends ASObject {

    public Provider(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.AbstractBuilder<Provider, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }

        public ASObject.AbstractBuilder client(String client) {
            return set("spt:client", client);
        }

        public Provider build() {
            return new Provider(this);
        }
    }

}
