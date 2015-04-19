---
title: citemgr
layout: page
---

## About ##

`citemgr` is an automated build system (using [gradle](http://www.gradle.org)) for managing CTS, CITE Collection and CITE Image repositories.    As of version 0.6.0, the project is organized into two subprojects:  

1. the `core` directory automatically assembles some infrastructure, and does not need to be directly used.  (If you want to find out more about the `core` subproject, see [these details](core).)
2. the `projects` directory provides tasks for working with CITE repositories, verifiying the integrity of their citation, and uniting data from one or more repositories in an RDF graph. 




## Prerequisites ##

- [gradle](http://www.gradle.org/)

## Configuration  and usage

You can use a configuration file for each of your projects to configure one or more of a CTS, CITE Collection and CITE Image repository.   The `projects` build file checks for a property named `proj` where you can supply the name of a configuration file relative to the `citemgr` root directory.   (If it does not find a `proj` property, it defaults to taking the settings from `projects/projconf.gradle`.)

Gradle allows setting a property on the command line with the `-P`flag.  To generate a TTL representation of
all services configured in a given configuration file, you can run from the CITE manager root directory:

    gradle -Pproj=FILENAME projects:ttl

(Note that if you run this from the `projects` directory, the syntax will be `gradle -Pproj=FILENAME ttl`, but `FILENAME` will still refer to a file relative to the CITE manager root directory!)

In `projects/build/ttl`, output will include individual TTL files for each service, and a composite
file  `all.ttl` with appropriate  `@prefix` statements so it can be directly loaded into
an RDF triple store.


## Links ##


- [github source](https://github.com/neelsmith/citemgr)
- [live specifications](specs/citeMgr/CiteMgr.html)
- [API docs](api)
  

