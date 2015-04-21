package no.spt.sdk.models;

import java.util.List;

/**
 * This class provides static helper methods for creating Activity Streams 2.0 objects
 */
public final class Makers {

    private Makers() {}

    public static Actor.Builder actor(String type, String id) {
        return new Actor.Builder(type, id);
    }

    public static Actor.Builder actor(TrackingIdentity id) {
        return new Actor.Builder(id);
    }

    public static Target.Builder target(String type, String id) {
        return new Target.Builder(type, id);
    }

    public static Target.Builder target(String type) {
        return new Target.Builder(type);
    }

    public static Result.Builder result(String type, String id) {
        return new Result.Builder(type, id);
    }

    public static Result.Builder result(String type) {
        return new Result.Builder(type);
    }

    public static Collection.Builder collection(List<ASObject> items) {
        return new Collection.Builder(items);
    }

    public static Provider.Builder provider(String type, String id) {
        return new Provider.Builder(type, id);
    }

    public static ASObject.Builder object(String type, String id) {
        return new ASObject.Builder(type, id);
    }

    public static ASObject.Builder object(String type) {
        return new ASObject.Builder(type);
    }

    public static Activity.Builder activity(String type, Provider provider, Actor actor, ASObject object) {
        return new Activity.Builder(type, provider, actor, object);
    }

    public static Link.Builder link(String href) {
        return new Link.Builder(href);
    }

}
