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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import net.pwall.util.ListMap;

/**
 * Test class for JSONObject.
 *
 * @author Peter Wall
 */
class TestObject {

    @Test
    void testParse() {
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
        assertEquals(123, ((JSONInteger)item).getValue());
        expected = "{\"first\":123}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse("{\"first\":123,\"second\":\"abc\"}");
        assertTrue(value instanceof JSONObject);
        object = (JSONObject)value;
        assertEquals(2, object.size());
        item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).getValue());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).getValue());
        expected = "{\"first\":123,\"second\":\"abc\"}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

        value = JSON.parse("{\"first\":123,\"second\":\"abc\",\"third\":[0,false]}");
        assertTrue(value instanceof JSONObject);
        object = (JSONObject)value;
        assertEquals(3, object.size());
        item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).getValue());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).getValue());
        item = object.get("third");
        assertTrue(item instanceof JSONArray);
        JSONArray array = (JSONArray)item;
        item = array.get(0);
        assertTrue(item instanceof JSONZero);
        assertEquals(0, ((JSONZero)item).getValue());
        item = array.get(1);
        assertTrue(item instanceof JSONBoolean);
        assertFalse(((JSONBoolean)item).getValue());
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
        assertEquals(123, ((JSONInteger)item).getValue());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).getValue());
        item = object.get("third");
        assertTrue(item instanceof JSONArray);
        array = (JSONArray)item;
        item = array.get(0);
        assertTrue(item instanceof JSONZero);
        assertEquals(0, ((JSONZero)item).getValue());
        item = array.get(1);
        assertTrue(item instanceof JSONBoolean);
        assertFalse(((JSONBoolean)item).getValue());
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
        assertEquals(123, ((JSONInteger)item).getValue());
        expected = "{\"first\":123}";
        assertEquals(expected, value.toJSON());
        assertEquals(expected, value.toString());

    }

    @Test
    void testParse2() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("{a:1}"));
        assertEquals("Illegal key in JSON object", e.getMessage());
    }

    @Test
    void testParse3() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("{\"a\":1,}"));
        assertEquals("Illegal key in JSON object", e.getMessage());
    }

    @Test
    void testParse4() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("{\"a\":1"));
        assertEquals("Missing closing brace in JSON object", e.getMessage());
    }

    @Test
    void testParse5() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("{\"a\":1,\"a\":2}"));
        assertEquals("Duplicate key in JSON object: \"a\"", e.getMessage());
    }

    @Test
    void testParse6() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("{\"a\":[{\"c\":0"));
        assertEquals("Missing closing brace in JSON object at /a/0", e.getMessage());
    }

    @Test
    void testConstructor() {
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
        assertEquals(123, ((JSONInteger)item).getValue());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).getValue());
        expected = "{\"first\":123,\"second\":\"abc\"}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());
    }

    @Test
    void testPutValue() {
        JSONObject object = new JSONObject();
        object.putValue("first", 123);
        assertEquals(1, object.size());
        JSONValue item = object.get("first");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123, ((JSONInteger)item).getValue());
        String expected = "{\"first\":123}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("second", "abc");
        assertEquals(2, object.size());
        item = object.get("second");
        assertTrue(item instanceof JSONString);
        assertEquals("abc", ((JSONString)item).getValue());
        expected = "{\"first\":123,\"second\":\"abc\"}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("third", true);
        assertEquals(3, object.size());
        item = object.get("third");
        assertTrue(item instanceof JSONBoolean);
        assertTrue(((JSONBoolean)item).getValue());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("fourth", -1000L);
        assertEquals(4, object.size());
        item = object.get("fourth");
        assertTrue(item instanceof JSONLong);
        assertEquals(-1000L, ((JSONLong)item).getValue());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("fifth", 0.123);
        assertEquals(5, object.size());
        item = object.get("fifth");
        assertTrue(item instanceof JSONDouble);
        assertEquals(0.123, ((JSONDouble)item).getValue(), 1e-64);
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putValue("sixth", 55.55F);
        assertEquals(6, object.size());
        item = object.get("sixth");
        assertTrue(item instanceof JSONFloat);
        assertEquals(55.55F, ((JSONFloat)item).getValue(), 1e-64);
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123,\"sixth\":55.55}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putNull("seventh");
        assertEquals(7, object.size());
        item = object.get("seventh");
        assertNull(item);
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123,\"sixth\":55.55,\"seventh\":null}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());

        object.putJSON("eighth", new JSONInteger(123456789));
        assertEquals(8, object.size());
        item = object.get("eighth");
        assertTrue(item instanceof JSONInteger);
        assertEquals(123456789, ((JSONInteger)item).getValue());
        expected = "{\"first\":123,\"second\":\"abc\",\"third\":true,\"fourth\":-1000," +
                "\"fifth\":0.123,\"sixth\":55.55,\"seventh\":null,\"eighth\":123456789}";
        assertEquals(expected, object.toJSON());
        assertEquals(expected, object.toString());
    }

    @Test
    void testGet() {
        JSONObject object = new JSONObject();
        object.putValue("first", 123);
        object.putValue("second", "abc");
        object.putValue("third", true);
        object.putValue("fourth", -1000L);
        object.putValue("fifth", 0.123);
        object.putValue("sixth", 55.55F);
        object.putValue("seventh", new BigDecimal("99.999"));
        object.putJSON("array", new JSONArray());
        object.putJSON("object", new JSONObject());

        assertEquals(123, object.getInt("first"));
        assertEquals("abc", object.getString("second"));
        assertTrue(object.getBoolean("third"));
        assertEquals(-1000L, object.getLong("fourth"));
        assertEquals(0.123, object.getDouble("fifth"), 1e-64);
        assertEquals(55.55F, object.getFloat("sixth"), 1e-64);
        assertEquals(new BigDecimal("99.999"), object.getDecimal("seventh"));
        assertEquals(0, object.getArray("array").size());
        assertEquals(0, object.getObject("object").size());
        assertNotEquals(null, object.get("first"));
        assertNull(object.get("nonexistant"));
        assertTrue(object.containsKey("first"));
        assertFalse(object.containsKey("nonexistant"));
        assertTrue(object.containsValue(new JSONString("abc")));
        assertFalse(object.containsValue(new JSONString("missing")));
    }

    @Test
    void testRemove() {
        JSONObject object = new JSONObject();
        object.putValue("first", 123);
        object.putValue("second", "abc");

        assertFalse(object.isEmpty());
        assertEquals(2, object.size());
        assertTrue(object.containsKey("first"));
        object.remove("first");
        assertFalse(object.isEmpty());
        assertEquals(1, object.size());
        assertFalse(object.containsKey("first"));
        object.remove("second");
        assertTrue(object.isEmpty());
        assertEquals(0, object.size());
        assertFalse(object.containsKey("second"));
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    void testMapActions() {
        JSONObject object = new JSONObject();
        object.putValue("first", 123);
        object.putValue("second", "abc");
        object.putValue("third", true);
        object.putValue("fourth", -1000L);
        JSONObject object2 = new JSONObject();
        object2.putAll(object);
        assertEquals(4, object2.size());
        assertEquals(object, object2);
        Set<String> keySet = new HashSet<>();
        keySet.add("first");
        keySet.add("second");
        keySet.add("third");
        keySet.add("fourth");
        assertEquals(keySet, object2.keySet());
        Set<JSONValue> valueSet = new HashSet<>();
        valueSet.add(new JSONInteger(123));
        valueSet.add(new JSONString("abc"));
        valueSet.add(new JSONBoolean(true));
        valueSet.add(new JSONLong(-1000L));
        assertEquals(valueSet, object2.values());
        Set<Map.Entry<String, JSONValue>> entrySet = new HashSet<>();
        entrySet.add(new TestMapEntry("first", new JSONInteger(123)));
        entrySet.add(new TestMapEntry("second", new JSONString("abc")));
        entrySet.add(new TestMapEntry("third", new JSONBoolean(true)));
        entrySet.add(new TestMapEntry("fourth", new JSONLong(-1000L)));
        assertEquals(entrySet, object2.entrySet());
        // check comparison both ways (a == b AND b == a)
        assertTrue(entrySet.equals(object2.entrySet()));
        assertTrue(object2.entrySet().equals(entrySet));
    }

    @Test
    void testOutput() {
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
    void testParseOld() {
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

        assertNull(JSON.parse("null"));
    }

    @Test
    void testConstructorFromMap() {
        Map<String, JSONValue> map = new ListMap<>();
        map.put("first", new JSONString("abc"));
        map.put("second", new JSONInteger(123));
        map.put("third", JSONBoolean.TRUE);
        JSONObject obj = new JSONObject(map);
        assertEquals("abc", obj.getString("first"));
        assertEquals(123, obj.getInt("second"));
        assertTrue(obj.getBoolean("third"));
        Iterator<Map.Entry<String, JSONValue>> entries = obj.entrySet().iterator();
        Map.Entry<String, JSONValue> entry = entries.next();
        assertEquals("first", entry.getKey());
        assertEquals(new JSONString("abc"), entry.getValue());
        entry = entries.next();
        assertEquals("second", entry.getKey());
        assertEquals(new JSONInteger(123), entry.getValue());
        entry = entries.next();
        assertEquals("third", entry.getKey());
        assertEquals(JSONBoolean.TRUE, entry.getValue());
        assertFalse(entries.hasNext());
    }

    public static class TestMapEntry implements Map.Entry<String, JSONValue> {

        private final String key;
        private JSONValue value;

        public TestMapEntry(String key, JSONValue value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public JSONValue getValue() {
            return value;
        }

        @Override
        public JSONValue setValue(JSONValue value) {
            JSONValue result = this.value;
            this.value = value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> mapEntry = (Map.Entry<?, ?>)obj;
            return Objects.equals(getKey(), mapEntry.getKey()) &&
                    Objects.equals(getValue(), mapEntry.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

    }

}
