package no.spt.sdk.identity;

import no.spt.sdk.models.TrackingIdentity;

public interface IdentityCallback {
    void onSuccess(TrackingIdentity trackingId);
}
