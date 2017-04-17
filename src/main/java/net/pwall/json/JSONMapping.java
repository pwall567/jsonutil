/*
 * @(#) JSONMapping.java
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import net.pwall.util.ListMap;
import net.pwall.util.Strings;

/**
 * A JSON mapping (the base for {@link JSONObject}, and possibly other similar collections).
 *
 * @author Peter Wall
 */
public class JSONMapping<V extends JSONValue> extends ListMap<String, V>
        implements JSONComposite, Iterable<String> {

    private static final long serialVersionUID = 4424892153019501302L;

    /**
     * Construct an empty {@code JSONMapping}.
     */
    public JSONMapping() {
    }

    /**
     * Construct a {@code JSONMapping}, copying the contents of another object.
     *
     * @param   other   the other {@code JSONMapping}
     */
    public JSONMapping(JSONMapping<V> other) {
        super(other);
    }

    /**
     * Get a value from the {@code JSONMapping} as a {@link String}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code null} if not found
     * @throws  JSONException if the value is found but is not a string
     */
    public String getString(String key) {
        return JSON.getString(get(key));
    }

    /**
     * Get a value from the {@code JSONMapping} as an {@code int}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  JSONException if the value is found but is not a number
     */
    public int getInt(String key) {
        return JSON.getInt(get(key));
    }

    /**
     * Get a value from the {@code JSONMapping} as a {@code long}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  JSONException if the value is found but is not a number
     */
    public long getLong(String key) {
        return JSON.getLong(get(key));
    }

    /**
     * Get a value from the {@code JSONMapping} as a {@code float}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  JSONException if the value is found but is not a number
     */
    public float getFloat(String key) {
        return JSON.getFloat(get(key));
    }

    /**
     * Get a value from the {@code JSONMapping} as a {@code double}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  JSONException if the value is found but is not a number
     */
    public double getDouble(String key) {
        return JSON.getDouble(get(key));
    }

    /**
     * Get a value from the {@code JSONMapping} as a {@code boolean}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code false} if not found
     * @throws  JSONException if the value is found but is not a number
     */
    public boolean getBoolean(String key) {
        return JSON.getBoolean(get(key));
    }

    /**
     * Get a value from the {@code JSONMapping} as a {@code JSONArray}.
     *
     * @param   key     the key of the value
     * @return  the value
     * @throws  JSONException if the array entry is not an array
     */
    public JSONArray getArray(String key) {
        return JSON.getArray(get(key));
    }

    /**
     * Get a value from the {@code JSONMapping} as a {@code JSONObject}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code null} if not found
     * @throws  JSONException if the array entry is not an object
     */
    public JSONObject getObject(String key) {
        return JSON.getObject(get(key));
    }

    /**
     * Get an {@link Iterator} over the names (keys) of the members of the object.
     *
     * @return  an {@link Iterator}
     * @see     Iterable#iterator()
     */
    @Override
    public Iterator<String> iterator() {
        return keySet().iterator();
    }

    /**
     * Create the JSON representation for this {@code JSONMapping}.
     *
     * @return  the JSON representation for this object
     * @see     JSONValue#toJSON()
     */
    @Override
    public String toJSON() {
        int estimate = size() * 40;
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
     * Append the external representation for this JSON object to a given {@link Appendable}.
     *
     * @param   a   the {@link Appendable}
     * @throws  IOException     if thrown by the {@link Appendable}
     * @see     JSONValue#appendJSON(Appendable)
     */
    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append('{');
        int n = list.size();
        if (n > 0) {
            int i = 0;
            for (;;) {
                Entry<String, V> entry = list.get(i++);
                a.append('"');
                Strings.appendEscaped(a, entry.getKey(), JSON.charMapper);
                a.append('"').append(':');
                JSON.appendJSON(a, entry.getValue());
                if (i >= n)
                    break;
                a.append(',');
            }
        }
        a.append('}');
    }

    /**
     * Test whether the composite is "simple", i.e.&nbsp;it contains only non-composite values
     * (to assist with formatting).
     *
     * @return  {@code true} if the composite is simple
     * @see     JSONComposite#isSimple()
     */
    @Override
    public boolean isSimple() {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getValue() instanceof JSONComposite)
                return false;
        return true;
    }

    /**
     * Get the {@link String} representation of this {@code JSONMapping}.
     *
     * @return  the string representation for this object
     * @see     Object#toString()
     */
    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * Get the hash code for this {@code JSONMapping}.
     *
     * @return  the hash code
     * @see     Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0, n = list.size(); i < n; i++)
            result ^= list.get(i).hashCode();
        return result;
    }

    /**
     * Compare this {@code JSONMapping} with another object for equality.
     *
     * @param   other   the other object
     * @return  {@code true} if the other object is a {@code JSONMapping} and is identical to
     *          this object
     * @see     Object#equals(Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof JSONMapping<?>))
            return false;
        JSONMapping<?> otherMapping = (JSONMapping<?>)other;
        if (list.size() != otherMapping.list.size())
            return false;
        for (Entry<String, V> entry : list)
            if (!Objects.equals(entry.getValue(), otherMapping.get(entry.getKey())))
                return false;
        return true;
    }

}
