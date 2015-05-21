/*
 * @(#) JSONObject.java
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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import net.pwall.util.Strings;

/**
 * A JSON object.
 *
 * @author Peter Wall
 */
public class JSONObject implements JSONValue, Iterable<String> {

    private List<Entry> list;

    public JSONObject() {
        list = new ArrayList<>();
    }

    public JSONValue get(String key) {
        int index = findIndex(Objects.requireNonNull(key));
        return index < 0 ? null : list.get(index).getValue();
    }

    public boolean containsKey(String key) {
        return findIndex(Objects.requireNonNull(key)) >= 0;
    }

    public void put(String key, JSONValue value) {
        int index = findIndex(Objects.requireNonNull(key));
        if (index >= 0)
            list.get(index).setValue(value);
        else
            list.add(new Entry(key, value));
    }

    public void putValue(String key, CharSequence cs) {
        put(key, new JSONString(cs));
    }

    public void putValue(String key, int value) {
        put(key, value == 0 ? JSONNumber.ZERO : new JSONNumber(value));
    }

    public void putValue(String key, long value) {
        put(key, new JSONNumber(value));
    }

    public void putValue(String key, float value) {
        put(key, new JSONNumber(value));
    }

    public void putValue(String key, double value) {
        put(key, new JSONNumber(value));
    }

    public void putValue(String key, Number value) {
        put(key, new JSONNumber(value));
    }

    public void putValue(String key, boolean value) {
        put(key, JSONBoolean.valueOf(value));
    }

    public void putValue(String key, Boolean value) {
        put(key, JSONBoolean.valueOf(Objects.requireNonNull(value).booleanValue()));
    }

    public void putNull(String key) {
        put(key, null);
    }

    public void remove(String key) {
        int index = findIndex(Objects.requireNonNull(key));
        if (index >= 0)
            list.remove(index);
    }

    private int findIndex(String key) {
        for (int i = 0, n = list.size(); i < n; i++)
            if (list.get(i).getKey().equals(key))
                return i;
        return -1;
    }

    /**
     * Get an {@link Iterator} over the names (keys) of the members of the object.
     *
     * @return  an {@link Iterator}
     */
    @Override
    public Iterator<String> iterator() {
        return new Iter();
    }

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

    @Override
    public String toString() {
        return toString();
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Entry entry : list) {
            result ^= entry.getKey().hashCode();
            result ^= Objects.hashCode(entry.getValue());
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JSONObject))
            return false;
        JSONObject o = (JSONObject)other;
        if (list.size() != o.list.size())
            return false;
        for (Entry entry : list)
            if (!Objects.equals(entry.getValue(), o.get(entry.getKey())))
                return false;
        return true;
    }

    public static class Entry {

        private String key;
        private JSONValue value;

        public Entry(String key, JSONValue value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public JSONValue getValue() {
            return value;
        }

        public void setValue(JSONValue value) {
            this.value = value;
        }

    }

    private class Iter implements Iterator<String> {

        private int index;

        public Iter() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public String next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return list.get(index++).getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
