package org.homermultitext.citemanager

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

class AnalyticalExemplarBuilder {

  public Integer debug = 0
  
  
  AnalyticalExemplarBuilder() {
  }

  
  String tripletToRdf(String exemplarId, String cite, String cts, String chunk, Integer seq) {
    StringBuilder rdf = new StringBuilder()
    CtsUrn exemplarUrn
    
    // test if this is at version level!
    CtsUrn srcUrn = new CtsUrn(cts)
    /*
    if ((srcUrn.levelForLabel() != "edition") && (srcUrn.levelForLabel() != "edition")) {
      System.err.println "${cts} WON'T WORK: " + srcUrn.levelForLabel()
    } else {
    ... process it
    }
    */

    if (srcUrn.isRange()) {
      // it's more complicated
    } else {

          if (debug > 1) {
	    System.err.println "In new triple for ${exemplarId}, CITE: ${cite} for CTS ${cts}, chunk ${chunk}"
	    
	    System.err.println "NEW URN ${srcUrn.getUrnWithoutPassage()}.${exemplarId}:${srcUrn.getPassageNode()}.${seq}"
	  }
	  String versionBase = srcUrn.getUrnWithoutPassage().substring(0,srcUrn.getUrnWithoutPassage().size() - 1)
	  exemplarUrn = new CtsUrn("${versionBase}.${exemplarId}:${srcUrn.getPassageNode()}.${seq}")


	  String quoted = chunk.replaceAll(/"/, "\\\\\"")

	  // Standard RDF expression of a CTS text node:
	  rdf.append("${exemplarUrn} rdf:label " + '"Analysis for ' + "'" +  exemplarId +   "' collection of text passage ${srcUrn}" + '"  .\n')
	  
	  rdf.append("${exemplarUrn} rdf:type CtsExemplar . \n")
	  rdf.append("${exemplarUrn} cts:belongsTo ${versionBase}:${srcUrn.getPassageNode()} . \n")
	  rdf.append("${versionBase} cts:possesses ${exemplarUrn} . \n")

	  rdf.append("${exemplarUrn} cts:hasSequence ${seq} . \n")
	  rdf.append("${exemplarUrn} cts:hasTextContent " + '"' + quoted + '" . \n')
	  rdf.append("${exemplarUrn} cts:citationDepth ${exemplarUrn.getCitationDepth()} . \n")


	  String baseUrn = exemplarUrn.getUrnWithoutPassage()
	  Integer levels = exemplarUrn.getCitationDepth()
	  while (levels > 1) {
	    rdf.append("${baseUrn}${exemplarUrn.getPassage(levels)} cts:containedBy  ${baseUrn}${exemplarUrn.getPassage(levels -1)} .\n")
	    rdf.append("${baseUrn}${exemplarUrn.getPassage(levels -1)} cts:contains ${baseUrn}${exemplarUrn.getPassage(levels)}   .\n")

	    levels--;
	    rdf.append("${baseUrn}${exemplarUrn.getPassage(levels)} cts:citationDepth ${levels}   .\n")
	  }
	  
	  // ORCA relation:
	  rdf.append("${cite} orca:analyzes ${srcUrn} .\n")
	  rdf.append("${srcUrn} orca:analyzedBy ${cite}  .\n")

    }

    
    
    return rdf.toString()

  }
  
  String rdfFromTsv(File srcCollection, String citeProp, String ctsProp, String chunkProp, String newCollection)
  throws Exception {
    
    
    StringBuilder rdf = new StringBuilder()
    Integer lineNo = 0
    Integer citeCol = -1
    Integer ctsCol = -1
    Integer chunkCol = -1

    srcCollection.eachLine {
      if (debug > 2) {System.err.println "Process line " + lineNo + " from TSV"}
      def cols = it.split(/\t/)
      if (lineNo == 0) {
	// Identify columns for properties:
	cols.eachWithIndex { txt, num ->
	  if (debug > 1) {
	    System.err.println "AEB: check column ${txt}, ${num + 1} out of ${cols.size()}"
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
	  errMsg.append("No CTS column " + ctsProp + "\n")
	  OK = false
	}


	if (chunkCol < 0) {
	  errMsg.append("No chunk column " + chunkProp + "\n")
	  OK = false
	}

	if (!OK) {
	  throw new Exception("AnalyticalExemplarBuilder: could not identify all columns.\n" + errMsg.toString())
	}

	
      } else {
	if (debug > 1) {System.err.println "${lineNo}, get rdf for ${cols}"}
	String ctsUrn = cols[ctsCol]
	String citeUrn = cols[citeCol]
	String chunk = cols[chunkCol]
	rdf.append(tripletToRdf(newCollection,citeUrn,ctsUrn,chunk,lineNo) + "\n\n")
      }
      lineNo++;
    }
    System.err.println rdf.toString()
    return rdf.toString()
  }
}
