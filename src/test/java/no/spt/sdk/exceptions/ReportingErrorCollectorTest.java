package no.spt.sdk.exceptions;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.JacksonASJsonConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportingErrorCollectorTest {

    @Mock
    HttpConnection httpConnection;
    ASJsonConverter jsonConverter;
    ReportingErrorCollector errorCollector;
    Options options;


    @Before
    public void setUp() throws Exception {
        options = new Options.Builder("abc123").build();
        jsonConverter = new JacksonASJsonConverter();
        errorCollector = new ReportingErrorCollector(options, httpConnection, jsonConverter);
    }

    @Test
    public void testCollect() throws Exception {
        DataTrackingException e = TestData.getDataTrackingException();
        errorCollector.collect(e);
        sleep(200);
        verify(httpConnection, times(0)).send(any(DataTrackingPostRequest.class));
    }

    @Test
    public void testCollectMoreThanMaxQueueSize() throws Exception {
        DataTrackingException e = TestData.getDataTrackingException();
        for(int i = 0; i <= 20; i++) {
            errorCollector.collect(e);
        }
        sleep(200);
        verify(httpConnection, times(1)).send(any(DataTrackingPostRequest.class));
    }

    @Test
    public void testCollect100Exceptions() throws Exception {
        DataTrackingException e = TestData.getDataTrackingException();
        for(int i = 0; i <= 100; i++) {
            errorCollector.collect(e);
        }
        sleep(500);
        verify(httpConnection, times(5)).send(any(DataTrackingPostRequest.class));
    }

    @Test
    public void testCollectHttpConnectionThrowsIOE() throws Exception {
        when(httpConnection.send(any(DataTrackingPostRequest.class))).thenThrow(new IOException());
        DataTrackingException e = TestData.getDataTrackingException();
        for(int i = 0; i <= 20; i++) {
            errorCollector.collect(e);
        }
        sleep(200);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}