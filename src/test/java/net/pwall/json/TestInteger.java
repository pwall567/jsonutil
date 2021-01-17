/*
 * @(#) TestInteger.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2016 Peter Wall
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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JSON integers.
 *
 * @author Peter Wall
 */
class TestInteger {

    @Test
    void testParse() {

        JSONValue value = JSON.parse("123");
        assertTrue(value instanceof JSONInteger);
        assertEquals("123", value.toString());
        assertEquals(123, ((JSONInteger)value).get());
        assertEquals("123", value.toJSON());

        value = JSON.parse("-1");
        assertTrue(value instanceof JSONInteger);
        assertEquals("-1", value.toString());
        assertEquals(-1, ((JSONInteger)value).get());
        assertEquals("-1", value.toJSON());

        value = JSON.parse("-1000000");
        assertTrue(value instanceof JSONInteger);
        assertEquals("-1000000", value.toString());
        assertEquals(-1000000, ((JSONInteger)value).get());
        assertEquals("-1000000", value.toJSON());

        value = JSON.parse(" 123456789 ");
        assertTrue(value instanceof JSONInteger);
        assertEquals("123456789", value.toString());
        assertEquals(123456789, ((JSONInteger)value).get());
        assertEquals("123456789", value.toJSON());

        value = JSON.parse("2147483647");
        assertTrue(value instanceof JSONInteger);
        assertEquals("2147483647", value.toString());
        assertEquals(2147483647, ((JSONInteger)value).get());
        assertEquals("2147483647", value.toJSON());

        value = JSON.parse("-2147483648");
        assertTrue(value instanceof JSONInteger);
        assertEquals("-2147483648", value.toString());
        assertEquals(-2147483648, ((JSONInteger)value).get());
        assertEquals("-2147483648", value.toJSON());

        value = JSON.parse("2147483648");
        assertFalse(value instanceof JSONInteger);

    }

    @Test
    void testParse2() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("1a"));
        assertEquals("Excess characters after JSON value", e.getMessage());
    }

    @Test
    void testParse3() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("1-1"));
        assertEquals("Excess characters after JSON value", e.getMessage());
    }

    @Test
    void testParse4() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("00"));
        assertEquals("Illegal JSON number", e.getMessage());
    }

    @Test
    void testParse5() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("01"));
        assertEquals("Illegal JSON number", e.getMessage());
    }

    @Test
    void testConstructor1() {
        JSONInteger int1 = new JSONInteger(123);
        assertEquals(123, int1.get());
        assertEquals("123", int1.toString());
        assertEquals("123", int1.toJSON());

        int1 = new JSONInteger(0);
        assertEquals(0, int1.get());
        assertEquals("0", int1.toString());
        assertEquals("0", int1.toJSON());

        int1 = new JSONInteger(-1000);
        assertEquals(-1000, int1.get());
        assertEquals("-1000", int1.toString());
        assertEquals("-1000", int1.toJSON());

        int1 = new JSONInteger(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, int1.get());
        assertEquals("2147483647", int1.toString());
        assertEquals("2147483647", int1.toJSON());

        int1 = new JSONInteger(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, int1.get());
        assertEquals("-2147483648", int1.toString());
        assertEquals("-2147483648", int1.toJSON());

    }

    @Test
    void testNumberMethods() {
        JSONInteger int1 = new JSONInteger(123);
        assertEquals(123, int1.intValue());
        assertEquals(123L, int1.longValue());
        assertEquals(123.0F, int1.floatValue(), 1e-64);
        assertEquals(123.0, int1.doubleValue(), 1e-64);
        assertEquals(123, int1.byteValue());
        assertEquals(123, int1.shortValue());
    }

    @Test
    void testAppend() throws Exception {
        JSONInteger int1 = new JSONInteger(123);
        StringBuilder sb = new StringBuilder();
        int1.appendJSON(sb);
        assertEquals("123", sb.toString());
    }

    @Test
    void testValueOf() {
        JSONInteger int1 = JSONInteger.valueOf(123);
        assertEquals(123, int1.get());
        assertEquals("123", int1.toString());
        assertEquals("123", int1.toJSON());

        int1 = JSONInteger.valueOf(0);
        assertEquals(0, int1.get());
        assertEquals("0", int1.toString());
        assertEquals("0", int1.toJSON());

        int1 = JSONInteger.valueOf(-1000);
        assertEquals(-1000, int1.get());
        assertEquals("-1000", int1.toString());
        assertEquals("-1000", int1.toJSON());

        int1 = JSONInteger.valueOf(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, int1.get());
        assertEquals("2147483647", int1.toString());
        assertEquals("2147483647", int1.toJSON());

        int1 = JSONInteger.valueOf(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, int1.get());
        assertEquals("-2147483648", int1.toString());
        assertEquals("-2147483648", int1.toJSON());

        int1 = JSONInteger.valueOf("123");
        assertEquals(123, int1.get());
        assertEquals("123", int1.toString());
        assertEquals("123", int1.toJSON());

        int1 = JSONInteger.valueOf("0");
        assertEquals(0, int1.get());
        assertEquals("0", int1.toString());
        assertEquals("0", int1.toJSON());
    }

    @Test
    void testValueEquals() {
        JSONInteger int1 = JSONInteger.valueOf(123);
        assertTrue(int1.valueEquals(123));
        assertTrue(int1.valueEquals(123L));
        assertTrue(int1.valueEquals(123.0));
        assertTrue(int1.valueEquals(123.0F));
        assertFalse(int1.valueEquals(122));
    }

}
