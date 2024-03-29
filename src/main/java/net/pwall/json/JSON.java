/*
 * @(#) JSON.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2017, 2020 Peter Wall
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import net.pwall.util.CharMapper;
import net.pwall.util.CharUnmapper;
import net.pwall.util.ParseText;
import net.pwall.util.ReaderBuffer;
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
    public static final String DUPLICATE_KEY = "Duplicate key in JSON object";
    public static final String MISSING_COLON = "Missing colon in JSON object";
    public static final String MISSING_CLOSING_BRACE = "Missing closing brace in JSON object";
    public static final String MISSING_CLOSING_BRACKET = "Missing closing bracket in JSON array";
    public static final String ILLEGAL_NUMBER = "Illegal JSON number";
    public static final String ILLEGAL_SYNTAX = "Illegal JSON syntax";
    public static final String ILLEGAL_STRING_TERM = "Unterminated JSON string";
    public static final String ILLEGAL_STRING_CHAR = "Illegal character in JSON string";
    public static final String ILLEGAL_STRING_UNICODE = "Illegal Unicode sequence in JSON string";
    public static final String ILLEGAL_STRING_ESCAPE = "Illegal escape sequence in JSON string";
    public static final String NOT_A_STRING = "Not a JSON string";
    public static final String NOT_A_NUMBER = "Not a JSON number";
    public static final String NOT_A_BOOLEAN = "Not a JSON boolean";
    public static final String NOT_AN_ARRAY = "Not a JSON array";
    public static final String NOT_AN_OBJECT = "Not a JSON object";
    public static final String MAX_DEPTH_EXCEEDED = "Maximum nesting depth exceeded";

    public static final String MAX_INTEGER_STRING = "2147483647";
    public static final String MIN_INTEGER_STRING = "-2147483648";
    public static final String MAX_LONG_STRING = "9223372036854775807";
    public static final String MIN_LONG_STRING = "-9223372036854775808";

    private static int maxDepth = 1000;

    /**
     * A {@link CharMapper} for escaping JSON strings.
     *
     * @see     Strings#escape(CharSequence, CharMapper)
     * @see     Strings#escape(String, CharMapper)
     * @see     Strings#escapeUTF16(CharSequence, CharMapper)
     * @see     Strings#escapeUTF16(String, CharMapper)
     */
    public static final CharMapper charMapper = codePoint -> {
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
                int n = Strings.convertHexToInt(s, offset + 2, offset + 6);
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
        try (InputStream is = new FileInputStream(f)) {
            return parse(is);
        }
    }

    /**
     * Parse the contents of a {@link File} as a JSON array.
     *
     * @param   f       the {@link File}
     * @return          the JSON array
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(File f) throws IOException {
        return (JSONArray)parse(f);
    }

    /**
     * Parse the contents of a {@link File} as a JSON object.
     *
     * @param   f       the {@link File}
     * @return          the JSON object
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(File f) throws IOException {
        return (JSONObject)parse(f);
    }

    /**
     * Parse the contents of a {@link File} as a JSON value, specifying the character set.
     *
     * @param   f       the {@link File}
     * @param   charSet the character set
     * @return          the JSON value
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(File f, Charset charSet) throws IOException {
        try (InputStream is = new FileInputStream(f)) {
            return parse(is, charSet);
        }
    }

    /**
     * Parse the contents of a {@link File} as a JSON array, specifying the character set.
     *
     * @param   f       the {@link File}
     * @param   charSet the character set
     * @return          the JSON array
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(File f, Charset charSet) throws IOException {
        return (JSONArray)parse(f, charSet);
    }

    /**
     * Parse the contents of a {@link File} as a JSON object, specifying the character set.
     *
     * @param   f       the {@link File}
     * @param   charSet the character set
     * @return          the JSON object
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(File f, Charset charSet) throws IOException {
        return (JSONObject)parse(f, charSet);
    }

    /**
     * Parse the contents of a {@link File} as a JSON value, specifying the character set by
     * name.
     *
     * @param   f       the {@link File}
     * @param   csName  the character set name
     * @return          the JSON value
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(File f, String csName) throws IOException {
        try (InputStream is = new FileInputStream(f)) {
            return parse(is, csName);
        }
    }

    /**
     * Parse the contents of a {@link File} as a JSON array, specifying the character set by
     * name.
     *
     * @param   f       the {@link File}
     * @param   csName  the character set name
     * @return          the JSON array
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(File f, String csName) throws IOException {
        return (JSONArray)parse(f, csName);
    }

    /**
     * Parse the contents of a {@link File} as a JSON object, specifying the character set by
     * name.
     *
     * @param   f       the {@link File}
     * @param   csName  the character set name
     * @return          the JSON object
     * @throws  JSONException if the file does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(File f, String csName) throws IOException {
        return (JSONObject)parse(f, csName);
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
        try (Reader rdr = new InputStreamReader(is)) {
            return parse(rdr);
        }
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON array.
     *
     * @param   is      the {@link InputStream}
     * @return          the JSON array
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(InputStream is) throws IOException {
        return (JSONArray)parse(is);
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON object.
     *
     * @param   is      the {@link InputStream}
     * @return          the JSON object
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(InputStream is) throws IOException {
        return (JSONObject)parse(is);
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON value, specifying
     * the character set by.
     *
     * @param   is      the {@link InputStream}
     * @param   charSet the character set
     * @return          the JSON value
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(InputStream is, Charset charSet) throws IOException {
        try (Reader rdr = new InputStreamReader(is, charSet)) {
            return parse(rdr);
        }
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON array, specifying
     * the character set.
     *
     * @param   is      the {@link InputStream}
     * @param   charSet the character set
     * @return          the JSON array
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(InputStream is, Charset charSet) throws IOException {
        return (JSONArray)parse(is, charSet);
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON object, specifying
     * the character set.
     *
     * @param   is      the {@link InputStream}
     * @param   charSet the character set
     * @return          the JSON object
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(InputStream is, Charset charSet) throws IOException {
        return (JSONObject)parse(is, charSet);
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON value, specifying
     * the character set by name.
     *
     * @param   is      the {@link InputStream}
     * @param   csName  the character set name
     * @return          the JSON value
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     */
    public static JSONValue parse(InputStream is, String csName) throws IOException {
        try (Reader rdr = new InputStreamReader(is, csName)) {
            return parse(rdr);
        }
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON array, specifying
     * the character set by name.
     *
     * @param   is      the {@link InputStream}
     * @param   csName  the character set name
     * @return          the JSON array
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(InputStream is, String csName) throws IOException {
        return (JSONArray)parse(is, csName);
    }

    /**
     * Parse a sequence of characters from an {@link InputStream} as a JSON object, specifying
     * the character set by name.
     *
     * @param   is      the {@link InputStream}
     * @param   csName  the character set name
     * @return          the JSON object
     * @throws  JSONException if the stream does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(InputStream is, String csName) throws IOException {
        return (JSONObject)parse(is, csName);
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
        return parse(new ReaderBuffer(rdr));
    }

    /**
     * Parse a sequence of characters from a {@link Reader} as a JSON array.
     *
     * @param   rdr     the {@link Reader}
     * @return          the JSON array
     * @throws  JSONException if the sequence does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(Reader rdr) throws IOException {
        return (JSONArray)parse(rdr);
    }

    /**
     * Parse a sequence of characters from a {@link Reader} as a JSON object.
     *
     * @param   rdr     the {@link Reader}
     * @return          the JSON object
     * @throws  JSONException if the sequence does not contain a valid JSON value
     * @throws  IOException on any I/O errors
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(Reader rdr) throws IOException {
        return (JSONObject)parse(rdr);
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
        if (!p.skipSpaces().isExhausted())
            throw new JSONException(EXCESS_CHARS);
        return result;
    }

    /**
     * Parse a {@link CharSequence} (e.g. a {@link String}) as a JSON array.
     *
     * @param   cs      the {@link CharSequence}
     * @return          the JSON array
     * @throws  JSONException if the sequence does not contain a valid JSON value
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(CharSequence cs) {
        return (JSONArray)parse(cs);
    }

    /**
     * Parse a {@link CharSequence} (e.g. a {@link String}) as a JSON object.
     *
     * @param   cs      the {@link CharSequence}
     * @return          the JSON object
     * @throws  JSONException if the sequence does not contain a valid JSON value
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(CharSequence cs) {
        return (JSONObject)parse(cs);
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
        return parse(p, "", 0);
    }

    private static JSONValue parse(ParseText p, String pointer, int depth) {
        if (depth > maxDepth)
            throw new JSONException(MAX_DEPTH_EXCEEDED);

        p.skipSpaces();

        // check for object

        if (p.match('{')) {
            JSONObject object = new JSONObject();
            if (!p.skipSpaces().match('}')) {
                for (;;) {
                    if (!p.match('"'))
                        throw new JSONException(pointerMessage(ILLEGAL_KEY, pointer));
                    String key = decodeString(p, pointer);
                    if (object.containsKey(key))
                        throw new JSONException(pointerMessage(DUPLICATE_KEY + ": \"" + key + '"', pointer));
                    if (!p.skipSpaces().match(':'))
                        throw new JSONException(pointerMessage(MISSING_COLON, pointer));
                    object.put(key, parse(p, pointer + '/' + key, depth + 1));
                    if (!p.skipSpaces().match(','))
                        break;
                    p.skipSpaces();
                }
                if (!p.match('}'))
                    throw new JSONException(pointerMessage(MISSING_CLOSING_BRACE, pointer));
            }
            return object;
        }

        // check for array

        if (p.match('[')) {
            JSONArray array = new JSONArray();
            if (!p.skipSpaces().match(']')) {
                do {
                    array.add(parse(p, pointer + '/' + array.size(), depth + 1));
                } while (p.skipSpaces().match(','));
                if (!p.match(']'))
                    throw new JSONException(pointerMessage(MISSING_CLOSING_BRACKET, pointer));
            }
            return array;
        }

        // check for string

        if (p.match('"')) {
            return new JSONString(decodeString(p, pointer));
        }

        // check for number

        int numberStart = p.getIndex();
        p.match('-'); // ignore the result, just step the index
        if (p.matchDec()) {
            boolean zero = false;
            if (p.getResultChar() == '0') {
                if (p.getResultLength() > 1)
                    throw new JSONException(pointerMessage(ILLEGAL_NUMBER, pointer));
                zero = true;
            }
            boolean floating = false;
            if (p.match('.')) {
                floating = true;
                if (!p.matchDec())
                    throw new JSONException(pointerMessage(ILLEGAL_NUMBER, pointer));
            }
            if (p.matchIgnoreCase('e')) {
                floating = true;
                p.matchAnyOf("-+"); // ignore the result, just step the index
                if (!p.matchDec())
                    throw new JSONException(pointerMessage(ILLEGAL_NUMBER, pointer));
            }
            int numberEnd = p.getIndex();
            String numberString = p.getString(numberStart, numberEnd);
            if (!floating) {
                if (zero)
                    return JSONZero.ZERO;
                if (numberString.length() < 10) // optimise the most common case
                    return new JSONInteger(Integer.parseInt(numberString));
                String unsignedNumber = numberString.charAt(0) == '-' ? numberString.substring(1) : numberString;
                if (unsignedNumber.length() < 10 ||
                        unsignedNumber.length() == 10 && unsignedNumber.compareTo(MAX_INTEGER_STRING) <= 0 ||
                        numberString.equals(MIN_INTEGER_STRING))
                    return new JSONInteger(Integer.parseInt(numberString));
                if (unsignedNumber.length() < 19 ||
                        unsignedNumber.length() == 19 && unsignedNumber.compareTo(MAX_LONG_STRING) <= 0 ||
                        numberString.equals(MIN_LONG_STRING))
                    return new JSONLong(Long.parseLong(numberString));
            }
            return new JSONDecimal(numberString);
        }
        if (p.getIndex() > numberStart)
            throw new JSONException(pointerMessage(ILLEGAL_NUMBER, pointer)); // minus sign without digits

        // check for keywords (true, false, null)

        if (p.matchName("true"))
            return JSONBoolean.TRUE;
        if (p.matchName("false"))
            return JSONBoolean.FALSE;
        if (p.matchName("null"))
            return null;

        // error

        throw new JSONException(pointerMessage(ILLEGAL_SYNTAX, pointer));
    }

    private static String pointerMessage(String message, String pointer) {
        return (pointer.length() == 0 ? message : message + " at " + pointer);
    }

    /**
     * Parse a JSON array from a {@link ParseText} object.  The index of the {@link ParseText}
     * is left positioned after the JSON value.
     *
     * @param   p       the {@link ParseText} object
     * @return          the JSON array
     * @throws  JSONException if the text in the {@link ParseText} is not a valid JSON value
     * @throws  ClassCastException if the value is not an array
     */
    public static JSONArray parseArray(ParseText p) {
        return (JSONArray)parse(p);
    }

    /**
     * Parse a JSON object from a {@link ParseText} object.  The index of the {@link ParseText}
     * is left positioned after the JSON value.
     *
     * @param   p       the {@link ParseText} object
     * @return          the JSON object
     * @throws  JSONException if the text in the {@link ParseText} is not a valid JSON value
     * @throws  ClassCastException if the value is not an object
     */
    public static JSONObject parseObject(ParseText p) {
        return (JSONObject)parse(p);
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
    private static String decodeString(ParseText p, String pointer) {
        // start by assuming we can take a substring from the input
        int start = p.getIndex();
        for (;;) {
            if (p.isExhausted())
                throw new JSONException(pointerMessage(ILLEGAL_STRING_TERM, pointer));
            char ch = p.getChar();
            if (ch == '"')
                return p.getString(start, p.getStart());
            if (ch == '\\')
                break;
            if (ch < 0x20)
                throw new JSONException(pointerMessage(ILLEGAL_STRING_CHAR, pointer));
        }
        // found a backslash, so we need to build a new string
        StringBuilder sb = new StringBuilder(p.getString(start, p.getStart()));
        for (;;) {
            if (p.isExhausted())
                throw new JSONException(pointerMessage(ILLEGAL_STRING_TERM, pointer));
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
                    throw new JSONException(pointerMessage(ILLEGAL_STRING_UNICODE, pointer));
                sb.append((char)p.getResultHexInt());
            }
            else
                throw new JSONException(pointerMessage(ILLEGAL_STRING_ESCAPE, pointer));
            for (;;) {
                if (p.isExhausted())
                    throw new JSONException(pointerMessage(ILLEGAL_STRING_TERM, pointer));
                ch = p.getChar();
                if (ch == '"')
                    return sb.toString();
                if (ch == '\\')
                    break;
                if (ch < 0x20)
                    throw new JSONException(pointerMessage(ILLEGAL_STRING_CHAR, pointer));
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
        if (!(value instanceof JSONNumberValue))
            throw new JSONException(NOT_A_NUMBER);
        return ((JSONNumberValue)value).intValue();
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
        if (!(value instanceof JSONNumberValue))
            throw new JSONException(NOT_A_NUMBER);
        return ((JSONNumberValue)value).longValue();
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
        if (!(value instanceof JSONNumberValue))
            throw new JSONException(NOT_A_NUMBER);
        return ((JSONNumberValue)value).floatValue();
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
        if (!(value instanceof JSONNumberValue))
            throw new JSONException(NOT_A_NUMBER);
        return ((JSONNumberValue)value).doubleValue();
    }

    /**
     * Get a {@link BigDecimal} from a {@link JSONValue}.  Return 0 if the {@link JSONValue} is
     * {@code null}.
     *
     * @param   value   the {@link JSONValue}
     * @return  the value as a {@code double}
     * @throws  JSONException if the value is not a number
     */
    public static BigDecimal getDecimal(JSONValue value) {
        if (value == null)
            return BigDecimal.ZERO;
        if (!(value instanceof JSONNumberValue))
            throw new JSONException(NOT_A_NUMBER);
        return ((JSONNumberValue)value).bigDecimalValue();
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

    /**
     * Replace characters in a string with their mapped equivalents, as required for JSON.  If the string contains no
     * characters to be mapped, the original string is returned unmodified.
     *
     * @param   string  the string to be converted
     * @return  the string with characters mapped as required
     */
    public static String escape(String string) {
        return Strings.escape(string, charMapper);
    }

    /**
     * Scan a string for JSON escape sequences and replace them by the original characters.  If the string contains no
     * escape sequences to be unmapped, the original string is returned unmodified.
     *
     * @param   string      the string to be converted
     * @return              the "unescaped" string
     */
    public static String unescape(String string) {
        return Strings.unescape(string, charUnmapper);
    }

    /**
     * Get the current maximum allowed nesting depth.
     *
     * @return      the maximum allowed nesting depth
     */
    public static int getMaxDepth() {
        return maxDepth;
    }

    /**
     * Set the maximum allowed nesting depth.
     *
     * @param   maxDepth    the new maximum allowed nesting depth
     */
    public static void setMaxDepth(int maxDepth) {
        if (maxDepth < 1 || maxDepth > 1200)
            throw new IllegalArgumentException("Maximum nesting depth must be 1..1200");
        JSON.maxDepth = maxDepth;
    }

}
