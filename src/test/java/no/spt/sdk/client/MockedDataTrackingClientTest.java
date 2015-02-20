package no.spt.sdk.client;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.batch.ISender;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.IErrorCollector;
import no.spt.sdk.models.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockedDataTrackingClientTest {

    @Mock
    private IErrorCollector errorCollector;
    @Mock
    private ISender sender;
    private DataTrackingClient client;
    private Options options;

    @Before
    public void setUp() throws Exception {
        options = new Options("http://localhost:8090/", "http://localhost:8091/", 10000, 1000, 2);
        client = new DataTrackingClient.Builder()
                .withOptions(options)
                .withActivitySender(sender)
                .withErrorCollector(errorCollector)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testSendingOneActivitySenderEnqueueThrowsException() throws Exception {
        doThrow(new DataTrackingException("An error occurred")).when(sender).enqueue(any(Activity.class));
        Activity activity = TestData.getTestActivity();
        client.send(activity);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testTrackingAndSendingOneActivitySenderFlushThrowsException() throws Exception {
        doThrow(new DataTrackingException("An error occurred")).when(sender).flush();
        Activity activity = TestData.getTestActivity();
        client.track(activity);
        client.send();
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testClosingClientSenderCloseThrowsException() throws Exception {
        doThrow(new DataTrackingException("An error occurred")).when(sender).close();
        client.close();
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

}
