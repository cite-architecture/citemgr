package citeMgr.dse;

import org.homermultitext.citemanager.DseManager;
import edu.harvard.chs.cite.CiteUrn;

import java.io.File;

import org.concordion.integration.junit3.ConcordionTestCase;

public class DseTest extends ConcordionTestCase {

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
