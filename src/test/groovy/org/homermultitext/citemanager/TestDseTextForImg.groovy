package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseTextForImg extends GroovyTestCase {


  File dataDir = new File("testdata/dse")
  File imgTbsIndex = new File(dataDir,"linesToImage-4.csv")
  ArrayList indexFiles = [imgTbsIndex]  

  @Test void testTextImageIndex() {
    DseManager dsemgr = new DseManager()
    dsemgr.textImageIndexFiles = indexFiles
    String imgStr = "urn:cite:hmt:vaimg.VA052RN-0053"    

    def linesList = dsemgr.textNodesForImage(imgStr, dsemgr.textImageIndexFiles[0])
    
    def expectedSize = 25
    assert linesList.size() == expectedSize

  }

  @Test void testAllTextImageIndices() {
    DseManager dsemgr = new DseManager()
    dsemgr.textImageIndexFiles = indexFiles

    String expectedFile = "testdata/dse/linesToImage-4.csv"
    assert expectedFile == dsemgr.textImageIndexFiles[0] as String

    String imgUrn = "urn:cite:hmt:vaimg.VA053RN-0054"
    def txtList = dsemgr.textNodesForImage(imgUrn)

    Integer expectedSize = 25
    assert txtList.size() == expectedSize
    
  }



}
