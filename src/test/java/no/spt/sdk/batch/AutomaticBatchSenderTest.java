package no.spt.sdk.batch;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpClientConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.LoggingErrorCollector;
import no.spt.sdk.exceptions.error.ActivitySendingError;
import no.spt.sdk.models.Activity;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;
import no.spt.sdk.stats.DataTrackingStats;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    private DataTrackingStats stats = new DataTrackingStats();

    @Before
    public void setUp() throws Exception {
        options = new Options.Builder("abc123").setDataCollectorUrl("http://localhost:8090/")
            .setCISUrl("http://localhost:8091/")
            .setMaxQueueSize(1000)
            .setTimeout(1000)
            .setRetries(2)
            .build();
        batchSender = new AutomaticBatchSender(options, dataCollectorConnector, errorCollector, jsonConverter, stats);
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
        assertEquals(1, stats.getQueuedActivitiesCount());

        batchSender.enqueue(activity);
        sleep(200);
        assertEquals(0, batchSender.getQueueDepth());
        assertEquals(2, stats.getQueuedActivitiesCount());
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
        assertEquals(options.getMaxActivityBatchSize() + 1, stats.getQueuedActivitiesCount());
        assertEquals(2, stats.getSentBatchesCount());
        verify(dataCollectorConnector, times(2)).send(any(DataTrackingPostRequest.class));
    }

    @Test
    public void testEnqueueMoreThanMaxQueueSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (200, null, "OK"));
        try {
            for (int i = 0; i <= options.getMaxQueueSize() * 2; i++) { // Assuming the client cannot send quick enough
                batchSender.enqueue(activity);
            }
            fail("Expected a DataTrackingException to be thrown");
        } catch (DataTrackingException exception) {
            assertEquals(ActivitySendingError.QUEUE_MAX_SIZE_REACHED, exception.getError());
        }
        assertEquals(1, stats.getDroppedCount());
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
                (400, null, TestData.getDataCollectorBadRequestAsJsonString()));
        batchSender.enqueue(activity);
        sleep(200);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
        assertEquals(1, stats.getValidationFailedCount());
    }

    @Test
    public void testHttpConnectionReturnsMultiStatus() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (207, null, TestData.getDataCollectorMultiStatusAsJsonString()));
        batchSender.enqueue(activity);
        sleep(200);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
        assertEquals(2, stats.getValidationFailedCount());
    }

    @Test
    public void testHttpConnectionReturnsUnexpectedResponse() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (409, null, "Unexpected error"));
        batchSender.enqueue(activity);
        sleep(200);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
        assertEquals(1, stats.getSendingFailedCount());
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
        assertEquals(0, stats.getSentBatchesCount());
        assertEquals(1, stats.getSendingFailedCount());
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}