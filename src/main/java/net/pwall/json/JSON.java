/*
 * @(#) JSON.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015 Peter Wall
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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

    public static final String INVALID_CHAR_SEQ = "Invalid JSON character sequence";
    public static final String EXCESS_CHARS = "Excess characters after JSON value";
    public static final String ILLEGAL_KEY = "Illegal key in JSON object";
    public static final String MISSING_COLON = "Missing colon in JSON object";
    public static final String MISSING_CLOSING_BRACE = "Missing closing brace in JSON object";
    public static final String MISSING_CLOSING_BRACKET =
            "Missing closing bracket in JSON array";
    public static final String ILLEGAL_NUMBER = "Illegal JSON number";
    public static final String ILLEGAL_SYNTAX = "Illegal JSON syntax";
    public static final String ILLEGAL_STRING_TERM = "Unterminated JSON string";
    public static final String ILLEGAL_STRING_CHAR = "Illegal character in JSON string";
    public static final String ILLEGAL_STRING_UNICODE =
            "Illegal Unicode sequence in JSON string";
    public static final String ILLEGAL_STRING_ESCAPE =
            "Illegal escape sequence in JSON string";
    public static final String NOT_A_STRING = "Not a JSON string";
    public static final String NOT_A_NUMBER = "Not a JSON number";
    public static final String NOT_A_BOOLEAN = "Not a JSON boolean";
    public static final String NOT_AN_ARRAY = "Not a JSON array";
    public static final String NOT_AN_OBJECT = "Not a JSON object";

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
            if (codePoint >= 0x20 && codePoint < 0x7F)
                return null;
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
                throw new JSONException(INVALID_CHAR_SEQ);
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
            throw new JSONException(INVALID_CHAR_SEQ);
        }
    };

    /**
     * Private constructor to prevent instantiation.  Attempts to instantiate the class via
     * reflection will cause an {@link IllegalAccessException}.
     *
     * @throws  IllegalAccessException in all cases
     */
    private JSON() throws IllegalAccessException {
        throw new IllegalAccessException("Attempt to instantiate JSON");
    }

    /**
     * Parse the contents of a {@link File} as a JSON value.
     *
     * @param   f       the {@link File}
     * @return          the JSON value
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(File f) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            return parse(is);
        }
        finally {
            if (is != null)
                is.close();
        }
    }

    /**
     * Parse the contents of a {@link File} as a JSON value, specifying the character set by
     * name.
     *
     * @param   f       the {@link File}
     * @param   charSet the character set name
     * @return          the JSON value
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(File f, String charSet) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            return parse(is, charSet);
        }
        finally {
            if (is != null)
                is.close();
        }
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON value.
     *
     * @param   is      the {@link InputStream}
     * @return          the JSON value
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(InputStream is) throws IOException {
        Reader rdr = null;
        try {
            rdr = new InputStreamReader(is);
            return parse(rdr);
        }
        finally {
            if (rdr != null)
                rdr.close();
        }
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON value, specifying
     * the character set by name.
     *
     * @param   is      the {@link InputStream}
     * @param   charSet the character set name
     * @return          the JSON value
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(InputStream is, String charSet) throws IOException {
        Reader rdr = null;
        try {
            rdr = new InputStreamReader(is, charSet);
            return parse(rdr);
        }
        finally {
            if (rdr != null)
                rdr.close();
        }
    }

    /**
     * Parse a sequence of characters from a {@link Reader} as a JSON value.
     *
     * @param   rdr     the {@link Reader}
     * @return          the JSON value
     * @throws  JSONException if the sequence does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(Reader rdr) throws IOException {
        if (!(rdr instanceof BufferedReader))
            rdr = new BufferedReader(rdr);
        StringBuilder sb = new StringBuilder();
        for (;;) {
            int i = rdr.read();
            if (i < 0)
                break;
            sb.append((char)i);
        }
        return parse(sb);
    }

    /**
     * Parse a {@link CharSequence} (e.g. a {@link String}) as a JSON value.
     *
     * @param   cs      the {@link CharSequence}
     * @return          the JSON value
     * @throws  JSONException if the sequence does not contain a valid JSON value
     */
    public static JSONValue parse(CharSequence cs) {
        ParseText p = new ParseText(cs);
        JSONValue result = parse(p);
        p.skipSpaces();
        if (!p.isExhausted())
            throw new JSONException(EXCESS_CHARS);
        return result;
    }

    /**
     * Parse a JSON value from a {@link ParseText} object.  The index of the {@link ParseText}
     * is left positioned after the JSON value.
     *
     * @param   p       the {@link ParseText} object
     * @return          the JSON value
     * @throws  JSONException if the text in the {@link ParseText} is not a valid JSON value
     */
    public static JSONValue parse(ParseText p) {
        p.skipSpaces();
        if (p.match('{')) {
            JSONObject object = new JSONObject();
            p.skipSpaces();
            if (!p.match('}')) {
                for (;;) {
                    if (!p.match('"'))
                        throw new JSONException(ILLEGAL_KEY);
                    String key = decodeString(p);
                    p.skipSpaces();
                    if (!p.match(':'))
                        throw new JSONException(MISSING_COLON);
                    p.skipSpaces();
                    JSONValue value = parse(p);
                    object.put(key, value);
                    p.skipSpaces();
                    if (!p.match(','))
                        break;
                    p.skipSpaces();
                }
                if (!p.match('}'))
                    throw new JSONException(MISSING_CLOSING_BRACE);
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
                    throw new JSONException(MISSING_CLOSING_BRACKET);
            }
            return array;
        }
        if (p.match('"')) {
            String s = decodeString(p);
            return new JSONString(s);
        }
        if (p.match('0')) {
            int start = p.getStart();
            boolean floating = false;
            if (p.match('.')) {
                floating = true;
                if (!p.matchDec())
                    throw new JSONException(ILLEGAL_NUMBER);
            }
            if (p.matchIgnoreCase('e')) {
                floating = true;
                if (p.match('+') || p.match('-'))
                    ; // do nothing - just step index
                if (!p.matchDec())
                    throw new JSONException(ILLEGAL_NUMBER);
            }
            if (floating)
                return JSONDouble.valueOf(p.getString(start, p.getIndex()));
            return JSONZero.ZERO;
        }
        if (p.match('-')) {
            int start = p.getStart();
            if (!p.match('0') && !p.matchDec())
                throw new JSONException(ILLEGAL_NUMBER);
            boolean floating = false;
            if (p.match('.')) {
                floating = true;
                if (!p.matchDec())
                    throw new JSONException(ILLEGAL_NUMBER);
            }
            if (p.matchIgnoreCase('e')) {
                floating = true;
                if (p.match('+') || p.match('-'))
                    ; // do nothing - just step index
                if (!p.matchDec())
                    throw new JSONException(ILLEGAL_NUMBER);
            }
            int end = p.getIndex();
            if (floating)
                return JSONDouble.valueOf(p.getString(start, end));
            return end - start < 10 ? JSONInteger.valueOf(p.getString(start, end)) :
                    JSONLong.valueOf(p.getString(start, end));
        }
        if (p.matchDec()) {
            int start = p.getStart();
            boolean floating = false;
            if (p.match('.')) {
                floating = true;
                if (!p.matchDec())
                    throw new JSONException(ILLEGAL_NUMBER);
            }
            if (p.matchIgnoreCase('e')) {
                floating = true;
                if (p.match('+') || p.match('-'))
                    ; // do nothing - just step index
                if (!p.matchDec())
                    throw new JSONException(ILLEGAL_NUMBER);
            }
            int end = p.getIndex();
            if (floating)
                return JSONDouble.valueOf(p.getString(start, end));
            return end - start < 10 ? JSONInteger.valueOf(p.getString(start, end)) :
                    JSONLong.valueOf(p.getString(start, end));
        }
        if (p.matchName("true"))
            return JSONBoolean.TRUE;
        if (p.matchName("false"))
            return JSONBoolean.FALSE;
        if (p.matchName("null"))
            return null;
        throw new JSONException(ILLEGAL_SYNTAX);
    }

    /**
     * Decode a JSON string, interpreting backslash sequences.  The {@link ParseText} is
     * assumed to be positioned just after the opening quote, and it is left positioned after
     * the closing quote.  This method does not check for UTF-16 surrogate sequences;
     * well-formed UTF-16 will pass through correctly, but so will incorrect data.
     *
     * @param   p   the {@link ParseText}
     * @return  the string
     * @throws  JSONException if the string is not valid, or not properly terminated
     */
    private static String decodeString(ParseText p) {
        // start by assuming we can take a substring from the input
        int start = p.getIndex();
        for (;;) {
            if (p.isExhausted())
                throw new JSONException(ILLEGAL_STRING_TERM);
            char ch = p.getChar();
            if (ch == '"')
                return p.getString(start, p.getStart());
            if (ch == '\\')
                break;
            if (ch < 0x20)
                throw new JSONException(ILLEGAL_STRING_CHAR);
        }
        // found a backslash, so we need to build a new string
        StringBuilder sb = new StringBuilder(p.getString(start, p.getStart()));
        for (;;) {
            if (p.isExhausted())
                throw new JSONException(ILLEGAL_STRING_TERM);
            char ch = p.getChar();
            if (ch == '"')
                sb.append('"');
            else if (ch == '\\')
                sb.append('\\');
            else if (ch == '/')
                sb.append('/');
            else if (ch == 'b')
                sb.append('\b');
            else if (ch == 'f')
                sb.append('\f');
            else if (ch == 'n')
                sb.append('\n');
            else if (ch == 'r')
                sb.append('\r');
            else if (ch == 't')
                sb.append('\t');
            else if (ch == 'u') {
                if (!p.matchHexFixed(4))
                    throw new JSONException(ILLEGAL_STRING_UNICODE);
                sb.append((char)p.getResultHexInt());
            }
            else
                throw new JSONException(ILLEGAL_STRING_ESCAPE);
            for (;;) {
                if (p.isExhausted())
                    throw new JSONException(ILLEGAL_STRING_TERM);
                ch = p.getChar();
                if (ch == '"')
                    return sb.toString();
                if (ch == '\\')
                    break;
                if (ch < 0x20)
                    throw new JSONException(ILLEGAL_STRING_CHAR);
                sb.append(ch);
            }
        }
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
     * @throws  IOException if thrown by the {@link Appendable}
     */
    public static void appendJSON(Appendable a, JSONValue value) throws IOException {
        if (value == null)
            a.append("null");
        else
            value.appendJSON(a);
    }

    /**
     * Get a {@link String} from a {@link JSONValue}.  Return {@code null} if the
     * {@link JSONValue} is {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@link String}
     * @throws  JSONException if the value is not a string
     */
    public static String getString(JSONValue value) {
        if (value == null)
            return null;
        if (!(value instanceof JSONString))
            throw new JSONException(NOT_A_STRING);
        return ((JSONString)value).toString();
    }

    /**
     * Get an {@code int} from a {@link JSONValue}.  Return 0 if the {@link JSONValue} is
     * {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as an {@code int}
     * @throws  JSONException if the value is not a number
     */
    public static int getInt(JSONValue value) {
        if (value == null)
            return 0;
        if (!(value instanceof Number))
            throw new JSONException(NOT_A_NUMBER);
        return ((Number)value).intValue();
    }

    /**
     * Get a {@code long} from a {@link JSONValue}.  Return 0 if the {@link JSONValue} is
     * {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@code long}
     * @throws  JSONException if the value is not a number
     */
    public static long getLong(JSONValue value) {
        if (value == null)
            return 0;
        if (!(value instanceof Number))
            throw new JSONException(NOT_A_NUMBER);
        return ((Number)value).longValue();
    }

    /**
     * Get a {@code float} from a {@link JSONValue}.  Return 0 if the {@link JSONValue} is
     * {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@code float}
     * @throws  JSONException if the value is not a number
     */
    public static float getFloat(JSONValue value) {
        if (value == null)
            return 0;
        if (!(value instanceof Number))
            throw new JSONException(NOT_A_NUMBER);
        return ((Number)value).floatValue();
    }

    /**
     * Get a {@code double} from a {@link JSONValue}.  Return 0 if the {@link JSONValue} is
     * {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@code double}
     * @throws  JSONException if the value is not a number
     */
    public static double getDouble(JSONValue value) {
        if (value == null)
            return 0;
        if (!(value instanceof Number))
            throw new JSONException(NOT_A_NUMBER);
        return ((Number)value).doubleValue();
    }

    /**
     * Get a {@code boolean} from a {@link JSONValue}.  Return {@code false} if the
     * {@link JSONValue} is {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@code boolean}
     * @throws  JSONException if the value is not a boolean
     */
    public static boolean getBoolean(JSONValue value) {
        if (value == null)
            return false;
        if (!(value instanceof JSONBoolean))
            throw new JSONException(NOT_A_BOOLEAN);
        return ((JSONBoolean)value).booleanValue();
    }

    /**
     * Get a {@link JSONArray} from a {@link JSONValue}.  Return {@code null} if the
     * {@link JSONValue} is {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@link JSONArray}
     * @throws  JSONException if the value is not an array
     */
    public static JSONArray getArray(JSONValue value) {
        if (value == null)
            return null;
        if (!(value instanceof JSONArray))
            throw new JSONException(NOT_AN_ARRAY);
        return (JSONArray)value;
    }

    /**
     * Get a {@link JSONObject} from a {@link JSONValue}.  Return {@code null} if the
     * {@link JSONValue} is {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@link JSONObject}
     * @throws  JSONException if the value is not an object
     */
    public static JSONObject getObject(JSONValue value) {
        if (value == null)
            return null;
        if (!(value instanceof JSONObject))
            throw new JSONException(NOT_AN_OBJECT);
        return (JSONObject)value;
    }

}
