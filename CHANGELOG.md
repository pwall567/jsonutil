# Change Log
Added this change log after project was already under way.  Early changes are not noted.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]
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
