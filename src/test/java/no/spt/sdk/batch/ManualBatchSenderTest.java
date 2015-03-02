package no.spt.sdk.batch;

import no.spt.sdk.Constants;
import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpClientConnection;
import no.spt.sdk.exceptions.DataTrackingException;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ManualBatchSenderTest {

    @Mock
    private HttpClientConnection dataCollectorConnector;
    private ManualBatchSender batchSender;
    private Options options;
    private static ASJsonConverter jsonConverter = new GsonASJsonConverter();

    @Before
    public void setUp() throws Exception {
        options = new Options.Builder().setDataCollectorUrl("http://localhost:8090/")
                .setAnonymousIdUrl("http://localhost:8091/")
                .setMaxQueueSize(1000)
                .setTimeout(1000)
                .setRetries(2)
                .build();
        batchSender = new ManualBatchSender(options, dataCollectorConnector, jsonConverter);
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse(200, null, "OK"));
    }

    @After
    public void tearDown() throws Exception {
        batchSender.close();
    }

    @Test
    public void testFlushEmptyQueue() throws Exception {
        batchSender.flush();
        verify(dataCollectorConnector, times(0)).send(any(DataTrackingPostRequest.class));
    }

    @Test
    public void testFlushNotEmptyQueue() throws Exception {
        Activity activity = TestData.getTestActivity();
        DataTrackingPostRequest request = TestData.getTestDataTrackingPostRequest(options);
        batchSender.enqueue(activity);
        batchSender.flush();
        verify(dataCollectorConnector, times(1)).send(any(DataTrackingPostRequest.class));
    }

    @Test
    public void testEnqueue() throws Exception {
        Activity activity = TestData.getTestActivity();
        batchSender.enqueue(activity);
        assertEquals(1, batchSender.getQueueDepth());
        batchSender.enqueue(activity);
        assertEquals(2, batchSender.getQueueDepth());
    }

    @Test
    public void testEnqueueMoreThanMaxBatchSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        for(int i = 0; i <= Constants.MAX_BATCH_SIZE; i++) {
            batchSender.enqueue(activity);
        }
        assertEquals(Constants.MAX_BATCH_SIZE + 1, batchSender.getQueueDepth());
        batchSender.flush();
        assertEquals(0, batchSender.getQueueDepth());
        verify(dataCollectorConnector, times(2)).send(any(DataTrackingPostRequest.class));
    }

    @Test(expected = DataTrackingException.class)
    public void testEnqueueMoreThanMaxQueueSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        for(int i = 0; i <= options.getMaxQueueSize() + 1; i++) {
            batchSender.enqueue(activity);
        }
    }

    @Test
    public void testClose() throws Exception {
        Activity activity = TestData.getTestActivity();
        batchSender.enqueue(activity);
        batchSender.close();
        assertEquals(0, batchSender.getQueueDepth());
        verify(dataCollectorConnector, times(1)).close();
    }

    @Test
     public void testConnectorThrowsOneException() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(asRequest(Arrays.asList(activity)))).thenThrow(new IOException())
                                                                  .thenReturn(new DataTrackingResponse(200, null,
                                                                          null));
        batchSender.enqueue(activity);

        batchSender.flush();
        assertEquals(0, batchSender.getQueueDepth());
    }

    @Test(expected = DataTrackingException.class)
    public void testConnectorThrowsExceptions() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class)))
                .thenThrow(new IOException());
        batchSender.enqueue(activity);

        batchSender.flush();
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


    @Test(expected = DataTrackingException.class)
    public void testHttpConnectionReturnsBadRequest() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse(400, null, "Not OK"));
        batchSender.enqueue(activity);
        batchSender.flush();
    }

    @Test(expected = DataTrackingException.class)
    public void testHttpConnectionReturnsMultiStatus() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse(207, null, "Not OK"));
        batchSender.enqueue(activity);
        batchSender.flush();
    }

    @Test(expected = DataTrackingException.class)
    public void testHttpConnectionReturnsUnexpectedResponse() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse
                (409, null, "Unexpected error"));
        batchSender.enqueue(activity);
        batchSender.flush();
    }

    private DataTrackingPostRequest asRequest(List<Activity> activities) throws IOException {
        return new DataTrackingPostRequest(options.getDataCollectorUrl(), null, jsonConverter.serialize(activities));
    }

}