package no.spt.sdk.models;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrackingIdentityTest {

    private static final String SESSION_ID = "abc123";
    private static final String ENVIRONMENT_ID = "xyz123";

    @Test
    public void testGetSessionId() throws Exception {
        TrackingIdentity id = new TrackingIdentity(getTrackingIdProps());
        assertEquals(SESSION_ID, id.getSessionId());
    }

    @Test
    public void testMissingSessionId() throws Exception {
        Map<String, Object> data = getTrackingIdProps();
        data.remove("sessionId");
        TrackingIdentity id = new TrackingIdentity(data);
        assertEquals("", id.getSessionId());
    }

    @Test
    public void testGetEnvironmentId() throws Exception {
        TrackingIdentity id = new TrackingIdentity(getTrackingIdProps());
        assertEquals(ENVIRONMENT_ID, id.getEnvironmentId());
    }

    @Test
    public void testMissingEnvironmentId() throws Exception {
        Map<String, Object> data = getTrackingIdProps();
        data.remove("environmentId");
        TrackingIdentity id = new TrackingIdentity(data);
        assertEquals("", id.getEnvironmentId());
    }

    @Test
    public void testGetData() throws Exception {
        TrackingIdentity id = new TrackingIdentity(getTrackingIdProps());
        assertEquals(getTrackingIdProps(), id.getData());
    }

    @Test
    public void testMissingData() throws Exception {
        TrackingIdentity id = new TrackingIdentity();
        assertTrue(id.getData().isEmpty());
    }

    public static Map<String, Object> getTrackingIdProps(){
        Map<String, Object> identifier = new HashMap<String, Object>();
        identifier.put("sessionId", SESSION_ID);
        identifier.put("environmentId", ENVIRONMENT_ID);
        return identifier;
    }
}