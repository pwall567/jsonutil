/*
 * @(#) JSONArray.java
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A JSON array.
 *
 * @author Peter Wall
 */
public class JSONArray extends ArrayList<JSONValue> implements JSONComposite {

    private static final long serialVersionUID = -6963671812529472759L;

    /**
     * Construct an empty JSON array.
     */
    public JSONArray() {
    }

    /**
     * Construct a JSON array by copying another JSON array.
     *
     * @param   array   the source {@code JSONArray}
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONArray(JSONValue[] array) {
        for (JSONValue item : Objects.requireNonNull(array))
            add(item);
    }

    /**
     * Construct a JSON array from a {@link Collection} of JSON values.
     *
     * @param   collection  the source {@link Collection}
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONArray(Collection<JSONValue> collection) {
        super(collection);
    }

    /**
     * Add a {@link JSONString} to the JSON array representing the supplied {@link CharSequence}
     * ({@link String}, {@link StringBuilder} etc.).
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
     * Add a {@link JSONInteger} to the JSON array representing the supplied {@code int}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(int value) {
        add(JSONInteger.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONLong} to the JSON array representing the supplied {@code long}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(long value) {
        add(JSONLong.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONFloat} to the JSON array representing the supplied {@code float}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(float value) {
        add(JSONFloat.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONDouble} to the JSON array representing the supplied {@code double}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(double value) {
        add(JSONDouble.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONBoolean} to the JSON array representing the supplied {@code boolean}.
     *
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONArray addValue(boolean value) {
        add(JSONBoolean.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONBoolean} to the JSON array representing the supplied {@link Boolean}.
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

    public <T extends CharSequence> JSONArray addValues(T[] array) {
        for (int i = 0, n = array.length; i < n; i++)
            addValue(array[i]);
        return this;
    }

    public JSONArray addValues(int[] array) {
        for (int i = 0, n = array.length; i < n; i++)
            addValue(array[i]);
        return this;
    }

    public JSONArray addValues(long[] array) {
        for (int i = 0, n = array.length; i < n; i++)
            addValue(array[i]);
        return this;
    }

    public JSONArray addValues(float[] array) {
        for (int i = 0, n = array.length; i < n; i++)
            addValue(array[i]);
        return this;
    }

    public JSONArray addValues(double[] array) {
        for (int i = 0, n = array.length; i < n; i++)
            addValue(array[i]);
        return this;
    }

    /**
     * Add a {@code null} value to the JSON array.
     *
     * @return          {@code this} (for chaining)
     */
    public JSONArray addNull() {
        add(null);
        return this;
    }

    /**
     * Get a {@link String} value from the array.  If the array entry is {@code null} return
     * {@code null}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONString}
     */
    public String getString(int index) {
        return JSON.getString(get(index));
    }

    /**
     * Get an {@code int} value from the array.  If the array entry is {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public int getInt(int index) {
        return JSON.getInt(get(index));
    }

    /**
     * Get a {@code long} value from the array.  If the array entry is {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public long getLong(int index) {
        return JSON.getLong(get(index));
    }

    /**
     * Get a {@code float} value from the array.  If the array entry is {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public float getFloat(int index) {
        return JSON.getFloat(get(index));
    }

    /**
     * Get a {@code double} value from the array.  If the array entry is {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public double getDouble(int index) {
        return JSON.getDouble(get(index));
    }

    /**
     * Get a {@code boolean} value from the array.  If the array entry is {@code null} return
     * {@code false}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONBoolean}
     */
    public boolean getBoolean(int index) {
        return JSON.getBoolean(get(index));
    }

    /**
     * Get a {@link JSONArray} value from the array.  If the array entry is {@code null} return
     * {@code null}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONArray}
     */
    public JSONArray getArray(int index) {
        return JSON.getArray(get(index));
    }

    /**
     * Get a {@link JSONObject} value from the array.  If the array entry is {@code null} return
     * {@code null}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONArray}
     */
    public JSONObject getObject(int index) {
        return JSON.getObject(get(index));
    }

    /**
     * Create the external representation for this JSON array.
     *
     * @return  the JSON representation for this array
     * @see     JSONValue#toJSON()
     */
    @Override
    public String toJSON() {
        int estimate = size() * 20;
        StringBuilder sb = new StringBuilder(estimate);
        try {
            appendJSON(sb);
        }
        catch (IOException e) {
            // can't happen - StringBuilder does not throw IOException
        }
        return sb.toString();
    }

    /**
     * Append the external representation for this JSON array to a given {@link Appendable}.
     *
     * @param   a   the {@link Appendable}
     * @throws  IOException     if thrown by the {@link Appendable}
     * @see     JSONValue#appendJSON(Appendable)
     */
    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append('[');
        if (size() > 0) {
            int i = 0;
            for (;;) {
                JSON.appendJSON(a, get(i++));
                if (i >= size())
                    break;
                a.append(',');
            }
        }
        a.append(']');
    }

    /**
     * Test whether the composite is "simple", i.e. it contains only non-composite values (to
     * assist with formatting).
     *
     * @return  {@code true} if the composite is simple
     * @see     JSONComposite#isSimple()
     */
    @Override
    public boolean isSimple() {
        for (int i = 0; i < size(); i++)
            if (get(i) instanceof JSONComposite)
                return false;
        return true;
    }

    /**
     * Return a string representation of the JSON array.  This is the same as the JSON format.
     *
     * @return  the JSON string
     */
    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * Compare two JSON arrays for equality.
     *
     * @param   other   the other JSON array
     * @return  {@code true} if the other object is a JSON array and the contents are equal
     */
    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof JSONArray && super.equals(other);
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
