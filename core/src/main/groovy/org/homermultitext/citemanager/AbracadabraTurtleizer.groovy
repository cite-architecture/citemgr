package org.homermultitext.citemanager

import edu.holycross.shot.abracadabra.*


/**
*
*/
class AbracadabraTurtleizer {

    boolean debug = false

    File inventory

    File data


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePrefix

    AbracadabraTurtleizer(File inv, File srcDir, File outFile) {
        this.inventory =  inv
        this.data = srcDir
        this.turtleOutput = outFile
        this.includePrefix = false
    }

    AbracadabraTurtleizer(File inv, File srcDir, File outFile, boolean prefix) {
        this.inventory =  inv
        this.data = srcDir
        this.turtleOutput = outFile
        this.includePrefix = prefix
    }


    /** 
    * main() method expects three arguments: a writable output file name,
    * a directory where the defined index files are to be found, and a
    * with one or more files of sequence data.
    */
    public static void main(String[] args) 
    throws Exception {

        switch (args.size()) {
            case 0:
                throw new Exception("main method requires five parameters.")
            System.exit(-1)
            break

            case 5:
                try {
                File inv = new File(args[0])
                File dataDir = new File(args[1])
                File outDir = new File(args[2])
                if (! outDir.exists()) {
                    outDir.mkdir()
                }
                File outFile = new File(outDir, args[3])
                

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

                if ( (!inv.exists())  || (!dataDir.exists())) {
                    System.err.println "AbracadabraTurtelizer:  ${inv} and ${dataDir} must exist."
                } else {
                    AbracadabraTurtleizer ttl = new AbracadabraTurtleizer(inv, dataDir, outFile, prefix)
                    ttl.generateTurtle()
                }

            } catch (Exception e) {
                throw e
            }
            break


            default:
            break
        }
    }


    void generateTurtle() {
        CiteIndex idx = new CiteIndex(inventory, data)
        idx.ttl(turtleOutput, includePrefix)
    }

}
