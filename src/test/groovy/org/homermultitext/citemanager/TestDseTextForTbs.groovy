package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseTextForTbs extends GroovyTestCase {


  File dataDir = new File("testdata/dse")
  File imgTbsIndex = new File(dataDir,"linesToImage-4.csv")
  ArrayList indexFiles = [imgTbsIndex]  

  @Test void testTextForTbs() {
    DseManager dsemgr = new DseManager()
    //    def linesList = dsemgr.textNodesForImage(imgStr, dsemgr.textImageIndexFiles[0])
    //    println linesList
  }


}
