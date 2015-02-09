package no.spt.sdk.serializers;

import no.spt.sdk.models.Activity;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static no.spt.sdk.models.Makers.*;
import static org.junit.Assert.assertEquals;

public class GsonASJsonConverterTest {

    ASJsonConverter jsonConverter;

    @Before
    public void setup() {
        jsonConverter = new GsonASJsonConverter();
    }

    @Test
    public void testActivitySerializing() {
        assertEquals("{" +
                        "\"verb\":\"receive\"," +
                        "\"published\":\"1970-01-01T01:00:00.000+01:00\"," +
                        "\"language\":\"no\"," +
                        "\"actor\":{" +
                            "\"objectType\":\"person\"," +
                            "\"id\":\"42\"," +
                            "\"ip\":\"127.0.0.1\"," +
                            "\"deviceLanguage\":\"no\"," +
                            "\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36" +
                        "\"}," +
                        "\"generator\":{" +
                            "\"ip\":\"192.168.0.1\"," +
                            "\"url\":\"http://www.vg.no\"," +
                            "\"title\":\"Innrømmer svikt i kommunikasjon med Northug - Langrenn - VG\"," +
                            "\"provider\":\"SP123\"," +
                            "\"referrer\":\"http://vg.no" +
                        "\"}," +
                        "\"object\":{" +
                            "\"objectType\":\"article\"," +
                            "\"displayName\":\"Innrømmer svikt i kommunikasjon med Northug - Langrenn - VG\"," +
                            "\"url\":\"http://www.vg.no/sport/langrenn/langrenn/innroemmer-svikt-i-kommunikasjon-med-northug/a/23383896/" +
                        "\"}," +
                        "\"target\":{" +
                            "\"objectType\":\"collection\"," +
                            "\"displayName\":\"FINN.no / Torget / Annonser / Elektronikk og hvitevarer / Lyd og bilde / TV\"," +
                            "\"url\":\"http://finn.no/" +
                        "\"}" +
                    "}",
                jsonConverter.serialize(getActivity()));
    }

    private Activity getActivity() {
        return activity()
                .verb("receive")
                .published(new Date(0L))
                        .language("no")
                        .actor(actor("person")
                                .id("42")
                                .ip("127.0.0.1")
                                .deviceLanguage("no")
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2)AppleWebKit/537.36 (KHTML," +
                                        " like Gecko) Chrome/40.0.2214.93 Safari/537.36"))
                        .generator(object()
                                .ip("192.168.0.1")
                                .url("http://www.vg.no")
                                .title("Innrømmer svikt i kommunikasjon med Northug - Langrenn - VG")
                                .provider("SP123")
                                .referrer("http://vg.no"))
                        .object(object()
                                .objectType("article")
                                .displayName("Innrømmer svikt i kommunikasjon med Northug - Langrenn - VG")
                                .url("http://www.vg.no/sport/langrenn/langrenn/innroemmer-svikt-i-kommunikasjon-med" +
                                        "-northug/a/23383896/"))
                .target(object()
                        .objectType("collection")
                        .displayName("FINN.no / Torget / Annonser / Elektronikk og hvitevarer / Lyd og bilde / TV")
                        .url("http://finn.no/"))
                .build();
    }
}
