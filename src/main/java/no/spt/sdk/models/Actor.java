package no.spt.sdk.models;

/**
 * Represents an Activity Streams 2.0 Actor
 *
 * @see <a href="http://www.w3.org/TR/activitystreams-core/#actors">http://www.w3.org/TR/activitystreams-core/#actors</a>
 */
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
