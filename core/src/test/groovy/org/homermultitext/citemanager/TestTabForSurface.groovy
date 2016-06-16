package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestTabForSurface{

  // Source data in .csv files
  def textToSurfaceFiles = [new File("testdata/il17/indices/venA-textToFolio-Iliad.csv")]
  def textToImageFiles = [new File("testdata/il17/indices/venA-textToImage-Iliad.csv")]
  def surfaceToImageFiles = [new File("testdata/il17/indices/venA-folioToImage.csv")]


  @Test
  void testTabData() {
    File tabDir = new File("testdata/il17/tabulated")
    File tabFile = new File(tabDir, "tab0.txt")


    String folio = "urn:cite:hmt:msA.223v"
    DseManager dsem = new DseManager()
    dsem.mapSurfaceToTextFromCsv(textToSurfaceFiles)
    dsem.mapImageToTextFromCsv(textToImageFiles)
    dsem.mapSurfaceToImageFromCsv(surfaceToImageFiles)

/*    def tabdata = dsem.tabDataForSurface(folio, tabFile)
    println "Tab data: " + tabdata.size() + " entries."
    println "First is " + tabdata[0]
    println "Second is " + tabdata[1]
*/
    def dirdata = dsem.tabDataForSurfaceInDirectory(folio, tabDir)
    println "Dir data: " + dirdata.size() + " entries."
    println "First is " + dirdata[0]
    println "Second is " + dirdata[1]
  }




}
