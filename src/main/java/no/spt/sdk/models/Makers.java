package no.spt.sdk.models;

public final class Makers {

    private Makers() {}

    public static Actor.Builder actor(String type, String id) {
        return new Actor.Builder(type, id);
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
}
