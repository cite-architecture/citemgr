package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestHocusPocusTtler{

  File invFile = new File("testdata/il17/editions/textinventory.xml")
  File citeFile = new File("testdata/il17/editions/textconfig.xml")
  File archive  = new File("testdata/il17/editions")
  @Test
  void testCorpusId() {
    File dummy = new File("/dev/null")
    File testdir = new File("testdata")
    File workOutput = new File(testdir,"output")
    if (! workOutput.exists()) {workOutput.mkdir()}
    File ttlFile = new File(workOutput, "cts.ttl")
    HocusPocusTurtleizer hpt = new HocusPocusTurtleizer(invFile,citeFile,archive, workOutput,ttlFile,true )
    //println "HP TTLer: corpus has " + hpt.corpus.filesInArchive()
    CtsUrn scholia = new CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert hpt.corpus.inventory.urnInInventory(scholia)

  }




}
