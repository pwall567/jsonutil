/*
 * @(#) JSONString.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015 Peter Wall
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

import java.io.IOException;
import java.util.Objects;

import net.pwall.util.Strings;

/**
 * A JSON string value.
 *
 * @author Peter Wall
 */
public class JSONString implements JSONValue, CharSequence {

    private static final long serialVersionUID = -7870545532058668339L;

    private String value;

    public JSONString(CharSequence cs) {
        value = Objects.requireNonNull(cs).toString();
    }

    public String get() {
        return value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public String toJSON() {
        int estimate = value.length() + 20;
        StringBuilder sb = new StringBuilder(estimate);
        try {
            appendJSON(sb);
        }
        catch (IOException e) {
            // can't happen - StringBuilder does not throw IOException
        }
        return sb.toString();
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append('"');
        for (int i = 0, n = value.length(); i < n; ) {
            char ch = value.charAt(i++);
            if (ch == '"' || ch == '\\') {
                a.append('\\');
                a.append(ch);
            }
            else if (ch >= 0x20 && ch < 0x7F) {
                a.append(ch);
            }
            else if (ch == '\b') {
                a.append('\\');
                a.append('b');
            }
            else if (ch == '\f') {
                a.append('\\');
                a.append('f');
            }
            else if (ch == '\n') {
                a.append('\\');
                a.append('n');
            }
            else if (ch == '\r') {
                a.append('\\');
                a.append('r');
            }
            else if (ch == '\t') {
                a.append('\\');
                a.append('t');
            }
            else {
                a.append('\\');
                a.append('u');
                Strings.appendHex(a, ch);
            }
        }
        a.append('"');
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JSONString && value.equals(((JSONString)other).value);
    }

}
