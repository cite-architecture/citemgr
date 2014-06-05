package org.homermultitext.citemanager


import edu.harvard.chs.cite.TextInventory
import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

import edu.holycross.shot.hocuspocus.Corpus
import edu.holycross.shot.prestochango.CollectionArchive



/** A class for managing a standard Digital Scholarly Editions archive.
 */
class DseManager {

  Integer debug = 1



  ArrayList tbsImageIndexFiles
  ArrayList textImageIndexFiles
  ArrayList textTbsIndexFiles
  
  Corpus ctsCorpus

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


  /** Performs completes DSE validation for a given
   * text-bearing surface. That means
   * all three edges of the DSE triangle:
   *
   * 1. image to TBS
   * 2. text to TBS
   * 3. text to image
   * 
   * @param urn The text-bearing surface to validate.
   * @returns True if all tests pass.
   */
  boolean verifyTbs(CiteUrn urn) {
    boolean valid = true

    // Test that one default image is indexed to TBS:
    CiteUrn img = imageForTbs(urn)
    if (! img) {
      valid = false
    }

    // verify text nodes for image by:

    // collect all text nodes for image
    def txtNodesForImage = this.textNodesForImage(img)
    if (debug > 0) {
      System.err.println "Text for Image:" + txtNodesForImage
    }


    // collect all text nodes for TBS
    // should be set-identical



    // verify text nodes for tbs
    return valid
  }



  //////////////// TRIO OF METHODS FOR TEXT -> SURFACE ////////////////

  ArrayList textNodesForSurface(String imgStr, File indexFile)   
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(imgStr)
      return textNodesForSurface(u, indexFile)

    } catch (Exception e) {
      throw e
    }
  }




  //////////////// TRIO OF METHODS FOR TEXT -> IMAGE ////////////////


  ArrayList textNodesForImage(String imgStr) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(imgStr)
      return textNodesForImage(u)

    } catch (Exception e) {
      throw e
    }
  }

  ArrayList textNodesForImage(CiteUrn urn) {
    // cycle all index files, and invoke
    // textNodesForImage with file
    System.err.println "Find image " + urn 
    System.err.println "DseManager:textNodesForImage: using textImage files ${this.textImageIndexFiles}"
    if ((! this.textImageIndexFiles) || (this.textImageIndexFiles.size() == 0)) {
      throw new Exception ("DseManager:texNodesForImage: no index files configured.")
    }



    def nodeList = []

    this.textImageIndexFiles.each { f ->
      def singleList = this.textNodesForImage(urn, f)
      if (debug > 0) {
	System.err.println "DseManager:textNodesForImage: ${urn} in ${f} yielded:"
	System.err.println "\t" + singleList
      }
      singleList.each { 
	nodeList.add(it)
      }
    }

    return nodeList  
  }



  ArrayList textNodesForImage(String imgStr, File indexFile) 
  throws Exception {
    try {
      CiteUrn u = new CiteUrn(imgStr)
      return textNodesForImage(u, indexFile)

    } catch (Exception e) {
      throw e
    }
  }

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