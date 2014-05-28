package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseValidator extends GroovyTestCase {


  File dataDir = new File("testdata/dse")
  File imgTbsIndex = new File(dataDir,"folioToOverviewImage.csv")
  ArrayList indexFiles = [imgTbsIndex]  

  @Test void testAllTbsIndices() {
    DseManager dsemgr = new DseManager()
    dsemgr.tbsImageIndexFiles = indexFiles
    
    String folioUrnStr = "urn:cite:hmt:u4.193r"
    assert dsemgr.verifyTbs(folioUrnStr)
  }


}
