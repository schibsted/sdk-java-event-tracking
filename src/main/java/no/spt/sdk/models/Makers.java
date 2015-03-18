package no.spt.sdk.models;

/**
 * This class provides static helper methods for creating Activity Streams 2.0 objects
 */
public final class Makers {

    private Makers() {}

    public static Actor.Builder actor(String type, String id) {
        return new Actor.Builder(type, id);
    }

    public static Actor.Builder actor(TrackingIdentity id) {
        return actor("Person", toIriVisitorId(id.getVisitorId()))
                .set("spt:sessionId", toIriSessionId(id.getSessionId()))
                .set("spt:environmentId", toIriEnvironmentId(id.getEnvironmentId()))
                .set("spt:userId", id.getUserId().isEmpty() ? null : id.getUserId());
    }

    public static Target.Builder target(String type, String id) {
        return new Target.Builder(type, id);
    }

    public static Provider.Builder provider(String type, String id) {
        return new Provider.Builder(type, id);
    }

    public static ASObject.Builder object(String type, String id) {
        return new ASObject.Builder(type, id);
    }

    public static Activity.Builder activity(String type) {
        return new Activity.Builder(type);
    }

    public static Link.Builder link(String href) {
        return new Link.Builder(href);
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
