package org.homermultitext.citemanager


class AnalyticalExemplarBuilder {

  Integer debug = 0
  
  
  AnalyticalExemplarBuilder() {
  }

  
  String tripletToRdf(String cite, String cts, String chunk, Integer seq) {
    return "${seq} ${cite} -> ${chunk}"
  }
  
  String rdfFromTsv(File srcCollection, String citeProp, String ctsProp, String chunkProp)
  throws Exception {
    
    
    StringBuilder rdf = new StringBuilder()
    Integer lineNo = 0
    Integer citeCol = -1
    Integer ctsCol = -1
    Integer chunkCol = -1

    srcCollection.eachLine { 
      def cols = it.split(/\t/)
      if (lineNo == 0) {
	// Identify columns for properties:
	cols.eachWithIndex { txt, num ->
	  if (debug > 1) {
	    System.err.println "AEB: check column ${txt}"
	  }
	  switch(txt) {
	  case citeProp:
	  citeCol = num
	  break

	  case urnProp:
	  ctsCol = num
	  break

	  case textProp:
	  chunkCol = num
	  break

	  default:
	  break
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
	}
	
      } else {
	String ctsUrn = cols[ctsCol]
	String citeUrn = cols[citeCol]
	String chunk = cols[chunkCol]
	rdf.append(tripletToRdf(citeUrn,ctsUrn,chunk,lineNo))	
      }
    }
    
    return rdf.toString()
  }
}
