package no.spt.sdk.batch;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpClientConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.LoggingErrorCollector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBatchSenderTest {

    private static ASJsonConverter jsonConverter = new GsonASJsonConverter();
    @Mock
    private HttpClientConnection dataCollectorConnector;
    @Mock
    private LoggingErrorCollector errorCollector;
    private AutomaticBatchSender batchSender;
    private Options options;

    @Before
    public void setUp() throws Exception {
        options = TestData.getDefaultOptions();
        batchSender = new AutomaticBatchSender(options, dataCollectorConnector, errorCollector, jsonConverter);
        batchSender.init();
    }

    @After
    public void tearDown() throws Exception {
        batchSender.close();
    }

    @Test
    public void testEnqueue() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (200, null, "OK"));
        batchSender.enqueue(activity);
        sleep(200);
        assertEquals(0, batchSender.getQueueDepth());

        batchSender.enqueue(activity);
        sleep(200);
        assertEquals(0, batchSender.getQueueDepth());
    }

    @Test
    public void testEnqueueMoreThanMaxBatchSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (200, null, "OK"));
        for (int i = 0; i <= options.getMaxActivityBatchSize(); i++) {
            batchSender.enqueue(activity);
        }
        sleep(200);
        assertEquals(0, batchSender.getQueueDepth());

        verify(dataCollectorConnector, times(2)).send(any(DataTrackingPostRequest.class));
    }

    @Test(expected = DataTrackingException.class)
    public void testEnqueueMoreThanMaxQueueSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (200, null, "OK"));
        for (int i = 0; i <= options.getMaxQueueSize() * 2; i++) { // Assuming the client cannot send quick enough
            batchSender.enqueue(activity);
        }
    }

    @Test
    public void testFlush() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (200, null, "OK"));
        batchSender.enqueue(activity);
        batchSender.flush();
        sleep(200);
        assertEquals(0, batchSender.getQueueDepth());
    }

    @Test
    public void testClose() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (200, null, "OK"));
        batchSender.enqueue(activity);
        batchSender.close();
        assertEquals(0, batchSender.getQueueDepth());
    }

    @Test
    public void testHttpConnectionReturnsBadRequest() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (400, null, "Not OK"));
        batchSender.enqueue(activity);
        sleep(200);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testHttpConnectionReturnsMultiStatus() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (207, null, "Not OK"));
        batchSender.enqueue(activity);
        sleep(200);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testHttpConnectionReturnsUnexpectedResponse() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (409, null, "Unexpected error"));
        batchSender.enqueue(activity);
        sleep(200);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testGetQueueDepth() throws Exception {
        // TODO find a way to test this without flickering
    }

    @Test
    public void testConnectorThrowsExceptions() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenThrow(new IOException());
        batchSender.enqueue(activity);
        sleep(100);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}