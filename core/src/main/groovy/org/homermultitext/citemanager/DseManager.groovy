package org.homermultitext.citemanager


import edu.harvard.chs.cite.TextInventory
import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

import edu.holycross.shot.hocuspocus.Corpus
import edu.holycross.shot.hocuspocus.TablesUtil
import edu.holycross.shot.prestochango.CollectionArchive

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
  def textToImageRoI = [:]

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

  void mapSurfaceToImageFromCsv(ArrayList surfaceToImageFiles) {
    surfaceToImageFiles.each {
      mapSurfaceToImageFromCsv(it)
    }
  }

  /** Reads entries in a csv file as mappings of DSE surface-image relation.
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





  ArrayList mapSurfaceToTextFromCsv(File surfaceToText) {
    SafeCsvReader srcReader = new SafeCsvReader(surfaceToImageCsv)
    srcReader.readAll().each { columns ->
      surfaceToImageMap[columns[0]] =  columns[1]
      textToSurfaceMap[columns[1]] = columns[0]
    }
  }
  ArrayList mapImageToTextFromCsv(File imageToText) {
  }



}
