# data-collector-sdk-java
[![Build Status](https://travis-ci.org/schibsted/sdk-java-event-tracking.svg)](https://travis-ci.org/schibsted/sdk-java-event-tracking)

A Java client for the Data Collector API

# Documentation
This documentation is the target for the functionality of v1.

## Usage
Maven:

Use maven to build the SDK.

- mvn install

To use the SDK in your projects include it in your pom.xml.

```xml
<dependency>
    <groupId>no.spt.sdk</groupId>
    <artifactId>data-collector-sdk-java</artifactId>
    <version>0.3.0-SNAPSHOT</version>
</dependency>
```

## Creating a client
A Data Tracking Client is created using a builder that has some sensible default values, but can also be configured
using the builder methods.

```java
DataTrackingClient client = new DataTrackingClient.Builder()
                    .withOptions(options)
                    .withAutomaticActivitySender()
                    .build();
```

The client can be configured to use either an automatic or a manual activity sender. The automatic sender will send
activities asynchronously on a non blocking separate thread as soon as they are tracked, and will be used by default.
The manual sender will queue activities and only send when being instructed to do so. Both activity senders will queue
activities and send batches of activities to the data collector if the queue contains multiple activities.

See the API documentation for more details.

### Error reporting
By default the client is setup with a reporting error collector that reports errors to a central error collecting
service for easy monitoring.

For development and debugging purposes the client can be set up with a logging error collector which uses
 a java.util.logging.Logger to log errors to console. This will replace the reporting error collector and should
 therefore not be used in production.

 ```java
 DataTrackingClient client = new DataTrackingClient.Builder()
                     .withOptions(options)
                     .withErrorCollector(new LoggingErrorCollector())
                     .build();
 ```

### Options
```java
// The unique client ID provided by SPT
String clientId = "4cf36fa274dea2117e030000";

// The url to the data collector endpoint
String dataTrackerUrl = "http://example.data-collector.com/api/v1/track";

// The url to the Central Identification Service (CIS) endpoint
String CISUrl = "http://example.CIS.com/api/v1/identify";

// The url to the error report collector endpoint
String errorReportingUrl = "http://example.error-reporting.com/api/v1/error";

// The maximum size of the activity queue waiting to be sent to the data collector. If the queue reaches
// this size, any additional activities will be dropped to prevent memory problems.
int maxActivityQueueSize = 10000;

// The amount of milliseconds before a request is marked as timed out
int sendTimeout = 1000;

// The amount of times to retry the request
int sendRetries = 2;

Options options = new Options.Builder(clientId)
             .setDataCollectorUrl(dataTrackerUrl)
             .setCISUrl(CISUrl)
             .setErrorReportingUrl(errorReportingUrl)
             .setMaxQueueSize(maxActivityQueueSize)
             .setTimeout(sendTimeout)
             .setRetries(sendRetries)
             .build();
```

## Using the client

```java

public class Example {

  public static void main(String... args) {
    String clientId = "4cf36fa274dea2117e030000";
    String dataTrackerUrl = "http://example.data-collector.com/api/v1/track";
    String CISUrl = "http://example.CIS.com/api/v1/identify";
    String errorReportingUrl = "http://example.error-reporting.com/api/v1/error";
    int maxActivityQueueSize = 10000;
    int sendTimeout = 1000;
    int sendRetries = 2;

    Options options = new Options.Builder(clientId)
             .setDataCollectorUrl(dataTrackerUrl)
             .setCISUrl(CISUrl)
             .setErrorReportingUrl(errorReportingUrl)
             .setMaxQueueSize(maxActivityQueueSize)
             .setTimeout(sendTimeout)
             .setRetries(sendRetries)
             .build();

    DataTrackingClient client = new DataTrackingClient.Builder()
             .withOptions(options)
             .withAutomaticActivitySender()
             .build();

    TrackingIdentity trackingId = null;
    Map<String, String> identifiers = new HashMap<String, String>();
    identifiers.put("SomeKey", "SomeUniqueValue");
    try {
        trackingId = client.getTrackingId(identifiers);
    } catch (DataTrackingException e) {
        e.printStackTrace();
    }

    Activity activity = activity("Read",
            provider("Organization", "urn:schibsted.com:vg123")
                    .displayName("Example organization").build(),
            actor("Person", "urn:schibsted.com:person:" + trackingId.getVisitorId())
                    .displayName("User with session ID " + trackingId.getSessionId()).build(),
            object("Article", "urn:example.no:article:art123")
                    .url("http://www.example.com/article/art123")
                    .displayName("An example article").build())
            .build();

    client.track(activity);
    client.close();
  }

}
```
### Closing the client
The client uses internal queues for tracked activities and separate threads for sending asynchronous to the data
collector. When closing your application you should first close the client to allow these queues to be flushed and
the threads to be shutdown. Note that once you have closed the client there is no way to restart it so you will have to
build a new client.

```java
client.close();
```

### Stats
The client keeps stats of tracked activities and errors. Currently the following stats are tracked:
- The number of activities that has been added to the tracking queue
- The number of batches that has been sent to the data collector
- The number activities that has been successfully sent to the data collector
- The number of activities that has been rejected by the data collector due to validation errors
- The number of activities that could not be sent to the data collector due to an error
- The number of activities that has been dropped because the activity queue was full
- The number of error reports that has been sent to the error reporting service

```java
DataTrackingStats stats = client.getStats();
```

## Creating activities
The activities are based on the [SPT ActivityStreams format](https://github.com/schibsted/activitystream-events).

An activity is created using a Builder with a fluent interface. It requires a type, provider, actor, and object
as constructor parameters.

__Example: User reads an article__
```java
Activity activity = new Activity.Builder("Read",
             provider("Organization", "urn:schibsted.com:vg123").build(),
             actor("Person", "urn:schibsted.com:person:abc123").build(),
             object("Article", "urn:example.no:article:art123").build())
             .build();
```

__Example: User sends a message__
```java
Activity activity = activity("Send",
             provider("Organization", "urn:schibsted.com:sp123").build(),
             actor("Person", "urn:schibsted.com:person:abc123").build(),
             object("Content", "urn:schibsted.com:message:abc123")
                 .title("<Message title>")
                 .content("<Message content>")
                 .build())
             .target(target("Person", "urn:example@email.com"))
             .build();
```

### Object
The Object is the primary object of the Activity. For available Object types and properties, see the
[SPT ActivityStreams format](https://github.com/schibsted/activitystream-events).

Provider, Actor and Target are all subclasses of Object and can therefore have the same attributes. There are some
predefined methods for common attributes (e.g. `.displayName("An example article")`) and you are also able to add
other attributes as long as they are defined in the [SPT ActivityStreams format](https://github.com/schibsted/activitystream-events)
(e.g. `.set("spt:tags", "training,PT")`).

### Provider
The Provider is the entity that is sending the Activity. The type is typically `Organization` and the ID is on the form
 `urn:schibsted.com:vg123` where the last part is the clientId supplied by SPT.

### Actor
The Actor is the entity that is carrying out the Activity. For user tracking this is typically a user with the type
`Person` and an ID on the form `urn:schibsted.com:person:abc123` where the last part can be fetched from the central identity
service.

### Target
The Target is the indirect object, or target, of the Activity. Target is a subclass of Object so all available Objects
 can also be Targets.

### Result
The result is the result of the Activity. Result is a subclass of Object so all available Objects can also be Results.

### Helper methods
```java
import static no.spt.sdk.models.Makers.*;
```
By static importing Makers you can use static helper methods for creating objects
(e.g. `actor("Person", "urn:schibsted.com:person:abc123").build()` to create an actor)

## Tracking ID
To be able to track users, each user has to be given a unique tracking ID. This is done based on some
identifiers that are sent to the Central Identification Service which returns an environmentId, sessionId, visitorId and
 potentially userId.
The environmentId is unique to the user's environment.
The sessionId is unique to this user's current session.
The visitorId is unique to this user and should be used as ID for the actor of the activity.
The userId is the user's login ID which depends on the identity provider that is used. Typically this is the user's
 SPiD ID if SPiD is used for authentication.
These IDs should be included in future requests for tracking IDs

The Tracking Client has a method for fetching a tracking ID from the Central Identification Service based on a
`Map<String, String>` of identifiers. These identifiers should be enough to uniquely identify the user or the
returned ID will only be a temporary ID used for this session.

__Example: No existing tracking ID__
```java
Map<String, String> identifiers = new HashMap<String, String>();
identifiers.put("clientIp", "127.0.0.1");
identifiers.put("userId", "urn:schibsted.com:user:abc123");
TrackingIdentity trackingId = client.getTrackingId(identifiers);
```

__Example: Existing tracking ID__
```java
Map<String, String> identifiers = new HashMap<String, String>();
identifiers.put("environmentId", oldTrackingId.getEnvironmentId());
identifiers.put("sessionId", oldTrackingId.getSessionId());
identifiers.put("userId", oldTrackingId.getUserId());
identifiers.put("visitorId", oldTrackingId.getVisitorId());
TrackingIdentity trackingId = client.getTrackingId(identifiers);
```

The actor can then be built with the tracking ID as an argument
```java
TrackingIdentity trackingId = client.getTrackingId(identifiers);
Actor actor = actor(trackingId).build();
```

### Asynchronous fetching Tracking ID
In cases where it is important that the tracking client is not blocking the application using it, the tracking ID can be
fetched asynchronously and then the activity can be tracked using a callback where the tracking ID is used to create
the actor.
```java
Map<String, String> identifiers = new HashMap<String, String>();
identifiers.put("clientIp", "127.0.0.1");
identifiers.put("userId", "urn:schibsted.com:user:abc123");
client.identifyActorAsync(identifiers, new IdentityCallback() {
    @Override
    public void onSuccess(TrackingIdentity trackingId) {
        client.track(activity("Send",
                provider("Organization", "urn:schibsted.com:sp123")
                    .build(),
                actor(trackingId)
                    .build(),
                object("Content", "urn:schibsted.com:message:abc123")
                    .title("<Message title>")
                    .build())
                .target(target("Person", "urn:example@email.com")
                    .build())
                .build());
    }
});
```
