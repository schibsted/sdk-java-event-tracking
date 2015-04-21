package no.spt.sdk.models;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static no.spt.sdk.models.Makers.collection;
import static no.spt.sdk.models.Makers.object;
import static org.junit.Assert.assertEquals;

public class CollectionTest {

    private static final String OBJECT_TYPE = "Type";
    private static final String OBJECT_TYPE2 = "Type2";
    private static final String OBJECT_ID = "object:id";
    private static final String OBJECT_ID2 = "object:id2";

    @Test
    public void testCreateCollection(){
        ASObject object1 = object(OBJECT_TYPE, OBJECT_ID).build();
        ASObject object2 = object(OBJECT_TYPE2, OBJECT_ID2).build();
        Collection collection = collection(Arrays.asList(object1, object2)).build();
        Map properties = collection.getMap();
        assertEquals(2, properties.size());
        assertEquals(OBJECT_TYPE, ((ASObject) ((List) properties.get("items")).get(0)).getMap()
            .get("@type"));
        assertEquals(OBJECT_ID, ((ASObject)((List) properties.get("items")).get(0))
            .getMap().get("@id"));
        assertEquals(OBJECT_TYPE2, ((ASObject) ((List) properties.get("items")).get(1)).getMap()
            .get("@type"));
        assertEquals(OBJECT_ID2, ((ASObject)((List) properties.get("items")).get(1))
            .getMap().get("@id"));
    }
}
