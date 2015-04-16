package no.spt.sdk;

import java.util.Properties;

public final class TrackingUtil {

    private TrackingUtil() {

    }

    public static String getSdkVersion() {
        return Version.VERSION.getValue();
    }

    public enum Version {

        VERSION("version");

        private static Properties properties;
        static {
            properties = new Properties();
            try {
                properties.load(Version.class.getClassLoader().getResourceAsStream(
                    "sdk.properties"));
            } catch (Exception e) {
                throw new RuntimeException("Error when loading property file", e);
            }
        }

        private String key;

        Version(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return properties.getProperty(key);
        }
    }

}
