package org.homermultitext.citemanager

import static org.junit.Assert.*
import org.junit.Test

class TestOrca extends GroovyTestCase {


  File orcaData = new File("testdata/gen44.csv")
  File outDir = new File("testdata/output")
  String separator = "#"

  @Test void testOrca() {
    outDir.deleteDir()
    outDir.mkdir()

    Orca orca = new Orca()
    try {
      orca.generate(orcaData,separator,outDir)    
    } catch (Exception e) {
      File capture = new File("/tmp/oracTestBomb.txt")
      capture.setText(e.toString(), "UTF-8")
    }
    
  }

}
