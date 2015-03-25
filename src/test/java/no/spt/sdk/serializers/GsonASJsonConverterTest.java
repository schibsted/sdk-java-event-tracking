package no.spt.sdk.serializers;

import no.spt.sdk.TestData;
import no.spt.sdk.client.DataCollectorResponse.DataCollectorResponse;
import no.spt.sdk.models.TrackingIdentity;
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
    public void testTrackingIdentityDeSerializing() throws JSONException, IOException {
        TrackingIdentity id = jsonConverter.deserializeTrackingIdentity(TestData.getTrackingIdResponseAsJsonString());
        assertEquals(TestData.TRACKING_SESSION_ID, id.getSessionId());
        assertEquals(TestData.TRACKING_ENVIRONMENT_ID, id.getEnvironmentId());
    }

    @Test
    public void testDataCollectorResponse207DeSerializing() throws JSONException, IOException {
        DataCollectorResponse response = jsonConverter.deserializeDataCollectorResponse(TestData
            .getDataCollectorMultiStatusAsJsonString());
        assertEquals(207, response.getCode());
    }

    @Test
    public void testDataCollectorResponse400DeSerializing() throws JSONException, IOException {
        DataCollectorResponse response = jsonConverter.deserializeDataCollectorResponse(TestData
            .getDataCollectorBadRequestAsJsonString());
        assertEquals(400, response.getCode());
    }

}
