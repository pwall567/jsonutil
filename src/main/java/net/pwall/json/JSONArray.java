/*
 * @(#) JSONArray.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2016, 2017 Peter Wall
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A JSON array.
 *
 * @author Peter Wall
 */
public class JSONArray extends JSONSequence<JSONValue> {

    private static final long serialVersionUID = -6963671812529472759L;

    /**
     * Construct an empty {@code JSONArray}.
     */
    public JSONArray() {
    }

    /**
     * Construct a {@code JSONArray} from an array of {@link JSONValue}s.
     *
     * @param   values  the source values
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONArray(JSONValue ... values) {
        super(values);
    }

    /**
     * Construct a {@code JSONArray} from a {@link Collection} of {@link JSONValue}s.
     *
     * @param   collection  the source {@link Collection}
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONArray(Collection<? extends JSONValue> collection) {
        super(collection);
    }

    /**
     * Add a {@link JSONString} representing the supplied {@link CharSequence} ({@link String},
     * {@link StringBuilder} etc.) to the {@code JSONArray}.
     *
     * @param   cs      the {@link CharSequence}
     * @return          {@code this} (for chaining)
     * @throws  NullPointerException if the value is {@code null}
     */
    public JSONArray addValue(CharSequence cs) {
        add(new JSONString(cs));
        return this;
    }

    /**
     * Add a {@link JSONInteger} representing the supplied {@code int} to the {@code JSONArray}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(int value) {
        add(JSONInteger.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONLong} representing the supplied {@code long} to the {@code JSONArray}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(long value) {
        add(JSONLong.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONFloat} representing the supplied {@code float} to the {@code JSONArray}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(float value) {
        add(JSONFloat.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONDouble} representing the supplied {@code double} to the
     * {@code JSONArray}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(double value) {
        add(JSONDouble.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONBoolean} representing the supplied {@code boolean} to the
     * {@code JSONArray}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(boolean value) {
        add(JSONBoolean.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONBoolean} representing the supplied {@link Boolean} to the
     * {@code JSONArray}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     * @throws  NullPointerException if the value is {@code null}
     */
    public JSONArray addValue(Boolean value) {
        add(JSONBoolean.valueOf(Objects.requireNonNull(value).booleanValue()));
        return this;
    }

    public <T extends CharSequence> JSONArray addValues(Collection<T> collection) {
        for (CharSequence value : collection)
            addValue(value);
        return this;
    }

    public <T extends CharSequence> JSONArray addValues(
            @SuppressWarnings("unchecked") T ... values) {
        for (int i = 0, n = values.length; i < n; i++)
            addValue(values[i]);
        return this;
    }

    public JSONArray addValues(int ... values) {
        for (int i = 0, n = values.length; i < n; i++)
            addValue(values[i]);
        return this;
    }

    public JSONArray addValues(long ... values) {
        for (int i = 0, n = values.length; i < n; i++)
            addValue(values[i]);
        return this;
    }

    public JSONArray addValues(float ... values) {
        for (int i = 0, n = values.length; i < n; i++)
            addValue(values[i]);
        return this;
    }

    public JSONArray addValues(double ... values) {
        for (int i = 0, n = values.length; i < n; i++)
            addValue(values[i]);
        return this;
    }

    /**
     * Add a {@code null} value to the {@code JSONArray}.
     *
     * @return          {@code this} (for chaining)
     */
    public JSONArray addNull() {
        add(null);
        return this;
    }

    /**
     * Add a {@link JSONValue} to the {@code JSONArray}.  This method duplicates the
     * {@link ArrayList#add(Object) add(JSONValue)} method inherited from the {@link ArrayList}
     * class, but it also returns {@code this} to allow for chaining.
     *
     * @param   json    the {@link JSONValue}
     * @return          {@code this} (for chaining)
     */
    public JSONArray addJSON(JSONValue json) {
        add(json);
        return this;
    }

    /**
     * Convenience method to create a {@code JSONArray}.  Supports the idiom:
     * <pre>
     *     JSONArray arr = JSONArray.create().addValue(0).addValue(1).addValue(2);
     * </pre>
     *
     * @return  the new {@code JSONArray}
     */
    public static JSONArray create() {
        return new JSONArray();
    }

}
