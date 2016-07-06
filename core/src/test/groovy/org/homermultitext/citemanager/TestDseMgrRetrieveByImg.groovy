package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn



class TestDseMgrRetrieveByImg {


  String img = "urn:cite:hmt:vaimg.VA223VN-0725"


  @Test
  void testRetrieveText () {
    def textToImageFiles = [new File("testdata/il17/indices/venA-textToImage-Iliad.csv")]


    DseManager dsem = new DseManager()

    dsem.mapImageToTextFromCsv(textToImageFiles)
    Integer expectedTextNodes = 760

    assert dsem.textToImageRoIMap.size() == expectedTextNodes

    Set textNodes =
    ["urn:cts:greekLit:tlg0012.tlg001.msA:17.1", "urn:cts:greekLit:tlg0012.tlg001.msA:17.2", "urn:cts:greekLit:tlg0012.tlg001.msA:17.3", "urn:cts:greekLit:tlg0012.tlg001.msA:17.4", "urn:cts:greekLit:tlg0012.tlg001.msA:17.5", "urn:cts:greekLit:tlg0012.tlg001.msA:17.6", "urn:cts:greekLit:tlg0012.tlg001.msA:17.7", "urn:cts:greekLit:tlg0012.tlg001.msA:17.8", "urn:cts:greekLit:tlg0012.tlg001.msA:17.9", "urn:cts:greekLit:tlg0012.tlg001.msA:17.10", "urn:cts:greekLit:tlg0012.tlg001.msA:17.11", "urn:cts:greekLit:tlg0012.tlg001.msA:17.12", "urn:cts:greekLit:tlg0012.tlg001.msA:17.13", "urn:cts:greekLit:tlg0012.tlg001.msA:17.14", "urn:cts:greekLit:tlg0012.tlg001.msA:17.15", "urn:cts:greekLit:tlg0012.tlg001.msA:17.16", "urn:cts:greekLit:tlg0012.tlg001.msA:17.17", "urn:cts:greekLit:tlg0012.tlg001.msA:17.18", "urn:cts:greekLit:tlg0012.tlg001.msA:17.19", "urn:cts:greekLit:tlg0012.tlg001.msA:17.20", "urn:cts:greekLit:tlg0012.tlg001.msA:17.21", "urn:cts:greekLit:tlg0012.tlg001.msA:17.22", "urn:cts:greekLit:tlg0012.tlg001.msA:17.23", "urn:cts:greekLit:tlg0012.tlg001.msA:17.24", "urn:cts:greekLit:tlg0012.tlg001.msA:17.25"]

    assert textNodes == dsem.textsForImage(img) as Set


  }


}
