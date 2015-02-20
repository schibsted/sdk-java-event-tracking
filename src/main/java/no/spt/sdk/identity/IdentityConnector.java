package no.spt.sdk.identity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonObject;
import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.IHttpConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.serializers.GsonASJsonConverter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class IdentityConnector {

    private Options options;
    private IHttpConnection httpConnection;
    private GsonASJsonConverter jsonConverter;
    private static LoadingCache<Map<String, String>, String> cache;
    private static final int CACHE_SIZE = 10000;
    private static final int CACHE_EXPIRATION_IN_MINUTES = 15;


    public IdentityConnector(Options options, IHttpConnection httpConnection) {
        this.options = options;
        this.httpConnection = httpConnection;
        this.jsonConverter = new GsonASJsonConverter();
        initializeCache();
    }

    private void initializeCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_SIZE)
                .expireAfterWrite(CACHE_EXPIRATION_IN_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<Map<String, String>, String>() {
                    @Override
                    public String load(Map<String, String> cacheKey) throws Exception {
                        return getIdFromService(cacheKey);
                    }
                });
    }

    public String getAnonymousId(Map<String, String> identifiers) throws DataTrackingException {
        try {
            return cache.get(identifiers);
        } catch (ExecutionException e) {
            throw new DataTrackingException(e);
        }
    }

    private String getIdFromService(Map<String, String> identifiers) throws DataTrackingException {
        DataTrackingResponse response;
        try {
            response = httpConnection.send(new DataTrackingPostRequest(options.getAnonymousIdUrl(), null, jsonConverter.serialize(identifiers)));
        } catch (IOException e) {
            throw new DataTrackingException(e);
        }
        if(response.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
            throw new DataTrackingException("Response from Anonymous Identity Service was not OK", response);
        } else if(response.getResponseCode() != HttpStatus.SC_OK) {
            throw new DataTrackingException("Unexpected response from Anonymous Identity Service", response);
        }
        try {
            JsonObject jsonObject = jsonConverter.deSerialize(response.getRawBody());
            return jsonObject.get("data").getAsJsonObject().get("sessionId").getAsString();
        } catch (Exception e) {
            throw new DataTrackingException(e);
        }
    }
}
