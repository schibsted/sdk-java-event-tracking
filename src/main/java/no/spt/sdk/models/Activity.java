package no.spt.sdk.models;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.UUID.randomUUID;

/**
 * Represents an Activity Streams 2.0 Activity
 *
 * @see <a href="http://www.w3.org/TR/activitystreams-core/#activities">http://www.w3
 * .org/TR/activitystreams-core/#activities</a>
 */
public class Activity {

    private static final DateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);

    private List<Object> context;
    private String id;
    private String type;
    private String published;
    private ASObject actor;
    private ASObject provider;
    private ASObject object;
    private ASObject target;

    private Activity() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("spt", "http://schema.schibsted.com/activitystreams");
        map.put("spt:sdkType", "JAVA");
        map.put("spt:sdkVersion", getClass().getPackage().getImplementationVersion());
        context = Arrays.asList("http://www.w3.org/ns/activitystreams", map);
        id = randomUUID().toString();
    }

    public List<Object> getContext() {
        return context;
    }

    public  String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPublished() {
        return published;
    }

    public ASObject getActor() {
        return actor;
    }

    public ASObject getProvider() {
        return provider;
    }

    public ASObject getObject() {
        return object;
    }

    public ASObject getTarget() {
        return target;
    }

    public static class Builder {

        private Activity _temp = new Activity();

        public Builder(String type) {
            type(type);
        }

        public Builder object(ASObject object) {
            _temp.object = object;
            return this;
        }

        public Builder object(ASObject.Builder builder) {
            return object(builder.build());
        }

        public Builder actor(Actor actor) {
            _temp.actor = actor;
            return this;
        }

        public Builder actor(Actor.Builder builder) {
            return actor(builder.build());
        }

        public Builder provider(Provider provider) {
            _temp.provider = provider;
            return this;
        }

        public Builder provider(Provider.Builder builder) {
            return provider(builder.build());
        }

        public Builder target(Target target) {
            _temp.target = target;
            return this;
        }

        public Builder target(Target.Builder builder) {
            return target(builder.build());
        }

        public Builder type(String type) {
            _temp.type = type;
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

        public Activity build() {
            return _temp;
        }
    }

}
