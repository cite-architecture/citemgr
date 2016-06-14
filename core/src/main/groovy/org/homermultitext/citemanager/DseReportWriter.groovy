package org.homermultitext.citemanager


  /*
  def scorecard = []
  def reptDetails = [:]
  Integer totalGood = 0
  */

class DseReportWriter {

}

// MAYBE USEFUL FOR HTML REPORTING?  FROM OLD DseManager:




  /** Creates a map of texts indexed to a given image in a given
   * index file.
   * @param img The image to map.
   * @param indexFile The index file to use.
   * @returns The map expressed as a CITE graph in XML.
   */
   /*
  LinkedHashMap imageMapsByText(CiteUrn img, File indexFile) {
    def results = [:]
    if (debug > 1) {
      System.err.println("DseMgr:imageMapsByText for file " + indexFile)
      System.err.println("Its text contens = " + indexFile.readLines().size() + " lines.")
    }

    if ( !indexFile.getName() ==~ /.+csv/) {
      System.err.println "Only dealing with csv:  no match for " + indexFile
    } else {
      SafeCsvReader reader = new SafeCsvReader(indexFile)
      def things = reader.readAll()
      if (debug > 0) {
	System.err.println "imageMapsByText: from ${indexFile}, read " + things.size() + " entries"
      }

      things.each { ln ->
	if (debug > 0) {println "Line " + ln }
	// allow for incomplete entries...
	if (ln.size() == 2) {
	  String imgStr = ln[1]
	  if (imgStr ==~ /${img}@.+$/) {
	    String doc = new CtsUrn(ln[0]).getUrnWithoutPassage()
	    def imgGroup = []
	    if (results[doc]) {
	      imgGroup = results[doc]
	    }

	    imgGroup.add([ln[0],ln[1]])
	    results[doc] = imgGroup
	  }
	}
      }
      //} else if (indexFile.toString() ==~ /.+tsv/) {
      // implement tsv reading
      //} else {
    }
    return results
  }
  */



/*
def rept = dse.dseReport(urn)

reptDetails[urn.toString()] = rept

String xml =  dse.getVisualInventoryXml(urn)
String fName = "${buildDir}/visualinventory/${urn.getCollection()}_${urn.getObjectId()}.xml"
File visinv = new File(fName)
visinv.setText(xml,"UTF-8")
boolean passfail = dse.verifyTbs(urn)
if (passfail) { totalGood++ }
def score = [urn,passfail]
scorecard.add(score)

}



StreamingMarkupBuilder smb = new StreamingMarkupBuilder()
smb.encoding = "UTF-8"




def doc = smb.bind {
mkp.xmlDeclaration()
html {
  head () {
meta(charset: "UTF-8")
title("DSE validation for ${urn.getCollection()}, pages ${urn.getObjectId()}")
  }
  body {
h1("DSE validation for ${urn.getCollection()}, pages ${urn.getObjectId()}")
h2("Summary")
p {
mkp.yield "Totals:  "
strong "${scorecard.size()}"
mkp.yield " pages examined, "
strong "${totalGood}"
mkp.yield " pages pass DSE validation."
}
h2("Page-by-page record")
p {
mkp.yield "Visual inventories for individual pages are in the "
code("visualinventory")
mkp.yield "subdirectory."
}
table {
tr {
  th("Page")
  th("Summary")
  th("See visual inventory")
  th("Default image")
  th("Same texts indexed to image and surface")
  th("Notes")
}
scorecard.each { pair ->
  CiteUrn pg = pair[0]
  String defaultImg = "none found"

  String bgcolor = "#ffb0b0"
  String summary = "Failed one or more tests."
  if (pair[1]) {
    bgcolor = "#afa"
    summary = "Passed all tests."
  }

  tr {
    td("${pg}")
    td(style: "padding: 0.2em; background-color: ${bgcolor};",summary)
    td {
a (href: "visualinventory/${pg.getCollection()}_${pg.getObjectId()}.xml", "inventory for ${pg.getObjectId()}")
    }

    def details = reptDetails[pg.toString()]
    bgcolor = "#ffb0b0"


    def imgRept = details[0]

    if (imgRept.success) {
bgcolor = "#afa"
defaultImg = imgRept.summary
    }
    td(style: "padding: 0.2em; background-color: ${bgcolor};",defaultImg)


    def mappingRept = details[1]


    bgcolor = "#ffb0b0"
    if (mappingRept.success) {
bgcolor = "#afa"
    }
    td(style: "padding: 0.2em; background-color: ${bgcolor};",mappingRept.summary)


    def txtCheck = dse.textNodesForSurface(pg)
    if (txtCheck.size() == 0) {
td(style: "padding: 0.2em; background-color: #ffffcc;", "Caution: no text units found for this page.")
    } else {
// check for no Iliad text
String iliad = "urn:cts:greekLit:tlg0012.tlg001.msA:"
def iMapKeys = dse.imageMapsByText(defaultImg).keySet()
if (! iMapKeys.contains(iliad)) {
  td(style: "padding: 0.2em; background-color: #ffffcc;") {
    strong("Caution")
    mkp.yield ": no "
    em("Iliad")
    mkp.yield (" text found for this page.")
  }
} else {
  td("")
}
    }
  }
}
}
  }
}
}
File totals = new File("${buildDir}/dse-validation-${urn.getObjectId()}.html")
totals.setText(doc.toString(), "UTF-8")
*/
