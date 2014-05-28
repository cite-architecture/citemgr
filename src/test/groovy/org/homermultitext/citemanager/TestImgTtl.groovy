package org.homermultitext.citemanager



import static org.junit.Assert.*
import org.junit.Test

class TestImgTtl extends GroovyTestCase {

  //File inventorySource = new File("testdata/imgs/inventories")
  // File imgSource = new File("testdata/imgs/imagedata")

    @Test void testImgTtl() {
      assert 1 > 0
      /*
        File outDir = new File("testdata/testout/imgtst.ttl")
        NysiTurtleizer imgttl = new NysiTurtleizer(inventorySource,imgSource,outDir)
        System.err.println "Made turtleizer with ${inventorySource}, ${imgSource}, ${outDir}"
        System.err.println "IMAGE ttl: " + imgttl.generateTurtle()
      */
        // Need to think carefully about how to test equivalence of two TTL
        // files
    }

}
