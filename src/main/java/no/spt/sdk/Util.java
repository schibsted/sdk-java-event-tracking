package no.spt.sdk;

public class Util {

    public static String getSdkVersion() {
        return Util.class.getPackage().getImplementationVersion();
    }

}
