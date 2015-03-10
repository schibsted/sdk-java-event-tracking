package no.spt.sdk.exceptions;

import no.spt.sdk.TestData;
import org.junit.Before;
import org.junit.Test;

public class LoggingErrorCollectorTest {

    LoggingErrorCollector errorCollector;

    @Before
    public void setUp() throws Exception {
        errorCollector = new LoggingErrorCollector();

    }

    @Test
    public void testCollectDataTrackingException() throws Exception {
        DataTrackingException e = TestData.getDataTrackingException();
        errorCollector.collect(e);
    }

    @Test
    public void testCollectCommunicationDataTrackingException() throws Exception {
        CommunicationDataTrackingException e = TestData.getCommunicationDataTrackingException();
        errorCollector.collect(e);
    }
}