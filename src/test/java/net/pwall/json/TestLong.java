/*
 * @(#) TestLong.java
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
 * Test class for JSON long values.
 *
 * @author Peter Wall
 */
class TestLong {

    @Test
    void testParse() {

        JSONValue value = JSON.parse("123456789012");
        assertTrue(value instanceof JSONLong);
        assertEquals("123456789012", value.toString());
        assertEquals(123456789012L, ((JSONLong)value).get());
        assertEquals("123456789012", value.toJSON());

        value = JSON.parse("-123456789012");
        assertTrue(value instanceof JSONLong);
        assertEquals("-123456789012", value.toString());
        assertEquals(-123456789012L, ((JSONLong)value).get());
        assertEquals("-123456789012", value.toJSON());

        value = JSON.parse("-1000000000000");
        assertTrue(value instanceof JSONLong);
        assertEquals("-1000000000000", value.toString());
        assertEquals(-1000000000000L, ((JSONLong)value).get());
        assertEquals("-1000000000000", value.toJSON());

        value = JSON.parse("9223372036854775807");
        assertTrue(value instanceof JSONLong);
        assertEquals("9223372036854775807", value.toString());
        assertEquals(9223372036854775807L, ((JSONLong)value).get());
        assertEquals("9223372036854775807", value.toJSON());

        value = JSON.parse("-9223372036854775808");
        assertTrue(value instanceof JSONLong);
        assertEquals("-9223372036854775808", value.toString());
        assertEquals(-9223372036854775808L, ((JSONLong)value).get());
        assertEquals("-9223372036854775808", value.toJSON());

        value = JSON.parse("2147483647");
        assertFalse(value instanceof JSONLong);

    }

    @Test
    void testParse2() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("123456789012a"));
        assertEquals("Excess characters after JSON value", e.getMessage());
    }

    @Test
    void testConstructor1() {
        JSONLong long1 = new JSONLong(123);
        assertEquals(123, long1.get());
        assertEquals("123", long1.toString());
        assertEquals("123", long1.toJSON());

        long1 = new JSONLong(0);
        assertEquals(0, long1.get());
        assertEquals("0", long1.toString());
        assertEquals("0", long1.toJSON());

        long1 = new JSONLong(-1000);
        assertEquals(-1000, long1.get());
        assertEquals("-1000", long1.toString());
        assertEquals("-1000", long1.toJSON());

        long1 = new JSONLong(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, long1.get());
        assertEquals("9223372036854775807", long1.toString());
        assertEquals("9223372036854775807", long1.toJSON());

        long1 = new JSONLong(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, long1.get());
        assertEquals("-9223372036854775808", long1.toString());
        assertEquals("-9223372036854775808", long1.toJSON());

    }

    @Test
    void testNumberMethods() {
        JSONLong long1 = new JSONLong(123);
        assertEquals(123, long1.intValue());
        assertEquals(123L, long1.longValue());
        assertEquals(123.0F, long1.floatValue(), 1e-64);
        assertEquals(123.0, long1.doubleValue(), 1e-64);
        assertEquals(123, long1.byteValue());
        assertEquals(123, long1.shortValue());
    }

    @Test
    void testAppend() throws Exception {
        JSONLong long1 = new JSONLong(123456789012L);
        StringBuilder sb = new StringBuilder();
        long1.appendJSON(sb);
        assertEquals("123456789012", sb.toString());
    }

    @Test
    void testValueOf() {
        JSONLong long1 = JSONLong.valueOf(123456789012L);
        assertEquals(123456789012L, long1.get());
        assertEquals("123456789012", long1.toString());
        assertEquals("123456789012", long1.toJSON());

        long1 = JSONLong.valueOf(0);
        assertEquals(0L, long1.get());
        assertEquals("0", long1.toString());
        assertEquals("0", long1.toJSON());

        long1 = JSONLong.valueOf(-1000);
        assertEquals(-1000L, long1.get());
        assertEquals("-1000", long1.toString());
        assertEquals("-1000", long1.toJSON());

        long1 = JSONLong.valueOf(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, long1.get());
        assertEquals("9223372036854775807", long1.toString());
        assertEquals("9223372036854775807", long1.toJSON());

        long1 = JSONLong.valueOf(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, long1.get());
        assertEquals("-9223372036854775808", long1.toString());
        assertEquals("-9223372036854775808", long1.toJSON());

        long1 = JSONLong.valueOf("123");
        assertEquals(123L, long1.get());
        assertEquals("123", long1.toString());
        assertEquals("123", long1.toJSON());

    }

    @Test
    void testValueEquals() {
        JSONLong long1 = JSONLong.valueOf(123);
        assertTrue(long1.valueEquals(123));
        assertTrue(long1.valueEquals(123L));
        assertTrue(long1.valueEquals(123.0));
        assertTrue(long1.valueEquals(123.0F));
        assertFalse(long1.valueEquals(122));
    }

}
