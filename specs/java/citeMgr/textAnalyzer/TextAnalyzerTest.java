package citeMgr.textAnalyzer;

import org.homermultitext.citemanager.*;
import org.homermultitext.utils.HmtTokenizer;
import edu.harvard.chs.cite.CiteUrn;
import edu.harvard.chs.cite.CtsUrn;



import java.io.File;
import java.util.LinkedHashMap;

import org.concordion.integration.junit3.ConcordionTestCase;

public class TextAnalyzerTest extends ConcordionTestCase {

    /** Path in concordion build to Corpus documentation.  Knowing this
     * lets us write markdown docs with relative references to data files
     * in the documentation.*/
    String docPath = "/build/concordion-results/citemgr/textAnalyzer/";

    /** Hands back a String parameter so we can save links using concordion's
     * #Href variable for use in later computations. */
    public String setHref(String path) {
	return (path);
    }
    
}
