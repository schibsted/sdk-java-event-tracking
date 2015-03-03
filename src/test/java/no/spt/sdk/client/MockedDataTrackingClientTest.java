package no.spt.sdk.client;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.batch.Sender;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.identity.IdentityConnector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.AnonymousIdentity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockedDataTrackingClientTest {

    @Mock
    private ErrorCollector errorCollector;
    @Mock
    private Sender sender;
    @Mock
    private IdentityConnector identityConnector;
    private DataTrackingClient client;
    private Options options;

    @Before
    public void setUp() throws Exception {
        options = new Options.Builder().setDataCollectorUrl("http://localhost:8090/")
                .setAnonymousIdUrl("http://localhost:8091/")
                .setMaxQueueSize(10000)
                .setTimeout(1000)
                .setRetries(2)
                .build();
        client = new DataTrackingClient.Builder()
                .withOptions(options)
                .withActivitySender(sender)
                .withErrorCollector(errorCollector)
                .withIdentityConnector(identityConnector)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testSendingOneActivitySenderEnqueueThrowsException() throws Exception {
        doThrow(TestData.getDataTrackingException()).when(sender).enqueue(any(Activity.class));
        Activity activity = TestData.getTestActivity();
        client.send(activity);
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testTrackingAndSendingOneActivitySenderFlushThrowsException() throws Exception {
        doThrow(TestData.getDataTrackingException()).when(sender).flush();
        Activity activity = TestData.getTestActivity();
        client.track(activity);
        client.send();
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testClosingClientSenderCloseThrowsException() throws Exception {
        doThrow(TestData.getDataTrackingException()).when(sender).close();
        client.close();
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testGetAnonymousId() throws DataTrackingException {
        when(identityConnector.getAnonymousId(any(Map.class))).thenReturn(new AnonymousIdentity(getDummyMap()));
        assertEquals("id123", client.getAnonymousId(TestData.getAnonymousIdIdentifiers()).getSessionId());
    }

    private Map<String, Object> getDummyMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sessionId", "id123");
        return map;
    }

}
