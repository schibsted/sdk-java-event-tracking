package no.spt.sdk.batch;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpClientConnection;
import no.spt.sdk.exceptions.DataTrackingException;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ManualBatchSenderTest {

    private static ASJsonConverter jsonConverter = new GsonASJsonConverter();
    @Mock
    private HttpClientConnection dataCollectorConnector;
    private ManualBatchSender batchSender;
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
        batchSender = new ManualBatchSender(options, dataCollectorConnector, jsonConverter, stats);
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
            (200, null, "OK"));
    }

    @After
    public void tearDown() throws Exception {
        batchSender.close();
    }

    @Test
    public void testFlushEmptyQueue() throws Exception {
        batchSender.flush();
        verify(dataCollectorConnector, times(0)).send(any(DataTrackingPostRequest.class));
        assertEquals(0, stats.getSentBatchesCount());
    }

    @Test
    public void testFlushNotEmptyQueue() throws Exception {
        Activity activity = TestData.getTestActivity();
        DataTrackingPostRequest request = TestData.getTestDataTrackingPostRequest(options);
        batchSender.enqueue(activity);
        batchSender.flush();
        verify(dataCollectorConnector, times(1)).send(any(DataTrackingPostRequest.class));
        assertEquals(1, stats.getSentBatchesCount());
        assertEquals(1, stats.getSuccessfulCount());
    }

    @Test
    public void testEnqueue() throws Exception {
        Activity activity = TestData.getTestActivity();
        batchSender.enqueue(activity);
        assertEquals(1, batchSender.getQueueDepth());
        assertEquals(1, stats.getQueuedActivitiesCount());
        batchSender.enqueue(activity);
        assertEquals(2, batchSender.getQueueDepth());
        assertEquals(2, stats.getQueuedActivitiesCount());
    }

    @Test
    public void testEnqueueMoreThanMaxBatchSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        for (int i = 0; i <= options.getMaxActivityBatchSize(); i++) {
            batchSender.enqueue(activity);
        }
        assertEquals(options.getMaxActivityBatchSize() + 1, stats.getQueuedActivitiesCount());
        assertEquals(options.getMaxActivityBatchSize() + 1, batchSender.getQueueDepth());
        batchSender.flush();
        assertEquals(0, batchSender.getQueueDepth());
        assertEquals(2, stats.getSentBatchesCount());
        verify(dataCollectorConnector, times(2)).send(any(DataTrackingPostRequest.class));
    }

    @Test
    public void testEnqueueMoreThanMaxQueueSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        try {
            for (int i = 0; i <= options.getMaxQueueSize() + 1; i++) {
                batchSender.enqueue(activity);
            }
            fail("Expected a DataTrackingException to be thrown");
        } catch (DataTrackingException exception) {
            assertEquals(ActivitySendingError.QUEUE_MAX_SIZE_REACHED, exception.getError());
        }
        assertEquals(options.getMaxQueueSize(), stats.getQueuedActivitiesCount());
        assertEquals(1, stats.getDroppedCount());
    }

    @Test
    public void testClose() throws Exception {
        Activity activity = TestData.getTestActivity();
        batchSender.enqueue(activity);
        batchSender.close();
        assertEquals(0, batchSender.getQueueDepth());
    }

    @Test
    public void testConnectorThrowsOneException() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(asRequest(Arrays.asList(activity)))).thenThrow(new IOException())
            .thenReturn(new DataTrackingResponse(200, null, null));
        batchSender.enqueue(activity);

        batchSender.flush();
        assertEquals(0, batchSender.getQueueDepth());
        assertEquals(1, stats.getSentBatchesCount());
        assertEquals(0, stats.getDroppedCount());
    }

    @Test
    public void testConnectorThrowsExceptions() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenThrow(new IOException());
        batchSender.enqueue(activity);
        try {
            batchSender.flush();
            fail("Expected a DataTrackingException to be thrown");
        } catch (DataTrackingException exception) {
            assertEquals(ActivitySendingError.HTTP_CONNECTION_ERROR, exception.getError());
        }
        assertEquals(0, stats.getSentBatchesCount());
        assertEquals(1, stats.getSendingFailedCount());
    }

    @Test
    public void testGetQueueDepth() throws Exception {
        Activity activity = TestData.getTestActivity();
        assertEquals(0, batchSender.getQueueDepth());
        batchSender.enqueue(activity);
        assertEquals(1, batchSender.getQueueDepth());
        batchSender.flush();
        assertEquals(0, batchSender.getQueueDepth());
    }


    @Test
    public void testHttpConnectionReturnsBadRequest() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
            (400, null, TestData.getDataCollectorBadRequestAsJsonString()));
        batchSender.enqueue(activity);
        try {
            batchSender.flush();
            fail("Expected a DataTrackingException to be thrown");
        } catch (DataTrackingException exception) {
            assertEquals(ActivitySendingError.BAD_REQUEST, exception.getError());
        }
        assertEquals(1, stats.getValidationFailedCount());
    }

    @Test
    public void testHttpConnectionReturnsMultiStatus() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
            (207, null, TestData.getDataCollectorMultiStatusAsJsonString()));
        batchSender.enqueue(activity);
        try {
            batchSender.flush();
            fail("Expected a DataTrackingException to be thrown");
        } catch (DataTrackingException exception) {
            assertEquals(ActivitySendingError.VALIDATION_ERROR, exception.getError());
        }
        assertEquals(2, stats.getValidationFailedCount());
    }

    @Test
    public void testHttpConnectionReturnsUnexpectedResponse() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
            (409, null, "Unexpected error"));
        batchSender.enqueue(activity);
        try {
            batchSender.flush();
            fail("Expected a DataTrackingException to be thrown");
        } catch (DataTrackingException exception) {
            assertEquals(ActivitySendingError.UNEXPECTED_RESPONSE, exception.getError());
        }
        assertEquals(1, stats.getSendingFailedCount());
    }

    private DataTrackingPostRequest asRequest(List<Activity> activities) throws IOException {
        return new DataTrackingPostRequest(options.getDataCollectorUrl(), null, jsonConverter.serialize(activities));
    }

}