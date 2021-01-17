/*
 * @(#) TestBoolean.java
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
 * Test class for JSON boolean values.
 *
 * @author Peter Wall
 */
class TestBoolean {

    @Test
    void testParse() {

        JSONValue value = JSON.parse("true");
        assertTrue(value instanceof JSONBoolean);
        assertEquals("true", value.toString());
        assertTrue(((JSONBoolean)value).getValue());
        assertEquals("true", value.toJSON());

        value = JSON.parse("false");
        assertTrue(value instanceof JSONBoolean);
        assertEquals("false", value.toString());
        assertFalse(((JSONBoolean)value).getValue());
        assertEquals("false", value.toJSON());

        value = JSON.parse("  false\n");
        assertTrue(value instanceof JSONBoolean);
        assertEquals("false", value.toString());
        assertFalse(((JSONBoolean)value).getValue());
        assertEquals("false", value.toJSON());

        value = JSON.parse("0");
        assertFalse(value instanceof JSONBoolean);

    }

    @Test
    void testParse2() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("TRUE"));
        assertEquals("Illegal JSON syntax", e.getMessage());
    }

    @Test
    void testParse3() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("YES"));
        assertEquals("Illegal JSON syntax", e.getMessage());
    }

    @Test
    void testConstructor1() {
        JSONBoolean bool = new JSONBoolean(true);
        assertTrue(bool.getValue());
        assertEquals("true", bool.toString());
        assertEquals("true", bool.toJSON());

        bool = new JSONBoolean(false);
        assertFalse(bool.getValue());
        assertEquals("false", bool.toString());
        assertEquals("false", bool.toJSON());
    }

    @Test
    void testAppend() throws Exception {
        JSONBoolean bool = new JSONBoolean(true);
        StringBuilder sb = new StringBuilder();
        bool.appendJSON(sb);
        assertEquals("true", sb.toString());
    }

}
