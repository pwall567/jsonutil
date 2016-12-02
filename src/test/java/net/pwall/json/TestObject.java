/*
 * @(#) TestObject.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2016 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
    public void testParse() {
        JSONValue value = JSON.parse("{}");
        assertTrue(value instanceof JSONObject);
        assertEquals(0, ((JSONObject)value).size());
        String expected = "{}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse("{\"first\":123}");
        assertTrue(value instanceof JSONObject);
        JSONObject object = (JSONObject)value;
        assertEquals(1, object.size());
        JSONValue item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).get());
        expected = "{\"first\":123}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse("{\"first\":123,\"second\":\"abc\"}");
        assertTrue(value instanceof JSONObject);
        object = (JSONObject)value;
        assertEquals(2, object.size());
        item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).get());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).get());
        expected = "{\"first\":123,\"second\":\"abc\"}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse("{\"first\":123,\"second\":\"abc\",\"third\":[0,false]}");
        assertTrue(value instanceof JSONObject);
        object = (JSONObject)value;
        assertEquals(3, object.size());
        item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).get());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).get());
        item = object.get("third");
        assertTrue(item instanceof JSONArray);
        JSONArray array = (JSONArray)item;
        item = array.get(0);
        assertTrue(item instanceof JSONZero);
        assertEquals(0, ((JSONZero)item).get());
        item = array.get(1);
        assertTrue(item instanceof JSONBoolean);
        assertFalse(((JSONBoolean)item).get());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":[0,false]}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse(
                "{\"first\":123,\"second\":\"abc\",\"third\":[0,false],\"fourth\":{}}");
        assertTrue(value instanceof JSONObject);
        object = (JSONObject)value;
        assertEquals(4, object.size());
        item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).get());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).get());
        item = object.get("third");
        assertTrue(item instanceof JSONArray);
        array = (JSONArray)item;
        item = array.get(0);
        assertTrue(item instanceof JSONZero);
        assertEquals(0, ((JSONZero)item).get());
        item = array.get(1);
        assertTrue(item instanceof JSONBoolean);
        assertFalse(((JSONBoolean)item).get());
        item = object.get("fourth");
        assertTrue(item instanceof JSONObject);
        assertEquals(0, ((JSONObject)item).size());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":[0,false],\"fourth\":{}}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse("  {        } ");
        assertTrue(value instanceof JSONObject);
        assertEquals(0, ((JSONObject)value).size());
        expected = "{}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse(" {\n\"first\" : 123\n}\n");
        assertTrue(value instanceof JSONObject);
        object = (JSONObject)value;
        assertEquals(1, object.size());
        item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).get());
        expected = "{\"first\":123}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse2() {
        JSON.parse("{a:1}");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse3() {
        JSON.parse("{\"a\":1,}");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse4() {
        JSON.parse("{\"a\":1");
    }

    @Test
    public void testConstructor() {
        JSONObject object = new JSONObject();
        assertEquals(0, object.size());
        String expected = "{}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        JSONObject object2 = new JSONObject();
        object2.putValue("first", 123);
        object2.putValue("second", "abc");
        object = new JSONObject(object2);
        assertEquals(2, object.size());
        JSONValue item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).get());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).get());
        expected = "{\"first\":123,\"second\":\"abc\"}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());
    }

    @Test
    public void testPutValue() {
        JSONObject object = new JSONObject();
        object.putValue("first", 123);
        assertEquals(1, object.size());
        JSONValue item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).get());
        String expected = "{\"first\":123}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("second", "abc");
        assertEquals(2, object.size());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).get());
        expected = "{\"first\":123,\"second\":\"abc\"}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("third", true);
        assertEquals(3, object.size());
        item = object.get("third");
        assertTrue(item instanceof JSONBoolean);
        assertTrue(((JSONBoolean)item).get());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("fourth", -1000L);
        assertEquals(4, object.size());
        item = object.get("fourth");
        assertTrue(item instanceof JSONLong);
        assertEquals(-1000L, ((JSONLong)item).get());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("fifth", 0.123);
        assertEquals(5, object.size());
        item = object.get("fifth");
        assertTrue(item instanceof JSONDouble);
        assertEquals(0.123, ((JSONDouble)item).get(), 1e-64);
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("sixth", 55.55F);
        assertEquals(6, object.size());
        item = object.get("sixth");
        assertTrue(item instanceof JSONFloat);
        assertEquals(55.55F, ((JSONFloat)item).get(), 1e-64);
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123,\"sixth\":55.55}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putNull("seventh");
        assertEquals(7, object.size());
        item = object.get("seventh");
        assertEquals(null, item);
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123,\"sixth\":55.55,\"seventh\":null}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putJSON("eighth", new JSONInteger(123456789));
        assertEquals(8, object.size());
        item = object.get("eighth");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123456789, ((JSONInteger)item).get());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123,\"sixth\":55.55,\"seventh\":null,\"eighth\":123456789}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());
    }

    @Test
    public void testGet() {
        JSONObject object = new JSONObject();
        object.putValue("first", 123);
        object.putValue("second", "abc");
        object.putValue("third", true);
        object.putValue("fourth", -1000L);
        object.putValue("fifth", 0.123);
        object.putValue("sixth", 55.55F);
        object.putJSON("array", new JSONArray());
        object.putJSON("object", new JSONObject());

        assertEquals(123, object.getInt("first"));
        assertEquals("abc", object.getString("second"));
        assertTrue(object.getBoolean("third"));
        assertEquals(-1000L, object.getLong("fourth"));
        assertEquals(0.123, object.getDouble("fifth"), 1e-64);
        assertEquals(55.55F, object.getFloat("sixth"), 1e-64);
        assertEquals(0, object.getArray("array").size());
        assertEquals(0, object.getObject("object").size());
        assertNotEquals(null, object.get("first"));
        assertEquals(null, object.get("nonexistant"));
        assertTrue(object.containsKey("first"));
        assertFalse(object.containsKey("nonexistant"));
        assertTrue(object.containsValue(new JSONString("abc")));
        assertFalse(object.containsValue(new JSONString("missing")));
    }

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

        JSONInteger intValue = new JSONInteger(123456789);
        assertEquals("123456789", intValue.toJSON());
        intValue = new JSONInteger(-1000);
        assertEquals("-1000", intValue.toJSON());
    }

    @Test
    public void testParseOld() {
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
