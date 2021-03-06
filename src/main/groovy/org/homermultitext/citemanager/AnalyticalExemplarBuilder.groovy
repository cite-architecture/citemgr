package org.homermultitext.citemanager

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

class AnalyticalExemplarBuilder {

  public Integer debug = 0
  
  
  AnalyticalExemplarBuilder() {
  }

  /**
   * @param txtUrn CTS URN identifying passage analyzed.
   * @param newExemplarId String identifying exemplar in new URN.
   * @param count Count of analytical elements.
   */
  CtsUrn composeAnalyticalUrn(CtsUrn txtUrn, String newExemplarId, Integer count) {
    try {
    // Guarantee String is a valid URN:
    String versionBase = txtUrn.getUrnWithoutPassage().substring(0,txtUrn.getUrnWithoutPassage().size() - 1)
    CtsUrn  exemplarUrn = new CtsUrn("${versionBase}.${newExemplarId}:${txtUrn.getPassageNode()}.${count}")
    return exemplarUrn
    
    } catch (Exception e) {
      System.err.println("composeAnalyticalUrn: could not create new URN with " + txtUrn)
    }
  }



  
  /** Composes RDF for a single triple of CTS URN, CITE URN and text content.
   * @param exemplarId String to use as exemplar identifier in new exemplar-level CTS URNs.
   * @param analyzer CITE URN representing a whole analysis in an ordered collection.
   * @param cite CITE URN expressing how passage is analyzed.
   * @param cts CTS URN identifying passage analyzed.
   * @param chunk Text content of new citable exemplar node.
   * @param seq Sequence number.
   */
  String tripletToRdf(String exemplarId, String analyzer, String cite, String cts, String chunk, Integer seq) {
    StringBuilder rdf = new StringBuilder()
    CtsUrn srcUrn = new CtsUrn(cts)
    CtsUrn exemplarUrn =   composeAnalyticalUrn(srcUrn, exemplarId, seq)
    

    
    if ((srcUrn.levelForLabel() != "edition") && (srcUrn.levelForLabel() != "edition")) {
      //System.err.println "${cts} WON'T WORK: " + srcUrn.levelForLabel()
    } else {
      //... process it
    }


    if (srcUrn.isRange()) {
      // it's more complicated



      
    } else {
      if (debug > 1) {
	System.err.println "In new triple for ${exemplarId}, CITE: ${cite} for CTS ${cts}, chunk ${chunk}"
	System.err.println "NEW URN ${srcUrn.getUrnWithoutPassage()}.${exemplarId}:${srcUrn.getPassageNode()}.${seq}"
      }
    }


    // NS? What's up with getUrnWithoutPassage?  Trailing colon or not?
    CtsUrn bareSrc = new CtsUrn(srcUrn.getUrnWithoutPassage())
    rdf.append("<${srcUrn}> cts:isSubstringOf  <${bareSrc}${srcUrn.getPassageNode()}> .\n")
    rdf.append("<${bareSrc}${srcUrn.getPassageNode()}> cts:hasSubstring  <${srcUrn}> .\n")

    
    exemplarUrn = composeAnalyticalUrn(srcUrn, exemplarId, seq)
    String quoted = chunk.replaceAll(/"/, "\\\\\"")
    //String quoted = chunk
	  
    // Standard RDF expression of a CTS text node:
    rdf.append("<${exemplarUrn}> rdf:label " + '"Analysis for ' + "'" +  exemplarId +   "' collection of text passage ${srcUrn}" + '"  .\n')
	  
    rdf.append("<${exemplarUrn}> rdf:type cts:Exemplar . \n")
    rdf.append("<${exemplarUrn}> cts:belongsTo <${exemplarUrn.getUrnWithoutPassage()}> . \n")
    rdf.append("<${exemplarUrn.getUrnWithoutPassage()}> cts:possesses <${exemplarUrn}> . \n")

    rdf.append("<${exemplarUrn}> cts:hasSequence ${seq} . \n")
    rdf.append("<${exemplarUrn}> cts:hasTextContent " + '"' + quoted + '" . \n')
    rdf.append("<${exemplarUrn}> cts:citationDepth ${exemplarUrn.getCitationDepth()} . \n")

    String baseUrn = exemplarUrn.getUrnWithoutPassage()
    Integer levels = exemplarUrn.getCitationDepth()
    while (levels > 1) {
      rdf.append("<${baseUrn}${exemplarUrn.getPassage(levels)}> cts:containedBy  <${baseUrn}${exemplarUrn.getPassage(levels -1)}> .\n")
      rdf.append("<${baseUrn}${exemplarUrn.getPassage(levels -1)}> cts:contains <${baseUrn}${exemplarUrn.getPassage(levels)}>   .\n")

      levels--;
      rdf.append("<${baseUrn}${exemplarUrn.getPassage(levels)}> cts:citationDepth ${levels}   .\n")
    }
    // ORCA relations:
   rdf.append("<${exemplarUrn}> orca:analyzedBy <${analyzer}> .\n")
   rdf.append("<${analyzer}> orca:analyzes <${exemplarUrn}>  .\n")

   rdf.append("<${exemplarUrn}> orca:analyzedAs <${cite}>  .\n")
   rdf.append("<${cite}> orca:analysisOf <${exemplarUrn}>  .\n")


    rdf.append("<${exemplarUrn}> orca:exemplifies <${srcUrn}> .\n")
    rdf.append("<${srcUrn}> orca:exemplifiedBy <${exemplarUrn}> .\n")
    
    return rdf.toString()
  }

  /**
   * Expresses in RDF the relation of an analysis to a text
   * as an analytical exemplar.
   * @param srcCollection A file in .tsv format relating a CTS URN, a 
   * a CITE URN and a String of text.  It should include a header line
   * with names of the columns.
   * @param citeProp Name of the column with CITE URNs for the analysis.
   * @param ctsProp Name of the column with CTS URN for the text analyzed.
   * @param chunkProp Name of the column with the text content of the passage in
   * th analytical exemplar.
   * @param newCollection Value to use in CTS URNs for the new analytical exemplar.
   *
   * @returns A String of RDF.
   * @throws Exception for all kinds of catastrophic results.
   */
  String rdfFromTsv(File srcCollection, String analyzer, String citeProp, String ctsProp, String chunkProp, String newCollection)
  throws Exception {
    return rdfFromTsv(srcCollection, analyzer, citeProp, ctsProp, chunkProp,newCollection,false)
  }


  /**
   * Expresses in RDF the relation of an analysis to a text
   * as an analytical exemplar.
   * @param srcCollection A file in .tsv format relating a CTS URN, a 
   * a CITE URN and a String of text.  It should include a header line
   * with names of the columns.
   * @param citeProp Name of the column with CITE URNs for the analysis.
   * @param ctsProp Name of the column with CTS URN for the text analyzed.
   * @param chunkProp Name of the column with the text content of the passage in
   * th analytical exemplar.
   * @param newCollection Value to use in CTS URNs for the new analytical exemplar.
   * @param prefix True if RDF prefix statements should be included.
   *
   * @returns A String of RDF.
   * @throws Exception for all kinds of catastrophic results.
   */
    String rdfFromTsv(File srcCollection, String analyzer, String citeProp, String ctsProp, String chunkProp, String newCollection, boolean prefix)
  throws Exception {
    
    StringBuilder rdf = new StringBuilder()
    if (prefix) {
      rdf.append("@prefix orca:        <http://www.homermultitext.org/orca/rdf/> .\n\n")
    }
    //Integer lineNo = 0
    Integer citeCol = -1
    Integer ctsCol = -1
    Integer chunkCol = -1
    Integer analysisCol = -1
    

    def tabLines = srcCollection.readLines()
    
    tabLines.eachWithIndex { ln, lineNo ->
      if (debug > 2) {System.err.println "Process line " + lineNo + " from TSV"}
      def cols = ln.split(/\t/)
      if (lineNo == 0) {
	// Identify columns for properties:
	cols.eachWithIndex { txt, num ->
	  if (debug > 1) {
	    System.err.println "AEB: check column #${txt}#, ${num + 1} out of ${cols.size()}"
	  }
	  switch(txt) {
	  case citeProp:
	  citeCol = num
	  break

	  case ctsProp:
	  ctsCol = num
	  break

	  case chunkProp:
	  chunkCol = num
	  break

	  case analyzer:
	  analysisCol = num
	  break
	  
	  default:
	  break
	  }
	}
	StringBuilder errMsg = new StringBuilder()
	boolean OK = true
	if (citeCol < 0) {
	  errMsg.append("No CITE column " + citeProp + "\n")
	  OK = false
	}

	if (ctsCol < 0) {//  && (ctsCol >= 0) && (chunkCol >= 0)) {
	  errMsg.append("No CTS column #" + ctsProp + "#\n")
	  OK = false
	}


	if (chunkCol < 0) {
	  errMsg.append("No chunk column " + chunkProp + "\n")
	  OK = false
	}

	if (analysisCol < 0) {
	  errMsg.append("No analysis column #" + analyzer + "#\n")
	  OK = false
	}

	if (!OK) {
	  throw new Exception("AnalyticalExemplarBuilder: could not identify all columns.\n" + errMsg.toString())
	} else {

	  System.err.println ("Found all columns: ready to analyze");
	}

	System.err.println("End of line " + lineNo);
	
	
      } else {
	if (debug > 1) {System.err.println "${lineNo}, get rdf for ${cols}"}
	String ctsUrn = cols[ctsCol]
	String citeUrn = cols[citeCol]
	String chunk = cols[chunkCol]
	String analysis = cols[analysisCol]

	if (lineNo == 1) {
	  CtsUrn txtUrn = new CtsUrn(ctsUrn)
	  CtsUrn exemplarUrn = composeAnalyticalUrn(txtUrn, newCollection, lineNo)
	  CtsUrn versionUrn = new CtsUrn(exemplarUrn.reduceToVersion())



	  rdf.append("<${exemplarUrn.getUrnWithoutPassage()}> rdf:label " + '"Exemplar analyzing text ' + "'" + versionUrn.getUrnWithoutPassage() + "' for analytical collection " +"'" +   newCollection + "'" + '" . \n')
	  rdf.append("<${exemplarUrn.getUrnWithoutPassage()}> cts:belongsTo <${versionUrn.getUrnWithoutPassage()}> .\n")
	  rdf.append(" <${versionUrn.getUrnWithoutPassage()}> cts:possesses <${exemplarUrn.getUrnWithoutPassage()}> .\n")
	  rdf.append("\n\n")
    	}

	rdf.append(tripletToRdf(newCollection,analysis, citeUrn,ctsUrn,chunk,lineNo))
	
	if (tabLines.size() > 1) {
	  CtsUrn txtUrn = new CtsUrn(ctsUrn)
	  CtsUrn currentUrn = composeAnalyticalUrn(txtUrn, newCollection, lineNo)
	  CtsUrn nextUrn = composeAnalyticalUrn(txtUrn, newCollection, lineNo + 1)
	  CtsUrn prevUrn = composeAnalyticalUrn(txtUrn, newCollection, lineNo - 1)
	  if (lineNo < tabLines.size()) {
	    rdf.append("<${currentUrn}> cts:next <${nextUrn}> .\n")
	  }
	  if (lineNo > 1) {
	    rdf.append("<${nextUrn}> cts:prev <${currentUrn}> .\n")
	  }
	}

      }
      
      rdf.append("\n\n\n\n")
      lineNo++;
    }
    return rdf.toString()
  }
}
