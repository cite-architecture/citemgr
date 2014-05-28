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

  ArrayList indexFiles
  Corpus ctsCorpus
  CollectionArchive citeColl
  

  DseManager()   {
  }


  /** Finds a default image for a requested
   * text-bearing surface.
   * @param urn URN value for the text-bearing surface, as a String.
   * @returns A CITE URN identifying the default image.
   * @throws Exception if urnStr is not a valid CITE URN.
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


  /** Finds a default image for a requested
   * text-bearing surface.
   * @param urn The text-bearing surface.
   * @returns A CITE URN identifying the default image.
   */
  CiteUrn imageForTbs(CiteUrn urn) {
  }
  
  // find default image for a TBS
  // 


}