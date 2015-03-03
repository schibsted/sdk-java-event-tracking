package no.spt.sdk.serializers;

import no.spt.sdk.TestData;
import no.spt.sdk.models.AnonymousIdentity;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GsonASJsonConverterTest {

    ASJsonConverter jsonConverter;

    @Before
    public void setup() {
        jsonConverter = new GsonASJsonConverter();
    }

    @Test
    public void testActivitySerializing() throws JSONException, IOException {
        JSONAssert.assertEquals(TestData.getTestActivityAsJsonString(), jsonConverter.serialize(TestData
                .getTestActivity()), false);
    }

    @Test
    public void testLinkSerializing() throws JSONException, IOException {
        JSONAssert.assertEquals(TestData.createLinkAsJsonString(), jsonConverter.serialize(TestData.createLink()),
                false);
    }

    @Test
    public void testAnonymousIdentityDeSerializing() throws JSONException, IOException {
        AnonymousIdentity id = jsonConverter.deSerializeAnonymousIdentity(TestData.getAnonymousIdResponseAsJsonString
                ());
        assertEquals(TestData.ANONYMOUS_SESSION_ID, id.getSessionId());
        assertEquals(TestData.ANONYMOUS_ENVIRONMENT_ID, id.getEnvironmentId());
    }

}
