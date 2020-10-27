/*
 * @(#) JSONSequence.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2016, 2017, 2020 Peter Wall
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A JSON sequence (the base for {@link JSONArray}, and possibly other similar collections).
 *
 * @author Peter Wall
 */
public class JSONSequence<V extends JSONValue> extends ArrayList<V> implements JSONComposite {

    private static final long serialVersionUID = 34670706002893562L;

    /**
     * Construct an empty {@code JSONSequence}.
     */
    public JSONSequence() {
    }

    /**
     * Construct a {@code JSONSequence} with a specified initial capacity.
     *
     * @param   capacity    the initial capacity
     */
    public JSONSequence(int capacity) {
        super(capacity);
    }

    /**
     * Construct a {@code JSONSequence} from an array of values.
     *
     * @param   values  the source values
     * @throws  NullPointerException if the collection is {@code null}
     */
    @SafeVarargs
    public JSONSequence(V ... values) {
        super(values.length);
        for (int i = 0, n = values.length; i < n; i++)
            add(values[i]);
    }

    /**
     * Construct a {@code JSONSequence} from a {@link Collection} of values.
     *
     * @param   collection  the source {@link Collection}
     * @throws  NullPointerException if the collection is {@code null}
     */
    public JSONSequence(Collection<? extends V> collection) {
        super(collection);
    }

    /**
     * Convert to a simple representation.
     *
     * @return  the sequence as a List.
     */
    @Override
    public List<Object> toSimpleValue() {
        int n = size();
        ArrayList<Object> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            list.add(JSONValue.simpleValue(get(i)));
        return list;
    }

    /**
     * Get a {@link String} value from the {@code JSONSequence}.  If the array entry is
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
     * Get an {@code int} value from the {@code JSONSequence}.  If the array entry is
     * {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public int getInt(int index) {
        return JSON.getInt(get(index));
    }

    /**
     * Get a {@code long} value from the {@code JSONSequence}.  If the array entry is
     * {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public long getLong(int index) {
        return JSON.getLong(get(index));
    }

    /**
     * Get a {@code float} value from the {@code JSONSequence}.  If the array entry is
     * {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public float getFloat(int index) {
        return JSON.getFloat(get(index));
    }

    /**
     * Get a {@code double} value from the {@code JSONSequence}.  If the array entry is
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
     * Get a {@link BigDecimal} value from the {@code JSONSequence}.  If the array entry is
     * {@code null} return 0.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link Number}
     */
    public BigDecimal getDecimal(int index) {
        return JSON.getDecimal(get(index));
    }

    /**
     * Get a {@code boolean} value from the {@code JSONSequence}.  If the array entry is
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
     * Get a {@link JSONArray} value from the {@code JSONSequence}.  If the array entry is
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
     * Get a {@link JSONObject} value from the {@code JSONSequence}.  If the array entry is
     * {@code null} return {@code null}.
     *
     * @param   index   the index of the value
     * @return  the value
     * @throws  JSONException if the array entry is not a {@link JSONObject}
     */
    public JSONObject getObject(int index) {
        return JSON.getObject(get(index));
    }

    /**
     * Get an {@link Iterable} of {@link String} from the {@code JSONSequence}.  Supports the
     * idiom:
     * <pre>
     *     for (String item : sequence.strings()) {
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
     * Get an {@link Iterable} of {@link Integer} from the {@code JSONSequence}.  Supports the
     * idiom:
     * <pre>
     *     for (Integer item : sequence.ints()) {
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
     * Get an {@link Iterable} of {@link Long} from the {@code JSONSequence}.  Supports the
     * idiom:
     * <pre>
     *     for (Long item : sequence.longs()) {
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
     * Get an {@link Iterable} of {@link Double} from the {@code JSONSequence}.  Supports the
     * idiom:
     * <pre>
     *     for (Double item : sequence.doubles()) {
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
     * Get an {@link Iterable} of {@link Float} from the {@code JSONSequence}.  Supports the
     * idiom:
     * <pre>
     *     for (Float item : sequence.floats()) {
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
     * Get an {@link Iterable} of {@link Boolean} from the {@code JSONSequence}.  Supports the
     * idiom:
     * <pre>
     *     for (Boolean item : sequence.booleans()) {
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
     *     for (JSONArray item : sequence.arrays()) {
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
     * Get an {@link Iterable} of {@link JSONObject} from the {@code JSONSequence}.  Supports
     * the idiom:
     * <pre>
     *     for (JSONObject item : sequence.objects()) {
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
     * Append the JSON representation for the {@code JSONSequence} to a given
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
     * Return a string representation of the {@code JSONSequence}.  This is the same as the JSON
     * format.
     *
     * @return  the JSON string
     */
    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * Compare two {@code JSONSequence}s for equality.
     *
     * @param   other   the other {@code JSONSequence}
     * @return  {@code true} if the other object is a {@code JSONSequence} and the contents are
     *          equal
     */
    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof JSONSequence && super.equals(other);
    }

    public abstract class BaseIterator<T> implements Iterator<T> {

        protected Iterator<V> iterator = iterator();

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
