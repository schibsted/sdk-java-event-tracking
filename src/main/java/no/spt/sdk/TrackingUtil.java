package no.spt.sdk;

public final class TrackingUtil {

    private TrackingUtil() {

    }

    public static String getSdkVersion() {
        return TrackingUtil.class.getPackage().getImplementationVersion();
    }

}
