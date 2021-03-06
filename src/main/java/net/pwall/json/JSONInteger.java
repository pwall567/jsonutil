/*
 * @(#) JSONInteger.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2016, 2017, 2020 Peter Wall
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
import java.math.BigDecimal;
import java.math.BigInteger;

import net.pwall.util.Strings;

/**
 * A JSON integer value.
 *
 * @author Peter Wall
 */
public class JSONInteger extends JSONNumberValue {

    private static final long serialVersionUID = -4271512047065758655L;

    public static final JSONInteger ZERO = new JSONInteger(0);

    private final int value;

    public JSONInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Integer toSimpleValue() {
        return value;
    }

    @Override
    public int intValue() {
        return value;
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

    @Override
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(value);
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        Strings.appendInt(a, value);
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public int hashCode() {
        return value;
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

    @Override
    public boolean valueEquals(BigInteger other) {
        return other.equals(BigInteger.valueOf(value));
    }

    @Override
    public boolean valueEquals(BigDecimal other) {
        return other.compareTo(BigDecimal.valueOf(value)) == 0;
    }

    public static JSONInteger valueOf(int value) {
        return value == 0 ? ZERO : new JSONInteger(value);
    }

    public static JSONInteger valueOf(String string) {
        try {
            return valueOf(Integer.parseInt(string));
        }
        catch (NumberFormatException e) {
            throw new JSONException(JSON.ILLEGAL_NUMBER);
        }
    }

}
