/*
 * @(#) JSONObject.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2017 Peter Wall
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

import java.util.Map;

/**
 * A JSON object.
 *
 * @author Peter Wall
 */
public class JSONObject extends JSONMapping<JSONValue> {

    private static final long serialVersionUID = 4424892153019501302L;

    /**
     * Construct an empty {@code JSONObject}.
     */
    public JSONObject() {
    }

    /**
     * Construct a {@code JSONObject}, copying the contents of another object.
     *
     * @param   other   the other {@code JSONObject}
     */
    public JSONObject(JSONObject other) {
        super(other);
    }

    /**
     * Add a {@link JSONString} representing the supplied {@link CharSequence} ({@link String},
     * {@link StringBuilder} etc.) to the {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, CharSequence value) {
        put(key, new JSONString(value));
        return this;
    }

    /**
     * Add a {@link JSONInteger} representing the supplied {@code int} to the
     * {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, int value) {
        put(key, JSONInteger.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONLong} representing the supplied {@code long} to the {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, long value) {
        put(key, JSONLong.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONFloat} representing the supplied {@code float} to the
     * {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, float value) {
        put(key, JSONFloat.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONDouble} representing the supplied {@code double} to the
     * {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, double value) {
        put(key, JSONDouble.valueOf(value));
        return this;
    }

    /**
     * Add a {@link JSONBoolean} representing the supplied {@code boolean} to the
     * {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, boolean value) {
        put(key, JSONBoolean.valueOf(value));
        return this;
    }

    /**
     * Add a {@code null} to the {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putNull(String key) {
        put(key, null);
        return this;
    }

    /**
     * Add a {@link JSONValue} to the {@code JSONObject}.  This method duplicates the
     * {@link #put(String, JSONValue)} method specified by the {@link Map} interface, but it
     * also returns {@code this} to allow for chaining.
     *
     * @param   key     the key to use when storing the value
     * @param   json    the {@link JSONValue}
     * @return          {@code this} (for chaining)
     */
    public JSONObject putJSON(String key, JSONValue json) {
        put(key, json);
        return this;
    }

    /**
     * Convenience method to create a {@code JSONObject}.  Supports the idiom:
     * <pre>
     *     JSONObject obj = JSONObject.create().putValue("zero", 0).putValue("one", 1);
     * </pre>
     *
     * @return  the new {@code JSONObject}
     */
    public static JSONObject create() {
        return new JSONObject();
    }

}
