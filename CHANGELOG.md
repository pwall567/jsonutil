# Change Log
Added this change log after project was already under way.  Early changes are not noted.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [5.1] - 2023-06-29
### Changed
- `JSON`: added check for excessive nesting
- `pom.xml`: updated dependency versions

## [5.0] - 2021-04-19
### Changed
- `JSON`: modified error messages (removed "at root" when pointer is at root of JSON)
- `JSON`: added `escape` and `unescape`
- `JSONBoolean`, `JSONDecimal`, `JSONDouble`, `JSONFloat`, `JSONInteger`, `JSONLong`, `JSONString`, `JSONZero`: changed
`get` to `getValue`
- `JSONDecimal`: bug fix - `hashCode()` now returns value consistent with `equals()`
- `JSONDecimal`: fixed bug in comparisons
- `JSONDecimal`: optimisation in `valueOf()`
- `JSONSequence`: improved `hashCode()` and `equals()`

## [4.2] - 2020-12-26
### Changed
- updated dependency

## [4.1] - 2020-10-27
### Changed
- `JSONValue` etc.: added `toSimpleValue()`

## [4.0] - 2020-10-14
### Changed
- `JSON`: improved error reporting by including JSON pointer
- `JSONFormat`: generalised to allow YAML results
- tests: switched to JUnit 5 (allows checking of exception messages)

## [3.1] - 2020-05-10
### Changed
- `JSON`: create `JSONDecimal` when integer number is too big for `Long`

## [3.0] - 2020-01-27
### Added
- `JSONDecimal`: New class to handle decimal (floating point) values

### Changed
- `JSONNumberValue`, `JSONDouble`, `JSONFloat`, `JSONInteger`, `JSONLong`, `JSONZero`: added conversions to and
comparisons with `BigInteger` and `BigDecimal`
- `JSON`: now creates `JSONDecimal` when parsing floating point numbers (instead of `JSONDouble`)

## [2.2] - 2019-12-08
### Changed
- updated dependency

## [2.1] - 2019-12-01
### Changed
- changed `JSONObject` and `JSONMapping` to add constructor from `Map`

## [2.0] - 2017-04-25
### Changed
- multiple classes changed for Java 8; README updated accordingly

## [1.6] - 2017-04-23
### Changed
- changed `JSON`; several new convenience methods

## [1.5] - 2017-04-20
### Changed
- changed `JSONObject`, `JSONMapping`, `JSONArray` and `JSONSequence`; new convenience
  methods and constructors

## [1.4] - 2017-04-17
### Added
- new class `JSONSequence` (generalisation of `JSONArray`)
- new class `JSONMapping` (generalisation of `JSONObject`)

### Changed
- changed `JSONArray` to derive from `JSONSequence`
- changed `JSONObject` to derive from `JSONMapping`
- changed `pom.xml` to use version 1.3 of `javautil`

## [1.3] - 2016-11-27
### Changed
- Used optimised string output of int and long
- Better discriminination between int and long in `JSON.parse()`

### Added
- More JUnit tests

## [1.2] - 2015-12-10
### Added
- `putJSON()` and `addJSON()` methods to allow chaining
- new means of iterating over array values
- Documentation

### Changed
- Added nested exception to `JSONException`
- Fixed compiler warnings

## [1.1] - 2015-11-30
### Added
- `JSONException`

### Changed
- Optimised `equals()` in several classes

## [1.0] - 2015-09-08
### Added
- Initial release
