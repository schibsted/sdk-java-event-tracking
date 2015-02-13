package no.spt.sdk.models;

import org.junit.Test;

import static no.spt.sdk.models.Makers.activity;
import static no.spt.sdk.models.Makers.object;
import static org.junit.Assert.assertEquals;

public class ActivityTest {

    @Test
    public void testCreatingActivity() {
        Activity activity = activity("type").build();
        assertEquals("type", activity.getType());
    }

    @Test
    public void testObject() throws Exception {
        ASObject.Builder objectBuilder = object("objectType", "objectId");
        Activity activity = activity("type").object(objectBuilder).build();
    }
}