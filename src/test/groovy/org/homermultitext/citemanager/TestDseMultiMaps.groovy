package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseMultiMaps extends GroovyTestCase {

  File dataDir = new File("testdata/dse2")

  File iliadImgIndex = new File(dataDir,"iliadToFolio.csv")
  File scholiaImgIndex = new File(dataDir,"scholiaToTbs.csv")
  ArrayList indexFiles = [scholiaImgIndex, iliadImgIndex] 

  CiteUrn folio = new CiteUrn ("urn:cite:hmt:msA.137v")
  CiteUrn img = new CiteUrn("urn:cite:hmt:vaimg.VA137VN-0639")


  Integer expectedSize = 50

  @Test void testTextTbsIndex() {
    DseManager dsemgr = new DseManager()

    dsemgr.textTbsIndexFiles = indexFiles
    def matches = dsemgr.textNodesForSurface(folio)

    assert matches.size() == expectedSize
  }


  @Test void testTextImageIndex() {
    DseManager dsemgr = new DseManager()
    dsemgr.debug = 5

    dsemgr.textImageIndexFiles = 
    [
      new File(dataDir, "scholiaToImage.csv"),
      new File(dataDir, "venA-textToImage-Iliad.csv")
    ]

    def matches =    dsemgr.textNodesForImage(img)
    assert matches.size() == expectedSize    
  }


}
