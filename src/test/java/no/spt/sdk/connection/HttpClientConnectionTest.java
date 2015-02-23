package no.spt.sdk.connection;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.models.Activity;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientConnectionTest {

    @Mock
    CloseableHttpClient httpClient;
    HttpClientConnection connection;
    Options options;
    private static ASJsonConverter jsonConverter = new GsonASJsonConverter();


    @Before
    public void setUp() throws Exception {
        this.options = TestData.getDefaultOptions();
        connection = new HttpClientConnection(options, httpClient);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testSendWithOkResponse() throws Exception {
        DataTrackingResponse mockResp = new DataTrackingResponse(HttpStatus.SC_OK, null, "OK");
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(mockResp);
        DataTrackingResponse resp = connection.send(asRequest(Arrays.asList(TestData.getTestActivity())));
        assertEquals(mockResp, resp);
    }

    @Test
    public void testSendWithSomeValidationFailedResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(new DataTrackingResponse
                (HttpStatus.SC_MULTI_STATUS, null, "ValidationFailedForSomeActivity"));
        DataTrackingResponse resp = connection.send(asRequest(Arrays.asList(TestData.getTestActivity())));
        assertEquals(HttpStatus.SC_MULTI_STATUS, resp.getResponseCode());
        assertNull(resp.getHeaders());
        assertEquals("ValidationFailedForSomeActivity", resp.getRawBody());
    }

    @Test
    public void testSendWithBadRequestResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(new DataTrackingResponse
                (HttpStatus.SC_BAD_REQUEST, null, "BadRequest"));
        DataTrackingResponse resp = connection.send(asRequest(Arrays.asList(TestData.getTestActivity())));
        assertEquals(HttpStatus.SC_BAD_REQUEST, resp.getResponseCode());
        assertNull(resp.getHeaders());
        assertEquals("BadRequest", resp.getRawBody());

    }

    @Test
    public void testSendUnexpectedResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(new DataTrackingResponse
                (HttpStatus.SC_NOT_IMPLEMENTED, null, "NotImplementedExample"));
        DataTrackingResponse resp = connection.send(asRequest(Arrays.asList(TestData.getTestActivity())));
        assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, resp.getResponseCode());
        assertNull(resp.getHeaders());
        assertEquals("NotImplementedExample", resp.getRawBody());
    }

    @Test
    public void testClose() throws Exception {
        connection.close();
    }

    private DataTrackingPostRequest asRequest(List<Activity> activities) throws IOException {
        return new DataTrackingPostRequest(options.getDataCollectorUrl(), null, jsonConverter.serialize(activities));
    }
}