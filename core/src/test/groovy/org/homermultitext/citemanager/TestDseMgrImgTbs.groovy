package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestDseMgrImgTbs {






  @Test
  void testImageSurfaceRelation() {
    def testImage = "urn:cite:hmt:vaimg.VA223VN-0725"
    def testFolio = "urn:cite:hmt:msA.223v"

    def surfaceToImageFiles = [new File("testdata/tinyvalid/venA17-surfaceToImage.csv")]
    DseManager dsem = new DseManager()
    dsem.tbsImageIndexFiles = surfaceToImageFiles

    assert dsem.imageForTbs(testFolio).toString() == testImage.toString()
  }


}
