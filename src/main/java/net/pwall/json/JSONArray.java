/*
 * @(#) JSONArray.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014 Peter Wall
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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A JSON array.
 *
 * @author Peter Wall
 */
public class JSONArray implements JSONValue, Iterable<JSONValue> {

    private List<JSONValue> list;

    /**
     * Construct an empty JSON array.
     */
    public JSONArray() {
        list = new ArrayList<>();
    }

    /**
     * Construct a JSON array by copying another JSON array.
     *
     * @param   array   the source {@code JSONArray}
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONArray(JSONValue[] array) {
        list = new ArrayList<>();
        for (JSONValue item : Objects.requireNonNull(array))
            list.add(item);
    }

    /**
     * Construct a JSON array from a {@link Collection} of JSON values.
     *
     * @param   collection  the source {@link Collection}
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONArray(Collection<JSONValue> collection) {
        list = new ArrayList<>(collection);
    }

    /**
     * Get the specified item from the JSON array.
     *
     * @param   index   the index of the required item
     * @return  the item at the requested index (may be {@code null})
     * @throws  IndexOutOfBoundsException if the index is beyond the end of the array
     */
    public JSONValue get(int index) {
        return list.get(index);
    }

    /**
     * Add an item to the JSON array.
     *
     * @param   item    the item to add
     */
    public void add(JSONValue item) {
        list.add(item);
    }

    /**
     * Add a {@link JSONString} to the JSON array representing the supplied {@link CharSequence}
     * ({@link String}, {@link StringBuilder} etc.).
     *
     * @param   cs      the {@link CharSequence}
     * @throws  NullPointerException if the value is {@code null}
     */
    public void addValue(CharSequence cs) {
        list.add(new JSONString(cs));
    }

    /**
     * Add a {@link JSONNumber} to the JSON array representing the supplied {@code int}.
     *
     * @param   value   the value
     */
    public void addValue(int value) {
        list.add(value == 0 ? JSONNumber.ZERO : new JSONNumber(value));
    }

    /**
     * Add a {@link JSONNumber} to the JSON array representing the supplied {@code long}.
     *
     * @param   value   the value
     */
    public void addValue(long value) {
        list.add(new JSONNumber(value));
    }

    /**
     * Add a {@link JSONNumber} to the JSON array representing the supplied {@code float}.
     *
     * @param   value   the value
     */
    public void addValue(float value) {
        list.add(new JSONNumber(value));
    }

    /**
     * Add a {@link JSONNumber} to the JSON array representing the supplied {@code double}.
     *
     * @param   value   the value
     */
    public void addValue(double value) {
        list.add(new JSONNumber(value));
    }

    /**
     * Add a {@link JSONNumber} to the JSON array representing the supplied {@link Number}.
     *
     * @param   value   the value
     * @throws  NullPointerException if the value is {@code null}
     */
    public void addValue(Number value) {
        list.add(new JSONNumber(value));
    }

    /**
     * Add a {@link JSONBoolean} to the JSON array representing the supplied {@code boolean}.
     *
     * @param   value   the value
     */
    public void addValue(boolean value) {
        list.add(JSONBoolean.valueOf(value));
    }

    /**
     * Add a {@link JSONBoolean} to the JSON array representing the supplied {@link Boolean}.
     *
     * @param   value   the value
     * @throws  NullPointerException if the value is {@code null}
     */
    public void addValue(Boolean value) {
        list.add(JSONBoolean.valueOf(Objects.requireNonNull(value).booleanValue()));
    }

    /**
     * Add a {@code null} value to the JSON array.
     */
    public void addNull() {
        list.add(null);
    }

    /**
     * Return an {@link Iterator} over the items in the array.
     *
     * @return  the {@link Iterator}
     */
    @Override
    public Iterator<JSONValue> iterator() {
        return new Iter();
    }

    /**
     * Create the external representation for this JSON array.
     *
     * @return  the JSON representation for this array
     */
    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
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
     */
    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append('[');
        if (list.size() > 0) {
            int i = 0;
            for (;;) {
                JSON.appendJSON(a, list.get(i++));
                if (i >= list.size())
                    break;
                a.append(',');
            }
        }
        a.append(']');
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
     * Return the hash code for the object.
     *
     * @return  the hash code
     */
    @Override
    public int hashCode() {
        return list.hashCode();
    }

    /**
     * Compare two JSON arrays for equality.
     *
     * @param   other   the other JSON array
     * @return  {@code true} if the other object is a JSON array and the contents are equal
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof JSONArray && list.equals(((JSONArray)other).list);
    }

    /**
     * Inner class to implement {@link Iterator} interface.
     */
    private class Iter implements Iterator<JSONValue> {

        private int index;

        public Iter() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public JSONValue next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return list.get(index++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
