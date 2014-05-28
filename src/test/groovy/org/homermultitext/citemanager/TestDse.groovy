package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

class TestDse extends GroovyTestCase {


  File dataDir = new File("testdata/dse")
  File imgTbsIndex = new File(dataDir,"folioToOverviewImage.csv")
  

  @Test void testDse() {
    DseManager dsemgr = new DseManager()
    assert dsemgr
  }

  @Test void testImgForTbs() {
    DseManager dsemgr = new DseManager()
    ArrayList indexFiles = [imgTbsIndex]
    dsemgr.indexFiles = indexFiles
    String expectedFile = "testdata/dse/folioToOverviewImage.csv"
    assert expectedFile == dsemgr.indexFiles[0] as String
  }


}
