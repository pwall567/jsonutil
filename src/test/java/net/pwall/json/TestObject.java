/*
 * @(#) TestObject.java
 */

package net.pwall.json;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for JSONObject.
 *
 * @author Peter Wall
 */
public class TestObject {

    @Test
    public void testOutput() {
        JSONObject object = new JSONObject();
        assertEquals("{}", object.toJSON());
        object.putValue("first", 1);
        assertEquals("{\"first\":1}", object.toJSON());
        object.putValue("second", "abc");
        assertEquals("{\"first\":1,\"second\":\"abc\"}", object.toJSON());
        JSONArray array = new JSONArray();
        array.addValue(0);
        array.addValue(false);
        object.put("third", array);
        assertEquals("{\"first\":1,\"second\":\"abc\",\"third\":[0,false]}", object.toJSON());
        object.put("fourth", new JSONObject());
        assertEquals("{\"first\":1,\"second\":\"abc\",\"third\":[0,false],\"fourth\":{}}",
                object.toJSON());

        JSONString str = new JSONString("\"\\\u1234");
        assertEquals("\"\\\"\\\\\\u1234\"", str.toJSON());

        assertEquals("null", JSON.toJSON(null));
    }

    @Test
    public void testParse() {
        JSONObject object = new JSONObject();
        assertEquals(JSON.parse("{}"), object);
        object.putValue("first", 1);
        assertEquals(JSON.parse("{\"first\":1}"), object);
        object.putValue("second", "abc");
        assertEquals(JSON.parse("{\"first\":1,\"second\":\"abc\"}"), object);
        JSONArray array = new JSONArray();
        array.addValue(0);
        array.addValue(false);
        object.put("third", array);
        assertEquals(JSON.parse("{\"first\":1,\"second\":\"abc\",\"third\":[0,false]}"),
                object);
        object.put("fourth", new JSONObject());
        assertEquals(JSON.parse(
                "{\"first\":1,\"second\":\"abc\",\"third\":[0,false],\"fourth\":{}}"), object);

        JSONString str = new JSONString("\"\\\u1234");
        assertEquals(JSON.parse("\"\\\"\\\\\\u1234\""), str);

        assertEquals(JSON.parse("null"), null);
    }

}
