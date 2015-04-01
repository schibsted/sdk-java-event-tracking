package no.spt.sdk.client;

import no.spt.sdk.models.TrackingIdentity;

public interface IdentityCallback {
    void onSuccess(TrackingIdentity trackingId);
}
