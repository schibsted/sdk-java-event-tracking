package no.spt.sdk.models;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnonymousIdentityTest {

    private static final String SESSION_ID = "abc123";
    private static final String ENVIRONMENT_ID = "xyz123";

    @Test
    public void testGetSessionId() throws Exception {
        AnonymousIdentity id = new AnonymousIdentity(getAnonymousIdProps());
        assertEquals(SESSION_ID, id.getSessionId());
    }

    @Test
    public void testMissingSessionId() throws Exception {
        Map<String, Object> data = getAnonymousIdProps();
        data.remove("sessionId");
        AnonymousIdentity id = new AnonymousIdentity(data);
        assertEquals("", id.getSessionId());
    }

    @Test
    public void testGetEnvironmentId() throws Exception {
        AnonymousIdentity id = new AnonymousIdentity(getAnonymousIdProps());
        assertEquals(ENVIRONMENT_ID, id.getEnvironmentId());
    }

    @Test
    public void testMissingEnvironmentId() throws Exception {
        Map<String, Object> data = getAnonymousIdProps();
        data.remove("environmentId");
        AnonymousIdentity id = new AnonymousIdentity(data);
        assertEquals("", id.getEnvironmentId());
    }

    @Test
    public void testGetData() throws Exception {
        AnonymousIdentity id = new AnonymousIdentity(getAnonymousIdProps());
        assertEquals(getAnonymousIdProps(), id.getData());
    }

    @Test
    public void testMissingData() throws Exception {
        AnonymousIdentity id = new AnonymousIdentity();
        assertTrue(id.getData().isEmpty());
    }

    public static Map<String, Object> getAnonymousIdProps(){
        Map<String, Object> identifier = new HashMap<String, Object>();
        identifier.put("sessionId", SESSION_ID);
        identifier.put("environmentId", ENVIRONMENT_ID);
        return identifier;
    }
}