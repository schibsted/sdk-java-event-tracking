package no.spt.sdk.models;

import java.util.List;

/**
 * Represents an Activity Streams 2.0 Collection
 *
 */
public class Collection extends ASObject {

    private Collection(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractBuilder<Collection, Builder> {

        public Builder(List<ASObject> items) {
            super("Collection", null);
            set("items", items);
        }

        public Collection build() {
            return new Collection(this);
        }
    }
}
