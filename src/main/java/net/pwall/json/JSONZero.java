/*
 * @(#) JSONZero.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2020 Peter Wall
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

/**
 * A JSON zero value.
 *
 * @author Peter Wall
 */
public class JSONZero extends JSONNumberValue {

    private static final long serialVersionUID = -2972159538314277194L;
    public static final JSONZero ZERO = new JSONZero();

    public JSONZero() {
    }

    public int getValue() {
        return 0;
    }

    @Override
    public Integer toSimpleValue() {
        return 0;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return BigInteger.ZERO;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.ZERO;
    }

    /**
     * Create the appropriate external representation for this JSON value.
     *
     * @return  the JSON representation for this value
     */
    @Override
    public String toJSON() {
        return "0";
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append('0');
    }

    @Override
    public String toString() {
        return "0";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        return other == this ||
                other instanceof JSONNumberValue && ((JSONNumberValue)other).valueEquals(0);
    }

    @Override
    public boolean valueEquals(int other) {
        return other == 0;
    }

    @Override
    public boolean valueEquals(long other) {
        return other == 0;
    }

    @Override
    public boolean valueEquals(float other) {
        return other == 0;
    }

    @Override
    public boolean valueEquals(double other) {
        return other == 0;
    }

    @Override
    public boolean valueEquals(BigInteger other) {
        return other.equals(BigInteger.ZERO);
    }

    @Override
    public boolean valueEquals(BigDecimal other) {
        return other.compareTo(BigDecimal.ZERO) == 0;
    }

}
