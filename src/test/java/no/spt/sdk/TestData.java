package no.spt.sdk;

import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Makers;

public class TestData {

    public static Activity getTestActivity() {
            return Makers.activity()
                    .verb("receive")
                    .publishedNow()
                    .language("no")
                    .actor(Makers.object("person")
                            .id("42")
                            .ip("127.0.0.1")
                            .deviceLanguage("no")
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2)AppleWebKit/537.36 (KHTML," +
                                    " like Gecko) Chrome/40.0.2214.93 Safari/537.36"))
                    .generator(Makers.object()
                            .ip("192.168.0.1")
                            .url("http://www.vg.no")
                            .title("Innrømmer svikt i kommunikasjon med Northug - Langrenn - VG")
                            .provider("SP123")
                            .referrer("http://vg.no"))
                    .object(Makers.object()
                            .objectType("article")
                            .displayName("Innrømmer svikt i kommunikasjon med Northug - Langrenn - VG")
                            .url("http://www.vg.no/sport/langrenn/langrenn/innroemmer-svikt-i-kommunikasjon-med" +
                                    "-northug/a/23383896/"))
                    .target(Makers.object()
                            .objectType("collection")
                            .displayName("FINN.no / Torget / Annonser / Elektronikk og hvitevarer / Lyd og bilde / TV")
                            .url("http://finn.no/"))
                    .build();
    }

}
