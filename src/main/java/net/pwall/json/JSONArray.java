/*
 * @(#) JSONArray.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2016 Peter Wall
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
import java.util.Objects;

/**
 * A JSON array.
 *
 * @author Peter Wall
 */
public class JSONArray extends ArrayList<JSONValue> implements JSONComposite {

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
        for (int i = 0, n = values.length; i < n; i++)
            add(values[i]);
    }

    /**
     * Construct a {@code JSONArray} from a {@link Collection} of {@link JSONValue}s.
     *
     * @param   collection  the source {@link Collection}
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONArray(Collection<JSONValue> collection) {
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
     * Get a {@link String} value from the {@code JSONArray}.  If the array entry is
     * {@code null} return {@code null}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONString}
     */
    public String getString(int index) {
        return JSON.getString(get(index));
    }

    /**
     * Get an {@code int} value from the {@code JSONArray}.  If the array entry is {@code null}
     * return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public int getInt(int index) {
        return JSON.getInt(get(index));
    }

    /**
     * Get a {@code long} value from the {@code JSONArray}.  If the array entry is {@code null}
     * return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public long getLong(int index) {
        return JSON.getLong(get(index));
    }

    /**
     * Get a {@code float} value from the {@code JSONArray}.  If the array entry is {@code null}
     * return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public float getFloat(int index) {
        return JSON.getFloat(get(index));
    }

    /**
     * Get a {@code double} value from the {@code JSONArray}.  If the array entry is
     * {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public double getDouble(int index) {
        return JSON.getDouble(get(index));
    }

    /**
     * Get a {@code boolean} value from the {@code JSONArray}.  If the array entry is
     * {@code null} return {@code false}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONBoolean}
     */
    public boolean getBoolean(int index) {
        return JSON.getBoolean(get(index));
    }

    /**
     * Get a {@link JSONArray} value from the {@code JSONArray}.  If the array entry is
     * {@code null} return {@code null}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONArray}
     */
    public JSONArray getArray(int index) {
        return JSON.getArray(get(index));
    }

    /**
     * Get a {@link JSONObject} value from the {@code JSONArray}.  If the array entry is
     * {@code null} return {@code null}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONArray}
     */
    public JSONObject getObject(int index) {
        return JSON.getObject(get(index));
    }

    /**
     * Get an {@link Iterable} of {@link String} from the {@code JSONArray}.  Supports the
     * idiom:
     * <pre>
     *     for (String item : jsonArray.strings()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link String} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link String}
     */
    public Iterable<String> strings() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new StringIterator();
            }
        };
    }

    /**
     * Get an {@link Iterable} of {@link Integer} from the {@code JSONArray}.  Supports the
     * idiom:
     * <pre>
     *     for (Integer item : jsonArray.ints()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link Integer} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link Integer}
     */
    public Iterable<Integer> ints() {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new IntegerIterator();
            }
        };
    }

    /**
     * Get an {@link Iterable} of {@link Long} from the {@code JSONArray}.  Supports the idiom:
     * <pre>
     *     for (Long item : jsonArray.longs()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link Long} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link Long}
     */
    public Iterable<Long> longs() {
        return new Iterable<Long>() {
            @Override
            public Iterator<Long> iterator() {
                return new LongIterator();
            }
        };
    }

    /**
     * Get an {@link Iterable} of {@link Double} from the {@code JSONArray}.  Supports the
     * idiom:
     * <pre>
     *     for (Double item : jsonArray.doubles()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link Double} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link Double}
     */
    public Iterable<Double> doubles() {
        return new Iterable<Double>() {
            @Override
            public Iterator<Double> iterator() {
                return new DoubleIterator();
            }
        };
    }

    /**
     * Get an {@link Iterable} of {@link Float} from the {@code JSONArray}.  Supports the idiom:
     * <pre>
     *     for (Float item : jsonArray.floats()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link Float} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link Float}
     */
    public Iterable<Float> floats() {
        return new Iterable<Float>() {
            @Override
            public Iterator<Float> iterator() {
                return new FloatIterator();
            }
        };
    }

    /**
     * Get an {@link Iterable} of {@link Boolean} from the {@code JSONArray}.  Supports the
     * idiom:
     * <pre>
     *     for (Boolean item : jsonArray.booleans()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link Boolean} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link Boolean}
     */
    public Iterable<Boolean> booleans() {
        return new Iterable<Boolean>() {
            @Override
            public Iterator<Boolean> iterator() {
                return new BooleanIterator();
            }
        };
    }

    /**
     * Get an {@link Iterable} of {@link JSONArray} from the {@code JSONArray}.  Supports the
     * idiom:
     * <pre>
     *     for (JSONArray item : jsonArray.arrays()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link JSONArray} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link JSONArray}
     */
    public Iterable<JSONArray> arrays() {
        return new Iterable<JSONArray>() {
            @Override
            public Iterator<JSONArray> iterator() {
                return new ArrayIterator();
            }
        };
    }

    /**
     * Get an {@link Iterable} of {@link JSONObject} from the {@code JSONArray}.  Supports the
     * idiom:
     * <pre>
     *     for (JSONObject item : jsonArray.objects()) {
     *         // process each item of the array
     *     }
     * </pre>
     * The resulting {@link Iterator} will throw a {@link JSONException} if any item in the
     * array is not a {@link JSONObject} or {@code null}.
     *
     * @return  an {@link Iterable} of {@link JSONObject}
     */
    public Iterable<JSONObject> objects() {
        return new Iterable<JSONObject>() {
            @Override
            public Iterator<JSONObject> iterator() {
                return new ObjectIterator();
            }
        };
    }

    /**
     * Create the external representation for the {@code JSONArray}.
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
     * Append the external representation for the {@code JSONArray} to a given
     * {@link Appendable}.
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
     * Return a string representation of the {@code JSONArray}.  This is the same as the JSON
     * format.
     *
     * @return  the JSON string
     */
    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * Compare two {@code JSONArray}s for equality.
     *
     * @param   other   the other {@code JSONArray}
     * @return  {@code true} if the other object is a {@code JSONArray} and the contents are
     *          equal
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

    public abstract class BaseIterator<T> implements Iterator<T> {

        protected Iterator<JSONValue> iterator = iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public void remove() {
            iterator.remove();
        }

    }

    public class StringIterator extends BaseIterator<String> {

        @Override
        public String next() {
            return JSON.getString(iterator.next());
        }

    }

    public class IntegerIterator extends BaseIterator<Integer> {

        @Override
        public Integer next() {
            JSONValue value = iterator.next();
            if (value == null)
                return null;
            if (!(value instanceof Number))
                throw new JSONException(JSON.NOT_A_NUMBER);
            return ((Number)value).intValue();
        }

    }

    public class LongIterator extends BaseIterator<Long> {

        @Override
        public Long next() {
            JSONValue value = iterator.next();
            if (value == null)
                return null;
            if (!(value instanceof Number))
                throw new JSONException(JSON.NOT_A_NUMBER);
            return ((Number)value).longValue();
        }

    }

    public class DoubleIterator extends BaseIterator<Double> {

        @Override
        public Double next() {
            JSONValue value = iterator.next();
            if (value == null)
                return null;
            if (!(value instanceof Number))
                throw new JSONException(JSON.NOT_A_NUMBER);
            return ((Number)value).doubleValue();
        }

    }

    public class FloatIterator extends BaseIterator<Float> {

        @Override
        public Float next() {
            JSONValue value = iterator.next();
            if (value == null)
                return null;
            if (!(value instanceof Number))
                throw new JSONException(JSON.NOT_A_NUMBER);
            return ((Number)value).floatValue();
        }

    }

    public class BooleanIterator extends BaseIterator<Boolean> {

        @Override
        public Boolean next() {
            JSONValue value = iterator.next();
            if (value == null)
                return null;
            if (!(value instanceof JSONBoolean))
                throw new JSONException(JSON.NOT_A_BOOLEAN);
            return ((JSONBoolean)value).booleanValue();
        }

    }

    public class ArrayIterator extends BaseIterator<JSONArray> {

        @Override
        public JSONArray next() {
            return JSON.getArray(iterator.next());
        }

    }

    public class ObjectIterator extends BaseIterator<JSONObject> {

        @Override
        public JSONObject next() {
            return JSON.getObject(iterator.next());
        }

    }

}
