package org.homermultitext.citemanager


import edu.harvard.chs.cite.TextInventory
import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

import edu.holycross.shot.hocuspocus.Corpus
import edu.holycross.shot.prestochango.CollectionArchive



/** A class for managing a standard Digital Scholarly Editions archive.
 */
class DseManager {

  Integer debug = 0


  /** List of files indexing text-bearing surfaces to images. */
  ArrayList tbsImageIndexFiles
  /** List of files indexing text nodes to images. */
  ArrayList textImageIndexFiles
  /** List of files indexing text nodes to text-bearing surfaces. */
  ArrayList textTbsIndexFiles


  /** Empty constructor */
  DseManager()   {
  }




  boolean verifyTbs(String urnStr) {
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



  //////////////// TRIO OF METHODS FOR TEXT -> SURFACE ////////////////



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

    def indexRecord =  indexFile.readLines().grep( ~/^.+,${urn}/ ) 
    indexRecord.each { ln ->
      String urnStr = ln.replaceFirst(/,.+/, '')
      try {
	CtsUrn psg = new CtsUrn(urnStr)
	results.add(urnStr)
      } catch (Exception e) {
	System.err.println "DseManager:textNodesForSurface: badly formed CTS URN ${urnStr}"
      }
    }
    return results

  }





  //////////////// TRIO OF METHODS FOR TEXT -> IMAGE ////////////////



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

    def indexRecord =  indexFile.readLines().grep( ~/^.+${img}@.+$/ ) 
    indexRecord.each { ln ->
      String urnStr = ln.replaceFirst(/,.+/, '')
      try {
	CtsUrn urn = new CtsUrn(urnStr)
	results.add(urnStr)
      } catch (Exception e) {
	System.err.println "DseManager:textNodesForImage: badly formed CTS URN ${urnStr}"
      }
    }
    return results
  }



  //////////////// TRIO OF METHODS FOR IMAGE -> SURFACE ////////////////


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
    def indexRecord = lines.grep( ~/^.+${urn}"?,.+$/ ) 

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