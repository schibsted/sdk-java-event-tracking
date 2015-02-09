package no.spt.sdk.models;

public class Campaign {

    private String campaignSource;
    private String campaignName;
    private String campaignMedium;

    private Campaign() {

    }

    public static class Builder {
        private Campaign _temp = new Campaign();

        public Builder campaignSource(String campaignSource) {
            _temp.campaignSource = campaignSource;
            return this;
        }

        public Builder campaignName(String campaignName) {
            _temp.campaignName = campaignName;
            return this;
        }

        public Builder campaignMedium(String campaignMedium) {
            _temp.campaignMedium = campaignMedium;
            return this;
        }

        public Campaign build() {
            return _temp;
        }
    }
}
