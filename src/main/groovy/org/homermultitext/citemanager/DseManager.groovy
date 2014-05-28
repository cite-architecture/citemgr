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

  
  Corpus ctsCorpus

  DseManager()   {
  }




  boolean verifyTbs(String urnStr) {
  }
  boolean verifyTbs(CiteUrn urn) {
  }




  CiteUrn imageForTbs(String urnStr) {
    try {
      CiteUrn u = new CiteUrn(urnStr)
      return imageForTbs(u)
    } catch (Exception e) {
      throw e
    }
  }


  /** Searches all index files for a default image
   * for a requested text-bearing surface.
   */
  CiteUrn imageForTbs(CiteUrn urn) {
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