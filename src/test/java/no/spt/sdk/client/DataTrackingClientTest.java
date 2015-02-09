package no.spt.sdk.client;


import no.spt.sdk.TestData;
import no.spt.sdk.models.Activity;
import no.spt.sdk.models.Options;
import no.spt.sdk.serializers.ASJsonConverter;
import no.spt.sdk.serializers.GsonASJsonConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.verify.VerificationTimes;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class DataTrackingClientTest {

    private static final String DATA_COLLECTOR_URL = "http://localhost:8090/";
    private static final int MAX_QUEUE_SIZE = 10000;
    private static final int MAX_BATCH_SIZE = 20;
    private static final int TIMEOUT = 5000;
    private static final int RETRIES = 2;
    private static final int BACKOFF = 1000;
    private static final boolean SEND_AUTOMATIC = true;
    private DataTrackingClient client;
    private ClientAndServer mockServer;
    private ASJsonConverter jsonConverter;

    @Before
    public void setup() {
        client = new DataTrackingClient(new Options(DATA_COLLECTOR_URL, MAX_QUEUE_SIZE, MAX_BATCH_SIZE, TIMEOUT,
                RETRIES, BACKOFF, SEND_AUTOMATIC));
        jsonConverter = new GsonASJsonConverter();
    }

    @Before
    public void setupMockServer() {
        mockServer = startClientAndServer(8090);
        mockServer.when(request().withPath("/"))
                  .respond(response().withHeaders(new Header("Content-Type", "application/json"))
                                     .withBody("OK"));
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @After
    public void tearDownMockServer() throws Exception {
        mockServer.stop();
    }

    @Test
    public void testSendingOneActivityManually() throws Exception {
        Activity activity = TestData.getTestActivity();
        client.send(activity);
        sleep(500);
        mockServer.verify(request().withPath("/")
                                   .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                                   .withBody(jsonConverter.serialize(Arrays.asList(activity))), VerificationTimes.exactly(1));
    }

    @Test
    public void testQueueingActivitiesAndSendingManually() throws Exception {
        int noActivites = MAX_BATCH_SIZE + 1;
        trackActivities(noActivites);
        client.send();
        sleep(500);
        mockServer.verify(request().withPath("/")
                                   .withHeaders(new Header("Content-Type", "application/json; charset=utf-8")),
                VerificationTimes.exactly((int) Math.ceil(((double) noActivites) / MAX_BATCH_SIZE)));
    }


    @Test
    public void testSendingMultipleActivities() throws Exception {
        trackActivities(1000);
        client.send();
        client.close();
        sleep(500);
        assertEquals(0, client.getQueueDepth());
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void trackActivities(int noActivities) throws Exception {
        for (int i = 0; i < noActivities; i++) {
            Activity activity = TestData.getTestActivity();
            client.track(activity);
        }
    }

}
