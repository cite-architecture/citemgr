# CITE manager #

Cite manager is an automated build system for managing CTS, CITE Collection and CITE Image repositories.


How `citemgr` helps manage CITE repositories:

- verify contents of an individual archive against its inventory. (Initial implementation for CTS archives validates files on disk against contents of a TextInventory; this is not yet implemented for CITE Collections.)
- verify the referential integrity of a project's citation of a standard [Digital Scholarly Editing data model](dse/Dse.html) relating texts, text-bearing physical surfaces, and documentary visual evidence.
- generate a complete RDF description of managed repositories