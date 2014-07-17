package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseTextForTbs extends GroovyTestCase {


  File dataDir = new File("testdata/dse")
  File txtTbsIndex = new File(dataDir,"venAIliad.csv")
  ArrayList textTbsFiles = [txtTbsIndex]  

  @Test void testTextForTbs() {
    DseManager dsemgr = new DseManager()
    dsemgr.textTbsIndexFiles = textTbsFiles
    String folio = "urn:cite:hmt:msA.225r"
    def linesList = dsemgr.textNodesForSurface(folio, dsemgr.textTbsIndexFiles[0])

    Integer expectedSize = 25
    assert linesList.size() == expectedSize
    linesList.each {
      System.err.println "Class of line: " + it.getClass()
    }
  }



  @Test void testAllTextSurfaceIndices() {
    DseManager dsemgr = new DseManager()
    dsemgr.textTbsIndexFiles = textTbsFiles

    String folio = "urn:cite:hmt:msA.225r"
    def txtList = dsemgr.textNodesForSurface(folio)

    Integer expectedSize = 25
    assert txtList.size() == expectedSize
    
  }




}
