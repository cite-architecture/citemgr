package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseValidator extends GroovyTestCase {




  File dataDir = new File("testdata/dse")

  File imgTbsIndex = new File(dataDir,"folioToOverviewImage.csv")
  ArrayList tbsImageFiles = [imgTbsIndex]  

  File textImageIndex = new File(dataDir, "linesToImage-4.csv")
  ArrayList textImageFiles = [textImageIndex]


  File textTbsIndex = new File(dataDir, "venAIliad.csv")
  ArrayList textSurfaceFiles = [textTbsIndex]

  @Test void testAllTbsIndices() {
    DseManager dsemgr = new DseManager()
    dsemgr.tbsImageIndexFiles = tbsImageFiles
    dsemgr.textImageIndexFiles = textImageFiles

    dsemgr.textTbsIndexFiles = textSurfaceFiles


    String folioUrnStr = "urn:cite:hmt:msA.53r"
    assert dsemgr.verifyTbs(folioUrnStr)
  }


}
