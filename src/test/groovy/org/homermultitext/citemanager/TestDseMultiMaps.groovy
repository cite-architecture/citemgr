package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseMultiMaps extends GroovyTestCase {

  File dataDir = new File("testdata/dse2")


  CiteUrn folio = new CiteUrn ("urn:cite:hmt:msA.137v")
  CiteUrn img = new CiteUrn("urn:cite:hmt:vaimg.VA137VN-0639")


  Integer expectedSize = 50

  @Test void testTextIndices() {
    DseManager dsemgr = new DseManager()
    dsemgr.debug = 5

    File iliadImgIndex = new File(dataDir,"iliadToFolio.csv")
    File scholiaImgIndex = new File(dataDir,"scholiaToTbs.csv")

    dsemgr.textTbsIndexFiles = [scholiaImgIndex, iliadImgIndex] 

    def folioMatches = dsemgr.textNodesForSurface(folio)
    assert folioMatches.size() == expectedSize

    dsemgr.textImageIndexFiles = 
    [
      new File(dataDir, "scholiaToImage.csv"),
      new File(dataDir, "venA-textToImage-Iliad.csv")
    ]

    def imgMatches =    dsemgr.textNodesForImage(img)
    assert imgMatches.size() == expectedSize


    assert imgMatches as Set == folioMatches as Set
  }


  @Test void testMore() {


  }


}
