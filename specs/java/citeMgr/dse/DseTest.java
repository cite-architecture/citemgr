package citeMgr.dse;

import org.homermultitext.citemanager.DseManager;
import edu.harvard.chs.cite.CiteUrn;

import java.io.File;
import java.util.LinkedHashMap;

import org.concordion.integration.junit3.ConcordionTestCase;

public class DseTest extends ConcordionTestCase {




    
    public String textImgMap(String urn, String filePath)
    throws Exception {
	
	DseManager dse = new DseManager();
	String current = new java.io.File( "." ).getCanonicalPath();
	File f = new File(current + filePath);
	dse.textImageIndexFiles.add(f);
	System.err.println ("text image index now " + dse.textImageIndexFiles);
	System.err.println ("with size " + dse.textImageIndexFiles.size());
	
	dse.debug = 10;
	LinkedHashMap imap;
	try {
	    System.err.println ("Invoke imageMapsByText with urn string " + urn);
	   imap = dse.imageMapsByText(urn);
	} catch (Exception e) {
	    return ("DseMgr:textImgMap: failed with exception " + e);
	}
	return ("Look up a hash map.  Size of imap is " + imap.size());
    }

    public String imgForTbs(String urn, String filePath)
    throws Exception {
	DseManager dse = new DseManager();
	dse.debug = 10;
	String current = new java.io.File( "." ).getCanonicalPath();
	File f = new File(current + filePath);
	dse.tbsImageIndexFiles.add(f);
	System.err.println("Number of index files: " + dse.tbsImageIndexFiles.size());
	
	try {
	    CiteUrn imgUrn = dse.imageForTbs(urn);
	    return (imgUrn.toString());
	} catch (Exception e) {
	    throw e;
	}
    }
}
