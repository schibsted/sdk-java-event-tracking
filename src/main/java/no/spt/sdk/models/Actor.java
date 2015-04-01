package no.spt.sdk.models;

import java.util.UUID;

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
        return "urn:spid.no:person:" + (!visitorId.isEmpty() ? visitorId : UUID.randomUUID().toString());
    }

    private static String toIriEnvironmentId(String environmentId) {
        return "urn:spid.no:environment:" + (!environmentId.isEmpty() ? environmentId : UUID.randomUUID().toString());
    }

    private static String toIriSessionId(String sessionId) {
        return "urn:spid.no:session:" + (!sessionId.isEmpty() ? sessionId : UUID.randomUUID().toString());
    }

}
