package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseTextForImg extends GroovyTestCase {


  File dataDir = new File("testdata/dse")
  File imgTbsIndex = new File(dataDir,"linesToImage-4.csv")
  ArrayList indexFiles = [imgTbsIndex]  

  @Test void testAllTbsIndices() {
    DseManager dsemgr = new DseManager()
    dsemgr.textImageIndexFiles = indexFiles
    String imgStr = "urn:cite:hmt:vaimg.VA052RN-0053"    

    //    def linesList = dsemgr.textNodesForImage(imgStr, dsemgr.textImageIndexFiles[0])
    //    println linesList

  }


}
