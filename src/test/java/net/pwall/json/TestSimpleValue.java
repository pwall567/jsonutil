/*
 * @(#) TestSimpleValue.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2016, 2020 Peter Wall
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
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestSimpleValue {

    @Test
    void shouldConvertString() {
        JSONValue value1 = new JSONString("Hello!");
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals("Hello!", simpleValue1);
    }

    @Test
    void shouldConvertInteger() {
        JSONValue value1 = new JSONInteger(123);
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals(123, simpleValue1);
    }

    @Test
    void shouldConvertLong() {
        JSONValue value1 = new JSONLong(123456789123456789L);
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals(123456789123456789L, simpleValue1);
    }

    @Test
    void shouldConvertFloat() {
        JSONValue value1 = new JSONFloat(0.123F);
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals(0.123F, simpleValue1);
    }

    @Test
    void shouldConvertDouble() {
        JSONValue value1 = new JSONDouble(0.123456);
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals(0.123456, simpleValue1);
    }

    @Test
    void shouldConvertDecimal() {
        JSONValue value1 = new JSONDecimal("12345.67");
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals(new BigDecimal("12345.67"), simpleValue1);
    }

    @Test
    void shouldConvertZero() {
        JSONValue value1 = JSONZero.ZERO;
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals(0, simpleValue1);
    }

    @Test
    void shouldConvertBoolean() {
        JSONValue value1 = JSONBoolean.TRUE;
        Object simpleValue1 = value1.toSimpleValue();
        assertTrue((Boolean)simpleValue1);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldConvertArray() {
        JSONValue value1 = new JSONArray(new JSONString("abcdef"), null, new JSONInteger(12345));
        Object simpleValue1 = value1.toSimpleValue();
        assertEquals("abcdef", ((List<Object>)simpleValue1).get(0));
        assertNull(((List<Object>)simpleValue1).get(1));
        assertEquals(12345, ((List<Object>)simpleValue1).get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldConvertObject() {
        JSONObject value1 = new JSONObject();
        value1.putValue("field1", "xyz");
        value1.putValue("field2", 99999);
        value1.putNull("field3");
        JSONValue value2 = new JSONObject(value1);
        Object simpleValue1 = value2.toSimpleValue();
        assertEquals("xyz", ((Map<String, Object>)simpleValue1).get("field1"));
        assertEquals(99999, ((Map<String, Object>)simpleValue1).get("field2"));
        assertNull(((Map<String, Object>)simpleValue1).get("field3"));
    }

}
