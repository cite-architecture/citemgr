package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestDseMgrSurfaceText{

  // Source data in .csv files
  def textToSurfaceFiles = [new File("testdata/il17/indices/venA-textToFolio-Iliad.csv")]



  Integer expectedNodes = 15661
  Integer expectedSurfaces = 629

  // test signature indexing single CSV file:
  @Test
  void testSingleFile() {
    DseManager dsem = new DseManager()
    dsem.mapSurfaceToTextFromCsv(textToSurfaceFiles[0])

    assert dsem.surfaceToTextMap.size() == expectedSurfaces
    assert dsem.textToSurfaceMap.size() == expectedNodes
    assert dsem.surfaceToTextMap.keySet() == dsem.textToSurfaceMap.values() as Set
  }

  // test signature indexing single CSV file:
  @Test
  void testFileListile() {
    DseManager dsem = new DseManager()
    dsem.mapSurfaceToTextFromCsv(textToSurfaceFiles)

    assert dsem.surfaceToTextMap.size() == expectedSurfaces
    assert dsem.textToSurfaceMap.size() == expectedNodes
    assert dsem.surfaceToTextMap.keySet() == dsem.textToSurfaceMap.values() as Set
  }

}
