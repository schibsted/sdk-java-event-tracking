package no.spt.sdk.identity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import no.spt.sdk.Options;
import no.spt.sdk.client.DataTrackingPostRequest;
import no.spt.sdk.client.DataTrackingResponse;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.models.AnonymousIdentity;
import no.spt.sdk.serializers.ASJsonConverter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A connection to the Anonymous Identity Service used to get anonymous IDs that caches fetched IDs.
 *
 */
public class CachingIdentityConnector implements IdentityConnector {

    private Options options;
    private HttpConnection httpConnection;
    private ASJsonConverter jsonConverter;
    private static LoadingCache<Map<String, String>, AnonymousIdentity> cache;
    private static final int CACHE_SIZE = 10000;
    private static final int CACHE_EXPIRATION_IN_MINUTES = 15;


    public CachingIdentityConnector(Options options, HttpConnection httpConnection, ASJsonConverter jsonConverter) {
        this.options = options;
        this.httpConnection = httpConnection;
        this.jsonConverter = jsonConverter;
        initializeCache();
    }

    private void initializeCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_SIZE)
                .expireAfterWrite(CACHE_EXPIRATION_IN_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<Map<String, String>, AnonymousIdentity>() {
                    @Override
                    public AnonymousIdentity load(Map<String, String> cacheKey) throws Exception {
                        return getIdFromService(cacheKey);
                    }
                });
    }

    @Override
    public AnonymousIdentity getAnonymousId(Map<String, String> identifiers) throws DataTrackingException {
        try {
            return cache.get(identifiers);
        } catch (ExecutionException e) {
            throw new DataTrackingException(e);
        }
    }

    private AnonymousIdentity getIdFromService(Map<String, String> identifiers) throws DataTrackingException {
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
        AnonymousIdentity anonymousIdentity;
        try {
            anonymousIdentity = jsonConverter.deSerializeAnonymousIdentity(response.getRawBody());
        } catch (Exception e) {
            throw new DataTrackingException(e);
        }
        if(anonymousIdentity.getData().isEmpty()) {
            throw new DataTrackingException("Unexpected data from Anonymous Identity Service", response);
        }
        return anonymousIdentity;
    }
}
