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
        return actor("Person", "urn:spid:person:" + id.getSessionId());
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
}
