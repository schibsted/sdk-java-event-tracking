package no.spt.sdk.batch;

import no.spt.sdk.TestData;
import no.spt.sdk.connection.DataCollectorConnector;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBatchSenderTest {

    @Mock
    private DataCollectorConnector dataCollectorConnector;
    private AutomaticBatchSender batchSender;
    private Options options;

    @Before
    public void setUp() throws Exception {
        options = TestData.getDefaultOptions();
        batchSender = new AutomaticBatchSender(options, dataCollectorConnector, new ErrorCollector());
        batchSender.init();
    }

    @After
    public void tearDown() throws Exception {
        batchSender.close();
    }

    @Test
    public void testEnqueue() throws Exception {
        Activity activity = TestData.getTestActivity();

        batchSender.enqueue(activity);
        sleep(500);
        assertEquals(0, batchSender.getQueueDepth());

        batchSender.enqueue(activity);
        sleep(500);
        assertEquals(0, batchSender.getQueueDepth());
    }

    @Test
    public void testEnqueueMoreThanMaxBatchSize() throws Exception {
        Activity activity = TestData.getTestActivity();
        for(int i = 0; i <= options.getMaxBatchSize(); i++) {
            batchSender.enqueue(activity);
        }
        sleep(500);
        assertEquals(0, batchSender.getQueueDepth());

        verify(dataCollectorConnector, times(2)).send(anyList());
    }

    @Test
    public void testFlush() throws Exception {
        Activity activity = TestData.getTestActivity();
        batchSender.enqueue(activity);
        batchSender.flush();
        sleep(500);
        assertEquals(0, batchSender.getQueueDepth());
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
    public void testGetQueueDepth() throws Exception {
        // TODO find a way to test this without flickering
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}