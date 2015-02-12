package no.spt.sdk;

import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;

import java.util.Date;

import static no.spt.sdk.models.Makers.*;

public class TestData {

    /// ACTIVITY

    private static final String ACTIVITY_TYPE = "Read";

    private static final String ACTOR_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2)" +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36";
    private static final String ACTOR_TYPE = "person";
    private static final String ACTOR_ID = "urn:spt:no:person:abc123";
    private static final String ACTOR_DEVICE_LANGAUGE = "no";
    private static final String ACTOR_IP = "127.0.0.1";

    private static final String PROVIDER_TYPE = "Organization";
    private static final String PROVIDER_ID = "urn:spid:no:organizations:sp123";
    private static final String PROVIDER_URL = "http://vg.no";
    private static final String PROVIDER_DISPLAY_NAME = "VG";
    private static final String PROVIDER_IP = "127.0.0.1";

    private static final String OBJECT_ID = "urn:spid:no:abc123";
    private static final String OBJECT_TYPE = "Page";
    private static final String OBJECT_URL = "http://vg.no";
    private static final String OBJECT_DISPLAY_NAME = "Forsiden - VG";

    public static String getTestActivityAsJsonString() {
        return "{" +
                    "\"@context\":[\"http://www.w3.org/ns/activitystreams\",{\"spt\":\"http://www.spt" +
                    ".no/activityStreams\"}]," +
                    "\"@type\":\"" + ACTIVITY_TYPE + "\"," +
                    "\"published\":\"1970-01-01T01:00:00.000+01:00\"," +
                    "\"actor\":{" +
                        "\"@type\":\"" + ACTOR_TYPE + "\"," +
                        "\"@id\":\"" + ACTOR_ID + "\"," +
                        "\"spt:ip\":\"" + ACTOR_IP + "\"," +
                        "\"spt:deviceLanguage\":\"" + ACTOR_DEVICE_LANGAUGE + "\"," +
                        "\"spt:userAgent\":\"" + ACTOR_USER_AGENT +
                    "\"}," +
                    "\"provider\":{" +
                        "\"@type\":\"" + PROVIDER_TYPE + "\"," +
                        "\"@id\":\"" + PROVIDER_ID + "\"," +
                        "\"spt:ip\":\"" + PROVIDER_IP + "\"," +
                        "\"url\":\"" + PROVIDER_URL + "\"," +
                        "\"displayName\":\"" + PROVIDER_DISPLAY_NAME +
                    "\"}," +
                    "\"object\":{" +
                        "\"@type\":\"" + OBJECT_TYPE + "\"," +
                        "\"@id\":\"" + OBJECT_ID + "\"," +
                        "\"displayName\":\"" + OBJECT_DISPLAY_NAME + "\"," +
                        "\"url\":\"" + OBJECT_URL +
                    "\"}" +
                "}";
    }

    public static Activity getTestActivity() {
        return activity(ACTIVITY_TYPE).published(new Date(0L))
                .actor(actor(ACTOR_TYPE, ACTOR_ID).set("spt:ip", ACTOR_IP)
                        .set("spt:deviceLanguage", ACTOR_DEVICE_LANGAUGE)
                        .set("spt:userAgent",ACTOR_USER_AGENT))
                .provider(provider(PROVIDER_TYPE, PROVIDER_ID).set("spt:ip", PROVIDER_IP)
                        .displayName(PROVIDER_DISPLAY_NAME)
                        .url(PROVIDER_URL))
                .object(object(OBJECT_TYPE, OBJECT_ID).url(OBJECT_URL)
                        .displayName(OBJECT_DISPLAY_NAME))
                .build();
    }

    /// OPTIONS

    private static final String DATA_COLLECTOR_URL = "http://localhost:8090/";
    private static final int MAX_QUEUE_SIZE = 10000;
    private static final int TIMEOUT = 5000;
    private static final int RETRIES = 2;
    private static final boolean SEND_AUTOMATIC = true;

    public static Options getDefaultOptions() {
        return new Options(DATA_COLLECTOR_URL, MAX_QUEUE_SIZE, TIMEOUT,
                RETRIES, SEND_AUTOMATIC);
    }

}
