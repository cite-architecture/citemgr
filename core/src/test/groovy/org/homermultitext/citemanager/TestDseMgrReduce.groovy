package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestDseMgrReduce {

  // Source data in .csv files
  def textToSurfaceFiles = [new File("testdata/il17/indices/venA-textToFolio-Iliad.csv")]
  def textToImageFiles = [new File("testdata/il17/indices/venA-textToImage-Iliad.csv")]
  def surfaceToImageFiles = [new File("testdata/il17/indices/venA-folioToImage.csv")]



  // test signature indexing single CSV file:
  @Test
  void testReduceByTbs() {
    DseManager dsem = new DseManager()
    dsem.mapSurfaceToTextFromCsv(textToSurfaceFiles)
    dsem.mapImageToTextFromCsv(textToImageFiles)
    dsem.mapSurfaceToImageFromCsv(surfaceToImageFiles)
    DseManager folioDse = dsem.reduceByTbs("urn:cite:hmt:msA.223v")
    // surfaceId:urn:cite:hmt:vaimg.VA223VN-0725

  }

}
