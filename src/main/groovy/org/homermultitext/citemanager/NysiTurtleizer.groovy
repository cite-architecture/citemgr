package org.homermultitext.citemanager

import edu.holycross.shot.nysi.*
import edu.holycross.shot.hocuspocus.Corpus

/**
*
*/
class NysiTurtleizer {


    File collectionDirectory 

    File imageDirectory 


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePrefix

    NysiTurtleizer(File collDir, File imgDir, File outFile) {
        this.collectionDirectory =  collDir
        this.imageDirectory = imgDir
        this.turtleOutput = outFile
        this.includePrefix = false
    }

    NysiTurtleizer(File collDir, File imgDir, File outFile, boolean prefix) {
        this.collectionDirectory =  collDir
        this.imageDirectory = imgDir
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
                File collDir = new File(args[0])
                File imgDir = new File(args[1])
                File outDir = new File(args[2])
                if (! outDir.exists()) {
                    outDir.mkdir()
                }
                File  outFile = new File(outDir, args[3])
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
                NysiTurtleizer ttl = new NysiTurtleizer(collDir, imgDir, outFile, prefix)
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
        ImgTurtleizer ittl  = new ImgTurtleizer(this.collectionDirectory.toString(), this.imageDirectory.toString())

        System.err.println "Generating img TTL from collections in " + this.collectionDirectory + " and data in " + this.imageDirectory
        ittl.ttl(turtleOutput, includePrefix) 
    }

}
