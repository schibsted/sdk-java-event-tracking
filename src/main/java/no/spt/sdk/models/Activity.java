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
    private final Actor actor;
    private final Provider provider;
    private final ASObject object;
    private final Target target;

    private Activity(String type, String published, Actor actor, Provider provider, ASObject object, Target target) {
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

    public Actor getActor() {
        return actor;
    }

    public Provider getProvider() {
        return provider;
    }

    public ASObject getObject() {
        return object;
    }

    public Target getTarget() {
        return target;
    }

    public static class Builder {

        private String type;
        private String published;
        private Actor actor;
        private Provider provider;
        private ASObject object;
        private Target target;

        public Builder(String type, Provider provider, Actor actor, ASObject object) {
            this.type = type;
            this.provider = provider;
            this.actor = actor;
            this.object = object;
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
            Activity activity = new Activity(type, published, actor, provider, object, target);
            if(activity.getProvider() == null) {
                throw new IllegalStateException("Provider must be not null");
            }
            if(activity.getObject() == null) {
                throw new IllegalStateException("Object must be not null");
            }
            if(activity.getActor() == null) {
                throw new IllegalStateException("Actor must be not null");
            }
            if(activity.getType() == null || activity.getType().isEmpty()) {
                throw new IllegalStateException("Type must be not null and not empty");
            }
            return activity;
        }
    }

}
