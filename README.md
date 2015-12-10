# jsonutil

JSON encode/decode classes for Java

## Background

Does the world need another Java JSON library?  Perhaps not, but let me explain.

This work was started before any of the currently-popular libraries had achieved any sort of
prominence.  I had a requirement for classes to parse incoming JSON data, to hold the JSON
representation in an internal form that was easy to access, and to output syntactically-correct
JSON results.  It seemed a reasonably simple task, and a good test of the `ParseText` class,
part of my `javautil` library.

When I later encountered the more widely used libraries I was struck by how complicated they
seemed.  And how large.  And one of them described itself as "fastJSON" which I saw as a
challenge.

There is a place for software that does a limited range of operations, and does them very well.
With that in mind, I decided to continue development of my JSON library.

## Guiding Principles

*Keep It Simple* --- don't add complications that will rarely be used; keep your software easy
to understand and easy to use.

*Follow Standards* --- where there is an existing standard that could be used, adopt that
standard rather than creating a new one.

*Make It Fast* --- one of the main reasons for using a limited-functionality library is performance.

## Design

In the light of those principles, the following design decisions were adopted:

1. Have a single interface `JSONValue` implemented by each of the five types of JSON data:
number, string, boolean, array and object.
2. Ensure that the array class `JSONArray` implements `List<JSONValue>`, so that anyone
familiar with that interface could easily access the contents of the array.
3. Ensure that the object class `JSONObject` implements `Map<String, JSONValue>`, for
similar reasons.
4. Limit the implementation to the current requirements.

## Implementation

The implementation follows a conventional Java pattern.  There is a main class `JSON` which
contains a set of static methods to parse a string (in memory, or in a file or stream) into the
internal form.  That internal form is a classic tree structure, where each node is either a leaf
node (string, number or boolean), or a composite (array or object) which contains more nodes.

The nodes all implement the `JSONValue` interface, which specifies two methods:

* `String toJSON();` --- convert to the JSON external representation
* `void appendJSON(Appendable a)` --- append the JSON form to an `Appendable`
(for example, a `StringBuilder` or a `Writer`).

The second method is an optimisation, which avoids the need to create an individual `String`
for each node when outputting a composite.

There is extensive JavaDoc which should assist in the use of the library.

## Performance

On most benchmarks, the library significantly out-performs its competitors.  For example:

![Diagram](https://github.com/pwall567/jsonutil/raw/develop/benchmark1.png "Benchmark 1")

## Usage

There are several static methods of the `JSON` class to parse a string, file or input stream
into a `JSONValue`.  The result may be `null` if the input consists simply of the token
`null`, one of the seven simple JSON forms:

* `JSONString`
* `JSONInteger`
* `JSONLong`
* `JSONDouble`
* `JSONFloat`
* `JSONZero` (an optimisation to improve handling of zero values)
* `JSONBoolean`

or one of the two composite forms:

* `JSONArray`
* `JSONObject`

All of the simple classes have a `get()` method to retrieve the value (this is not specified by
the interface because the return type differs in each case).  Also, the number classes all
extend the `Number` class, so the accessors `intvalue()`, `doubleValue()` etc. may be used
to retrieve the value in a particular form.  And the `toString()` methods on these classes all
return the string representation of the value, not the JSON.

The simplest way to use the `JSONArray` and `JSONObject` classes is to treat them as
`List<JSONValue>` and `Map<String, JSONValue>` respectively.  In addition, there are several
convenience methods (`getString()`, `getObject()` etc.) for use when the type of the value is
known in advance.  Also, several overloaded forms of `addValue()` (for `JSONArray`) and
`putValue()` (for `JSONObject`) exist, to simplify adding values to arrays and objects.

The two static methods `JSONArray.create()` and `JSONObject.create()` exist to facilitate
the "fluent" style of coding (see example below).

To create the JSON string value of an object, the `toJSON()` method may be called; this will
serialize the value, recursively calling `toJSON()` on any member items as necessary.  To avoid
the unnecessary creation of a large number of intermediate string objects, the `appendJSON()`
method may be used to append to an existing `Stringbuilder` etc.  It should be noted that this
is also useful for serializing directly to an output stream, e.g. a `PrintStream` or `Writer`.

## Examples

To parse and process an incoming JSON object, assuming the JSON is of the form:

```javascript
  {
    "id": "A23456",
    "qty": 12,
    "value": 60.00
  }
```

The following code will process that JSON:

```java
JSONObject jsonObject = (JSONObject)JSON.parse(str);
String accountId = jsonObject.getString('id');
int quantity = jsonObject.getInt("qty");
double value = jsonObject.getDouble("value");
```

(The cast is required because `JSON.parse(str)` returns `JSONValue`; the developer has the
option of testing the type of the returned value or allowing the system to throw a
`ClassCastException`.)

To create an object of that form and then output it:

```java
JSONObject jsonObject = JSONObject.create().putValue("id", "A23456").putValue("qty", 12).
        putValue("value", 60.00);
System.out.println(jsonObject.toJSON()); 
```

And if that JSON object was an item in an array of objects, the array could be processed by:

```java
JSONArray jsonArray = (JSONArray)JSON.parse(str);
for (JSONObject jsonObject : jsonArray.objects()) {
    String accountId = jsonObject.getString('id');
    // etc...
}
```

This is just a taste of what is available; see the JavaDoc for more information.
