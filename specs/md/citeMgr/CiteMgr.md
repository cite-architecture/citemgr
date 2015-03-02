# CITE manager, version @version@ #

Cite manager is an automated build system for managing CTS, CITE Collection and CITE Image repositories.


How `citemgr` helps manage CITE repositories:

- verify contents of an individual archive against its inventory. (Initial implementation for CTS archives validates files on disk against contents of a TextInventory; this is not yet implemented for CITE Collections.)
- verify the referential integrity of a project's citation of a standard <a concordion:run="concordion" href="dse/Dse.html">Digital Scholarly Editing data model</a> relating texts, text-bearing physical surfaces, and documentary visual evidence.
- help manage analyses of or commentaries on texts by expressing the analysis as an <a concordion:run="concordion" href="textAnalyzer/TextAnalyzer.html"> "analytical exemplar"</a>
- generate a complete RDF description of managed repositories

## Relation to other CITE packages ##

`citemgr` relies on several [other code packages](dependencies/Dependencies.html)
