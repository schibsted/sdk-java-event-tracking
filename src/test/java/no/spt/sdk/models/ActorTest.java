package no.spt.sdk.models;

import org.junit.Test;

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

}
