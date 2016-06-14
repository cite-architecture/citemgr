package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseMgrRetrieveByTbs {

  String surf = "urn:cite:hmt:msA.12v"

  @Test
  void testRetrieveImage () {
    def surfaceToImageFiles = [new File("testdata/il17/indices/venA-folioToImage.csv")]
    DseManager dsem = new DseManager()
    dsem.mapSurfaceToImageFromCsv(surfaceToImageFiles)
    Integer expectedMappings = 654
    assert dsem.surfaceToImageMap.size() == expectedMappings

    String expectedImg = "urn:cite:hmt:vaimg.VA012VN-0514"
    assert dsem.imageForSurface(surf) == expectedImg
  }

  @Test
  void testRetrieveText () {
    def textToSurfaceFiles = [new File("testdata/il17/indices/venA-textToFolio-Iliad.csv")]
    DseManager dsem = new DseManager()

    dsem.mapSurfaceToTextFromCsv(textToSurfaceFiles)
    Integer numExpectedNodes = 15661
    assert dsem.textToSurfaceMap.size() == numExpectedNodes

    Set expectedNodes =    ["urn:cts:greekLit:tlg0012.tlg001.msA:1.26", "urn:cts:greekLit:tlg0012.tlg001.msA:1.27", "urn:cts:greekLit:tlg0012.tlg001.msA:1.28", "urn:cts:greekLit:tlg0012.tlg001.msA:1.29", "urn:cts:greekLit:tlg0012.tlg001.msA:1.30", "urn:cts:greekLit:tlg0012.tlg001.msA:1.31", "urn:cts:greekLit:tlg0012.tlg001.msA:1.32", "urn:cts:greekLit:tlg0012.tlg001.msA:1.33", "urn:cts:greekLit:tlg0012.tlg001.msA:1.34", "urn:cts:greekLit:tlg0012.tlg001.msA:1.35", "urn:cts:greekLit:tlg0012.tlg001.msA:1.36", "urn:cts:greekLit:tlg0012.tlg001.msA:1.37", "urn:cts:greekLit:tlg0012.tlg001.msA:1.38", "urn:cts:greekLit:tlg0012.tlg001.msA:1.39", "urn:cts:greekLit:tlg0012.tlg001.msA:1.40", "urn:cts:greekLit:tlg0012.tlg001.msA:1.41", "urn:cts:greekLit:tlg0012.tlg001.msA:1.42", "urn:cts:greekLit:tlg0012.tlg001.msA:1.43", "urn:cts:greekLit:tlg0012.tlg001.msA:1.44", "urn:cts:greekLit:tlg0012.tlg001.msA:1.45", "urn:cts:greekLit:tlg0012.tlg001.msA:1.46", "urn:cts:greekLit:tlg0012.tlg001.msA:1.47", "urn:cts:greekLit:tlg0012.tlg001.msA:1.48", "urn:cts:greekLit:tlg0012.tlg001.msA:1.49", "urn:cts:greekLit:tlg0012.tlg001.msA:1.50"]
    assert expectedNodes == dsem.textsForSurface(surf) as Set
  }


}
