package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestDseMgrTextImage {

  // Source data in .csv files
  def textToImageFiles = [new File("testdata/il17/indices/venA-textToImage-Iliad.csv")]
  Integer expectedTextNodes = 760
  Integer expectedImages = 31


  // test signature indexing single CSV file:
  @Test
  void testSingleFile() {
    DseManager dsem = new DseManager()
    dsem.mapImageToTextFromCsv(textToImageFiles[0])
    assert dsem.textToImageRoIMap.size() == expectedTextNodes
    assert dsem.imageToTextMap.size() == expectedImages
    dsem.imageToTextMap.each { img, txtList ->
      txtList.each { txt ->
        assert dsem.textToImageRoIMap.keySet().contains(txt)
      }
    }

  }
/*
  // test signature indexing single CSV file:
  @Test
  void testFileListile() {
    DseManager dsem = new DseManager()
    dsem.mapSurfaceToTextFromCsv(textToSurfaceFiles)

    assert dsem.surfaceToTextMap.size() == expectedSurfaces
    assert dsem.textToSurfaceMap.size() == expectedNodes
    assert dsem.surfaceToTextMap.keySet() == dsem.textToSurfaceMap.values() as Set
  }
*/
}
