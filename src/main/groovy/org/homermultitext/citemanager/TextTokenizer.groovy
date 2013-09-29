package org.homermultitext.citemanager

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.*

/**
*
*/
class TextTokenizer {


    File ttlDirectory 
    
    Corpus corpus

   

    TextTokenizer(String tkSystem, File inventory, File archive, File outDir)
    throws Exception {
        def tokenSystem = Class.forName(tkSystem).newInstance()
        Corpus c = new Corpus(inventory, archive)
        c.tabulateRepository(outDir)
        c.tokenizeInventory(tokenSystem, outDir)
    }


    /** 
    * main() method expects three arguments: a writable output file name,
    * 
    */
    public static void main(String[] args) 
    throws Exception {
        try {
            String tokenSystem = args[0]
            File inv = new File(args[1])
            File archive = new File(args[2])
            File outDir = new File(args[3])
            if (! outDir.exists()) {
                outDir.mkdir()
            }
            TextTokenizer tt = new TextTokenizer(tokenSystem, inv, archive, outDir)
        } catch (Exception e) {
            System.err.println "Unable to tokenize archvie with 4 arguments " + args
            throw e
        }
    }

//    void generateTtl() {
//        this.corpus.turtleizeRepository(this.ttlDirectory)
//    }

}
