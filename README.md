#citemgr#

An automated build system (using gradle) for managing CTS, CITE Collection and CITE Image repositories.

Its main function is to generate RDF representations of CITE repositories.

##Prerequisites##

- [gradle](http://www.gradle.org/)


##Configuration  and generating RDF##

A configuration file configures one or more of a CTS, CITE Collection and 
CITE Image repository. The configuration is identified by the `conf` project
property or by default uses `conf.gradle`.  



To generate a TTL representation of
all services conifgured in a given configuration file, you can use

    gradle -Pconf=FILENAME ttl
    

Output in `build/ttl` will include individual TTL files for each service, and a composite
file `all.ttl` with appropriate `@prefix` statements so it can be directly loaded into
an RDF triple store.

##Other functionality##

See the [project wiki](https://github.com/cite-architecture/citemgr/wiki).



