/*
 * @(#) JSONDecimal.java
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import net.pwall.util.Strings;

/**
 * A JSON decimal (floating point) value.
 *
 * @author Peter Wall
 */
public class JSONDecimal extends JSONNumberValue {

    private static final long serialVersionUID = 5004532203973159380L;

    public static final JSONDecimal ZERO = new JSONDecimal(BigDecimal.ZERO);

    private final BigDecimal bigDecimal;
    private final String string;

    public JSONDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
        this.string = bigDecimal.toString();
    }

    public JSONDecimal(String string) {
        if (string == null || string.length() == 0)
            throw new IllegalArgumentException("Must not be null or empty");
        try {
            bigDecimal = new BigDecimal(string);
        }
        catch (Exception e) {
            throw new JSONException(JSON.ILLEGAL_NUMBER);
        }
        this.string = string;
    }

    public BigDecimal getValue() {
        return bigDecimal;
    }

    @Override
    public BigDecimal toSimpleValue() {
        return bigDecimal;
    }

    @Override
    public int intValue() {
        return bigDecimal.intValue();
    }

    @Override
    public long longValue() {
        return bigDecimal.longValue();
    }

    @Override
    public float floatValue() {
        return bigDecimal.floatValue();
    }

    @Override
    public double doubleValue() {
        return bigDecimal.doubleValue();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return bigDecimal.toBigInteger();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return bigDecimal;
    }

    @Override
    public String toJSON() {
        return string;
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append(toJSON());
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public int hashCode() {
        return bigDecimal.intValue();
    }

    @Override
    public boolean equals(Object other) {
        return other == this ||
                other instanceof JSONNumberValue && ((JSONNumberValue)other).valueEquals(bigDecimal);
    }

    @Override
    public boolean valueEquals(int other) {
        return BigDecimal.valueOf(other).compareTo(bigDecimal) == 0;
    }

    @Override
    public boolean valueEquals(long other) {
        return BigDecimal.valueOf(other).compareTo(bigDecimal) == 0;
    }

    @Override
    public boolean valueEquals(float other) {
        return BigDecimal.valueOf(other).compareTo(bigDecimal) == 0;
    }

    @Override
    public boolean valueEquals(double other) {
        return BigDecimal.valueOf(other).compareTo(bigDecimal) == 0;
    }

    @Override
    public boolean valueEquals(BigInteger other) {
        return (new BigDecimal(other)).compareTo(bigDecimal) == 0;
    }

    @Override
    public boolean valueEquals(BigDecimal other) {
        return other.compareTo(bigDecimal) == 0;
    }

    public static JSONDecimal valueOf(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.ZERO) == 0 ? ZERO : new JSONDecimal(bigDecimal);
    }

    public static JSONDecimal valueOf(String s) {
        if (s == null || s.length() == 0)
            throw new IllegalArgumentException("Must not be null or empty");
        if (s.indexOf('.') >= 0)
            return s.charAt(0) == '0' && s.charAt(s.length() - 1) == '0' &&
                    Strings.trim(s, ch -> ch == '0').equals(".") ? ZERO : new JSONDecimal(s);
        return Strings.trim(s, ch -> ch == '0').length() == 0 ? ZERO : new JSONDecimal(s);
    }

    public static JSONDecimal valueOf(double d) {
        return d == 0.0 ? ZERO : new JSONDecimal(BigDecimal.valueOf(d));
    }

    public static JSONDecimal valueOf(long i) {
        return i == 0 ? ZERO : new JSONDecimal(BigDecimal.valueOf(i));
    }

}
