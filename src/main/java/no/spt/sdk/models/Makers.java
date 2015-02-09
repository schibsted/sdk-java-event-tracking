package no.spt.sdk.models;

public final class Makers {

    private Makers() {}

    public static Actor.Builder actor() {
        return new Actor.Builder();
    }

    public static Actor.Builder actor(String objectType) {
        return new Actor.Builder(objectType);
    }

    public static Target.Builder target() {
        return new Target.Builder();
    }

    public static Target.Builder target(String objectType) {
        return new Target.Builder(objectType);
    }

    public static Campaign.Builder campaign() {
        return new Campaign.Builder();
    }

    public static Generator.Builder generator() {
        return new Generator.Builder();
    }

    public static ASObject.Builder object() {
        return new ASObject.Builder();
    }

    public static ASObject.Builder object(String objectType) {
        return new ASObject.Builder(objectType);
    }

    public static Activity.Builder activity() {
        return new Activity.Builder();
    }
}
