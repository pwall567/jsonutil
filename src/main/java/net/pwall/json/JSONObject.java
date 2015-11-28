/*
 * @(#) JSONObject.java
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
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import net.pwall.util.Strings;

/**
 * A JSON object.
 *
 * @author Peter Wall
 */
public class JSONObject implements JSONComposite, Map<String, JSONValue>, Iterable<String> {

    private static final long serialVersionUID = 4424892153019501302L;

    private List<Entry> list;

    /**
     * Construct an empty {@code JSONObject}.
     */
    public JSONObject() {
        list = new ArrayList<>();
    }

    /**
     * Construct a {@code JSONObject}, copying the contents of another object.
     *
     * @param   other   the other {@code JSONObject}
     */
    public JSONObject(JSONObject other) {
        list = new ArrayList<>(other.list);
    }

    public JSONObject putValue(String key, CharSequence cs) {
        put(key, new JSONString(cs));
        return this;
    }

    /**
     * Add a {@link JSONInteger} to the {@code JSONObject} representing the supplied
     * {@code int}.
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
     * Add a {@link JSONLong} to the {@code JSONObject} representing the supplied {@code long}.
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
     * Add a {@link JSONFloat} to the {@code JSONObject} representing the supplied
     * {@code float}.
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
     * Add a {@link JSONDouble} to the {@code JSONObject} representing the supplied
     * {@code double}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, double value) {
        put(key, JSONDouble.valueOf(value));
        return this;
    }

//    /**
//     * Add a {@link JSONNumber} to the {@code JSONObject}.
//     *
//     * @param   key     the key to use when storing the value
//     * @param   value   the value
//     * @return          {@code this} (for chaining)
//     */
//    public JSONObject putValue(String key, Number value) {
//        put(key, new JSONNumber(value));
//        return this;
//    }

    /**
     * Add a {@link JSONBoolean} to the {@code JSONObject} representing the supplied
     * {@code boolean}.
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
     * Add a {@link JSONBoolean} to the {@code JSONObject}.
     *
     * @param   key     the key to use when storing the value
     * @param   value   the value
     * @return          {@code this} (for chaining)
     */
    public JSONObject putValue(String key, Boolean value) {
        put(key, JSONBoolean.valueOf(Objects.requireNonNull(value).booleanValue()));
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
     * Get a value from the {@code JSONObject} as a {@link String}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code null} if not found
     * @throws  IllegalStateException if the value is found but is not a string
     */
    public String getString(String key) {
        return JSON.getString(get(key));
    }

    /**
     * Get a value from the {@code JSONObject} as an {@code int}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  IllegalStateException if the value is found but is not a number
     */
    public int getInt(String key) {
        return JSON.getInt(get(key));
    }

    /**
     * Get a value from the {@code JSONObject} as a {@code long}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  IllegalStateException if the value is found but is not a number
     */
    public long getLong(String key) {
        return JSON.getLong(get(key));
    }

    /**
     * Get a value from the {@code JSONObject} as a {@code float}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  IllegalStateException if the value is found but is not a number
     */
    public float getFloat(String key) {
        return JSON.getFloat(get(key));
    }

    /**
     * Get a value from the {@code JSONObject} as a {@code double}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code 0} if not found
     * @throws  IllegalStateException if the value is found but is not a number
     */
    public double getDouble(String key) {
        return JSON.getDouble(get(key));
    }

    /**
     * Get a value from the {@code JSONObject} as a {@code boolean}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code false} if not found
     * @throws  IllegalStateException if the value is found but is not a number
     */
    public boolean getBoolean(String key) {
        return JSON.getBoolean(get(key));
    }

    /**
     * Get a value from the {@code JSONObject} as a {@code JSONArray}.
     *
     * @param   key     the key of the value
     * @return  the value
     * @throws  IllegalStateException if the array entry is not an array
     */
    public JSONArray getArray(String key) {
        return JSON.getArray(get(key));
    }

    /**
     * Get a value from the {@code JSONObject} as a {@code JSONObject}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code null} if not found
     * @throws  IllegalStateException if the array entry is not an object
     */
    public JSONObject getObject(String key) {
        return JSON.getObject(get(key));
    }

    /**
     * Get a value from the {@code JSONObject}.
     *
     * @param   key     the key of the value
     * @return          the value, or {@code null} if not found
     * @see     Map#get(Object)
     */
    @Override
    public JSONValue get(Object key) {
        int index = findIndex(Objects.requireNonNull(key));
        return index < 0 ? null : list.get(index).getValue();
    }

    /**
     * Test whether the {@code JSONObject} contains a specified key.
     *
     * @param   key     the key to test for
     * @return          {@code true} if the key is found
     * @see     Map#containsKey(Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return findIndex(Objects.requireNonNull(key)) >= 0;
    }

    /**
     * Store a value in the {@code JSONObject} with the specified key.
     *
     * @param   key     the key
     * @param   value   the value
     * @return          the previous value stored with that key, or {@code null} if no previous
     *                  value
     * @see     Map#put(Object, Object)
     */
    @Override
    public JSONValue put(String key, JSONValue value) {
        int index = findIndex(Objects.requireNonNull(key));
        if (index >= 0) {
            JSONValue oldValue = list.get(index).getValue();
            list.get(index).setValue(value);
            return oldValue;
        }
        list.add(new Entry(key, value));
        return null;
    }

    /**
     * Remove the specified key-value mapping from the {@code JSONObject}.
     *
     * @param   key     the key
     * @return          the value stored with that key, or {@code null} if key not used
     * @see     Map#remove(Object)
     */
    @Override
    public JSONValue remove(Object key) {
        int index = findIndex(Objects.requireNonNull(key));
        if (index >= 0) {
            Entry entry = list.remove(index);
            return entry.getValue();
        }
        return null;
    }

    /**
     * Get the number of values in the {@code JSONObject}.
     *
     * @return  the number of values
     * @see     Map#size()
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * Test whether the {@code JSONObject}is empty.
     *
     * @return  {@code true} if the {@code JSONObject} is empty
     * @see     Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Test whether the {@code JSONObject} contains the specified value.
     *
     * @param   value   the value
     * @return          {@code true} if the {@code JSONObject} contains the value
     * @see     Map#containsValue(Object)
     */
    @Override
    public boolean containsValue(Object value) {
        for (int i = 0, n = list.size(); i < n; i++)
            if (Objects.equals(list.get(i).getValue(), value))
                return true;
        return false;
    }

    /**
     * Add all the members of another {@link Map} to this {@code JSONObject}.
     *
     * @param   m       the other {@link Map}
     * @see     Map#putAll(Map)
     */
    @Override
    public void putAll(Map<? extends String, ? extends JSONValue> m) {
        for (String s : m.keySet())
            put(s, m.get(s));
    }

    /**
     * Remove all members from this {@code JSONObject}.
     *
     * @see     Map#clear()
     */
    @Override
    public void clear() {
        list.clear();
    }

    /**
     * Get a {@link Set} representing the keys in use in the {@code JSONObject}.
     *
     * @return  the {@link Set} of keys
     * @see     Map#keySet()
     */
    @Override
    public Set<String> keySet() {
        return new KeySet();
    }

    /**
     * Get a {@link Collection} of the values in the {@code JSONObject}.
     *
     * @return  the {@link Collection} of values
     * @see     Map#values()
     */
    @Override
    public Collection<JSONValue> values() {
        return new ValueCollection();
    }

    /**
     * Get a {@link Set} of the key-value pairs in use in the {@code JSONObject}.
     *
     * @return  the {@link Set} of key-value pairs
     * @see     Map#entrySet()
     */
    @Override
    public Set<Map.Entry<String, JSONValue>> entrySet() {
        return new EntrySet();
    }

    /**
     * Get an {@link Iterator} over the names (keys) of the members of the object.
     *
     * @return  an {@link Iterator}
     * @see     Iterable#iterator()
     */
    @Override
    public Iterator<String> iterator() {
        return new KeyIterator();
    }

    /**
     * Create the external representation for this {@code JSONObject}.
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
                Entry entry = list.get(i++);
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
     * Test whether the composite is "simple", i.e. it contains only non-composite values (to
     * assist with formatting).
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
     * Get the {@link String} representation of this {@code JSONObject}.
     *
     * @return  the string representation for this object
     * @see     Object#toString()
     */
    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * Get the hash code for this {@code JSONObject}.
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
     * Compare this {@code JSONObject} with another object for equality.
     *
     * @param   other   the other object
     * @return  {@code true} if the other object is a {@code JSONObject} and is identical to
     *          this object
     * @see     Object#equals(Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof JSONObject))
            return false;
        JSONObject otherObj = (JSONObject)other;
        if (list.size() != otherObj.list.size())
            return false;
        for (Entry entry : list)
            if (!Objects.equals(entry.getValue(), otherObj.get(entry.getKey())))
                return false;
        return true;
    }

    /**
     * Get an {@link Entry} by index.
     *
     * @param   index   the index
     * @return  the list entry
     */
    public Entry getEntry(int index) {
        return list.get(index);
    }

    /**
     * Find the index for the specified key.
     *
     * @param   key     the key
     * @return          the index for this key, or -1 if not found
     */
    private int findIndex(Object key) {
        for (int i = 0, n = list.size(); i < n; i++)
            if (list.get(i).getKey().equals(key))
                return i;
        return -1;
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

    /**
     * Inner class to represent a key-value pair in the {@code JSONObject}.
     */
    public static class Entry implements Map.Entry<String, JSONValue>, Serializable {

        private static final long serialVersionUID = 7415672545416600849L;

        private String key;
        private JSONValue value;

        public Entry(String key, JSONValue value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public JSONValue getValue() {
            return value;
        }

        @Override
        public JSONValue setValue(JSONValue value) {
            JSONValue oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Entry))
                return false;
            Entry otherEntry = (Entry)other;
            return Objects.equals(key, otherEntry.key) &&
                    Objects.equals(value, otherEntry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

    }

    /**
     * An {@link Iterator} over the {@link Set} of key-value pairs in the {@code JSONObject}.
     */
    private class EntryIterator extends BaseIterator<Map.Entry<String, JSONValue>> {

        @Override
        public Entry next() {
            return nextEntry();
        }

    }

    /**
     * An {@link Iterator} over the {@link Set} of keys in the {@code JSONObject}.
     */
    private class KeyIterator extends BaseIterator<String> {

        @Override
        public String next() {
            return nextEntry().getKey();
        }

    }

    /**
     * An {@link Iterator} over the {@link Collection} of values in the {@code JSONObject}.
     */
    private class ValueIterator extends BaseIterator<JSONValue> {

        @Override
        public JSONValue next() {
            return nextEntry().getValue();
        }

    }

    /**
     * Abstract base class for various iterators.
     *
     * @param   <T>     the returned type
     */
    private abstract class BaseIterator<T> implements Iterator<T> {

        private int index;

        public BaseIterator() {
            index = 0;
        }

        public Entry nextEntry() {
            if (!hasNext())
                throw new NoSuchElementException();
            return list.get(index++);
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * A collection of the key-value pairs in the {@code JSONObject}.
     *
     * @see     #entrySet()
     */
    private class EntrySet extends CollectionBase<Map.Entry<String, JSONValue>> {

        @Override
        public boolean contains(Object o) {
            return list.contains(o);
        }

        @Override
        public Iterator<Map.Entry<String, JSONValue>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c)
                if (!list.contains(o))
                    return false;
            return true;
        }

    }

    /**
     * A collection of the keys in the {@code JSONObject}.
     *
     * @see     #keySet()
     */
    private class KeySet extends CollectionBase<String> {

        @Override
        public boolean contains(Object o) {
            return containsKey(o);
        }

        @Override
        public Iterator<String> iterator() {
            return new KeyIterator();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c)
                if (!containsKey(o))
                    return false;
            return true;
        }

    }

    /**
     * A collection of the values in the {@code JSONObject}.
     *
     * @see     #values()
     */
    private class ValueCollection extends CollectionBase<JSONValue> {

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public Iterator<JSONValue> iterator() {
            return new ValueIterator();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c)
                if (!containsValue(o))
                    return false;
            return true;
        }

    }

    /**
     * Abstract base class for various returned collections.  All modifying operations throw an
     * {@link UnsupportedOperationException}.
     *
     * @param   <T>     the returned type
     */
    private abstract class CollectionBase<T> extends AbstractSet<T> {

        /**
         * Return the number of elements in the set.  All returned collections are the same size
         * as the underlying collection.
         *
         * @return  the number of elements in the collection
         */
        @Override
        public int size() {
            return list.size();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

    }

}
