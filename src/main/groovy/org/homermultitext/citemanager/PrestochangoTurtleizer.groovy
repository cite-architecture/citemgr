package org.homermultitext.citemanager

import edu.holycross.shot.prestochango.*


/**
*
*/
class PrestochangoTurtleizer {

    boolean debug = true

    File collectionInventory

    File dataDirectory 

    String inventorySource


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePrefix


    PrestochangoTurtleizer(File inv, String invSch, File srcDir, File outDir) {
        this.collectionInventory =  inv
        this.inventorySource = invSch
        this.dataDirectory = srcDir
        this.turtleOutput = new File(outDir, "collections.ttl")
        this.includePrefix = false
    }

    PrestochangoTurtleizer(File inv, String invSch, File srcDir, File outDir, boolean prefix) {
        this.collectionInventory =  inv
        this.inventorySource = invSch
        this.dataDirectory = srcDir
        this.turtleOutput = new File(outDir, "collections.ttl")
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
                throw new Exception("main method requires three parameters.")
            System.exit(-1)
            break

            case 5:
                try {
                
                File inv = new File(args[0])
                String invSchema = args[1]
                File dataDir = new File(args[2])
                File outDir = new File(args[3])
                if (! outDir.exists()) {
                    outDir.mkdir()
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

                PrestochangoTurtleizer pcttl = new PrestochangoTurtleizer(inv, invSchema, dataDir, outDir, prefix)
                pcttl.generateTurtle()

            } catch (Exception e) {
                throw e
            }
            break


            default:
            break
        }
    }


    void generateTurtle() {
        CollectionArchive cc = new CollectionArchive(this.collectionInventory, this.inventorySource, this.dataDirectory)

        cc.ttl(turtleOutput, includePrefix)
    }

}
