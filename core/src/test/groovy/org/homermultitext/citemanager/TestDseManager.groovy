package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestDseManager {

  def textToImageFiles = [new File("testdata/venA17-textToImage-Iliad.csv")]
  def testImage = "urn:cite:hmt:vaimg.VA223VN-0725"

  def textToSurfaceFiles = [new File("testdata/venA17-textToFolio-Iliad.csv")]
  def testFolio = "urn:cite:hmt:msA.224v"




  @Test
  void testTextImageRelation() {
    DseManager dsem = new DseManager()
    dsem.textImageIndexFiles = textToImageFiles


    def expectedUrns =     [ new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.1"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.2"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.3"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.4"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.5"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.6"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.7"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.8"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.9"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.10"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.11"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.12"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.13"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.14"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.15"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.16"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.17"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.18"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.19"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.20"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.21"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.22"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.23"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.24"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.25")]


    // Two signatures to test: URN as string value, and
    // as CITE URN object
    def actualUrns = dsem.textNodesForImage(testImage)
    assert expectedUrns.size() == actualUrns.size()

    expectedUrns.eachWithIndex { u,i ->
        assert u.toString() == actualUrns[i].toString()
    }

    actualUrns = dsem.textNodesForImage(new CiteUrn(testImage))
    assert expectedUrns.size() == actualUrns.size()

    expectedUrns.eachWithIndex { u,i ->
        assert u.toString() == actualUrns[i].toString()
    }
  }



  @Test
  void testTextSurfaceRelation() {
    DseManager dsem = new DseManager()
    dsem.textTbsIndexFiles = textToSurfaceFiles


    def expectedUrns = [new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.63"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.64"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.65"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.66"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.67"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.68"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.69"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.70"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.71"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.72"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.73"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.74"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.75"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.61"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.62"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.51"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.52"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.53"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.54"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.55"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.56"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.57"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.58"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.59"), new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:17.60")]

    def actualUrns = dsem.textNodesForSurface(testFolio)
    assert expectedUrns.size() == actualUrns.size()

    actualUrns = dsem.textNodesForSurface(new CiteUrn(testFolio))
    assert expectedUrns.size() == actualUrns.size()

  }

/*


scholiaInventories = "/vagrant/hc-il17/collections/venA-exterior.csv"),/vagrant/hc-il17/collections/venA-intermarginal.csv,/vagrant/hc-il17/collections/venA-interior.csv,/vagrant/hc-il17/collections/venA-mainScholia.csv,/vagrant/hc-il17/collections/venA-interlinear.csv"
*/

}
