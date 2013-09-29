package org.homermultitext.citemanager

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.TextInventory

import edu.holycross.shot.hocuspocus.*

/** Class for automatically making new editions citable at the
* level of tokens. 
*/
class TokenEditionGenerator {

    /** Separator character used in tabular representation. */
    String sepChar = "#"
    
    TokenEditionGenerator() {
    }

    /** Creates new edition by extending source citation scheme of
    * the source edition.  First creates a tabular representation of
    * the requested version, then tokenizes that version, and finally
    * geneates the RDF representation of a new edition citable by token
    * directly from the tokens list.
    * @param urn The version-level CTS URN of the work to tokenize.
    * @param extension String to append to version-level identifier in
    * making URNs for the new token-level edition.
    * @param tokenizerClassName Name of a class implementing the hocuspocus
    * TokenizationSystem api.
    */
    void generateEdition(CtsUrn urn, String extension, String tokenizerClassName, TextInventory ti, File ctsArchive, File outDir) {

        Tabulator tab = new Tabulator()

        File fullFile = new File("${ctsArchive}/${ti.onlineDocname(urn)}")
        tab.tabulate(urn, ti, fullFile, outDir)

        System.out.println "Tabulated in " + outDir

        def tokenSystem = Class.forName(tokenizerClassName).newInstance()

        EditionGenerator eg = new EditionGenerator()
        outDir.eachFileMatch(~/.*.txt/) { tabFile ->  
            System.out.println "Now tokenize tab file ${tabFile}"
            ArrayList tokens = tokenSystem.tokenize(tabFile,sepChar )
            System.out.println "Found ${tokens.size()} tokens in ${urn}"
            eg.generateEdition(tokens,outDir,"${urn.getTextGroup()}_${urn.getWork()}_${extension}.ttl", extension, ti)

        }

    }


    /** 
    * main() method expects six arguments, as follows
    *
    *   String urnStr = args[0]
    *   String extension = args[1]
    *   String tokenizerClassName =  args[2]
    *   String textInvName = args[3]
    *   String archiveName = args[4]
    *   String outDirName = args[5]
    *
    * @throws Exception if unable to convert any of the required
    * parameters to the appropriate object type.
    */
    public static void main(String[] args) 
    throws Exception {



        System.out.println "Executing ed gen with args ${args}"
        String urnStr = args[0]
        String extension = args[1]
        String tokenizerClassName =  args[2]
        String textInvName = args[3]
        String archiveName = args[4]
        String outDirName = args[5]

        CtsUrn urn
        try {
            urn = new CtsUrn(args[0])
        } catch (Exception e) {
            System.err.println "EditionGenerator: could not make urn from param " + args[0]
            throw e
        }

        File ctsArchive
        try {
            ctsArchive = new File (archiveName)
        } catch (Exception e) {
            System.err.println "EditionGenerator: could create file ${archiveName}"
            throw e
        }


        File invFile 
        try {
            invFile = new File (textInvName)
        } catch (Exception e) {
            System.err.println "EditionGenerator: could create TI file ${textInvName}"
            throw e
        }

        File outDir 
        try {
            outDir = new File (outDirName)
            if (!outDir.exists()) {
                outDir.mkdir()
            }
        } catch (Exception e) {
            System.err.println "EditionGenerator: could create output director ${outDirName}"
            throw e
        }

        TextInventory ti 
        try { 
            ti = new TextInventory(invFile)
        } catch (Exception e) {
            System.err.println "EditionGenerator: could create TextInventroy from file ${invFile}"
            throw e
        }

        TokenEditionGenerator teg = new TokenEditionGenerator()
        teg.generateEdition(urn, extension, tokenizerClassName, ti, ctsArchive, outDir)
    }

}


