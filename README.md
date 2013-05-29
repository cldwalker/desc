## Description

This repl tool enables you to take structured notes about clojure functions. Scope may change to
other data types or to more function-related features, whatever's most useful.

[![Build Status](https://travis-ci.org/cldwalker/desc.png?branch=master)](https://travis-ci.org/cldwalker/desc)

## Install

```
$ lein install
```

I can release this as a clojar if anyone asks.

## Usage

In your repl:

```
$ lein repl
user=> (use 'desc.core)
nil

; Add a record
user=> (desc "comparator" "converts pred to comparable fn")
Record added.
nil

; Search for it later
user=> (desc "comparator")
Name: comparator
Desc: converts pred to comparable fn
nil

; If multiple fns are found, you'll get a list
user=> (desc "comp")
+------------+----------------------------------------------+
| name       | desc                                         |
+------------+----------------------------------------------+
| complement | opposite of fn                               |
| comparator | converts pred fns to comparable fn           |
| comp       | (comp a b c) = (a (b (c))) = opposite of ->> |
+------------+----------------------------------------------+

; Update a record
user=> (desc "comparator" "converts pred fns to comparable fn")
Record updated.
nil
```

NOTE: Removing a record from the repl isn't possible yet. For now, just do so by directly editing ~/.desc.clj.

## Motivation

As I run into new functions I like to type a quick note about them. While this was originally in a
text file, I wanted more control over searching as well as access to the Clojure runtime to
mine a function's meta information.


## Credits

Thanks to @relevance fridays for giving me time to start this!

## TODO

* Search by :name or :desc fields
* Remove records via the repl
* Tag functions and search by them
* Search any functions by argument names
* Search any functions by their source string
