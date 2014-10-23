/*
 * @(#) JSONString.java
 */

package net.pwall.json;

import java.util.Objects;

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
        return toString(value);
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

    public static String toString(CharSequence cs) {
        if (cs == null)
            return "null";
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < cs.length(); i++) {
            char ch = cs.charAt(i);
            if (ch == '"')
                sb.append("\\\"");
            else if (ch == '\\')
                sb.append("\\\\");
            else if (ch >= ' ' && ch < 0x7F)
                sb.append(ch);
            else if (ch == 8)
                sb.append("\\b");
            else if (ch == 9)
                sb.append("\\t");
            else if (ch == 0xA)
                sb.append("\\n");
            else if (ch == 0xC)
                sb.append("\\f");
            else if (ch == 0xD)
                sb.append("\\r");
            else {
                sb.append("\\u");
                for (int j = 12; j >= 0; j -= 4)
                    sb.append("0123456789abcdef".charAt((ch >>> j) & 0xF));
            }
        }
        sb.append('"');
        return sb.toString();
    }

}
