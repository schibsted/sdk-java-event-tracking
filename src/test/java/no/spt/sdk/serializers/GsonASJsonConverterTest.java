package no.spt.sdk.serializers;

import no.spt.sdk.TestData;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class GsonASJsonConverterTest {

    ASJsonConverter jsonConverter;

    @Before
    public void setup() {
        jsonConverter = new GsonASJsonConverter();
    }

    @Test
    public void testActivitySerializing() throws JSONException {
        JSONAssert.assertEquals(TestData.getTestActivityAsJsonString(), jsonConverter.serialize(TestData
                .getTestActivity()), false);
    }
}
