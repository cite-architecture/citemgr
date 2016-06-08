package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestDseMgrImgMap {




  def textToImageFiles = [new File("testdata/venA17-textToImage-Iliad.csv")]
  def testImage = "urn:cite:hmt:vaimg.VA223VN-0725"
  CtsUrn txtUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")

  @Test
  void testImageMapForText() {

    //def testFolio = "urn:cite:hmt:msA.223v"

    //def surfaceToImageFiles = [new File("testdata/tinyvalid/venA17-surfaceToImage.csv")]

    DseManager dsem = new DseManager()
    dsem.textImageIndexFiles = textToImageFiles

    def mappedTexts = dsem.imageMapsByText(new CiteUrn(testImage))

    def textList = mappedTexts.keySet()
    assert textList.size() == 1
    assert textList[0] == "urn:cts:greekLit:tlg0012.tlg001.msA:"
    def vaChunks = mappedTexts[textList[0]]
    assert vaChunks.size() == 25
  }



}
