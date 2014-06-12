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

  @Test void testTextImageIndex() {
    DseManager dsemgr = new DseManager()

    dsemgr.textTbsIndexFiles = indexFiles
    def matches = dsemgr.textNodesForSurface(folio)
    Integer expectedSize = 50
    assert matches.size() == expectedSize
  }

}
