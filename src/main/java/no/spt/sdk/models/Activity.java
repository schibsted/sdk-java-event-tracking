package no.spt.sdk.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity {

    private static final DateFormat ISO_8601_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);

    private String verb;
    private String published;
    private String language;
    private ASObject actor;
    private ASObject generator;
    private ASObject object;
    private ASObject target;
    private ASObject result;

    private Activity() {

    }

    public static class Builder {

        private Activity _temp = new Activity();

        public Builder actor(ASObject.Builder builder) {
            _temp.actor = builder.build();
            return this;
        }

        public Builder generator(ASObject.Builder builder) {
            _temp.generator = builder.build();
            return this;
        }

        public Builder target(ASObject.Builder builder) {
            _temp.target = builder.build();
            return this;
        }

        public Builder language(String language) {
            _temp.language = language;
            return this;
        }

        public Builder verb(String verb) {
            _temp.verb = verb;
            return this;
        }

        public Builder published(String published) {
            _temp.published = published;
            return this;
        }

        public Builder published(Date published) {
            _temp.published = ISO_8601_FORMAT.format(published);
            return this;
        }

        public Builder publishedNow() {
            Calendar currentTime = Calendar.getInstance();
            _temp.published = ISO_8601_FORMAT.format(currentTime.getTime());
            return this;
        }

        public Builder object(ASObject.Builder builder) {
            _temp.object = builder.build();
            return this;
        }

        public Activity build() {
            return _temp;
        }
    }


}
