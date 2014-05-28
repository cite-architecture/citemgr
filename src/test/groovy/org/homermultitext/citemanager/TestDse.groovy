package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDse extends GroovyTestCase {


  File dataDir = new File("testdata/dse")
  File imgTbsIndex = new File(dataDir,"folioToOverviewImage.csv")
  ArrayList indexFiles = [imgTbsIndex]  

  @Test void testDse() {
    DseManager dsemgr = new DseManager()
    assert dsemgr
  }

  @Test void testImgForTbs() {
    DseManager dsemgr = new DseManager()
    dsemgr.tbsImageIndexFiles = indexFiles

    String expectedFile = "testdata/dse/folioToOverviewImage.csv"
    assert expectedFile == dsemgr.tbsImageIndexFiles[0] as String


    String folioUrnStr = "urn:cite:hmt:u4.193r"
    CiteUrn folioUrn = new CiteUrn(folioUrnStr)


    CiteUrn expectedUrn = new  CiteUrn("urn:cite:hmt:u4img.U4193RN-0395")
    CiteUrn actualUrn = dsemgr.imageForTbs(folioUrn,dsemgr.tbsImageIndexFiles[0])

    assert actualUrn.toString() == expectedUrn.toString()
    assert expectedUrn.toString() == dsemgr.imageForTbs(folioUrnStr,dsemgr.tbsImageIndexFiles[0]).toString()
  }


  @Test void testAllTbsIndices() {
    DseManager dsemgr = new DseManager()
    dsemgr.tbsImageIndexFiles = indexFiles
    CiteUrn expectedUrn = new  CiteUrn("urn:cite:hmt:u4img.U4193RN-0395")
    
    String folioUrnStr = "urn:cite:hmt:u4.193r"
    CiteUrn actualUrn = dsemgr.imageForTbs(folioUrnStr)

    //    assert actualUrn.toString() == expectedUrn.toString()
  }


}
