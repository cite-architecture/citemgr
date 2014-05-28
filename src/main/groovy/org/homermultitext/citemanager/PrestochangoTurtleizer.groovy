package org.homermultitext.citemanager

import edu.holycross.shot.prestochango.CollectionArchive


/**
* Wrapper class with a main method using the PrestoChango class to generate
* an RDF representation of a CITE Collection archive.
*/
class PrestochangoTurtleizer {

    boolean debug = true

    /** Collection inventory. */
    File collectionInventory


    /** Directory with collection data in delimited text files. */
    File dataDirectory 


    /** */
    String inventorySource


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput


    /** True if TTL output should include prefix definition statements. */
    boolean includePrefix


    PrestochangoTurtleizer(File inv, String invSch, File srcDir, File outDir) {
        this.collectionInventory =  inv
        this.inventorySource = invSch
        this.dataDirectory = srcDir
        this.turtleOutput = new File(outDir, "collections.ttl")
        this.includePrefix = false
    }

    /** Constructor with all five arguments */
    PrestochangoTurtleizer(File inv, String invSch, File srcDir, File outDir, boolean prefix) {
      this.collectionInventory =  inv
      this.inventorySource = invSch
      this.dataDirectory = srcDir
      this.turtleOutput = new File(outDir, "collections.ttl")
      this.includePrefix = prefix
    }


  /** 
   * main() method expects five arguments: a Collection inventory, a
   * Collection inventory schema, a writable output file name,
   * a directory where the defined index files are to be found, and a
   * boolean flag indicating whether or not to include TTL prefix statements.
   */
  public static void main(String[] args) 
  throws Exception {

    switch (args.size()) {
      
    case 0:
    throw new Exception("main method requires four or five parameters.")
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

      System.err.println "SCHEMA: " + invSchema

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
    throw new Exception("main method requires five parameters.")
    System.exit(-1)
    break
    }
  }

  
  /** Writes TTL representation of the CITE Collection archive to the specified
   * output directory.
   */
  void generateTurtle() {
    CollectionArchive cc = new CollectionArchive(this.collectionInventory, this.inventorySource, this.dataDirectory)
    cc.ttl(turtleOutput, includePrefix)
  }

}
