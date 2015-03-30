package no.spt.sdk.models;

import org.junit.Test;

import static no.spt.sdk.models.Makers.*;
import static org.junit.Assert.assertEquals;

public class ActivityTest {

    @Test
    public void testCreatingActivity() throws Exception {
        ASObject object = createObject();
        Actor actor = createActor();
        Provider provider = createProvider();
        Activity activity = activity("type", provider, actor, object).build();
        assertEquals("type", activity.getType());
        assertEquals("objectType", activity.getObject().getMap().get("@type"));
        assertEquals("objectId", activity.getObject().getMap().get("@id"));
        assertEquals("actorType", activity.getActor().getMap().get("@type"));
        assertEquals("actorId", activity.getActor().getMap().get("@id"));
    }

    @Test(expected = IllegalStateException.class )
    public void testThatProviderIsRequired() {
        ASObject object = createObject();
        Actor actor = createActor();
        Provider provider = null;
        Activity activity = activity("type", provider, actor, object).build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatActorIsRequired() {
        ASObject object = createObject();
        Actor actor = null;
        Provider provider = createProvider();
        Activity activity = activity("type", provider, actor, object).build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatObjectIsRequired() {
        ASObject object = null;
        Actor actor = createActor();
        Provider provider = createProvider();
        Activity activity = activity("type", provider, actor, object).build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatActivityTypeCannotBeNull() {
        ASObject object = createObject();
        Actor actor = createActor();
        Provider provider = createProvider();
        Activity activity = activity(null, provider, actor, object).build();
    }

    @Test(expected = IllegalStateException.class )
    public void testThatActivityTypeCannotBeEmpty() {
        ASObject object = createObject();
        Actor actor = createActor();
        Provider provider = createProvider();
        Activity activity = activity("", provider, actor, object).build();
    }

    private Provider createProvider() {
        return provider("providerType", "providerId").build();
    }

    private Actor createActor() {
        return actor("actorType", "actorId").build();
    }

    private ASObject createObject() {
        return object("objectType", "objectId").build();
    }

}