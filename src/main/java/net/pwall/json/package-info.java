/*
 * @(#) package-info.java
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

/**
 * <p>JSON encode / decode classes for Java.</p>
 *
 * <div style="font-weight:bold">Background</div>
 *
 * <p>Does the world need another Java JSON library?  Perhaps not, but let me explain.
 * This work was started before any of the currently-popular libraries had achieved any sort of
 * prominence.  I had a requirement for classes to parse incoming JSON data, to hold the JSON
 * representation in an internal form that was easy to access, and to output
 * syntactically-correct JSON results.  It seemed a reasonably simple task, and a good test of
 * the {@link net.pwall.util.ParseText ParseText} class, part of my {@code javautil} library.
 * </p>
 *
 * <p>When I later encountered the more widely used libraries I was struck by how complicated
 * they seemed.  And how large.  And one of them described itself as "fastJSON" which I saw as
 * a challenge.</p>
 *
 * <p>There is a place for software that does a limited range of operations, and does them very
 * well.  With that in mind, I decided to continue development of my JSON library.</p>
 *
 * <div style="font-weight:bold">Guiding Principles</div>
 *
 * <p><i>Keep It Simple</i> &mdash; don't add complications that will rarely be used; keep your
 * software easy to understand and easy to use.</p>
 * <p><i>Follow Standards</i> &mdash; where there is an existing standard that could be used,
 * adopt that standard rather than creating a new one.</p>
 * <p><i>Make It Fast</i> &mdash; one of the main reasons for using a limited-functionality
 * library is performance.</p>
 *
 * <div style="font-weight:bold">Design</div>
 *
 * <p>In the light of those principles, the following design decisions were adopted:</p>
 * <ol>
 * <li>Have a single interface {@link net.pwall.json.JSONValue JSONValue} implemented by each of
 * the five types of JSON data: number, string, boolean, array and object.</li>
 * <li>Ensure that the array class {@link net.pwall.json.JSONArray JSONArray} implements
 * {@link java.util.List List&lt;JSONValue&gt;}, so that anyone familiar with that interface
 * could easily access the contents of the array.</li>
 * <li>Ensure that the object class {@link net.pwall.json.JSONObject JSONObject} implements
 * {@link java.util.Map Map&lt;String, JSONValue&gt;}, for similar reasons.</li>
 * <li>Limit the implementation to the current requirements.</li>
 * </ol>
 *
 * <div style="font-weight:bold">Implementation</div>
 *
 * <p>The implementation follows a conventional Java pattern.  There is a main class
 * {@link net.pwall.json.JSON JSON} which contains a set of static methods to parse a string (in
 * memory, or in a file or stream) into the internal form.  That internal form is a classic tree
 * structure, where each node is either a leaf node (string, number or boolean), or a composite
 * (array or object) which contains more nodes.</p>
 *
 * <p>The nodes all implement the {@link net.pwall.json.JSONValue JSONValue} interface, which
 * specifies two methods:</p>
 * <ul>
 * <li>{@link java.lang.String String} {@link net.pwall.json.JSONValue#toJSON() toJSON()}
 * &mdash; convert to the JSON external representation</li>
 * <li>{@code void}
 * {@link net.pwall.json.JSONValue#appendJSON(Appendable) appendJSON(Appendable a)} &mdash;
 * append the JSON form to an {@link java.lang.Appendable Appendable} (for example, a
 * {@link java.lang.StringBuilder StringBuilder} or a {@link java.io.Writer Writer}).</li>
 * </ul>
 * <p>The second method is an optimisation, which avoids the need to create an individual
 * {@link java.lang.String String} for each node when outputting a composite.</p>
 *
 * <p>There is extensive JavaDoc in the individual classes which should assist in the use of the
 * library.</p>
 *
 * <div style="font-weight:bold">Usage</div>
 *
 * There are several static methods of the {@link net.pwall.json.JSON JSON} class to parse a
 * string, file or input stream into a {@link net.pwall.json.JSONValue JSONValue}.  The result
 * may be {@code null} if the input consists simply of the token {@code null}, or one of the
 * seven simple JSON forms:
 *
 * <ul>
 * <li>{@link net.pwall.json.JSONString JSONString}</li>
 * <li>{@link net.pwall.json.JSONInteger JSONInteger}</li>
 * <li>{@link net.pwall.json.JSONLong JSONLong}</li>
 * <li>{@link net.pwall.json.JSONDouble JSONDouble}</li>
 * <li>{@link net.pwall.json.JSONFloat JSONFloat}</li>
 * <li>{@link net.pwall.json.JSONZero JSONZero} (an optimisation to improve handling of zero
 * values)</li>
 * <li>{@link net.pwall.json.JSONBoolean JSONBoolean}</li>
 * </ul>
 *
 * <p>or one of the two composite forms:</p>
 *
 * <ul>
 * <li>{@link net.pwall.json.JSONArray JSONArray}</li>
 * <li>{@link net.pwall.json.JSONObject JSONObject}</li>
 * </ul>
 *
 * <p>All of the simple classes have a {@code get()} method to retrieve the value (this is not
 * specified by the interface because the return type differs in each case).  Also, the numeric
 * classes all extend the {@link java.lang.Number Number} class, so the accessors
 * {@link java.lang.Number#intValue() intValue()},
 * {@link java.lang.Number#doubleValue() doubleValue()} etc. may be used to retrieve the value
 * in a particular form.  And the {@link java.lang.Object#toString() toString()} methods on
 * these classes all return the string representation of the value, not the JSON.</p>
 *
 * <p>The simplest way to use the {@link net.pwall.json.JSONArray JSONArray} and
 * {@link net.pwall.json.JSONObject JSONObject} classes is to treat them as
 * {@link java.util.List List&lt;JSONValue&gt;} and
 * {@link java.util.Map Map&lt;String, JSONValue&gt;} respectively.  In addition, there are
 * several convenience methods ({@code getString()}, {@code getObject()} etc.) for use when the
 * type of the value is known in advance.  Also, several overloaded forms of {@code addValue()}
 * (for {@link net.pwall.json.JSONArray JSONArray}) and {@code putValue()} (for
 * {@link net.pwall.json.JSONObject JSONObject}) exist, to simplify adding values to arrays and
 * objects.</p>
 *
 * <p>The two static methods {@link net.pwall.json.JSONArray#create() JSONArray.create()} and
 * {@link net.pwall.json.JSONObject#create() JSONObject.create()} exist to facilitate the
 * "fluent" style of coding (see example below).</p>
 *
 * <p>To create the JSON string value of an object, the
 * {@link net.pwall.json.JSONValue#toJSON() toJSON()} method may be called; this will serialize
 * the value, recursively calling {@link net.pwall.json.JSONValue#toJSON() toJSON()} on any
 * member items as necessary.  To avoid the unnecessary creation of a large number of
 * intermediate string objects, the
 * {@link net.pwall.json.JSONValue#appendJSON(Appendable) appendJSON()} method may be used to
 * append to an existing {@link java.lang.StringBuilder StringBuilder} etc.  It should be noted
 * that this is also useful for serializing directly to an output stream, e.g. a
 * {@link java.io.PrintStream PrintStream} or {@link java.io.Writer Writer}.</p>
 *
 * <div style="font-weight:bold">Examples</div>
 *
 * <p>To parse and process an incoming JSON object, assuming the JSON is of the form:</p>
 *
 * <pre>
 *   {
 *     "id": "A23456",
 *     "qty": 12,
 *     "value": 60.00
 *   }
 * </pre>
 *
 * <p>The following code will process that JSON:</p>
 *
 * <pre>
 * JSONObject jsonObject = (JSONObject)JSON.parse(str);
 * String accountId = jsonObject.getString("id");
 * int quantity = jsonObject.getInt("qty");
 * double value = jsonObject.getDouble("value");
 * </pre>
 *
 * <p>(The cast is required because {@link net.pwall.json.JSON#parse(String) JSON.parse(str)}
 * returns {@link net.pwall.json.JSONValue JSONValue}; the developer has the option of testing
 * the type of the returned value or allowing the system to throw a
 * {@link java.lang.ClassCastException ClassCastException}.)</p>
 *
 * <p>To create an object of that form and then output it:</p>
 *
 * <pre>
 * JSONObject jsonObject = JSONObject.create().putValue("id", "A23456").putValue("qty", 12).
 *         putValue("value", 60.00);
 * System.out.println(jsonObject.toJSON());
 * </pre>
 *
 * <p>And if that JSON object was an item in an array of objects, the array could be processed
 * by:</p>
 *
 * <pre>
 * JSONArray jsonArray = (JSONArray)JSON.parse(str);
 * for (JSONObject jsonObject : jsonArray.objects()) {
 *     String accountId = jsonObject.getString("id");
 *     // etc...
 * }
 * </pre>
 *
 * <p>This is just a taste of what is available; see the individual class JavaDoc for more
 * information.</p>
 *
 * @author Peter Wall
 */

package net.pwall.json;
