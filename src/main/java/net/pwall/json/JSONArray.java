/*
 * @(#) JSONArray.java
 */

package net.pwall.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A JSON array.
 *
 * @author Peter Wall
 */
public class JSONArray implements JSONValue, Iterable<JSONValue> {

    private List<JSONValue> list;

    public JSONArray() {
        list = new ArrayList<>();
    }

    public JSONArray(JSONValue[] array) {
        list = new ArrayList<>();
        for (JSONValue item : array)
            list.add(item);
    }

    public JSONArray(Collection<JSONValue> collection) {
        list = new ArrayList<>(collection);
    }

    public JSONValue get(int index) {
        return list.get(index);
    }

    public void add(JSONValue item) {
        list.add(item);
    }

    public void addValue(CharSequence cs) {
        list.add(new JSONString(cs));
    }

    public void addValue(int value) {
        list.add(new JSONNumber(value));
    }

    public void addValue(long value) {
        list.add(new JSONNumber(value));
    }

    public void addValue(float value) {
        list.add(new JSONNumber(value));
    }

    public void addValue(double value) {
        list.add(new JSONNumber(value));
    }

    public void addValue(Number value) {
        list.add(new JSONNumber(value));
    }

    public void addValue(boolean value) {
        list.add(new JSONBoolean(value));
    }

    public void addValue(Boolean value) {
        list.add(new JSONBoolean(value));
    }

    @Override
    public Iterator<JSONValue> iterator() {
        return new Iter();
    }

    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (list.size() > 0) {
            int i = 0;
            for (;;) {
                sb.append(JSON.toJSON(list.get(i++)));
                if (i >= list.size())
                    break;
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JSONArray && list.equals(((JSONArray)other).list);
    }

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
