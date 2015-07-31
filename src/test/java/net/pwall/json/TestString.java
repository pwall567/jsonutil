/*
 * @(#) TestString.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2015 Peter Wall
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
 * Test class for JSON strings.
 *
 * @author Peter Wall
 */
public class TestString {

    @Test
    public void testParse() {
        JSONValue value = JSON.parse("\"abc\"");
        assertEquals("abc", value.toString());

        value = JSON.parse("\"abc\\\\def\"");
        assertEquals("abc\\def", value.toString());

        value = JSON.parse("\"abc\\\\def\\\\ghi\"");
        assertEquals("abc\\def\\ghi", value.toString());

        value = JSON.parse("\"\"");
        assertEquals("", value.toString());

        value = JSON.parse("\" \\t \"");
        assertEquals(" \t ", value.toString());

        value = JSON.parse("\" \\r\\n \"");
        assertEquals(" \r\n ", value.toString());

        value = JSON.parse("\" \\\"\\t\\\" \"");
        assertEquals(" \"\t\" ", value.toString());

        value = JSON.parse("\" \\u1234 \"");
        assertEquals(3, value.toString().length());
        assertEquals(' ', value.toString().charAt(0));
        assertEquals(0x1234, value.toString().charAt(1));
        assertEquals(' ', value.toString().charAt(2));

    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse2() {
        JSON.parse("\"abc");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse3() {
        JSON.parse("\"\\");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse4() {
        JSON.parse("\"\\g\"");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse5() {
        JSON.parse("\" \\uXYZA \"");
    }

}
