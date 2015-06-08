/*
 * @(#) JSONNumber.java
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

/**
 * A JSON number value.
 *
 * @author Peter Wall
 */
public class JSONNumber extends Number implements JSONValue {

    private static final long serialVersionUID = -414008228926856838L;

    public static final JSONNumber ZERO = new JSONNumber(0);

    private Number value;

    public JSONNumber(int value) {
        this.value = Integer.valueOf(value);
    }

    public JSONNumber(long value) {
        this.value = new Long(value);
    }

    public JSONNumber(float value) {
        if (Float.isNaN(value))
            throw new IllegalArgumentException("Can't store NaN as JSON");
        if (Float.isInfinite(value))
            throw new IllegalArgumentException("Can't store infinity as JSON");
        this.value = new Float(value);
    }

    public JSONNumber(double value) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("Can't store NaN as JSON");
        if (Double.isInfinite(value))
            throw new IllegalArgumentException("Can't store infinity as JSON");
        this.value = new Double(value);
    }

    public JSONNumber(Number value) {
        if (Objects.requireNonNull(value) instanceof Double && ((Double)value).isNaN() ||
                value instanceof Float && ((Float)value).isNaN())
            throw new IllegalArgumentException("Can't store NaN as JSON");
        if (value instanceof Double && ((Double)value).isInfinite() ||
                value instanceof Float && ((Float)value).isInfinite())
            throw new IllegalArgumentException("Can't store infinity as JSON");
        this.value = value;
    }

    public Number get() {
        return value;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    /**
     * Create the appropriate external representation for this JSON value.
     *
     * @return  the JSON representation for this value
     */
    @Override
    public String toJSON() {
        return value.toString();
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append(value.toString());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JSONNumber && value.equals(((JSONNumber)other).get());
    }

    public static JSONNumber valueOf(int value) {
        return value == 0 ? JSONNumber.ZERO : new JSONNumber(value);
    }

    public static JSONNumber valueOf(long value) {
        return value == 0 ? JSONNumber.ZERO :
                value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE ?
                        new JSONNumber((int)value) : new JSONNumber(value);
    }

    public static JSONNumber valueOf(float value) {
        return value == 0 ? JSONNumber.ZERO : new JSONNumber(value);
    }

    public static JSONNumber valueOf(double value) {
        return value == 0 ? JSONNumber.ZERO : new JSONNumber(value);
    }

}
