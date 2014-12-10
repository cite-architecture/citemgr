package org.homermultitext.citemanager


import edu.harvard.chs.cite.TextInventory
import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

import edu.holycross.shot.hocuspocus.Corpus
import edu.holycross.shot.hocuspocus.TabUtil
import edu.holycross.shot.prestochango.CollectionArchive

import au.com.bytecode.opencsv.CSVReader


/** A class for managing a standard Digital Scholarly Editions archive.
 */
class DseManager {

  public Integer debug = 0


  /** List of files indexing text-bearing surfaces to images. */
  public ArrayList tbsImageIndexFiles = new ArrayList()
  /** List of files indexing text nodes to images. */
  public ArrayList textImageIndexFiles = new ArrayList()
  /** List of files indexing text nodes to text-bearing surfaces. */
  public ArrayList textTbsIndexFiles = new ArrayList()

  /** Xsl stylesheet for formatting web page from XML
   * expressing a CITE graph
   **/
  String invXsl = "xslt/dse_inventory.xsl"


  /** Empty constructor */
  DseManager()   {
  }





  
  //////////////// METHODS FOR GETTING TABULAR DATA FROM DSE RELATIONS ////////////////
  

  /* USE THIS:
   * @returns A list of CTS URN values.
   * @throws Exception if artifactStr is not a valid CiteUrn.
   ArrayList textNodesForSurface(String artifactStr) 
  */
  
  ArrayList tabDataForSurface(String artifactStr, Corpus corpus, File tabDir)  {
    def txtNodesForSurface = this.textNodesForSurface(artifactStr)
    corpus.tabulateRepository(tabDir)
    def ctsUrns = this.textNodesForSurface(artifactStr)
    TabUtil tab = new TabUtil()
    if (debug > 0) { println "tabDataForSurface:  urns: " + ctsUrns }
    return tab.tabEntriesForDirectory(tabDir, ctsUrns)    
  }

  





  /** Creates the CITE graph representation of all relations
   * indexed to the default image of given text-bearing surface.
   * @param urnStr CITE URN of a text-bearing surface, as a String value
   * @returns CITE graph XML, with link to XSL for human 
   * reading as a web page.
   * @throws Exception if DSE relations are not configured, or if
   * urnStr is not a valid CITE URN.
   */
  String getVisualInventoryXml (String urnStr) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(urnStr)
      return getVisualInventoryXml(u)
    } catch (Exception e) {
      throw e
    }
   }


  /** Creates the CITE graph representation of all relations
   * indexed to the default image of given text-bearing surface.
   * @param tbsUrn CITE URN of a text-bearing surface.
   * @returns CITE graph XML, with link to XSL for human 
   * reading as a web page.
   * @throws Exception if DSE relations are not configured.
   */
  String getVisualInventoryXml (CiteUrn tbsUrn)  
  throws Exception {
    CiteUrn defaultImageUrn = this.imageForTbs(tbsUrn)
    def imgMaps = imageMapsByText(defaultImageUrn)

    String verb = "http://www.homermultitext.org/cite/rdf/illustrates"


    def xml = new groovy.xml.StreamingMarkupBuilder().bind {
      mkp.declareNamespace('':'http://chs.harvard.edu/xmlns/citeindex')
      mkp.pi("xml-stylesheet": "type='text/xsl' href='" + invXsl + "'" )

      citegraph {
	request {
	  urn("${defaultImageUrn}")
	  sparqlEndPoint("locallyEditedData")
	}
	reply {
	  graph(urn: "${defaultImageUrn}") {

	    imgMaps.keySet().each { txt ->
	      sequence {
		label ("${txt}")
		value {
		  
		  def imgMapping = imgMaps[txt]
		  imgMapping.each { img ->
		    node (type: "text", s : "${img[1]}", v : "${verb}") {
			label("${img[0]}")
			value("${img[0]}")
		      }
		  }
		}
	      }
	    }
	  }
	}
      }
    }
    return xml.toString()
  }


  /** Cycles through all files in textImageIndexFiles, and creates 
   * map of text passages indexed to a given image.
   * @param urnStr URN of the image to map, as a String.
   * @returns The map expressed as a CITE graph in
   * XML.
   * @throws Exception if urnStr is not a valid URN.
   */
  LinkedHashMap imageMapsByText(String urnStr) 
  throws Exception {
    if (debug > 0)  { System.err.println ("Get maps for urn with string val " + urnStr) }
    try {
      CiteUrn u = new CiteUrn(urnStr)
      return imageMapsByText(u)

    } catch (Exception e) {
      throw e
    }
  }

  /** Cycles through all files in textImageIndexFiles, and creates 
   * map of text passages indexed to a given image.
   * @param img The image to map.
   * @returns The map expressed as a CITE graph in XML.
   */
  LinkedHashMap imageMapsByText(CiteUrn img) {
    if (debug > 0) {    System.err.println("Find mappings for " + img)}

    
    if ((! this.textImageIndexFiles) || (this.textImageIndexFiles.size() == 0)) {
      throw new Exception ("DseManager:imageMapsByText: no index files configured.")
    }
    def nodeMap = [:]
    // cycle all index files, and invoke textNodesForImage with file
    this.textImageIndexFiles.each { f ->
      if (debug > 0) {
	System.err.println "DseMgr:imageMapsByText: examine " + f
      }
      def singleMap = this.imageMapsByText(img, f)
      if (debug > 0) {
	System.err.println "DseMgr:imageMapsByText: got map " + singleMap
      }

      nodeMap << singleMap
      if (debug > 0) {
	println "file ${f}: map:"
	println singleMap
	println "node map now : " 
	println "${nodeMap}\n\n"
      }
    }
    return nodeMap
  }




  /** Creates a map of texts indexed to a given image in a given
   * index file.
   * @param imgStr The URN of the image to map, as a String value.
   * @param indexFile The index file to use.
   * @returns The map expressed as a CITE graph in XML.
   * @throws Exception if imgStr is not a valid CITE URN.
   */
  LinkedHashMap imageMapsByText(String imgStr, File indexFile) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(imgStr)
      return imageMapsByText(u, indexFile)

    } catch (Exception e) {
      throw e
    }
  }

  /** Creates a map of texts indexed to a given image in a given
   * index file.
   * @param img The image to map.
   * @param indexFile The index file to use.
   * @returns The map expressed as a CITE graph in XML.
   */
  LinkedHashMap imageMapsByText(CiteUrn img, File indexFile) {
    def results = [:]
    if (debug > 1) {
      System.err.println("DseMgr:imageMapsByText for file " + indexFile)
      System.err.println("Its text contens = " + indexFile.readLines().size() + " lines.")
    }
    
    if ( !indexFile.getName() ==~ /.+csv/) {
      System.err.println "Only dealing with csv:  no match for " + indexFile
    } else {
      CSVReader reader = new CSVReader(new FileReader(indexFile))
      def things = reader.readAll()
      if (debug > 0) {
	System.err.println "imageMapsByText: from ${indexFile}, read " + things.size() + " entries"
      }

      things.each { ln ->
	if (debug > 0) {println "Line " + ln }
	// allow for incomplete entries...
	if (ln.size() == 2) {
	  String imgStr = ln[1]
	  if (imgStr ==~ /${img}@.+$/) {
	    String doc = new CtsUrn(ln[0]).getUrnWithoutPassage()
	    def imgGroup = []
	    if (results[doc]) {
	      imgGroup = results[doc]
	    }

	    imgGroup.add([ln[0],ln[1]])
	    results[doc] = imgGroup
	  }
	}
      }
      //} else if (indexFile.toString() ==~ /.+tsv/) {
      // implement tsv reading
      //} else {
    }
    return results
  }

  /** Performs DSE validation for a given
   * text-bearing surface. Checks for referntial integrity
   * across the three edges of the DSE triangle:
   *
   * 1. image to TBS
   * 2. text to TBS
   * 3. text to image
   * 
   * Tests currently verify that a single default image is
   * indexed to TBS, that an identical set of text nodes are indexed
   * to TBS and the default image.
   *
   * @param urnStr URN of the text-bearing surface to validate, as a String value.
   * @returns True if all tests pass.
   */
  boolean verifyTbs(String urnStr) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(urnStr)
      return verifyTbs(u)

    } catch (Exception e) {
      throw e
    }
  }


  /** Performs DSE validation for a given
   * text-bearing surface. Checks for referntial integrity
   * across the three edges of the DSE triangle:
   *
   * 1. image to TBS
   * 2. text to TBS
   * 3. text to image
   * 
   * Tests currently verify that a single default image is
   * indexed to TBS, that an identical set of text nodes are indexed
   * to TBS and the default image.
   *
   * @param urn The text-bearing surface to validate.
   * @returns True if all tests pass.
   */
  boolean verifyTbs(CiteUrn urn) {
    boolean valid = true

    // 1. Test that one default image is indexed to TBS:
    CiteUrn img = imageForTbs(urn)
    if (! img) {
      valid = false
    }

    // 2. Verify text nodes for image by:
    // A. collect all text nodes for image
    def txtNodesForImage = this.textNodesForImage(img)
    if (debug > 0) {
      System.err.println "Text for Image:" + txtNodesForImage
    }
    // B. collect all text nodes for TBS
    def txtNodesForSurface = this.textNodesForSurface(urn)

    // should be set-identical
    if (txtNodesForSurface as Set != txtNodesForImage as Set) {
     valid = false
    }

    return valid
  }


  // report on a single page
  def dseReport(CiteUrn urn) {
    def tbsToImgReport = []
    
    CiteUrn img = imageForTbs(urn)
    if (! img) {
      tbsToImgReport = [false, null]
    } else {
      tbsToImgReport = [true, img.toString()]
    }

    def txtNodesForImage = this.textNodesForImage(img)
    if (debug > 0) {
      System.err.println "Text for Image:" + txtNodesForImage
    }
    // B. collect all text nodes for TBS
    def txtNodesForSurface = this.textNodesForSurface(urn)
    boolean validMapping = false
    String cf
    // should be set-identical
    def surfSet = txtNodesForSurface as Set
    def   imgSet  = txtNodesForImage as Set
    if (debug > 0) {
      System.err.println "\n\n-->Num. texts mapped to surface: " + txtNodesForSurface.size()
      System.err.println "-->Num. texts mapped to image: " + txtNodesForImage.size() + "\n\n"
    }
    if ( surfSet == imgSet) {
     validMapping = true

     
     

     cf = "For image ${img}, ${txtNodesForImage.size()} text units match ${txtNodesForSurface.size()} text units for surface ${urn}."
    } else {

      def disjunctSet = (surfSet + imgSet) - surfSet.intersect(imgSet)
      cf = "Found the following discrepancies between ${imgSet.size()} entries for image ${img} and ${surfSet.size()} entries for surface ${urn}:  ${disjunctSet}"
      System.err.println "DseManager:DseRept: mismatch between mappings"
      System.err.println cf
    }

    def mappingReport = [validMapping, cf]

    def report = [tbsToImgReport, mappingReport]
    return report    
  }

  //////////////// TRIO OF METHODS FOR TEXT -> SURFACE INDEX ////////////////



  /** Searches all index files for text nodes appearing on
   * a requested physical surface.
   * @param artifactStr URN value, as a String, of the physical surfae.
   * @returns A list of CTS URN values.
   * @throws Exception if artifactStr is not a valid CiteUrn.
   */
  ArrayList textNodesForSurface(String artifactStr) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(artifactStr)
      return textNodesForSurface(u)

    } catch (Exception e) {
      throw e
    }
  }



  /** Searches all index files for text nodes appearing on
   * a requested physical surface.
   * @param urn URN of the physical surface.
   * @returns A list of CTS URN values.
   * @throws Exception if index files are not configured.
   */
  ArrayList textNodesForSurface(CiteUrn urn)  
  throws Exception {

    if ((! this.textTbsIndexFiles) || (this.textTbsIndexFiles.size() == 0)) {
      throw new Exception ("DseManager:textNodesForSurface: no index files configured.")
    }

    def nodeList = []
    // cycle all index files, and invoke textNodesForImage with file
    this.textTbsIndexFiles.each { f ->
      def singleList = this.textNodesForSurface(urn, f)
      singleList.each { 
	nodeList.add(it)
      }
    }
    return nodeList  
  }


  /** Searches a given index file for text nodes appearing on
   * a requested physical surface.
   * @param artifactStr URN of the surface, as a String.
   * @param indexFile The index file to search.
   * @returns A list of CTS URN values.
   * @throws Exception if artifactStr is not a valid CITE URN.
   */
  ArrayList textNodesForSurface(String artifactStr, File indexFile)   
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(artifactStr)
      return textNodesForSurface(u, indexFile)

    } catch (Exception e) {
      throw e
    }
  }




  /** Searches a given index file for text nodes appearing on
   * a requested physical surface.
   * @param urn URN of the surface, as a String.
   * @param indexFile The index file to search.
   * @returns A list of CTS URN values.
   */
  ArrayList textNodesForSurface(CiteUrn urn, File indexFile) {

    def results = []
    def indexRecord =  indexFile.readLines().grep( ~/^.+,"?${urn}"?$/ )

    //     def indexRecord =  indexFile.readLines().grep( ~/^.+${img}@.+$/ ) 
    if (debug > 0) {
      System.err.println "textNodesForSurface: file ${indexFile}, grepped " + indexRecord + " from " + urn
    }

    indexRecord.each { ln ->
      String urnStr = ln.replaceFirst(/,.+/, '').replaceAll(/"/,'')
      if (debug > 0) {System.err.println "textNodesForSurface:  parse urnStr " + urnStr}
      try {
	CtsUrn psg = new CtsUrn(urnStr)
	results.add(urnStr)
      } catch (Exception e) {
	System.err.println "DseManager:textNodesForSurface: badly formed CTS URN ${urnStr}"
      }
    }
    return results

  }





  //////////////// TRIO OF METHODS FOR TEXT -> IMAGE INDEX ////////////////



  /** Searches all index files for text nodes appearing in
   * a requested image.
   * @param imgStr URN value, as a String, of the image.
   * @returns A list of CTS URN values.
   * @throws Exception if imgStr is not a valid CiteUrn.
   */
  ArrayList textNodesForImage(String imgStr) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(imgStr)
      return textNodesForImage(u)

    } catch (Exception e) {
      throw e
    }
  }


  /** Searches all index files for text nodes appearing in
   * a requested image.
   * @param urn URN of the image.
   * @returns A list of CTS URN values.
   * @throws Exception if indexes not configured.
   */
  ArrayList textNodesForImage(CiteUrn urn) 
  throws Exception {
    if ((! this.textImageIndexFiles) || (this.textImageIndexFiles.size() == 0)) {
      throw new Exception ("DseManager:texNodesForImage: no index files configured.")
    }

    def nodeList = []
    // cycle all index files, and invoke textNodesForImage with file
    this.textImageIndexFiles.each { f ->
      def singleList = this.textNodesForImage(urn, f)
      singleList.each { 
	nodeList.add(it)
      }
    }
    return nodeList  
  }





  /** Searches a given index files for text nodes appearing in
   * a requested image.
   * @param imgStr URN of the image, as a String value.
   * @param indexFile The index file to search.
   * @returns A list of CTS URN values.
   * @throws Exception if indexes not configured.
   */
  ArrayList textNodesForImage(String imgStr, File indexFile) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(imgStr)
      return textNodesForImage(u, indexFile)

    } catch (Exception e) {
      throw e
    }
  }




  /** Searches a given index files for text nodes appearing in
   * a requested image.
   * @param img URN of the image.
   * @param indexFile The index file to search.
   * @returns A list of CTS URN values.
   */
  ArrayList textNodesForImage(CiteUrn img, File indexFile) {
    def results = []
    if (debug > 0) { System.err.println "textNodesForImage: grep in ${indexFile} for ${img}" }
    def indexRecord =  indexFile.readLines().grep( ~/^.+${img}@.+$/ ) 
    indexRecord.each { ln ->
      String urnStr = ln.replaceFirst(/,.+/, '').replaceAll(/"/,'')

      if (debug > 0) {System.err.println "textNodesForImage:  parse urnStr " + urnStr}

      try {
	CtsUrn urn = new CtsUrn(urnStr)
	results.add(urnStr)
      } catch (Exception e) {
	System.err.println "DseManager:textNodesForImage: badly formed CTS URN ${urnStr}"
      }
    }
    return results
  }



  //////////////// TRIO OF METHODS FOR IMAGE -> SURFACE INDEX ////////////////


  /** Searches all index files for a default image
   * for a requested text-bearing surface.
   * @param urnStr URN value, as a String, of the text-bearing surface to look for.
   * @returns The URN of the default image, or null if none found.
   * @throws Exception if indexes not configured.
   */
  CiteUrn imageForTbs(String urnStr) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(urnStr)
      return imageForTbs(u)
    } catch (Exception e) {
      throw e
    }
  }


  /** Searches all index files for a default image
   * for a requested text-bearing surface.
   * @param urn The text-bearing surface to look for.
   * @returns The URN of the default image, or null if none found.
   * @throws Exception if indexes not configured.
   */
  CiteUrn imageForTbs(CiteUrn urn) 
  throws Exception {
    
    if ((! this.tbsImageIndexFiles) || (this.tbsImageIndexFiles.size() == 0)) {
      throw new Exception ("DseManager:imageForTbs: no index files configured.")
    }

    CiteUrn defaultImage = null
    this.tbsImageIndexFiles.each { f ->
      if (debug > 1) {
	System.err.println "DseMgr:imageForTbs: examine file " + f + " of class " + f.getClass()
      }
      CiteUrn imgUrn = this.imageForTbs(urn,f)
      if (imgUrn != null) {
       defaultImage = imgUrn
      }
    }
    return defaultImage  
  }


  /** Looks in an index file for a default image for a requested
   * text-bearing surface.
   * @param urn URN value for the text-bearing surface, as a String.
   * @returns A CITE URN identifying the default image.
   * @throws Exception if urnStr is not a valid CITE URN.
   */
  CiteUrn imageForTbs(String urnStr, File indexFile) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(urnStr)
      return imageForTbs(u, indexFile)
    } catch (Exception e) {
      throw e
    }
  }



  /** Looks in an index ffile for a default image for a requested
   * text-bearing surface.
   * @param urn The text-bearing surface.
   * @returns A CITE URN identifying the default image, or null
   * if none found.
   * @throws Exception if no index files defined, or
   * if multiple default images found.
   */
  CiteUrn imageForTbs(CiteUrn urn, File indexFile) 
  throws Exception {
    def lines = indexFile.readLines()
    def indexRecord =  lines.grep( ~/^.*${urn}"?,.+$/ ) 
    if (debug > 1) {
      System.err.println "Grepping for ${urn} yielded " + indexRecord + " of size " + indexRecord.size()
      }


    switch (indexRecord.size()) {
    case 0:

    return null
    break

    case 1:
    String urnStr = indexRecord[0].replaceAll(/[^,]+,/,"").replaceAll(/"/,"")
    CiteUrn imgUrn = new CiteUrn(urnStr)
    return imgUrn
    break
    

    default:
    throw new Exception("DseManager:imageForTbs: found more than one default image for ${urn} in indexFile.")
    break
    }

  }


 

}