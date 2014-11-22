/*
 * @(#) TestObject.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014 Peter Wall
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
