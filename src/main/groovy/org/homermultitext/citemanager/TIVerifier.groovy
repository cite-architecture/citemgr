package org.homermultitext.citemanager

import edu.holycross.shot.hocuspocus.TextInventory


/**
* A class for verifying the internal consistency of the 
* contents of a file validating against the schema for 
* a CTS TextInventory.
*/
class TIVerifier {



    /** Constructor with one parameter.
    * @param inventoryFile TextInventory file.
    */
    TIVerifier(File inventoryFile) 
    throws Exception {
        try {
            TextInventory ti = new TextInventory(inventoryFile)
            System.out.println "TextInventory passes all tests."
        } catch (Exception e) {
            System.out.println "FAILURE:  could not validate text inventory '" + inventoryFile + "'."
            System.out.println e
        }
    }


    /** 
    * main() method expects three arguments: a writable output file name,
    * 
    */
    public static void main(String[] args) 
    throws Exception {
        File invFile
        try {
            invFile = new File(args[0])
        } catch (Exception e) {
            throw e
        }
        System.out.println "Testing file '" + invFile + "' ..."
        TIVerifier tiv = new TIVerifier(invFile)
    }

}
