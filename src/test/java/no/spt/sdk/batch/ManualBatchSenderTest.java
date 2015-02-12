package no.spt.sdk.batch;

import no.spt.sdk.Constants;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.DataCollectorConnector;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ManualBatchSenderTest {

    @Mock
    private DataCollectorConnector dataCollectorConnector;
    private ManualBatchSender batchSender;
    private Options options;

    @Before
    public void setUp() throws Exception {
        options = TestData.getDefaultOptions();
        batchSender = new ManualBatchSender(options, dataCollectorConnector);
    }

    @After
    public void tearDown() throws Exception {
        batchSender.close();
    }

    @Test
    public void testFlushEmptyQueue() throws Exception {
        batchSender.flush();
        verify(dataCollectorConnector, times(0)).send(anyList());
    }


    @Test
    public void testFlushNotEmptyQueue() throws Exception {
        Activity activity = TestData.getTestActivity();
        batchSender.enqueue(activity);
        batchSender.flush();
        verify(dataCollectorConnector, times(1)).send(Arrays.asList(activity));
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
        verify(dataCollectorConnector, times(2)).send(anyList());
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
        options.setRetries(2);
        when(dataCollectorConnector.send(Arrays.asList(activity))).thenThrow(new IOException())
                                                                  .thenReturn(new DataTrackingResponse(200, null,
                                                                          null));
        batchSender.enqueue(activity);

        batchSender.flush();
        assertEquals(0, batchSender.getQueueDepth());
    }

    @Test(expected = DataTrackingException.class)
    public void testConnectorThrowsExceptions() throws Exception {
        Activity activity = TestData.getTestActivity();
        when(dataCollectorConnector.send(Arrays.asList(activity))).thenThrow(new IOException());
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
}