/*
 * @(#) TestZero.java
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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for JSON zero values.
 *
 * @author Peter Wall
 */
public class TestZero {

    @Test
    public void testParse() {

        JSONValue value = JSON.parse("0");
        assertTrue(value instanceof JSONZero);
        assertEquals("0", value.toString());
        assertEquals(0, ((JSONZero)value).get());
        assertEquals("0", value.toJSON());

        value = JSON.parse("1");
        assertFalse(value instanceof JSONZero);

    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse2() {
        JSON.parse("00");
    }

    @Test
    public void testConstructor1() {
        JSONZero zero = new JSONZero();
        assertEquals(0, zero.get());
        assertEquals("0", zero.toString());
        assertEquals("0", zero.toJSON());
    }

    @SuppressWarnings("cast")
    @Test
    public void testNumberMethods() {
        JSONZero zero = new JSONZero();
        assertTrue(zero instanceof Number);
        assertEquals(0, zero.intValue());
        assertEquals(0L, zero.longValue());
        assertEquals(0.0F, zero.floatValue(), 1e-64);
        assertEquals(0.0, zero.doubleValue(), 1e-64);
        assertEquals(0, zero.byteValue());
        assertEquals(0, zero.shortValue());
    }

    @Test
    public void testAppend() throws Exception {
        JSONZero zero = new JSONZero();
        StringBuilder sb = new StringBuilder();
        zero.appendJSON(sb);
        assertEquals("0", sb.toString());
    }

    @Test
    public void testValueEquals() {
        JSONZero zero = JSONZero.ZERO;
        assertTrue(zero.valueEquals(0));
        assertTrue(zero.valueEquals(0L));
        assertTrue(zero.valueEquals(0.0));
        assertTrue(zero.valueEquals(0.0F));
        assertFalse(zero.valueEquals(1));
    }

}
