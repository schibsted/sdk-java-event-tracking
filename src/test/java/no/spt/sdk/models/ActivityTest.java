package no.spt.sdk.models;

import org.junit.Test;

import static no.spt.sdk.models.Makers.*;
import static org.junit.Assert.assertEquals;

public class ActivityTest {

    @Test
    public void testCreatingActivity() throws Exception {
        ASObject.Builder objectBuilder = object("objectType", "objectId");
        Actor.Builder actorBuilder = actor("actorType", "actorId");
        Provider.Builder providerBuilder = provider("providerType", "providerId");
        Activity activity = activity("type")
            .provider(providerBuilder)
            .object(objectBuilder)
            .actor(actorBuilder)
            .build();
        assertEquals("type", activity.getType());
        assertEquals("objectType", activity.getObject().getMap().get("@type"));
        assertEquals("objectId", activity.getObject().getMap().get("@id"));
        assertEquals("actorType", activity.getActor().getMap().get("@type"));
        assertEquals("actorId", activity.getActor().getMap().get("@id"));
    }

    @Test(expected = IllegalStateException.class )
    public void testThatProviderIsRequired() {
        activity("type")
            .object(object("objectType", "objectId"))
            .actor(actor("actorType", "actorId"))
            .build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatActorIsRequired() {
        activity("type")
            .provider(provider("providerType", "providerId"))
            .object(object("objectType", "objectId"))
            .build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatObjectIsRequired() {
        activity("type")
            .provider(provider("providerType", "providerId"))
            .actor(actor("actorType", "actorId"))
            .build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatActivityTypeCannotBeNull() {
        activity(null)
            .actor(actor("actorType", "actorId"))
            .object(object("objectType", "objectId"))
            .build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatActivityTypeCannotBeEmpty() {
        activity("")
            .actor(actor("actorType", "actorId"))
            .object(object("objectType", "objectId"))
            .build();
    }

}