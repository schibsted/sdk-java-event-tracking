package no.spt.sdk.connection;

import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.exceptions.DataTrackingException;
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

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientDataCollectorConnectionTest {

    @Mock
    CloseableHttpClient httpClient;
    HttpClientDataCollectorConnection connection;


    @Before
    public void setUp() throws Exception {
        connection = new HttpClientDataCollectorConnection(TestData.getDefaultOptions(), httpClient);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testSendWithOkResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(new DataTrackingResponse
                (HttpStatus.SC_OK, null, "OK"));
        connection.send(Arrays.asList(TestData.getTestActivity()));
    }

    @Test(expected = DataTrackingException.class)
    public void testSendWithSomeValidationFailedResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(new DataTrackingResponse
                (HttpStatus.SC_MULTI_STATUS, null, "ValidationFailedForSomeActivity"));
        connection.send(Arrays.asList(TestData.getTestActivity()));
    }

    @Test(expected = DataTrackingException.class)
    public void testSendWithBadRequestResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(new DataTrackingResponse
                (HttpStatus.SC_BAD_REQUEST, null, "BadRequest"));
        connection.send(Arrays.asList(TestData.getTestActivity()));
    }

    @Test(expected = DataTrackingException.class)
    public void testSendUnexpectedResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn(new DataTrackingResponse
                (HttpStatus.SC_REQUEST_TIMEOUT, null, "RequestTimeOut"));
        connection.send(Arrays.asList(TestData.getTestActivity()));
    }

    @Test
    public void testClose() throws Exception {
        connection.close();
    }
}