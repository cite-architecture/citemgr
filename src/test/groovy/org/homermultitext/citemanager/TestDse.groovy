package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



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


    String folioUrnStr = "urn:cite:hmt:u4.193r"
    CiteUrn folioUrn = new CiteUrn(folioUrnStr)


    CiteUrn expectedUrn = new  CiteUrn("urn:cite:hmt:u4img.U4193RN-0395")
    CiteUrn actualUrn = dsemgr.imageForTbs(folioUrn,dsemgr.indexFiles[0])

    assert actualUrn.toString() == expectedUrn.toString()

  }




}
