package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseImgMaps extends GroovyTestCase {


  File dataDir = new File("testdata/dse")

  File textImgIndex = new File(dataDir,"linesToImage-4.csv")
  ArrayList indexFiles = [textImgIndex] 


  @Test void testTextImageIndex() {
    DseManager dsemgr = new DseManager()
    dsemgr.debug = 10
    dsemgr.textImageIndexFiles = indexFiles
    String imgStr = "urn:cite:hmt:vaimg.VA052RN-0053"  

    //    def linesMap = dsemgr.imageMapsByText(imgStr, dsemgr.textImageIndexFiles[0])

    def linesMap = dsemgr.imageMapsByText(imgStr, textImgIndex)
    assert linesMap.keySet().size() == 1

    String expectedKey = "urn:cts:greekLit:tlg0012.tlg001.msA"
    Integer expectedMappings = 25

    linesMap.keySet().each { k ->
      assert k == expectedKey
      assert linesMap[k].size() == expectedMappings
    }
  }

  /*

  @Test void testAllIndexes() {
    DseManager dsemgr = new DseManager()
    dsemgr.textImageIndexFiles = indexFiles
    String imgStr = "urn:cite:hmt:vaimg.VA052RN-0053"   

    def linesMap = dsemgr.imageMapsByText(imgStr)
    assert linesMap.keySet().size() == 1

    String expectedKey = "urn:cts:greekLit:tlg0012.tlg001.msA:"
    Integer expectedMappings = 25

    linesMap.keySet().each { k ->
      assert k == expectedKey
      assert linesMap[k].size() == expectedMappings
    }

    }*/


}
