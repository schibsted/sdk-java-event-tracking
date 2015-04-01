package no.spt.sdk.identity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import no.spt.sdk.Options;
import no.spt.sdk.connection.HttpConnection;
import no.spt.sdk.exceptions.DataTrackingException;
import no.spt.sdk.exceptions.error.TrackingIdentityError;
import no.spt.sdk.models.TrackingIdentity;
import no.spt.sdk.serializers.ASJsonConverter;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A connection to the Central Identification Service, used to get tracking IDs that caches fetched IDs.
 */
public class CachingIdentityConnector extends SimpleIdentityConnector {

    private static final int CACHE_SIZE = 10000;
    private static final int CACHE_EXPIRATION_IN_MINUTES = 15;
    private static LoadingCache<Map<String, String>, TrackingIdentity> cache;


    public CachingIdentityConnector(Options options, HttpConnection httpConnection, ASJsonConverter jsonConverter) {
        super (options, httpConnection, jsonConverter);
        initializeCache();
    }

    private void initializeCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_SIZE)
                .expireAfterWrite(CACHE_EXPIRATION_IN_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<Map<String, String>, TrackingIdentity>() {
                    @Override
                    public TrackingIdentity load(Map<String, String> cacheKey) throws Exception {
                        return getIdFromService(cacheKey);
                    }
                });
    }

    @Override
    public TrackingIdentity getTrackingId(Map<String, String> identifiers) throws DataTrackingException {
        try {
            return cache.get(identifiers);
        } catch (ExecutionException e) {
            throw new DataTrackingException(e, TrackingIdentityError.CACHE_ERROR);
        }
    }

}
