package org.homermultitext.citemanager


import org.junit.Test
import static groovy.test.GroovyAssert.shouldFail


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class to test cite library's CiteCollection class.
*/
class TestPrestoChangoTtler{

  String schemaFileName =   "specs/resources/schemas/cite/CiteCollectionInventory.rng"
  File invFile = new File("testdata/pc/signs-collection.xml")
  File filesDir  = new File("testdata/pc")
  File outDir = new File("testdata/testoutput")
  @Test
  void testPcTtl() {
    if (! outDir.exists() ) {
      outDir.mkdir()
    }
    //CollectionArchive cca = new CollectionArchive()
    PrestochangoTurtleizer pct = new PrestochangoTurtleizer(invFile, schemaFileName, filesDir,outDir,true)
    
    pct.generateTurtle()
//PrestochangoTurtleizer(File inv, String invSch, File srcDir, File outDir
  }




}
