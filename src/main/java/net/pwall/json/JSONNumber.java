/*
 * @(#) JSONNumber.java
 */

package net.pwall.json;

import java.io.IOException;
import java.util.Objects;

/**
 * A JSON number value.
 *
 * @author Peter Wall
 */
@SuppressWarnings("serial")
public class JSONNumber extends Number implements JSONValue {

    private Number value;

    public JSONNumber(int value) {
        this.value = new Integer(value);
    }

    public JSONNumber(long value) {
        this.value = new Long(value);
    }

    public JSONNumber(float value) {
        if (Float.isNaN(value))
            throw new IllegalArgumentException("Can't store NaN as JSON");
        if (Float.isInfinite(value))
            throw new IllegalArgumentException("Can't store infinity as JSON");
        this.value = new Float(value);
    }

    public JSONNumber(double value) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("Can't store NaN as JSON");
        if (Double.isInfinite(value))
            throw new IllegalArgumentException("Can't store infinity as JSON");
        this.value = new Double(value);
    }

    public JSONNumber(Number value) {
        if (Objects.requireNonNull(value) instanceof Double && ((Double)value).isNaN() ||
                value instanceof Float && ((Float)value).isNaN())
            throw new IllegalArgumentException("Can't store NaN as JSON");
        if (value instanceof Double && ((Double)value).isInfinite() ||
                value instanceof Float && ((Float)value).isInfinite())
            throw new IllegalArgumentException("Can't store infinity as JSON");
        this.value = value;
    }

    public Number get() {
        return value;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    /**
     * Create the appropriate external representation for this JSON value.
     *
     * @return  the JSON representation for this value
     */
    @Override
    public String toJSON() {
        return value.toString();
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append(value.toString());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JSONNumber && value.equals(((JSONNumber)other).get());
    }

}
