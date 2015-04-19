---
layout: page
title: "CITE manager's <pre>core</core> subproject"
---

The `core` subproject is a standard gradle build for compiling code, assembling a `.jar` libary, and uploading a `.jar` to a nexus repository. 

It also includes concordion specifications, and provides a task called `conc` that will run concordion tests on specifications written in markdown.

There is very little code in the current version of `core`  since most of CITE manager's work is done by libraries for working with individual types of CITE repositories. 

