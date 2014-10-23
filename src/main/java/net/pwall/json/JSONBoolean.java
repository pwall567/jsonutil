/*
 * @(#) JSONBoolean.java
 */

package net.pwall.json;

import java.io.IOException;
import java.util.Objects;

/**
 * A JSON boolean value.
 *
 * @author Peter Wall
 */
public class JSONBoolean implements JSONValue {

    public static JSONBoolean FALSE = new JSONBoolean(false);
    public static JSONBoolean TRUE = new JSONBoolean(true);

    private boolean value;

    public JSONBoolean(boolean value) {
        this.value = value;
    }

    public JSONBoolean(Boolean value) {
        this.value = Objects.requireNonNull(value).booleanValue();
    }

    public boolean get() {
        return value;
    }

    public boolean booleanValue() {
        return value;
    }

    @Override
    public String toJSON() {
        return value ? "true" : "false";
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append(toJSON());
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public int hashCode() {
        return value ? 1 : 0;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JSONBoolean && value == ((JSONBoolean)other).get();
    }

    public static JSONBoolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }

}
