package io.gmartov;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class JsonUtilsTest {

    private String json = "{\n" +
            "    \"store\": {\n" +
            "        \"book\": [\n" +
            "            {\n" +
            "                \"author\": \"Nigel Rees\",\n" +
            "                \"title\": \"Sayings of the Century\",\n" +
            "                \"category\": \"reference\",\n" +
            "                \"price\": 8.95\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Evelyn Waugh\",\n" +
            "                \"title\": \"Sword of Honour\",\n" +
            "                \"price\": 1\n" +
            "            }" +
            "         ]" +
            "      }}";

    @Test
    public void testWithConfig() {
    }

    @Test
    public void testIsPathExists() {
        JsonUtils.withConfig(Configuration.defaultConfiguration());
        JsonUtils.returnNullIfNoPath(true);
        Assert.assertTrue(JsonUtils.isPathExists(json, "store.book[0].price"));
        Assert.assertFalse(JsonUtils.isPathExists(json, "store.book[0].price1"));
    }


    @Test
    public void testGetValue() {
        JsonUtils.withConfig(Configuration.defaultConfiguration());
        JsonUtils.returnNullIfNoPath(true);
        Assert.assertEquals(JsonUtils.getValue(json, "store.book[0].price"), 8.95);
    }

    @Test
    public void testGetValueWType() {
        JsonUtils.withConfig(Configuration.defaultConfiguration());
        JsonUtils.returnNullIfNoPath(true);
        Assert.assertEquals(JsonUtils.getValue(json, "store.book[0].price", String.class), "8.95");
    }

    @Test
    public void testGetValueWrongPath() {
        JsonUtils.withConfig(Configuration.defaultConfiguration());
        JsonUtils.returnNullIfNoPath(true);
        Assert.assertNull(JsonUtils.getValue(json, "store.book[0].price1"));
    }

    @Test(expectedExceptions = PathNotFoundException.class)
    public void testGetValueException() {
        JsonUtils.withConfig(Configuration.defaultConfiguration());
        JsonUtils.returnNullIfNoPath(false);
        JsonUtils.getValue(json, "store.book[0].price1");
    }


    @Test
    public void testSetJsonAttribute01() {

        JsonPath.isPathDefinite("store.book[0].price");
        final String j = JsonUtils.setJsonAttribute(json, "store.book[0].price1", "qwe");
    }

    @Test
    public void t() {
        DocumentContext dc = JsonUtils.parseJson("{}");
        dc = dc.put("$", "a", 1);
        final String s = dc.jsonString();
    }

    @Test
    public void t01() {
        Object o = JsonUtils.parseJson(json).set("$.store.book[*].author", 1).jsonString();

        DocumentContext dc = JsonUtils.parseJson("{}");
        dc = dc.put("$", "a", new ArrayList<>());
        final String s = dc.jsonString();
        dc = JsonUtils.parseJson(s).put("$.a[0]", "s", "s");
        final String s1 = dc.jsonString();
    }
}