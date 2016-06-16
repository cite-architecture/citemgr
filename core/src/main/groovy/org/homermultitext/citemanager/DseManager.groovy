package org.homermultitext.citemanager


import edu.harvard.chs.cite.CiteUrn
import edu.holycross.shot.hocuspocus.TablesUtil
import edu.holycross.shot.safecsv.SafeCsvReader

/** A class for managing a standard Digital Scholarly Editions archive.
* The three birectional relations of  the DSE model are implemented as
* six HashMaps.  One-to-one mappings are implemented as mappings of
* a String to a String; one-to-many mappings are implemented as mappings of
* a String to an ArrayList of Strings.
 */
class DseManager {

  public Integer debug = 0

  // The DSE model of three pairs of bidirectional relations is
  // implemented as three pairs of inverse maps.
  //
  //  1st pair of inverse mappings.
  /** One-to-many mapping of text-bearing surface to CTS URNs
  * appearing on that surface.*/
  def surfaceToTextMap = [:]
  /** One-to-one mapping of CTS URN to text-bearing surface. */
  def textToSurfaceMap = [:]


  //  2nd pair of inverse mappings.
  /** One-to-one mapping of text-bearing surface to a reference image
  * used in indexing DSE relations. */
  def surfaceToImageMap = [:]
  /** One-to-one mapping of a reference image
  * used in indexing DSE relations to a text-bearing surface, */
  def imageToSurfaceMap = [:]


  //  3rd pair of inverse mappings.
  /** One-to-many mapping of image URNs for an entire image to text nodes
  * appearing on that image.*/
  def imageToTextMap = [:]
  /** One-to-many mapping of text nodes to image URNs with RoI.*/
  def textToImageRoIMap = [:]

  /** Empty constructor */
  DseManager()   {
  }

  /** Composes a summary of indexed relations.
   * @returns A multi-line string summarizing sizes of indices
   * in this DSE.
   */
  String summary(){
    return """
     Surface-image relations: ${surfaceToImageMap}
     Surfaces mapped to texts: ${surfaceToTextMap.size()}
     Text-surface relations: ${textToSurfaceMap.size()}
     Images mapped to texts: ${imageToTextMap.size()}
     Text-image relations: ${textToImageRoIMap.size()}
     """
  }

  /** Assesses DSE validity of a text-bearing surface.
   * @param surface Identifier for surface to validate.
   * @returns True if surface is valid under the DSE model.
   */
  boolean isValid(String surface) {
    boolean valid = true
    DseManager surfaceDse = reduceByTbs(surface)
    if (debug > 0) {System.err.println(surfaceDse.summary())}
    Set cfSet = [surface] as Set
    if (surfaceDse.surfaceToImageMap.keySet() != cfSet) {
      valid = false
      System.err.println "Set ${surfaceDse.surfaceToImageMap.keySet()} != ${cfSet}"
    }
    
    String img
    surfaceDse.surfaceToImageMap.each {k,v ->
      img = v
    }
    
    if (surfaceDse.textToSurfaceMap.keySet() !=  surfaceDse.textToImageRoIMap.keySet()) {
      System.err.println "${surfaceDse.textToSurfaceMap.keySet()} !=  ${surfaceDse.textToImageRoIMap.keySet()}"
      valid = false
    }

    Set surfSet = [surface] as Set
    if (surfSet != surfaceDse.textToSurfaceMap.values() as Set) {
      System.err.println "${surfSet} does not match ${surfaceDse.textToSurfaceMap.values()}"
      valid = false
    }
    surfaceDse.textToImageRoIMap.each { k,v ->
      CiteUrn imgUrn = new CiteUrn(v)
      if (imgUrn.reduceToObject() != img) {
	if (debug < 0) { System.err.println "For pair ${k}/${v}, ${imgUrn.reduceToObject()} != ${img}"}
	valid = false
      }
    }
    return valid
  }

  /** Reads entries from a list of csv files as mappings of DSE surface-image relation, and
  * assigns values to surfaceToImageMap and imageToSurfaceMap.  Note that each of
  * these is a one-to-one mapping (String<->String).
  * @param surfaceToImageCsv CSV file with entries for text bearing surface in column 0,
  * and corresponding value for image in column 1.
  */
  void mapSurfaceToImageFromCsv(ArrayList surfaceToImageFiles) {
    surfaceToImageFiles.each {
      mapSurfaceToImageFromCsv(it)
    }
  }

  /** Reads entries in a csv file as mappings of DSE surface-image relation, and
  * assigns values to surfaceToImageMap and imageToSurfaceMap.  Note that each of
  * these is a one-to-one mapping (String<->String).
  * @param surfaceToImageCsv CSV file with entries for text bearing surface in column 0,
  * and corresponding value for image in column 1.
  */
  void mapSurfaceToImageFromCsv(File surfaceToImageCsv) {
    SafeCsvReader srcReader = new SafeCsvReader(surfaceToImageCsv)
    srcReader.readAll().each { columns ->
      surfaceToImageMap[columns[0]] =  columns[1]
      imageToSurfaceMap[columns[1]] = columns[0]
    }
  }

  /** Reads entries in a csv file as mappings of DSE text-surface relation, and
  * assigns values to surfaceToTextMap and textToSurfaceMap.  Note that textToSurfaceMap
  * a one-to-one mapping (String<->String), but surfaceToTextMap is a one-to-many mapping
  * (String for surface -> ArrayList of text nodes).  Text nodes are stored in the order
  * they appear in the CSV source, so will be in document order if the CSV source is
  * properly sorted.
  * @param textToSurfaceCsv CSV file with entries for text bearing surface in column 0,
  * and corresponding value for image in column 1.
  */
  void mapSurfaceToTextFromCsv(File textToSurfaceCsv) {
    SafeCsvReader srcReader = new SafeCsvReader(textToSurfaceCsv)
    srcReader.readAll().each { columns ->
      String textNode = columns[0]
      String surface = columns[1]

      // one-to-one mapping:
      textToSurfaceMap[textNode] = surface

      // other direction is many-to-one,
      // keyed by surface String  to list
      // of text nodes
      def textList = []
      if (surfaceToTextMap[surface]) {
        textList = surfaceToTextMap[surface]
      }
      textList.add(textNode)
      surfaceToTextMap[surface] = textList

    }
  }

  /** Reads entries from a list of csv files as mappings of DSE surface-image relation, and
  * assigns values to surfaceToImageMap and imageToSurfaceMap.  Note that each of
  * these is a one-to-one mapping (String<->String).
  * @param surfaceToImageCsv CSV file with entries for text bearing surface in column 0,
  * and corresponding value for image in column 1. Text nodes are stored in the order
  * they appear in the CSV source, so will be in document order if the CSV source is
  * properly sorted.
  */
  void mapSurfaceToTextFromCsv(ArrayList surfaceToImageFiles) {
    surfaceToImageFiles.each {
      mapSurfaceToTextFromCsv(it)
    }
  }



  /** Reads entries in a csv file as mappings of DSE text-image relation, and
   * assigns values to imageToTextMap and textToImageRoIMap.  Note that textToImageRoIMap
   * a one-to-one mapping (String<->String), but imageToTextMap is a one-to-many mapping
   * (String for reference image -> ArrayList of text nodes).
   * @param imageToTextCsv CSV file with entries for text in column 0,
   * and corresponding value for image with RoI in column 1. 
   * Text nodes are stored in the order they appear in the CSV source, 
   * so will be in document order if the CSV source is properly sorted.
   */
  ArrayList mapImageToTextFromCsv(File imageToTextCsv) {
    SafeCsvReader srcReader = new SafeCsvReader(imageToTextCsv)
    srcReader.readAll().each { columns ->
      String textNode = columns[0]
      CiteUrn imageUrn = new CiteUrn(columns[1])
      String image = imageUrn.reduceToObject()

      // one-to-one mapping of text URN
      // to full image URN with ROI:
      textToImageRoIMap[textNode] = columns[1]
      
      // other direction is many-to-one,
      // keyed by surface String  to list
      // of text nodes
      def textList = []
      if (imageToTextMap[image]) {
        textList = imageToTextMap[image]
      }
      textList.add(textNode)
      imageToTextMap[image] = textList
    }
  }

  /** Reads entries in a list of csv files as mappings of DSE text-image relation, and
   * assigns values to imageToTextMap and textToImageRoIMap.  Note that textToImageRoIMap
   * a one-to-one mapping (String<->String), but imageToTextMap is a one-to-many mapping
   * (String for reference image -> ArrayList of text nodes).
   * @param imageToTextFiles List of CSV files with entries for text in column 0,
   * and corresponding value for image with RoI in column 1.
   * Text nodes are stored in the order they appear in the CSV source, 
   * so will be in document order if the CSV source is properly sorted.
   */
  void mapImageToTextFromCsv(ArrayList imageToTextFiles) {
    imageToTextFiles.each {
      mapImageToTextFromCsv(it)
    }
  }

  /** Looks up reference image for a surface.
   * @param surfaceId Surface to look up.
   * @returns Identifer of reference image, as 
   * a String.
   */
  String imageForSurface(String surfaceId) {
    return surfaceToImageMap[surfaceId]
  }

  /** Looks up texts appearing on a surface.
   * @param surfaceId Surface to look up.
   * @returns List of CTS URNs, as Strings, in
   * source order.
   */
  ArrayList textsForSurface(String surfaceId) {
    return surfaceToTextMap[surfaceId]
  }

  /** Looks up texts appearing on an image.
   * @param imgId Image to look up.
   * @returns List of CTS URNs, as Strings, in
   * source order.
   */
  ArrayList textsForImage(String imgId) {
    return imageToTextMap[imgId]
  }


  /** Looks up texts appearing on an image, and 
   * maps them to a URN String for the entire image.
   * @param imgId Image to look up.
   * @returns List of CTS URNs, as Strings, in
   * source order.
   */
  LinkedHashMap textMappingsForImage(String img) {
    def textUrns = textsForImage(img)
    def textHash = [:]
    textUrns.each { txt ->
      textHash[txt] = textToImageRoIMap[txt]
    }
    return textHash
  }


  /** Creates a new DseManger composed only of
   * DSE relations for a single text-bearing surface.
   * @param surfaceId Identifier of surface.
   * @returns A DseManager with all data for the surface. 
   */
  DseManager reduceByTbs(String surfaceId) {
    // Add check here:
    //println "Surface indexed? "+ surfaceToTextMap.keySet().contains(surfaceId)

    DseManager reduced = new DseManager()
    String img = imageForSurface(surfaceId)

    reduced.surfaceToImageMap = ["${surfaceId}": img]
    reduced.imageToSurfaceMap = ["${img}": surfaceId]

    reduced.surfaceToTextMap[surfaceId] =  textsForSurface(surfaceId)
    reduced.textToSurfaceMap = [:]
    reduced.surfaceToTextMap[surfaceId].each { txt ->
      reduced.textToSurfaceMap[txt] = surfaceId
    }

    reduced.imageToTextMap = ["${img}": textsForImage(img)]
    reduced.textToImageRoIMap = textMappingsForImage(img)

    return reduced
  }


  /** Extracts tabulated text data from a tabular file
   * for texts occurring on a given text-bearing surface.
   * @param surface Surface to look for.
   * @param tabFile Tabular formatted file to search.
   * @returns A List of strings in tabular format.
   */
  ArrayList tabDataForSurface(String surface, File tabFile) {
    ArrayList tabs = []
    TablesUtil tu = new TablesUtil()
    textsForSurface(surface).each { txt ->
      tabs.add( tu.tabEntryForUrn(tabFile, txt ))
    }
    return tabs
  }


  /** Extracts tabulated text data for all .txt files
   * in a given directory for texts occurring on a 
   * given text-bearing surface. Files are expected to
   * be name FILE.txt.
   * @param surface Surface to look for.
   * @param tabDir Directory to search through.
   * @returns A List of strings in tabular format.
   */
  ArrayList tabDataForSurfaceInDirectory(String surface, File tabDir) {
    ArrayList tabs = []
    TablesUtil tu = new TablesUtil()
    ArrayList textsToCheck =  textsForSurface(surface)
    tabDir.eachFileMatch(~/.*.txt/) { txtFile ->
      Integer prevCount = tabs.size()
      System.err.println("Check file " + txtFile)
      textsToCheck.each { txt ->
	System.err.println("Look for " + txt)
	def oneEntry = tu.tabEntryForUrn(txtFile, txt )
	if (oneEntry != null) {
	  System.err.println("Got " + oneEntry)
	  tabs.add( oneEntry)
	}
      }
    }
    return tabs
  }
    
}
