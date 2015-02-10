package no.spt.sdk;

import no.spt.sdk.models.Activity;

import java.util.Date;

import static no.spt.sdk.models.Makers.*;

public class TestData {

    public static Activity getTestActivity() {
            return activity("receive")
                    .published(new Date(0L))
                    .actor(actor("person", "42").ip("127.0.0.1")
                                                .deviceLanguage("no")
                                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2)" +
                                                        "AppleWebKit/537.36 (KHTML," + " like Gecko) Chrome/40.0" +
                                                        ".2214" +
                                                        ".93 Safari/537.36"))
                    .provider(object("Organization", "SP123").ip("192.168.0.1")
                                                             .url("http://www.vg.no")
                                                             .title("Innrømmer svikt i kommunikasjon med " + "Northug" +
                                                                     " - Langrenn - VG"))
                    .object(object("article", "Innrømmer svikt i kommunikasjon med Northug - Langrenn - VG").url
                            ("http://www.vg.no/sport/langrenn/langrenn/innroemmer-svikt-i-kommunikasjon-med" +
                                    "-northug/a/23383896/").displayName("Innrømmer svikt i kommunikasjon med Northug " +
                            "- Langrenn - VG"))
                    .target(object("collection", "http://finn.no/").displayName("FINN.no / Torget / Annonser / Elektronikk og hvitevarer / Lyd " + "og bilde / TV"))
                    .build();
    }

}
