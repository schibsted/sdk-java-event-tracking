package no.spt.sdk.models;

/**
 * Represents an Activity Streams 2.0 Actor
 *
 * @see <a href="http://www.w3.org/TR/activitystreams-core/#actors">http://www.w3.org/TR/activitystreams-core/#actors</a>
 */
public class Actor extends ASObject {

    private Actor(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.AbstractBuilder<Actor, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }

        public Builder(TrackingIdentity trackingId, ASObject actor) {
            super(actor.getMap());
            type("Person");
            id(toIriVisitorId(trackingId.getVisitorId()));
            set("spt:sessionId", toIriSessionId(trackingId.getSessionId()));
            set("spt:environmentId", toIriEnvironmentId(trackingId.getEnvironmentId()));
            set("spt:userId", trackingId.getUserId().isEmpty() ? null : trackingId.getUserId());
        }

        public Builder(TrackingIdentity trackingId) {
            super("Person", toIriVisitorId(trackingId.getVisitorId()));
            set("spt:sessionId", toIriSessionId(trackingId.getSessionId()));
            set("spt:environmentId", toIriEnvironmentId(trackingId.getEnvironmentId()));
            set("spt:userId", trackingId.getUserId().isEmpty() ? null : trackingId.getUserId());
        }

        public Actor build() {
            return new Actor(this);
        }
    }

    private static String toIriVisitorId(String visitorId) {
        return !visitorId.isEmpty() ? "urn:spid.no:person:" + visitorId : null;
    }

    private static String toIriEnvironmentId(String environmentId) {
        return !environmentId.isEmpty() ? "urn:spid.no:environment:" + environmentId : null;
    }

    private static String toIriSessionId(String sessionId) {
        return !sessionId.isEmpty() ? "urn:spid.no:session:" + sessionId : null;
    }

}
