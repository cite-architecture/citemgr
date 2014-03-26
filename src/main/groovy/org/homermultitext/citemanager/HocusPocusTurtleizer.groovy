package org.homermultitext.citemanager

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus

/**
*
*/
class HocusPocusTurtleizer {

    // writable working directory
    File workDirectory 

    File ttlFile
    
    Corpus corpus

    boolean includePrefix

    HocusPocusTurtleizer(File inventoryFile,  File archiveDir,  File workingDir, File outFile, boolean prefix) 
    throws Exception {

        try {
            if (! workingDir.exists()) {
                workingDir.mkdir()
            }
            this.workDirectory =  workingDir                
            this.ttlFile = outFile
            this.includePrefix =  prefix

            corpus = new Corpus(inventoryFile, archiveDir)

	    if (!corpus.filesAndInventoryMatch()) {
	      System.err.println "HPTurtleizer: files and inventory do not match."
	      System.err.println "Files in inventory: " 
	      corpus.filesInInventory().each { f ->
		System.err.println "\t" + f
	      }
	      System.err.println "Files in disk archive: "
	      corpus.filesInArchive().each { f ->
		System.err.println "\t" + f
	      }
	    }

        } catch (Exception e) {
            throw new Exception ("HocusPocusTurtelizer: unable to create Corpus from inventory ${inventoryFile} for archive ${archiveDir}: ${e}")
        }
    }


    /** 
    * main() method expects arguments: 
    * 
    */
    public static void main(String[] args)  {
        boolean runIt = true
        
        String invFileName = args[0]
        String archiveName = args[1]
        String outputDirName = args[2]

        HocusPocusTurtleizer t            
        switch (args.size()) {
            case 5:
                File outputDir
            File inventory
            File archiveDir
            File outFile

            try {
                outputDir = new File(outputDirName)
                outFile = new File(outputDir, args[3])
                inventory = new File(invFileName)
                archiveDir = new File(archiveName)

            } catch (Exception e) {
                System.err.println "HocusPocusTurteizer: unable to create files: ${e} " 
                runIt = false
            }

            try {
                if (! outputDir.exists()) {
                    outputDir.mkdir()
                }
            } catch (Exception e) {
                System.err.println "HocusPocusTurtelizer main: ${e}"
                runIt = false
            }

            if (! inventory.canRead()) {
                System.err.println "HocusPocusTurtelizer main: cannot read inventory file '" + inventory + "'"
                runIt = false
            }

            if (! archiveDir.canRead()) {
                System.err.println "HocusPocusTurtelizer main: cannot read archive directory ${archiveDir}"
                runIt = false
            }

            boolean prefix
            switch (args[4]) {
                case "t":
                    case "T":
                    case "true":
                    case "TRUE":
                    case "y":
                    case "Y":
                    case "yes":
                    case "YES":
                    prefix = true
                break
                
                default :
                    prefix = false
                break
            }

            if (runIt) {
                t  = new HocusPocusTurtleizer(inventory, archiveDir, outputDir, outFile, prefix)
                t.generateTtl()
            } else {
                outFile.append("\n")
            }
            break


            
            default :
                throw new Exception ("HocusPocusTurteizer: main method needs 5 args" )
            break
        }

    }

    void generateTtl() {
        this.corpus.ttl(ttlFile, includePrefix, workDirectory)
    }

}
