/*
 * @(#) JSON.java
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

import net.pwall.util.CharMapper;
import net.pwall.util.CharUnmapper;
import net.pwall.util.ParseText;
import net.pwall.util.Strings;

/**
 * JSON utilities.  Includes code for parsing JSON; the code for outputting JSON objects is in
 * the individual classes for the JSON data types.
 *
 * @author Peter Wall
 */
public class JSON {

    /**
     * A {@link CharMapper} for escaping JSON strings.
     *
     * @see     Strings#escape(CharSequence, CharMapper)
     * @see     Strings#escape(String, CharMapper)
     * @see     Strings#escapeUTF16(CharSequence, CharMapper)
     * @see     Strings#escapeUTF16(String, CharMapper)
     */
    public static final CharMapper charMapper = new CharMapper() {
        @Override
        public String map(int codePoint) {
            if (codePoint == '"')
                return "\\\"";
            if (codePoint == '\\')
                return "\\\\";
            if (codePoint == 0x08)
                return "\\b";
            if (codePoint == 0x0C)
                return "\\f";
            if (codePoint == 0x0A)
                return "\\n";
            if (codePoint == 0x0D)
                return "\\r";
            if (codePoint == 0x09)
                return "\\t";
            if (codePoint < 0x20 || codePoint >= 0x7F) {
                StringBuilder sb = new StringBuilder("\\u");
                try {
                    if (Character.isBmpCodePoint(codePoint))
                        Strings.appendHex(sb, (char)codePoint);
                    else {
                        Strings.appendHex(sb, Character.highSurrogate(codePoint));
                        sb.append("\\u");
                        Strings.appendHex(sb, Character.lowSurrogate(codePoint));
                    }
                }
                catch (IOException e) {
                    // can't happen - StringBuilder does not throw IOException
                }
                return sb.toString();
            }
            return null;
        }
    };

    /**
     * A {@link CharUnmapper} for unescaping JSON strings.
     *
     * @see     Strings#unescape(CharSequence, CharUnmapper)
     * @see     Strings#unescape(String, CharUnmapper)
     * @see     ParseText#unescape(CharUnmapper, char)
     */
    public static final CharUnmapper charUnmapper = new CharUnmapper() {
        @Override
        public boolean isEscape(CharSequence s, int offset) {
            return s.charAt(offset) == '\\';
        }
        @Override
        public int unmap(StringBuilder sb, CharSequence s, int offset) {
            if (offset + 1 >= s.length())
                throw new IllegalArgumentException("Invalid JSON character sequence");
            char ch = s.charAt(offset + 1);
            if (ch == '"') {
                sb.append('"');
                return 2;
            }
            if (ch == '\\') {
                sb.append('\\');
                return 2;
            }
            if (ch == '/') {
                sb.append('/');
                return 2;
            }
            if (ch == 'b') {
                sb.append('\b');
                return 2;
            }
            if (ch == 'f') {
                sb.append('\f');
                return 2;
            }
            if (ch == 'n') {
                sb.append('\n');
                return 2;
            }
            if (ch == 'r') {
                sb.append('\r');
                return 2;
            }
            if (ch == 't') {
                sb.append('\t');
                return 2;
            }
            if (ch == 'u' && offset + 6 <= s.length()) {
                int n = Integer.parseInt(s.subSequence(offset + 2, offset + 6).toString(), 16);
                sb.append((char)n);
                return 6;
            }
            throw new IllegalArgumentException("Invalid JSON character sequence");
        }
    };

    /**
     * Private constructor to prevent instantiation.
     */
    private JSON() {
    }

    /**
     * Parse a {@link CharSequence} (e.g. a {@link String}) as a JSON value.
     *
     * @param   cs      the {@link CharSequence}
     * @return          the JSON value
     * @throws  IllegalArgumentException if the sequence does not contain a valid JSON value
     */
    public static JSONValue parse(CharSequence cs) {
        ParseText p = new ParseText(cs);
        JSONValue result = parse(p);
        p.skipSpaces();
        if (!p.isExhausted())
            throw new IllegalArgumentException("Excess characters after JSON value");
        return result;
    }

    /**
     * Parse a JSON value from a {@link ParseText} object.  The index of the {@link ParseText}
     * is left positioned after the JSON value.
     *
     * @param   p       the {@link ParseText} object
     * @return          the JSON value
     * @throws  IllegalArgumentException if the text in the {@link ParseText} is not a valid
     *          JSON value
     */
    public static JSONValue parse(ParseText p) {
        p.skipSpaces();
        if (p.match('{')) {
            JSONObject object = new JSONObject();
            p.skipSpaces();
            if (!p.match('}')) {
                for (;;) {
                    if (!p.match('"'))
                        throw new IllegalArgumentException("Illegal key in JSON object");
                    String key = decodeString(p);
                    p.skipSpaces();
                    if (!p.match(':'))
                        throw new IllegalArgumentException("Missing colon in JSON object");
                    p.skipSpaces();
                    JSONValue value = parse(p);
                    object.put(key, value);
                    p.skipSpaces();
                    if (!p.match(','))
                        break;
                    p.skipSpaces();
                }
                if (!p.match('}'))
                    throw new IllegalArgumentException("Missing closing brace in JSON object");
            }
            return object;
        }
        if (p.match('[')) {
            JSONArray array = new JSONArray();
            p.skipSpaces();
            if (!p.match(']')) {
                for (;;) {
                    array.add(parse(p));
                    p.skipSpaces();
                    if (!p.match(','))
                        break;
                    p.skipSpaces();
                }
                if (!p.match(']'))
                    throw new IllegalArgumentException("Missing closing bracket in JSON array");
            }
            return array;
        }
        if (p.match('"')) {
            String s = decodeString(p);
            return new JSONString(s);
        }
        if (p.match('-') || p.matchDecFixed(1)) {
            int start = p.getStart();
            p.revert().match('-');
            if (!p.match('0') && !p.matchDec())
                throw new IllegalArgumentException("Illegal JSON number");
            boolean floating = false;
            if (p.match('.')) {
                floating = true;
                if (!p.matchDec())
                    throw new IllegalArgumentException("Illegal JSON number");
            }
            if (p.matchIgnoreCase('e')) {
                floating = true;
                if (p.match('+') || p.match('-'))
                    ; // do nothing - just step index
                if (!p.matchDec())
                    throw new IllegalArgumentException("Illegal JSON number");
            }
            if (floating)
                return new JSONNumber(Double.parseDouble(p.getString(start, p.getIndex())));
            long result = Long.parseLong(p.getString(start, p.getIndex()));
            if (result == 0L)
                return JSONNumber.ZERO;
            if (result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE)
                return new JSONNumber((int)result);
            return new JSONNumber(result);
        }
        if (p.match("true"))
            return JSONBoolean.TRUE;
        if (p.match("false"))
            return JSONBoolean.FALSE;
        if (p.match("null"))
            return null;
        throw new IllegalArgumentException("Illegal JSON syntax");
    }

    /**
     * Decode a JSON string, interpreting backslash sequences.  The {@link ParseText} is
     * assumed to be positioned just after the opening quote, and it is left positioned after
     * the closing quote.
     *
     * @param   p   the {@link ParseText}
     * @return  the string
     * @throws  IllegalArgumentException if the string is not valid, or not properly terminated
     */
    private static String decodeString(ParseText p) {
        String s = p.unescape(charUnmapper, '"');
        if (!p.match('"'))
            throw new IllegalArgumentException("Illegal JSON string");
        return s;
    }

    /**
     * Convenience method to output the JSON string for a value, for cases where the value may
     * be {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the JSON string for this value
     */
    public static String toJSON(JSONValue value) {
        return value == null ? "null" : value.toJSON();
    }

    /**
     * Convenience method to append the JSON string for a value to an {@link Appendable}, for
     * cases where the value may be {@code null}.
     *
     * @param   a       the {@link Appendable}
     * @param   value   the {@link JSONValue}
     * @return  the JSON string for this value
     * @throws  IOException if thrown by the {@link Appendable}
     */
    public static void appendJSON(Appendable a, JSONValue value) throws IOException {
        if (value == null)
            a.append("null");
        else
            value.appendJSON(a);
    }

}
