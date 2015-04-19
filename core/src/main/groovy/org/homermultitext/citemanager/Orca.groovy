package org.homermultitext.citemanager

import edu.harvard.chs.cite.CtsUrn

/* ORCA formatted table:
1. analysis URN
2. source text URN (including any subreference)
3. text reading (a String value for the node of the digital exemplar)
4. 
 */

class Orca {

  Integer debug = 1

  /** Returns a human-readable description of the system
   * for aliging citable analyses with a source edition. 
   */
  String getDescription()   {
    return "The Universal ORCA system."
  }

  /** Returns a human-readable description of the system
   * for generating token editions derived from and aligned 
   * with a source edition. 
   */
  /*String getDescription() {
    return "Generates a tokenized edition of a text from the output of a classified tokenization by treating each token as a citable node of the new edition.  Makes ranges in the token edition legible by appending a white space to each token, and include a token with a special value at the end of each citation node in the source edition."
  }
  */




  /** Generates a digital exemplar from an ORCA Collection.
   * @param inputFile File with the tabular representation of the text.
   * @param separatorStr String used to separate columns in the tabular file.
   * @param outputDirectory A writable directory where output will be created.
   * @param outputFileName Name to use for tabular output file with digital exemplar.
   */
  /* void generate(File inputFile, String separatorStr, File outputDirectory, String ouputFileName) {
    
     }*/




  String srcUrnName = ""

  String derivedExtension = "tokens"

  /** Character encoding to use for reading and writing files. */
  String charEnc = "UTF-8"

  /** String to use when demarking columns of tabular files. */
  String tab = "#"

  /** String to use for null value in columns of tabular files. */
  String nullCol = " "

  /** String to use as text value of token representing end of 
   * source editions' citation block.
   */
  String endBlockMarker = "<br/>"


  /** Name of file with tabular representation of tokenized edition. */
  String digitalExemplarName = "digitalExemplar.txt"

  /** Name of file with TTL statements mapping tokenized edition nodes
   * to node of source edition. */
  String exemplarTtlName = "exemplar.ttl"


  String getUrnName() {
    return srcUrnName + ".${derivedExtension}"
  } 

  /** Empty constructor */
  Orca() {
  }

  /** Formats a line for tabular representation of text.
   * @param baseUrn URN of the citable node in the source edition.
   * @param i Running index count for the node.
   * @returns A string in HMT project's tabular format.
   */
  String formatLine(String baseUrn, String prev, String curr, String nxt, String tokenValue) {
    return ("${baseUrn}.${curr}${tab}${curr}${tab}${prev}${tab}${nxt}${tab}${nullCol}${tab}${tokenValue}${tab}${nullCol}${tab}\n")
  }




  void generate(File inputFile, String separatorStr, File outputDirectory, String outputFileName) {
    this.digitalExemplarName = "${outputFileName}-tokenEdition.txt"
    this.exemplarTtlName = "${outputFileName}-tokenToSrcIndex.ttl"
    generate(inputFile, separatorStr, outputDirectory)
  }

  /** Generates a tokenized edition from a tabulated representation of a text.
   * It creates the following artifacts in outputDirectory:
   * - a file with the tabular representation of
   * the tokenized edition
   * - a file with TTL mapping the citable nodes of the token edition to the
   * source edition.
   * @param inputFile File with the tabular representation of the text.
   * @param separatorStr String used to separate columns in the tabular file.
   * @param outputDirectory A writable directory where output will be created.
   */
  void generate(File inputFile, String separatorStr, File outputDirectory) {

    //File kludge = new File("/tmp/logtkanalysis.txt")
    //kludge.text = ""
    if (debug > 0) {
      System.err.println "Orca:generate:  input ${inputFile}"
    }
    File outFile = new File(outputDirectory, digitalExemplarName)
    File idxFile = new File(outputDirectory, exemplarTtlName)

    Integer count = 0  
    Integer prevCount = 0
    Integer prevPrevCount = -1
    String prevText = ""
    String prevUrn = ""
    String prevSrc = ""

    boolean sawEndBlock = false
    String currUrnBase = ""
    inputFile.getText(charEnc).eachLine { l ->
      def cols = l.split("${separatorStr}")
      CtsUrn urn
      try {
	urn = new CtsUrn(cols[1])
      } catch (Exception e) {
	System.err.println "Orca: omitting data line ${l} from source edition."
      }

      if (urn) {
	this.srcUrnName = urn.getUrnWithoutPassage()
	String baseUrn = this.getUrnName() + ":" + urn.getPassageNode()
	if (debug > 1) {
	  //kludge.append( "Set baseUrn to ${baseUrn} at prevText ${prevText}\n", "UTF-8")
	}


	if (sawEndBlock) {
	  if ( prevUrn != "") {
	    //kludge.append ("Appending EOL\n", "UTF-8")
	    outFile.append(formatLine(prevUrn, "${prevPrevCount}", "${prevCount}", "${count}", endBlockMarker), charEnc)
	    prevPrevCount++;
	  }
	  prevUrn = baseUrn
	  
	  count++;
	  prevCount++;
	  sawEndBlock = false
	}
	
	if (baseUrn != prevUrn) {
	  if (debug > 0) {
	    System.err.println "Base URN ${baseUrn} differs from previous ${prevUrn}\n"
	    //kludge.append("Base URN ${baseUrn} differs from previous ${prevUrn} at prev text ${prevText} \n", "UTF-8")
	  }
	  sawEndBlock = true
	}
	

	if (count > 1) {
	  idxFile.append("<${prevSrc}> hmt:analyzesAs <${prevUrn}.${prevCount}> .\n")
	  idxFile.append("<${prevUrn}.${prevCount}> hmt:analyzedFrom <${prevSrc}>.\n")

	  if (prevPrevCount == 0) {
	    //kludge.append ("Appending ${prevText}\n", "UTF-8")
	    outFile.append(formatLine(prevUrn, nullCol, "${prevCount}", "${count}", prevText), charEnc)
	  } else {
	    //kludge.append ("Appending ${prevText}\n", "UTF-8")
	    outFile.append(formatLine(prevUrn, "${prevPrevCount}", "${prevCount}", "${count}", prevText), charEnc)
	  }
	}
	
	prevPrevCount = prevCount;
	prevCount = count;
	count++;

	prevText = urn.getSubref1()
	prevSrc = this.srcUrnName + ":" + urn.getPassageComponent()
      } 
    }
    
    idxFile.append("<${prevSrc}> hmt:analyzesAs <${prevUrn}.${prevCount}> .\n")
    idxFile.append("<${prevUrn}.${prevCount}> hmt:analyzedFrom <${prevSrc}> .\n")

    //kludge.append ("Tack on ${prevText} followed by EOL\n", "UTF-8")      


    outFile.append(formatLine(prevUrn, "${prevPrevCount}", "${prevCount}", "${count}", prevText), charEnc)
    outFile.append(formatLine(prevUrn, "${prevCount}", "${count}", nullCol, endBlockMarker), charEnc)
  }

  
}