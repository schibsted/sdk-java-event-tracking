package no.spt.sdk.exceptions;

import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LoggingErrorCollectorTest {

    LoggingErrorCollector errorCollector;

    @Before
    public void setUp() throws Exception {
        errorCollector = new LoggingErrorCollector();

    }

    @Test
    public void testCollect() throws Exception {
        Map<String, String> header = new HashMap<String, String>();
        header.put("contentType", "application/json");
        DataTrackingResponse response = new DataTrackingResponse(200, header, "Message body");
        DataTrackingException e = TestData.getDataTrackingException();
        errorCollector.collect(e);
    }
}