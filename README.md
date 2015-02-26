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
    <version>1.0-SNAPSHOT</version>
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

### Options
```java
// The url to the data collector endpoint
String dataTrackerUrl = "http://example.data-collector.com/api/v1/track";

// The url to the anonymous identity service endpoint
String anonymousIdServiceUrl = "http://example.anonymous-id.com/api/v1/identify";

// The maximum size of the activity queue waiting to be sent to the data collector. If the queue reaches
// this size, any additional activities will be dropped to prevent memory problems.
int maxActivityQueueSize = 10000;

// The amount of milliseconds before a request is marked as timed out
int sendTimeout = 1000;

// The amount of times to retry the request
int sendRetries = 2;

Options options = new Options(dataTrackerUrl,
                      anonymousIdServiceUrl,
                      maxActivityQueueSize,
                      sendTimeout,
                      sendRetries);
```

## Using the client

```java

public class Example {

  public static void main(String... args) {
    String dataTrackerUrl = "http://example.data-collector.com/api/v1/track";
    String anonymousIdServiceUrl = "http://example.anonymous-id.com/api/v1/identify";
    int maxActivityQueueSize = 50000;
    int sendTimeout = 1000;
    int sendRetries = 2;

    Options options = new Options(dataTrackerUrl,
                          anonymousIdServiceUrl,
                          maxActivityQueueSize,
                          sendTimeout,
                          sendRetries);

    DataTrackingClient client = new DataTrackingClient.Builder()
                    .withOptions(options)
                    .withAutomaticActivitySender()
                    .build();

    Activity activity = activity("Read")
             .publishedNow()
             .actor(actor("Person", "urn:spid.no:person:abc123")
                     .displayName("User with ID abc123"))
             .provider(provider("Organization", "urn:spid.no:vg123")
                     .displayName("Example organization"))
             .object(object("Article", "urn:example.no:article:art123")
                     .url("http://www.example.com/article/art123")
                     .displayName("An example article"))
             .build();

     client.track(activity);

  }

}
```

## Creating activities
The activities are based on the [SPT ActivityStreams format](https://github.com/schibsted/activitystream-events).

An activity is created using a Builder with a fluent interface. It requires a type and that at least the actor, object
and provider are set.

```java
Activity activity = activity("Read")
             .publishedNow()
             .actor(actor("Person", "urn:spid.no:person:abc123"))
             .provider(provider("Organization", "urn:spid.no:vg123"))
             .object(object("Article", "urn:example.no:article:art123"))
             .build();
```

### Provider
The Provider is the entity that is sending the Activity. The type is typically `Organization` and the ID is on the form
 `urn:spid.no:vg123` where the last part is supplied by SPT.

### Actor
The Actor is the entity that is carrying out the Activity. For anonymous tracking this is typically a user with the type
`Person` and an ID on the form `urn:spid.no:person:abc123` where the last part can be fetched from the anonymous identity
service.

### Object
The Object is the primary object of the Activity. For available Object types and properties, see the
[SPT ActivityStreams format](https://github.com/schibsted/activitystream-events).

### Target
The Target is the indirect object, or target, of the Activity. Target is a subclass of Object so all available Objects
 can also be Targets.

### Helper methods
```java
import static no.spt.sdk.models.Makers.*;
```
By static importing Makers you can use static helper methods for creating objects
(e.g. `actor("Person", "ID-123").build()` to create an actor)
