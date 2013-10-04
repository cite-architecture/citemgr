package org.homermultitext.citemanager

import edu.holycross.shot.hocuspocus.Corpus

import static org.junit.Assert.*
import org.junit.Test

class TestCtsTtl extends GroovyTestCase {

    File archiveDir = new File ("testdata/cts/editions")
    File inv = new File("testdata/cts/inventory.xml")

    @Test void testCts() {
        Corpus c = new Corpus(inv,archiveDir)
        File outDir = new File("testdata/output")
        if (!outDir.exists()) {
            outDir.mkdir()
        }
        c.tabulateRepository(outDir)
        System.err.println "TABULATED CTS REPOSITORY"
    }

}
