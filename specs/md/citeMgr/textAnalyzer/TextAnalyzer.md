# Text analyser #

A systematic commentary on or analysis of a text can be represented as a CITE Collection with one property documenting with a CTS URN the passage of text discussed or analyzed.  Since every analysis identifies chunks of the text, or tokens, to discuss or analyze, we could think of this action as organizing the version with one further level of hierarchy (although this further organization could of course cross the boundaries of the citation hierarchy).


For this situation, `citemgr` can express the organization of the analysis as an additional tier of citation, that is specific to this analysis.  The text of the analyzed chunks can be represented as a specific exemplar of this version.

@openex@

### Example ###

Given 
<a href="../../../resources/test/data/tokens.tsv" concordion:set="#ti = setHref(#HREF)">this `.tsv` data</a> from a CITE Collection, if we specify that the property named <strong>Analysis</strong> is the canical identifier for the analysis, the property named <strong>SourceText</strong> is a CTS URN identifying the text analyzed, and the property named <strong>TextToken</strong> has the "lemma" or tokenization that the analysis comments on, then we can represent this as RDF as follows.

@closeex@