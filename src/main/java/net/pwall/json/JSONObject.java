/*
 * @(#) JSONObject.java
 */

package net.pwall.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private Map<String, Integer> map;

    public JSONObject() {
        list = new ArrayList<>();
        map = new HashMap<>();
    }

    public JSONValue get(String key) {
        Integer index = map.get(Objects.requireNonNull(key));
        return index == null ? null : list.get(index).getValue();
    }

    public void put(String key, JSONValue value) {
        Integer index = map.get(Objects.requireNonNull(key));
        if (index != null)
            list.get(index).setValue(value);
        else {
            map.put(key, list.size());
            list.add(new Entry(key, value));
        }
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

    public void remove(String key) {
        Integer index = map.remove(Objects.requireNonNull(key));
        if (index != null)
            list.remove(index.intValue());
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
        if (list.size() > 0) {
            int i = 0;
            for (;;) {
                Entry entry = list.get(i++);
                a.append('"');
                Strings.appendEscaped(a, entry.getKey(), JSON.charMapper);
                a.append('"').append(':');
                JSON.appendJSON(a, entry.getValue());
                if (i >= list.size())
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
