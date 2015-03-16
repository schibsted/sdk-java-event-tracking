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
    <version>0.2.0-SNAPSHOT</version>
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
activities asynchronously on a non blocking separate thread as soon as they are tracked, and will be used by default. The manual
sender will queue activities and only send when being instructed to do so. Both activity senders will queue activities
and send batches of activities to the data collector if the queue contains multiple activities.

See the API documentation for more details.

### Error reporting
By default the client is setup to report errors to a central error collector for easy monitoring.

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

    Activity activity = activity("Read")
            .publishedNow()
            .actor(actor("Person", "urn:spid.no:person:" + trackingId.getSessionId())
                    .displayName("User with session ID " + trackingId.getSessionId()))
            .provider(provider("Organization", "urn:spid.no:vg123")
                    .displayName("Example organization"))
            .object(object("Article", "urn:example.no:article:art123")
                    .url("http://www.example.com/article/art123")
                    .displayName("An example article"))
            .build();

    client.track(activity);
    client.close();
  }

}
```

## Creating activities
The activities are based on the [SPT ActivityStreams format](https://github.com/schibsted/activitystream-events).

An activity is created using a Builder with a fluent interface. It requires a type and that at least the actor, object
and provider are set.

__Example: User reads an article__
```java
Activity activity = activity("Read")
             .publishedNow()
             .provider(provider("Organization", "urn:spid.no:vg123"))
             .actor(actor("Person", "urn:spid.no:person:abc123"))
             .object(object("Article", "urn:example.no:article:art123"))
             .build();
```

__Example: User sends a message__
```java
Activity activity = activity("Send")
             .provider(provider("Organization", "urn:spid.no:sp123"))
             .actor(actor("Person", "urn:spid.no:person:abc123"))
             .object(object("Content", "urn:spid.no:message:abc123")
                 .title("<Message title>")
                 .content("<Message content>"))
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
 `urn:spid.no:vg123` where the last part is supplied by SPT.

### Actor
The Actor is the entity that is carrying out the Activity. For user tracking this is typically a user with the type
`Person` and an ID on the form `urn:spid.no:person:abc123` where the last part can be fetched from the central identity
service.

### Target
The Target is the indirect object, or target, of the Activity. Target is a subclass of Object so all available Objects
 can also be Targets.

### Helper methods
```java
import static no.spt.sdk.models.Makers.*;
```
By static importing Makers you can use static helper methods for creating objects
(e.g. `actor("Person", "urn:spid.no:person:abc123").build()` to create an actor)

## Tracking ID
To be able to track users, each user has to be given a unique tracking ID. This is done based on some
identifiers that are sent to the Central Identity Service which returns an environmentId and a sessionId.
The environmentId is unique to the user's environment and should be included in future requests for tracking IDs.
The sessionId is unique to this user's current session and should be used as ID for the actor of the activity.

The Tracking Client has a method for fetching a tracking ID from the Central Identity Service based on a
`Map<String, String>` of identifiers. These identifiers should be enough to uniquely identify the user or the
returned ID will only be a temporary ID used for this session.

__Example: No existing tracking ID__
```java
Map<String, String> identifiers = new HashMap<String, String>();
identifiers.put("clientIp", "127.0.0.1");
identifiers.put("userId", "abc123");
TrackingIdentity trackingId = client.getTrackingId(identifiers);
```

__Example: Existing tracking ID__
```java
Map<String, String> identifiers = new HashMap<String, String>();
identifiers.put("environmentId", oldTrackingId.getEnvironmentId());
identifiers.put("sessionId", oldTrackingId.getSessionId());
TrackingIdentity trackingId = client.getTrackingId(identifiers);
```

