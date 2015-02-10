---
title: citemgr
layout: page
---

Some links
        <ul>
          <li><a href="https://github.com/neelsmith/citemgr/zipball/master">Download <strong>ZIP File</strong></a></li>
          <li><a href="https://github.com/neelsmith/citemgr/tarball/master">Download <strong>TAR Ball</strong></a></li>
          <li><a href="https://github.com/neelsmith/citemgr">View On <strong>GitHub</strong></a></li>
        </ul>
      
      
  

Its main function is to generate RDF representations of CITE repositories.

## Prerequisites ##



- [gradle](http://www.gradle.org/)

## Configuration  and usage

A configuration file configures one or more of a CTS, CITE Collection and CITE Image repository. The configuration is identified by the <code>conf</code> project property or by default uses <code>conf.gradle</code>.  To generate a TTL representation of
all services configured in a given configuration file, you can use

    gradle -Pconf=FILENAME ttl


Output in <code>build/ttl</code> will include individual TTL files for each service, and a composite
file <code>all.ttl</code> with appropriate <code>@prefix</code> statements so it can be directly loaded into
an RDF triple store.
