package org.homermultitext.citemanager

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn


/**
*
*/
class ObjectImageTurtleizer {


    /** Prefix statement for TTL output. */
    String prefixes = "@prefix dse:        <http://www.homermultitext.org/hmt/rdfverbs/> .\n\n"

    /** Single directory with fixed set of index files specified as
    * keys to the indexStructs map below. */
    File indexDirectory 

    /** Single directory with one or more .txt files listing 
    * navigation sequences.  Verb is always 'next' for navigation relations.
    */
    File sequenceDirectory 


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePreface = true

    /** Map of required indices to type information */
    def indexStructs = [
        "surfaceToDefaultImage" : ["cite","hasDefaultImage","cite"],
        "surfaceToImages": ["cite", "illustratedBy", "cite"],
        "textToSurface": ["cts", "appearsOn", "cite"] 
    ]


    def imageCollectionList = []
    def surfaceCollectionList = []

    ObjectImageTurtleizer(File turtleFile, File indexDir, File sequenceDir) {
        this.turtleOutput =  turtleFile
        this.indexDirectory = indexDir
        this.sequenceDirectory = sequenceDir
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
                throw new Exception("main method requires name of output file.")
            System.exit(-1)
            break

            case 3:
                try {
                File outFile = new File(args[0])
                File indexDirectory = new File(args[1])
                File sequenceDirectory = new File(args[2])
                ObjectImageTurtleizer ttl = new ObjectImageTurtleizer(outFile, indexDirectory, sequenceDirectory)
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
        if (includePreface) {
            this.turtleOutput.append(prefixes)
        }

        // Generate relations from indices:
        this.indexStructs.keySet().each { idx ->
            File idxFile = new File(this.indexDirectory.toString(), "${idx}.txt")
            
            formatIndex(idxFile, this.indexStructs[idx])
            System.err.println "Found index file ${idxFile}"
        }
        
        // Generate object to collection relations for surfaces
        // and images
        
        formatObjectCollections()

        // Generate relations from separate sequence files,
        // normally 1 per Collection
        formatSequences()
    }

    void formatObjectCollections() {
        File idxFile = new File(this.indexDirectory.toString(), "surfaceToImages.txt")
        idxFile.eachLine {
            def cols = it.split(/\t/)
            if (cols.size() != 2) {
                System.err.println "Bad input from index ${idxFile}, line ${cols}"
            } else {
                try {
                    CiteUrn surface = new CiteUrn(cols[0])
                    
                    String surfaceCollection = "urn:cite:${surface.getNs()}:${surface.getCollection()}"
                    this.turtleOutput.append("<${surface}>\tdse:memberOf\t<${surfaceCollection}> .\n")

                    if (! surfaceCollectionList.contains(surfaceCollection)) {
                        surfaceCollectionList.add(surfaceCollection)
                    }

                } catch (Exception e) {
                    System.err.println "Bad CTS URN value ${cols[0]} in record ${cols}"
                }
                    
                try {
                    CiteUrn image = new CiteUrn(cols[1])
                    String imageCollection = "urn:cite:${image.getNs()}:${image.getCollection()}"
                    this.turtleOutput.append("<${image}>\tdse:memberOf\t<${imageCollection}> .\n")

                    if (! imageCollectionList.contains(imageCollection)) {
                        imageCollectionList.add(imageCollection)
                    }


                } catch (Exception e) {
                    System.err.println "Bad CITE URN value ${cols[1]} in record ${cols}"

                }
            }
        }

        this.turtleOutput.append ("\n\n")
        imageCollectionList.each { i ->
            this.turtleOutput.append("<${i}> a 'IMAGECOLLECTION' . \n")
        }
        surfaceCollectionList.each { s ->
            this.turtleOutput.append("<${s}> a 'SURFACECOLLECTION' . \n")
        }
        this.turtleOutput.append ("\n\n")
    }


    void formatSequences() {

        this.sequenceDirectory.eachFileMatch (~/^.*txt$/) { f ->
            String prevUrn = null
            f.eachLine {
                def cols = it.split(/\t/)
                try {
                    CtsUrn u = new CtsUrn(cols[0])

                    String uString = "<urn:cts:${u.getTextGroup()}." + u.getWork(false) + ":${u.getPassageComponent()}>"

                    this.turtleOutput.append("<${cols[0]}> dse:isVersionOf ${uString} .\n")

                } catch (Exception e) {
                    // version mapping is only for CTS URNs: ignore others
                }

                this.turtleOutput.append("<${cols[0]}> dse:seq ${cols[1]} .\n")
                if (prevUrn) {
                    this.turtleOutput.append("<${prevUrn}> dse:next <${cols[0]}> .\n")
                }
                prevUrn = cols[0]
            }
        }
    }


    void formatIndex(File idxFile, Object idxStruct) {
        //System.err.println "Turtleize " + idxFile + " in ${this.turtleOutput} with ${idxStruct}" 

        String subj = idxStruct[0]
        String verb = idxStruct[1]
        String obj = idxStruct[2]

        idxFile.eachLine {
            def cols = it.split(/\t/)
            if (cols.size() != 2) {
                System.err.println "Bad input from index ${idxFile}, line ${cols}"
            } else {
                // test that each is a valid urn... but need to know type.
                switch (subj) {
                    case "cts":
                        try {
                        CtsUrn urn = new CtsUrn(cols[0])
                    } catch (Exception e) {
                        System.err.println "Bad CTS URN value ${cols[0]} in record ${cols}"
                        // throw e
                    }
                    break
                    
                    case "cite":
                        try {
                        CiteUrn urn = new CiteUrn(cols[0])
                    } catch (Exception e) {
                        System.err.println "Bad CITE URN value ${cols[0]} in record ${cols}"
                        //throw e
                    }
                    break

                    default:
                        throw new Exception("Unrecognized type for idxstruct subj: ${subj}")
                        break
                }


                switch (obj) {
                    case "cts":
                        try {
                        CtsUrn urn = new CtsUrn(cols[1])
                    } catch (Exception e) {
                        System.err.println "Bad CTS URN value ${cols[1]}"
                        //throw e
                    }
                    break
                    
                    case "cite":
                        try {
                        CiteUrn urn = new CiteUrn(cols[1])
                    } catch (Exception e) {
                        System.err.println "Bad CITE URN value ${cols[1]}"
                        //throw e
                    }
                    break

                    default:
                        throw new Exception("Unrecognized type for idxstruct obj: ${obj}")
                        break
                }

                this.turtleOutput.append("<${cols[0]}>\tdse:${verb}\t<${cols[1]}> .\n")
            }
        }
    }

}
