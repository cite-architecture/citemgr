# Text analyser #

A systematic commentary on or analysis of a text can be represented as a CITE Collection with one property documenting with a CTS URN the passage of text discussed or analyzed.  Since every analysis identifies chunks of the text, or tokens, to discuss or analyze, we could think of this action as organizing the version with one further level of hierarchy (although this further organization could of course cross the boundaries of the citation hierarchy).


For this situation, `citemgr` can express the organization of the analysis as an additional tier of citation, specific to this analysis.  The text of the analyzed chunks can be represented as a specific *exemplar* of this version.


## `.tsv` to RDF ##


A system analysis

@openex@

### Example ###
<a href="../../../resources/test/data/personalNamesCE.tsv" concordion:set="#tsv = setHref(#HREF)">this `.tsv` data</a> from a CITE Collection, i



Given 
<a href="../../../resources/test/data/tokens.tsv" concordion:set="#tsvother = setHref(#HREF)">this `.tsv` data</a> from a CITE Collection, if we specify these properties:

- the record of the entire analysis named <strong concordion:set="#analyzer">citeUrn</strong>
- the results of the analysis is named <strong concordion:set="#canon">analysis</strong>:  
- the text passage under analysis  is named <strong concordion:set="#txt">ctsUrn</strong>
- the text content of the new exemplar is named <strong concordion:set="#lemma">surfaceForm</strong>


and we  define a new exemplar named <strong concordion:set="#exid">pers</strong>, then can  <strong concordion:assertEquals="exemplify(#tsv,#analyzer,#canon,#txt,#lemma,#exid)">generate an expression of the exemplar text as RDF</strong>.

@closeex@