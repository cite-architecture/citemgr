package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseRept extends GroovyTestCase {


  File dataDir = new File("testdata/dse/venA")
  def txtImgFiles = ["venA-captions-img.csv", "venA-extraIliadic-img.csv"]
  def txtTbsFiles = ["venA-captions-tbs.csv", "venA-extraIliadic-tbs.csv"]

  String testPage = "urn:cite:hmt:msA.1r"
  
  
  @Test void testTextImageIndex() {
    DseManager dsemgr = new DseManager()
    def txtImgArry = []
    txtImgFiles.each {
      File f = new File(dataDir, it)
      txtImgArry.add(f)
    }
    dsemgr.textImageIndexFiles = txtImgArry
    

    def txtTbsArry = []
    txtTbsFiles.each {
      File f = new File(dataDir, it)
      txtTbsArry.add(f)
    }
    dsemgr.textTbsIndexFiles = txtTbsArry

    def tbsImgArry = [new File(dataDir, "tbsToImg.csv")]
    dsemgr.tbsImageIndexFiles = tbsImgArry


    def rept =  dsemgr.dseReport(new CiteUrn(testPage))
    def imgRept = rept[0]

    System.err.println "Image report: " + imgRept[1]
    System.err.println "Image report: " + imgRept[0]
    assert imgRept[0] == true

    def mapRept = rept[1]
    System.err.println "Mappings: " + mapRept[0]
    System.err.println "Mappings: " + mapRept[1]
    assert mapRept[0] == true
    
  }



}
