package org.homermultitext.citemanager

import edu.holycross.shot.nysi.*
import edu.holycross.shot.hocuspocus.Corpus

/**
*
*/
class NysiTurtleizer {


    File collectionDirectory 

//    File imageDirectory 


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePrefix

    NysiTurtleizer(File collDir, File outFile) {
        this.collectionDirectory =  collDir
        //this.imageDirectory = imgDir
        this.turtleOutput = outFile
        this.includePrefix = false
    }

    NysiTurtleizer(File collDir, File outFile, boolean prefix) {
        this.collectionDirectory =  collDir
        //this.imageDirectory = imgDir
        this.turtleOutput = outFile
        this.includePrefix = prefix
    }


    /** 
    * main() method expects four arguments: a directory where
    * image-collection inventories may be found; a writeable output file.
    * A
    */
    public static void main(String[] args) 
    throws Exception {

        switch (args.size()) {
            case 0:
                throw new Exception("main method requires five parameters.")
            System.exit(-1)
            break

            case 4:
                try {
                File collDir = new File(args[0])
                //File imgDir = new File(args[1])
                File outDir = new File(args[1])
                if (! outDir.exists()) {
                    outDir.mkdir()
                }
                File  outFile = new File(outDir, args[2])
                boolean prefix
                switch (args[3]) {
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
                NysiTurtleizer ttl = new NysiTurtleizer(collDir, outFile, prefix)
                ttl.generateTurtle()

            } catch (Exception e) {
                throw e
            }
            break


            default:
            break
        }
    }


    void generateTurtle() {
        System.err.println "Starting..."
        ImgTurtleizer ittl  = new ImgTurtleizer(this.collectionDirectory.toString() )

        System.err.println "Generating img TTL from collections in " + this.collectionDirectory + "."
        ittl.ttl(turtleOutput, includePrefix) 
    }

}
