package no.spt.sdk.models;

import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

public class ASObjectTest {

    private static final String OBJECT_TYPE = "Type";
    private static final String OBJECT_ID = "object:id";

    @Test
    public void testCreateASObject(){
        ASObject object = builder().build();
        Map properties = object.getMap();
        assertEquals(OBJECT_TYPE, properties.get("@type"));
        assertEquals(OBJECT_ID, properties.get("@id"));
    }

    @Test
    public void testSettingNullObject() {
        ASObject object = builder().set("null_value", (ASObject) null).build();
        assertNotExistsInMap(object, "null_value");
    }

    @Test
    public void testSettingNullLink() {
        ASObject object = builder().set("null_value", (Link)null).build();
        assertNotExistsInMap(object, "null_value");
    }

    @Test
    public void testSet() throws Exception {
        ASObject object = builder().set("key", "value").build();
        assertExistsInMap(object, "key", "value");
    }

    @Test
    public void testId() throws Exception {
        ASObject object = builder().id("idValue").build();
        assertExistsInMap(object, "@id", "idValue");
    }

    @Test
    public void testType() throws Exception {
        ASObject object = builder().type("typeValue").build();
        assertExistsInMap(object, "@type", "typeValue");
    }

    @Test
    public void testTitle() throws Exception {
        ASObject object = builder().title("titleValue").build();
        assertExistsInMap(object, "title", "titleValue");
    }

    @Test
    public void testUrl() throws Exception {
        ASObject object = builder().url("urlValue").build();
        assertExistsInMap(object, "url", "urlValue");
    }

    @Test
    public void testDisplayName() throws Exception {
        ASObject object = builder().displayName("displayNameValue").build();
        assertExistsInMap(object, "displayName", "displayNameValue");
    }

    @Test
    public void testContent() throws Exception {
        ASObject object = builder().content("contentValue").build();
        assertExistsInMap(object, "content", "contentValue");
    }

    @Test
    public void testPublished() throws Exception {
        ASObject object = builder().published(new Date(0L)).build();
        assertExistsInMap(object, "published", "1970-01-01T01:00:00.000+01:00");
    }

    @Test
    public void testPublished1() throws Exception {
        ASObject object = builder().publishedNow().build();
        assertExistsInMap(object, "published");
    }

    @Test
    public void testPublishedNow() throws Exception {
        ASObject object = builder().published("1970-01-01T01:00:00.000+01:00").build();
        assertExistsInMap(object, "published", "1970-01-01T01:00:00.000+01:00");
    }

    private static void assertExistsInMap(ASObject object, String key, Object value) {
        Map properties = object.getMap();
        assertTrue(properties.containsKey(key));
        assertEquals(properties.get(key), value);
    }

    private static void assertExistsInMap(ASObject object, String key) {
        Map properties = object.getMap();
        assertTrue(properties.containsKey(key));
    }

    private static void assertNotExistsInMap(ASObject object, String key) {
        Map properties = object.getMap();
        assertFalse(properties.containsKey(key));
    }

    private static ASObject.Builder builder() {
        return new ASObject.Builder(OBJECT_TYPE, OBJECT_ID);
    }

}