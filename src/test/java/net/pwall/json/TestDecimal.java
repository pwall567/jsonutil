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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for JSON decimal values.
 *
 * @author Peter Wall
 */
public class TestDecimal {

    private static final double delta = 0.000000001;

    @Test
    public void testParse() {

        JSONValue value = JSON.parse("123.0");
        assertTrue(value instanceof JSONDecimal);
        assertEquals("123.0", value.toString());
        assertEquals(0, BigDecimal.valueOf(123.0).compareTo(((JSONDecimal)value).get()));
        assertEquals(123.0, ((JSONDecimal)value).doubleValue(), delta);
        assertEquals("123.0", value.toJSON());

        value = JSON.parse("-123.0");
        assertTrue(value instanceof JSONDecimal);
        assertEquals("-123.0", value.toString());
        assertEquals(0, BigDecimal.valueOf(-123.0).compareTo(((JSONDecimal)value).get()));
        assertEquals(-123.0, ((JSONDecimal)value).doubleValue(), delta);
        assertEquals("-123.0", value.toJSON());

        value = JSON.parse("123e2");
        assertTrue(value instanceof JSONDecimal);
        assertEquals("123e2", value.toString());
        assertEquals(0, BigDecimal.valueOf(123e2).compareTo(((JSONDecimal)value).get()));
        assertEquals(12300, ((JSONDecimal)value).intValue());
        assertEquals("123e2", value.toJSON());

    }

}
