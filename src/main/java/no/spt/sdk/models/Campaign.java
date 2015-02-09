package no.spt.sdk.models;

public class Campaign extends ASObject {

    public Campaign(Builder builder) {
        super(builder);
    }

    public static class Builder extends ASObject.Builder {

        public Builder() {
        }

        public Builder campaignSource(String campaignSource) {
            set("campaignSource", campaignSource);
            return this;
        }

        public Builder campaignName(String campaignName) {
            set("campaignName", campaignName);
            return this;
        }

        public Builder campaignMedium(String campaignMedium) {
            set("campaignMedium", campaignMedium);
            return this;
        }

        public Campaign build() {
            return new Campaign(this);
        }
    }

}
