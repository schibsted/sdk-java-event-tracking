package no.spt.sdk;

import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.models.*;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;

import java.util.Date;

import static no.spt.sdk.models.Makers.*;

public class TestData {

    private static final String ACTIVITY_TYPE = "Read";

    /// ACTIVITY
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
    private static final String DATA_COLLECTOR_URL = "http://localhost:8090/";
    private static final String ANONYMOUS_ID_SERVICE_URL = "http://localhost:8091/";
    private static final int MAX_QUEUE_SIZE = 10000;
    private static final int TIMEOUT = 5000;
    private static final int RETRIES = 2;
    private static ASJsonConverter jsonConverter = new GsonASJsonConverter();

    public static String getTestActivityAsJsonString() {
        return "{" +
                "\"@context\":[\"http://www.w3.org/ns/activitystreams\",{\"spt\":\"http://schema.schibsted" +
                ".com/activitystreams\"}]," +
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
                        .set("spt:userAgent", ACTOR_USER_AGENT))
                .provider(provider(PROVIDER_TYPE, PROVIDER_ID).set("spt:ip", PROVIDER_IP)
                        .displayName(PROVIDER_DISPLAY_NAME)
                        .url(PROVIDER_URL))
                .object(object(OBJECT_TYPE, OBJECT_ID).url(OBJECT_URL)
                        .displayName(OBJECT_DISPLAY_NAME))
                .build();
    }

    public static Activity createActivity() {
        return activity("Login").publishedNow()
                .actor(createActor())
                .object(createObject())
                .provider(createProvider())
                .build();
    }

    /// OPTIONS

    public static Provider createProvider() {
        return provider("Organization", "urn:spid:no:4cf36fa274dea2117e030000").build();
    }

    public static ASObject createObject() {
        return object("Organization", "urn:spid:47001:4cf36fa274dea2117e030000").displayName("Verdens Gang")
                .set("spt:merchant", "47001")
                .set("spt:client", "4cf36fa274dea2117e030000")
                .build();
    }

    public static Actor createActor() {
        return actor("Person", "urn:spid:no:286668").set("spt:userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X " +
                "10_10_2)AppleWebKit/537.36 (KHTML, like" + " Gecko) Chrome/40.0.2214.93 Safari/537.36")
                .set("spt:ip", "127.0.0.1")
                .set("spt:gender", "male")
                .set("spt:age", "40")
                .set("spt:created", "2015-01-01T12:00:27.87+00:20")
                .set("spt:distinctId", "XdffuwXmr3wp1X1o4fb1")
                .build();
    }

    public static Link createLink() {
        return link("http://example.org/abc").mediaType("text/html")
                .displayName("An example link")
                .build();
    }

    public static String createLinkAsJsonString() {
        return "{" +
                "\"@type\":\"Link\"," +
                "\"href\":\"http://example.org/abc\"," +
                "\"mediaType\":\"text/html\"," +
                "\"displayName\":\"An example link\"" +
                "}";
    }

    public static Options getDefaultOptions() {
        return new Options(DATA_COLLECTOR_URL, ANONYMOUS_ID_SERVICE_URL, MAX_QUEUE_SIZE, TIMEOUT, RETRIES);
    }


    public static DataTrackingPostRequest getTestDataTrackingPostRequest(Options options) {
        return new DataTrackingPostRequest(options.getDataCollectorUrl(), null, jsonConverter.serialize
                (getTestActivity()));
    }
}
