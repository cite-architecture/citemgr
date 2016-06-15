package org.homermultitext.citemanager


import edu.harvard.chs.cite.CiteUrn
import edu.holycross.shot.safecsv.SafeCsvReader

/** A class for managing a standard Digital Scholarly Editions archive.
* The three birectional relations of  the DSE model are implemented as
* six HashMaps.  One-to-one mappings are implemented as mappings of
* a String to a String; one-to-many mappings are implemented as mappings of
* a String to an ArrayList of Strings.
 */
class DseManager {

  public Integer debug = 0


  //  Inverse relations:
  /** One-to-many mapping of text-bearing surface to CTS URNs
  * appearing on that surface.*/
  def surfaceToTextMap = [:]
  def textToSurfaceMap = [:]

  // Inverse relations:
  /** One-to-one mapping of text-bearing surface to a reference image
  * used in indexing DSE relations. */
  def surfaceToImageMap = [:]
  /** One-to-one mapping of a reference image
  * used in indexing DSE relations to a text-bearing surface, */
  def imageToSurfaceMap = [:]



  //  Inverse relations:
  /** One-to-many mapping of image URNs for an entire image to text nodes
  * appearing on that image.*/
  def imageToTextMap = [:]

  /** One-to-many mapping of text nodes to image URNs with RoI.*/
  def textToImageRoIMap = [:]

  /** Empty constructor */
  DseManager()   {
  }


  /** Performs DSE validation for a given
   * text-bearing surface. Checks for referntial integrity
   * across the three edges of the DSE triangle:
   *
   * 1. image to TBS
   * 2. text to TBS
   * 3. text to image
   *
   * Tests currently verify that a single default image is
   * indexed to TBS, that an identical set of text nodes are indexed
   * to TBS and the default image.
   *
   * @param urn The text-bearing surface to validate.
   * @returns True if all tests pass.
   */

   // reimplment as:
   boolean verifyTbs(CiteUrn urn) {
     // 1. reduceByTbs
     // 2. validate
   }

   String summary(){
     return """
     Surface-image relations: ${surfaceToImageMap}
     Surfaces mapped to texts: ${surfaceToTextMap.size()}
     Text-surface relations: ${textToSurfaceMap.size()}
     Images mapped to texts: ${imageToTextMap.size()}
     Text-image relations: ${textToImageRoIMap.size()}
     """
   }
   boolean isValid(String surface) {
     boolean valid = true
     DseManager surfaceDse = reduceByTbs(surface)
     System.err.println(surfaceDse.summary())
     Set cfSet = [surface] as Set
     if (surfaceDse.surfaceToImageMap.keySet() != cfSet) {
       valid = false
       System.err.println "Set ${surfaceDse.surfaceToImageMap.keySet()} != ${cfSet}"
     }
     String img
     surfaceDse.surfaceToImageMap.each {k,v ->
       //println "#${k}# -> ${v}"
       //println "in other words "  + surfaceDse.surfaceToImageMap[k]
       img = v
     }
     System.err.println "Img is " + img

     if (surfaceDse.textToSurfaceMap.keySet() !=  surfaceDse.textToImageRoIMap.keySet()) {
       // FAILING
       System.err.println "${surfaceDse.textToSurfaceMap.keySet()} !=  ${surfaceDse.textToImageRoIMap.keySet()}"
       valid = false
     }
     Set surfSet = [surface] as Set
     if (surfSet != surfaceDse.textToSurfaceMap.values() as Set) {
        // FAILING
       System.err.println "${surfSet} does not match ${surfaceDse.textToSurfaceMap.values()}"
       valid = false
     }
     surfaceDse.textToImageRoIMap.each { k,v ->
       CiteUrn imgUrn = new CiteUrn(v)
       if (imgUrn.reduceToObject() != img) {
         System.err.println "For pair ${k}/${v}, ${imgUrn.reduceToObject()} != ${img}"
         valid = false
       }
     }

     return valid
   }
   /*
   boolean verifyTbs(CiteUrn urn) {
    boolean valid = true
    String surface = urn.toString()
    String img
    // 1. Test that one default image is indexed to TBS:
    if ((! surfaceToImageMap.keySet().contains(surface))
    || (! surfaceToTextMap.keySet().contains(surface))) {
      valid = false
    } else {
      img = surfaceToImageMap(urn.toString())
    }
    // 2. Test that same set of text nodes is mapped to
    // image and to surface
    if (imageToTextMap[img] as Set != surfaceToTextMap[surface] as Set) {
     valid = false
    }

    return valid
  }

*/



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
  * (String for surface -> ArrayList of text nodes).
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
  * and corresponding value for image in column 1.
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

  void mapImageToTextFromCsv(ArrayList imageToTextFiles) {
    imageToTextFiles.each {
      mapImageToTextFromCsv(it)
    }
  }


  String imageForSurface(String surfaceId) {
    return surfaceToImageMap[surfaceId]
  }

  ArrayList textsForSurface(String surfaceId) {
    return surfaceToTextMap[surfaceId]
  }


  ArrayList textsForImage(String imgId) {
    return imageToTextMap[imgId]
  }

  LinkedHashMap textMappingsForImage(String img) {
    def textUrns = textsForImage(img)
    def textHash = [:]
    textUrns.each { txt ->
      textHash[txt] = textToImageRoIMap[txt]
    }
    return textHash
  }

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

}
