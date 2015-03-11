package no.spt.sdk.models;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static no.spt.sdk.models.Makers.actor;
import static org.junit.Assert.assertEquals;

public class ActorTest {

    private static final String OBJECT_TYPE = "Type";
    private static final String OBJECT_ID = "object:id";

    @Test
    public void testCreateActor(){
        Actor actor = actor(OBJECT_TYPE, OBJECT_ID).build();
        Map properties = actor.getMap();
        assertEquals(OBJECT_TYPE, properties.get("@type"));
        assertEquals(OBJECT_ID, properties.get("@id"));
    }

    @Test
    public void testCreateActorFromTrackingIdentity(){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("environmentId", "abc123");
        data.put("sessionId", "xyz321");
        TrackingIdentity trackingId = new TrackingIdentity(data);
        Actor actor = actor(trackingId).build();
        Map properties = actor.getMap();
        assertEquals("Person", properties.get("@type"));
        assertEquals("urn:spid.no:person:xyz321", properties.get("@id"));
    }
}
