/*
 * @(#) JSONNumberValue.java
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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Additional interface to assist with comparison of numeric JSON values.
 *
 * @author Peter Wall
 */
public abstract class JSONNumberValue extends Number implements JSONValue {

    public abstract BigInteger bigIntegerValue();

    public abstract BigDecimal bigDecimalValue();

    /**
     * Compare value with an {@code int}.
     *
     * @param   other   the other {@code int}
     * @return  {@code true} if the values are equal
     */
    public abstract boolean valueEquals(int other);

    /**
     * Compare value with a {@code long}.
     *
     * @param   other   the other {@code long}
     * @return  {@code true} if the values are equal
     */
    public abstract boolean valueEquals(long other);

    /**
     * Compare value with a {@code float}.
     *
     * @param   other   the other {@code float}
     * @return  {@code true} if the values are equal
     */
    public abstract boolean valueEquals(float other);

    /**
     * Compare value with a {@code double}.
     *
     * @param   other   the other {@code double}
     * @return  {@code true} if the values are equal
     */
    public abstract boolean valueEquals(double other);

    /**
     * Compare value with a {@link BigInteger}.
     *
     * @param   other   the other {@link BigInteger}
     * @return  {@code true} if the values are equal
     */
    public abstract boolean valueEquals(BigInteger other);

    /**
     * Compare value with a {@link BigDecimal}.
     *
     * @param   other   the other {@link BigInteger}
     * @return  {@code true} if the values are equal
     */
    public abstract boolean valueEquals(BigDecimal other);

}
