package no.spt.sdk.client;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.batch.Sender;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.ErrorCollector;
import no.spt.sdk.exceptions.error.TrackingIdentityError;
import no.spt.sdk.identity.IdentityConnector;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Actor;
import no.spt.sdk.models.TrackingIdentity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static no.spt.sdk.models.Makers.activity;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsynchronousIdentifyingDataTrackerTest {

    private AsynchronousIdentifyingDataTracker asynchronousIdentifyingDataTracker;
    Options options;
    @Mock
    Sender sender;
    @Mock
    IdentityConnector identityConnector;
    @Mock
    ErrorCollector errorCollector;

    @Before
    public void setUp() throws Exception {
        options = TestData.getDefaultOptions();
        asynchronousIdentifyingDataTracker = new AsynchronousIdentifyingDataTracker(options, sender, identityConnector, errorCollector);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testIdentifyAndTrack() throws Exception {
        Activity activity = TestData.getTestActivity();
        Map<String, String> identifiers = TestData.getTrackingIdentifiers();
        when(identityConnector.getTrackingId(identifiers)).thenReturn(new TrackingIdentity());
        asynchronousIdentifyingDataTracker.identifyActorAndTrack(identifiers, activity);
        sleep(200);
        verify(identityConnector, times(1)).getTrackingId(identifiers);
        verify(sender, times(1)).enqueue(any(Activity.class));
    }

    @Test
    public void testThatCloseShutsDownExecutor() {
        try {
            asynchronousIdentifyingDataTracker.close();
        } catch (DataTrackingException e) {
            e.printStackTrace();
        }
        sleep(1000);
        Map<String, String> identifiers = TestData.getTrackingIdentifiers();
        Activity activity = TestData.getTestActivity();
        asynchronousIdentifyingDataTracker.identifyActorAndTrack(identifiers, activity);
    }

    @Test
    public void testIdentityConnectorThrowsDataTrackingException() throws Exception {
        Activity activity = TestData.getTestActivity();
        Map<String, String> identifiers = TestData.getTrackingIdentifiers();
        when(identityConnector.getTrackingId(identifiers)).thenThrow(new DataTrackingException("An error",
            TrackingIdentityError.HTTP_CONNECTION_ERROR));
        asynchronousIdentifyingDataTracker.identifyActorAndTrack(identifiers, activity);
        sleep(200);
        verify(identityConnector, times(1)).getTrackingId(identifiers);
        verify(sender, times(0)).enqueue(any(Activity.class));
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testSenderThrowsDataTrackingException() throws Exception {
        Activity activity = TestData.getTestActivity();
        Map<String, String> identifiers = TestData.getTrackingIdentifiers();
        doThrow(new DataTrackingException("An error", TrackingIdentityError.HTTP_CONNECTION_ERROR))
            .when(sender).enqueue(any(Activity.class));
        when(identityConnector.getTrackingId(identifiers)).thenReturn(new TrackingIdentity());
        asynchronousIdentifyingDataTracker.identifyActorAndTrack(identifiers, activity);
        sleep(200);
        verify(identityConnector, times(1)).getTrackingId(identifiers);
        verify(sender, times(1)).enqueue(any(Activity.class));
        verify(errorCollector, times(1)).collect(any(DataTrackingException.class));
    }

    @Test
    public void testIdentifyAndTrackUsingUnidentifiedActorBuilder() throws Exception {
        Activity activity = activity("Send", TestData.createProvider(), Actor.getUnidentifiedActorBuilder().build(),
            TestData.createObject()).build();
        Map<String, String> identifiers = TestData.getTrackingIdentifiers();
        when(identityConnector.getTrackingId(identifiers)).thenReturn(new TrackingIdentity());
        asynchronousIdentifyingDataTracker.identifyActorAndTrack(identifiers, activity);
        sleep(200);
        verify(identityConnector, times(1)).getTrackingId(identifiers);
        verify(sender, times(1)).enqueue(any(Activity.class));
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}