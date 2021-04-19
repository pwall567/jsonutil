/*
 * @(#) TestDecimal.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2020 Peter Wall
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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JSON decimal values.
 *
 * @author Peter Wall
 */
class TestDecimal {

    private static final double delta = 0.000000001;

    @Test
    void testParse() {

        JSONValue value = JSON.parse("123.0");
        assertTrue(value instanceof JSONDecimal);
        assertEquals("123.0", value.toString());
        assertEquals(0, BigDecimal.valueOf(123.0).compareTo(((JSONDecimal)value).getValue()));
        assertEquals(123.0, ((JSONDecimal)value).doubleValue(), delta);
        assertEquals("123.0", value.toJSON());

        value = JSON.parse("-123.0");
        assertTrue(value instanceof JSONDecimal);
        assertEquals("-123.0", value.toString());
        assertEquals(0, BigDecimal.valueOf(-123.0).compareTo(((JSONDecimal)value).getValue()));
        assertEquals(-123.0, ((JSONDecimal)value).doubleValue(), delta);
        assertEquals("-123.0", value.toJSON());

        value = JSON.parse("123e2");
        assertTrue(value instanceof JSONDecimal);
        assertEquals("123e2", value.toString());
        assertEquals(0, BigDecimal.valueOf(123e2).compareTo(((JSONDecimal)value).getValue()));
        assertEquals(12300, ((JSONDecimal)value).intValue());
        assertEquals("123e2", value.toJSON());

        value = JSON.parse("9223372036854775808"); // one greater than max long
        assertTrue(value instanceof JSONDecimal);
        assertEquals("9223372036854775808", value.toString());
        assertEquals(new BigDecimal("9223372036854775808"), ((JSONDecimal)value).getValue());
        assertEquals("9223372036854775808", value.toJSON());

        value = JSON.parse("-9223372036854775809"); // one less than min long
        assertTrue(value instanceof JSONDecimal);
        assertEquals("-9223372036854775809", value.toString());
        assertEquals(new BigDecimal("-9223372036854775809"), ((JSONDecimal)value).getValue());
        assertEquals("-9223372036854775809", value.toJSON());

    }

    @Test
    void testValueOf() {
        JSONDecimal value0 = JSONDecimal.ZERO;
        JSONDecimal value123 = new JSONDecimal(new BigDecimal(123));
        JSONDecimal value = JSONDecimal.valueOf(123);
        assertEquals(value123, value);
        value = JSONDecimal.valueOf("123");
        assertEquals(value123, value);
        value = JSONDecimal.valueOf(0);
        assertSame(value0, value);
        value = JSONDecimal.valueOf("0");
        assertSame(value0, value);
        value = JSONDecimal.valueOf("0.0");
        assertSame(value0, value);
        value = JSONDecimal.valueOf("0.0000");
        assertSame(value0, value);
        value = JSONDecimal.valueOf("00000000");
        assertSame(value0, value);
    }

    @Test
    void testHashCode() {
        assertEquals(123, new JSONDecimal("123").hashCode());
        assertEquals(1234, new JSONDecimal(new BigDecimal(1234)).hashCode());
        assertEquals(12345, new JSONDecimal("12345.0").hashCode());
        assertEquals(12345, new JSONDecimal("12345.678").hashCode());
    }

}
