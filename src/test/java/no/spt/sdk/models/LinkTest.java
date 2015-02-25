package no.spt.sdk.models;

import org.junit.Test;

import java.util.Map;

import static no.spt.sdk.models.Makers.link;
import static org.junit.Assert.assertEquals;

public class LinkTest {

    private static final String HREF = "http://example.org";

    @Test
    public void testCreateLink(){
        Link link = link(HREF).build();
        Map properties = link.getMap();
        assertEquals("Link", properties.get("@type"));
        assertEquals(HREF, properties.get("href"));
    }
}
