/*
 * @(#) TestString.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2015, 2016 Peter Wall
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
 * Test class for JSON strings.
 *
 * @author Peter Wall
 */
class TestString {

    @Test
    void testParse() {
        JSONValue value = JSON.parse("\"abc\"");
        assertTrue(value instanceof JSONString);
        assertEquals("abc", value.toString());
        assertEquals("abc", ((JSONString)value).get());
        assertEquals("\"abc\"", value.toJSON());

        value = JSON.parse("\"abc\\\\def\"");
        assertTrue(value instanceof JSONString);
        assertEquals("abc\\def", value.toString());
        assertEquals("abc\\def", ((JSONString)value).get());
        assertEquals("\"abc\\\\def\"", value.toJSON());

        value = JSON.parse("\"abc\\\\def\\\\ghi\"\n");
        assertTrue(value instanceof JSONString);
        assertEquals("abc\\def\\ghi", value.toString());
        assertEquals("abc\\def\\ghi", ((JSONString)value).get());
        assertEquals("\"abc\\\\def\\\\ghi\"", value.toJSON());

        value = JSON.parse("\"\"");
        assertTrue(value instanceof JSONString);
        assertEquals("", value.toString());
        assertEquals("", ((JSONString)value).get());
        assertEquals("\"\"", value.toJSON());

        value = JSON.parse(" \" \\t \"\t");
        assertTrue(value instanceof JSONString);
        assertEquals(" \t ", value.toString());
        assertEquals(" \t ", ((JSONString)value).get());
        assertEquals("\" \\t \"", value.toJSON());

        value = JSON.parse("\" \\r\\n \"");
        assertTrue(value instanceof JSONString);
        assertEquals(" \r\n ", value.toString());
        assertEquals(" \r\n ", ((JSONString)value).get());
        assertEquals("\" \\r\\n \"", value.toJSON());

        value = JSON.parse("\" \\\"\\t\\\" \"");
        assertTrue(value instanceof JSONString);
        assertEquals(" \"\t\" ", value.toString());
        assertEquals(" \"\t\" ", ((JSONString)value).get());
        assertEquals("\" \\\"\\t\\\" \"", value.toJSON());

        value = JSON.parse("\" \\u1234 \"");
        assertTrue(value instanceof JSONString);
        assertEquals(3, value.toString().length());
        assertEquals(' ', value.toString().charAt(0));
        assertEquals(0x1234, value.toString().charAt(1));
        assertEquals(' ', value.toString().charAt(2));
        assertEquals("\" \\u1234 \"", value.toJSON());

    }

    @Test
    void testParse2() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("\"abc"));
        assertEquals("Unterminated JSON string at root", e.getMessage());
    }

    @Test
    void testParse3() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("\"\\"));
        assertEquals("Unterminated JSON string at root", e.getMessage());
    }

    @Test
    void testParse4() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("\"\\g\""));
        assertEquals("Illegal escape sequence in JSON string at root", e.getMessage());
    }

    @Test
    void testParse5() {
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse("\" \\uXYZA \""));
        assertEquals("Illegal Unicode sequence in JSON string at root", e.getMessage());
    }

    @Test
    void testConstructor1() {
        JSONString str = new JSONString("abc");
        assertEquals("abc", str.get());
        assertEquals("abc", str.toString());
        assertEquals("\"abc\"", str.toJSON());

        str = new JSONString("abc\\def");
        assertEquals("abc\\def", str.get());
        assertEquals("abc\\def", str.toString());
        assertEquals("\"abc\\\\def\"", str.toJSON());

        str = new JSONString("");
        assertEquals("", str.get());
        assertEquals("", str.toString());
        assertEquals("\"\"", str.toJSON());
    }

    @Test
    void testConstructor2() {
        assertThrows(NullPointerException.class, () -> new JSONString(null));
    }

    @Test
    void testCharSequenceMethods() {
        JSONString str = new JSONString("abc");
        assertEquals(3, str.length());
        assertEquals('a', str.charAt(0));
        assertEquals('b', str.charAt(1));
        assertEquals('c', str.charAt(2));
        assertEquals("ab", str.subSequence(0, 2).toString());
        assertEquals("bc", str.subSequence(1, 3).toString());
    }

    @Test
    void testAppend() throws Exception {
        JSONString str = new JSONString("abc");
        StringBuilder sb = new StringBuilder();
        str.appendJSON(sb);
        assertEquals("\"abc\"", sb.toString());
    }

}
