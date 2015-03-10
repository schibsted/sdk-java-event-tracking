package no.spt.sdk;

import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.CommunicationDataTrackingException;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.error.ActivitySendingError;
import no.spt.sdk.models.*;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static no.spt.sdk.models.Makers.*;

public class TestData {

    public static final String ANONYMOUS_SESSION_ID = "abcde123";
    public static final String ANONYMOUS_ENVIRONMENT_ID = "xyz123";
    private static final DateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
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
                "\"published\":\"" + ISO_8601_FORMAT.format(new Date(0L)) + "\"," +
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
        return new Options.Builder("abc123").setDataCollectorUrl(DATA_COLLECTOR_URL)
                .setAnonymousIdUrl(ANONYMOUS_ID_SERVICE_URL)
                .setMaxQueueSize(MAX_QUEUE_SIZE)
                .setTimeout(TIMEOUT)
                .setRetries(RETRIES)
                .build();
    }


    public static DataTrackingPostRequest getTestDataTrackingPostRequest(Options options) throws IOException {
        return new DataTrackingPostRequest(options.getDataCollectorUrl(), null, jsonConverter.serialize
                (getTestActivity()));
    }

    public static Map<String, String> getAnonymousIdIdentifiers() {
        Map<String, String> identifier = new HashMap<String, String>();
        identifier.put("SomeKey", "SomeValue");
        return identifier;
    }

    public static String getAnonymousIdResponseAsJsonString() {
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"status\": \"OK\",\n" +
                "    \"type\": \"anonymous_identity\",\n" +
                "    \"data\": {\n" +
                "        \"someKey\": \"someValue\",\n" +
                "        \"environmentId\": \"" + ANONYMOUS_ENVIRONMENT_ID + "\",\n" +
                "        \"environmentIdUniqScore\": 50,\n" +
                "        \"environmentIdTemporary\": true,\n" +
                "        \"missingInput\": [\n" +
                "            \"accept\",\n" +
                "            \"accept-charset\",\n" +
                "            \"accept-language\"\n" +
                "        ],\n" +
                "        \"sessionId\": \"" + ANONYMOUS_SESSION_ID + "\",\n" +
                "        \"clientIp\": \"127.0.0.1\"\n" +
                "    },\n" +
                "    \"errors\": []\n" +
                "}";
    }

    public static DataTrackingException getDataTrackingException() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("contentType", "application/json");
        DataTrackingResponse response = new DataTrackingResponse(200, header, "Message body");
        return new DataTrackingException("error", ActivitySendingError.GENERAL_ACTIVITY_SENDING_ERROR);
    }

    public static CommunicationDataTrackingException getCommunicationDataTrackingException() throws IOException {
        Map<String, String> header = new HashMap<String, String>();
        header.put("contentType", "application/json");
        DataTrackingResponse response = new DataTrackingResponse(200, header, "Message body");
        return new CommunicationDataTrackingException("error", response, getTestDataTrackingPostRequest
                (getDefaultOptions()), ActivitySendingError.GENERAL_ACTIVITY_SENDING_ERROR);
    }
}
