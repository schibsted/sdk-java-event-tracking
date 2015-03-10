package no.spt.sdk.identity;

import no.spt.sdk.Options;
import no.spt.sdk.TestData;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.serializers.JacksonASJsonConverter;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CachingIdentityConnectorTest {

    @Mock
    private HttpConnection httpConnection;
    private CachingIdentityConnector identityConnector;

    @Before
    public void setUp() throws Exception {
        Options options = TestData.getDefaultOptions();
        identityConnector = new CachingIdentityConnector(options, httpConnection, new JacksonASJsonConverter());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetTrackingId() throws Exception {
        when(httpConnection.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse(200, null,
                "{\n" +
                "    \"code\": 200,\n" +
                "    \"status\": \"OK\",\n" +
                "    \"type\": \"cis_identity\",\n" +
                "    \"data\": {\n" +
                "        \"someKey\": \"someValue\",\n" +
                "        \"environmentId\": \"b3cb8a31-9691-434c-94a5-9a197a3dcc01\",\n" +
                "        \"environmentIdUniqScore\": 50,\n" +
                "        \"environmentIdTemporary\": true,\n" +
                "        \"missingInput\": [\n" +
                "            \"accept\",\n" +
                "            \"accept-charset\",\n" +
                "            \"accept-language\"\n" +
                "        ],\n" +
                "        \"sessionId\": \"18ecfe96-ba40-4ebd-84e9-97186711f890\",\n" +
                "        \"clientIp\": \"127.0.0.1\"\n" +
                "    },\n" +
                "    \"errors\": []\n" +
                "}"));
        assertEquals("18ecfe96-ba40-4ebd-84e9-97186711f890", identityConnector.getTrackingId(TestData
                .getTrackingIdentifiers()).getSessionId());
    }

    @Test(expected = DataTrackingException.class)
    public void testHttpConnectionThrowsException() throws Exception {
        when(httpConnection.send(any(DataTrackingPostRequest.class))).thenThrow(new IOException());
        identityConnector.getTrackingId(TestData.getTrackingIdentifiers());
    }

    @Test(expected = DataTrackingException.class)
    public void testBadRequest() throws Exception {
        when(httpConnection.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse(HttpStatus
                .SC_BAD_REQUEST, null, "{\n" +
                "    \"code\": 400,\n" +
                "    \"status\": \"NOTOK\",\n" +
                "    \"type\": \"cis_identity\",\n" +
                "    \"data\": {\n" +
                "        \"someKey\": \"someValue\",\n" +
                "        \"environmentId\": \"b3cb8a31-9691-434c-94a5-9a197a3dcc01\",\n" +
                "        \"environmentIdUniqScore\": 50,\n" +
                "        \"environmentIdTemporary\": true,\n" +
                "        \"missingInput\": [\n" +
                "            \"accept\",\n" +
                "            \"accept-charset\",\n" +
                "            \"accept-language\"\n" +
                "        ],\n" +
                "        \"sessionId\": \"18ecfe96-ba40-4ebd-84e9-97186711f890\",\n" +
                "        \"clientIp\": \"127.0.0.1\"\n" +
                "    },\n" +
                "    \"errors\": []\n" +
                "}"));
        identityConnector.getTrackingId(TestData.getTrackingIdentifiers());
    }

    @Test(expected = DataTrackingException.class)
    public void testUnexpectedResponse() throws Exception {
        when(httpConnection.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse(HttpStatus
                .SC_NOT_IMPLEMENTED, null, "{\n" +
                "    \"code\": 501,\n" +
                "    \"status\": \"NOTOK\",\n" +
                "    \"type\": \"cis_identity\",\n" +
                "    \"data\": {\n" +
                "        \"someKey\": \"someValue\",\n" +
                "        \"environmentId\": \"b3cb8a31-9691-434c-94a5-9a197a3dcc01\",\n" +
                "        \"environmentIdUniqScore\": 50,\n" +
                "        \"environmentIdTemporary\": true,\n" +
                "        \"missingInput\": [\n" +
                "            \"accept\",\n" +
                "            \"accept-charset\",\n" +
                "            \"accept-language\"\n" +
                "        ],\n" +
                "        \"sessionId\": \"18ecfe96-ba40-4ebd-84e9-97186711f890\",\n" +
                "        \"clientIp\": \"127.0.0.1\"\n" +
                "    },\n" +
                "    \"errors\": []\n" +
                "}"));
        identityConnector.getTrackingId(TestData.getTrackingIdentifiers());
    }

    @Test(expected = DataTrackingException.class)
    public void testUnexpectedResponseDataFormat() throws Exception {
        when(httpConnection.send(any(DataTrackingPostRequest.class))).thenReturn(new DataTrackingResponse(200, null,
                "{\n" +
                "    \"code\": 200,\n" +
                "    \"status\": \"OK\",\n" +
                "    \"type\": \"cis_identity\",\n" +
                "    \"info\": {\n" +                           // Should be data
                "        \"someKey\": \"someValue\",\n" +
                "        \"environmentId\": \"b3cb8a31-9691-434c-94a5-9a197a3dcc01\",\n" +
                "        \"environmentIdUniqScore\": 50,\n" +
                "        \"environmentIdTemporary\": true,\n" +
                "        \"missingInput\": [\n" +
                "            \"accept\",\n" +
                "            \"accept-charset\",\n" +
                "            \"accept-language\"\n" +
                "        ],\n" +
                "        \"sessionId\": \"18ecfe96-ba40-4ebd-84e9-97186711f890\",\n" +
                "        \"clientIp\": \"127.0.0.1\"\n" +
                "    },\n" +
                "    \"errors\": []\n" +
                "}"));
        identityConnector.getTrackingId(TestData.getTrackingIdentifiers());
    }

}