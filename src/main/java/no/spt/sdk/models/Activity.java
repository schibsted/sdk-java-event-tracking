package no.spt.sdk.models;


import no.spt.sdk.TrackingUtil;

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

    private final List<Object> context;
    private final String id;
    private final String type;
    private final String published;
    private final ASObject actor;
    private final ASObject provider;
    private final ASObject object;
    private final ASObject target;

    private Activity(String type, String published, ASObject actor, ASObject provider, ASObject object, ASObject target) {
        this.type = type;
        this.published = published;
        this.actor = actor;
        this.provider = provider;
        this.object = object;
        this.target = target;
        Map<String, String> map = new HashMap<String, String>();
        map.put("spt", "http://schema.schibsted.com/activitystreams");
        map.put("spt:sdkType", "JAVA");
        map.put("spt:sdkVersion", TrackingUtil.getSdkVersion());
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

        private String type;
        private String published;
        private ASObject actor;
        private ASObject provider;
        private ASObject object;
        private ASObject target;

        public Builder(String type) {
            this.type = type;
        }

        public Builder(Activity activity) {
            this.type = activity.getType();
            this.published = activity.getPublished();
            this.actor = activity.getActor();
            this.provider = activity.getProvider();
            this.object = activity.getObject();
            this.target = activity.getTarget();
        }

        public Builder object(ASObject object) {
            this.object = object;
            return this;
        }

        public Builder object(ASObject.Builder builder) {
            return object(builder.build());
        }

        public Builder actor(Actor actor) {
            this.actor = actor;
            return this;
        }

        public Builder actor(Actor.Builder builder) {
            return actor(builder.build());
        }

        public Builder provider(Provider provider) {
            this.provider = provider;
            return this;
        }

        public Builder provider(Provider.Builder builder) {
            return provider(builder.build());
        }

        public Builder target(Target target) {
            this.target = target;
            return this;
        }

        public Builder target(Target.Builder builder) {
            return target(builder.build());
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder published(String published) {
            this.published = published;
            return this;
        }

        public Builder published(Date published) {
            this.published = ISO_8601_FORMAT.format(published);
            return this;
        }

        public Builder publishedNow() {
            Calendar currentTime = Calendar.getInstance();
            this.published = ISO_8601_FORMAT.format(currentTime.getTime());
            return this;
        }

        public Activity build() {
            if(published == null || published.isEmpty()) {
                publishedNow();
            }
            if(provider == null) {
                throw new IllegalStateException("The Activity builder is missing a provider");
            }
            if(actor == null) {
                throw new IllegalStateException("The Activity builder is missing an actor");
            }
            if(object == null) {
                throw new IllegalStateException("The Activity builder is missing an object");
            }
            if(type == null || type.isEmpty()) {
                throw new IllegalStateException("The Activity builder is missing a type");
            }
            return new Activity(type, published, actor, provider, object, target);
        }
    }

}
