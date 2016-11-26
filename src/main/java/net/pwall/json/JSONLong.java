/*
 * @(#) JSONLong.java
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

import net.pwall.util.Strings;

/**
 * A JSON long value.
 *
 * @author Peter Wall
 */
public class JSONLong extends Number implements JSONNumberValue {

    private static final long serialVersionUID = 4342545343730856828L;

    public static final JSONLong ZERO = new JSONLong(0);

    private long value;

    public JSONLong(long value) {
        this.value = value;
    }

    public long get() {
        return value;
    }

    @Override
    public int intValue() {
        return (int)value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    /**
     * Create the external representation for this {@code JSONLong}.
     *
     * @return  the JSON representation for this value
     * @see     JSONValue#toJSON()
     */
    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder(12);
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
        Strings.appendLong(a, value);
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public int hashCode() {
        return (int)value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this ||
                other instanceof JSONNumberValue && ((JSONNumberValue)other).valueEquals(value);
    }

    @Override
    public boolean valueEquals(int other) {
        return other == value;
    }

    @Override
    public boolean valueEquals(long other) {
        return other == value;
    }

    @Override
    public boolean valueEquals(float other) {
        return other == value;
    }

    @Override
    public boolean valueEquals(double other) {
        return other == value;
    }

    public static JSONLong valueOf(long value) {
        return value == 0 ? ZERO : new JSONLong(value);
    }

    public static JSONLong valueOf(String string) {
        try {
            return valueOf(Long.parseLong(string));
        }
        catch (NumberFormatException e) {
            throw new JSONException(JSON.ILLEGAL_NUMBER);
        }
    }

}
