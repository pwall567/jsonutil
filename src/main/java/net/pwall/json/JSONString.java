/*
 * @(#) JSONString.java
 */

package net.pwall.json;

import java.io.IOException;
import java.util.Objects;

import net.pwall.util.Strings;

/**
 * A JSON string value.
 *
 * @author Peter Wall
 */
public class JSONString implements JSONValue, CharSequence {

    private String value;

    public JSONString(CharSequence cs) {
        value = Objects.requireNonNull(cs).toString();
    }

    public String get() {
        return value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
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
        a.append('"');
        Strings.appendEscaped(a, value, JSON.charMapper);
        a.append('"');
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JSONString && value.equals(((JSONString)other).value);
    }

}
