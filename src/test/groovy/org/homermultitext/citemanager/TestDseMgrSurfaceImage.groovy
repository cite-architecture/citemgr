package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestDseMgrSurfaceImage{

  // Source data in .csv files
  def surfaceToImageFiles = [new File("testdata/il17/indices/venA-folioToImage.csv")]




  // test signature indexing single CSV file:
  @Test
  void testSingleFile() {
    DseManager dsem = new DseManager()
    Integer expectedMappings = 654

    dsem.mapSurfaceToImageFromCsv(surfaceToImageFiles[0])
    assert dsem.surfaceToImageMap.size() == expectedMappings
    assert dsem.imageToSurfaceMap.size() == expectedMappings

    // Test equivalence of two sets up, down, and sideways:
    assert dsem.surfaceToImageMap.keySet() ==  dsem.imageToSurfaceMap.values() as Set
    dsem.imageToSurfaceMap.each { img, folio ->
      assert dsem.surfaceToImageMap.keySet().contains(folio)
    }

  }


  // test signature indexing list of csv files:
  @Test
  void testFileList() {
    DseManager dsem = new DseManager()
    Integer expectedMappings = 654

    dsem.mapSurfaceToImageFromCsv(surfaceToImageFiles)
    assert dsem.surfaceToImageMap.size() == expectedMappings
    assert dsem.imageToSurfaceMap.size() == expectedMappings

    // Test equivalence of two sets up, down, and sideways:
    assert dsem.surfaceToImageMap.keySet() ==  dsem.imageToSurfaceMap.values() as Set
    dsem.imageToSurfaceMap.each { img, folio ->
      assert dsem.surfaceToImageMap.keySet().contains(folio)
    }

  }

}
