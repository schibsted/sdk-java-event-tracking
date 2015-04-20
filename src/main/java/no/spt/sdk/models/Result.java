package no.spt.sdk.models;

/**
 * Represents an Activity Streams 2.0 Result
 *
 */
public class Result extends ASObject {

    private Result(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractBuilder<Result, Builder> {

        public Builder(String type, String id) {
            super(type, id);
        }

        public Result build() {
            return new Result(this);
        }
    }
}
