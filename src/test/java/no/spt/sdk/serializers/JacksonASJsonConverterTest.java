package no.spt.sdk.serializers;

import no.spt.sdk.TestData;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

public class JacksonASJsonConverterTest {

    ASJsonConverter jsonConverter;

    @Before
    public void setup() {
        jsonConverter = new JacksonASJsonConverter();
    }

    @Test
    public void testActivitySerializing() throws JSONException, IOException {
        JSONAssert.assertEquals(TestData.getTestActivityAsJsonString(), jsonConverter.serialize(TestData
                .getTestActivity()), false);
    }

    @Test
    public void testLinkSerializing() throws JSONException, IOException {
        JSONAssert.assertEquals(TestData.createLinkAsJsonString(), jsonConverter.serialize(TestData
                .createLink()), false);
    }
}
