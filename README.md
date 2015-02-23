# data-collector-sdk-java

A Java client for the Data Collector API

## Usage
Maven:

Use maven to build the SDK.

- mvn install

To use the SDK in your projects include it in your pom.xml.

```xml
<dependency>
    <groupId>no.spt</groupId>
    <artifactId>data-collector-sdk-java</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Using the client

```java

public class Example {

  public static void main(String... args) {

    Options options = new Options("http://example.data-collector.com/api/v1/track", 50000, 1000, 2);
    DataTrackingClient client = new DataTrackingClient.Builder()
                    .withOptions(options)
                    .withAutomaticActivitySender()
                    .build();

    Activity activity = activity("Read")
             .publishedNow()
             .actor(actor("Person", "urn:spid.no:person:abc123")
                     .displayName("User with ID abc123"))
             .provider(provider("Organization", "urn:example:organization:vg123")
                     .displayName("Example organization"))
             .object(object("Article", "urn:example:article:art123")
                     .url("http://www.example.com/article/art123")
                     .displayName("An example article"))
             .build();

     client.track(activity);

  }

}
```
