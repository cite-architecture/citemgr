package org.homermultitext.citemanager

import java.text.Normalizer

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus
import edu.holycross.shot.hocuspocus.TokenizationSystem
import edu.holycross.shot.hocuspocus.HmtGreekTokenization



/* add support for 'label' class of token */


class TokenTurtleizer {
    /** Directory containing tabular formatted input files. */
    File tabDir
    /** String value separating columns of tabular files. */
    String separator = "#"
    /** File for output of tokenization. */
    File tokensFile

    // urn base strings
    String lexurnbase = "urn:cite:perseus:lextoken"
    String numurnbase = "urn:cite:hmt:numerictoken"
    String nameurnbase = "urn:cite:hmt:namedentitytoken"
    String literalurnbase = "urn:cite:hmt:literaltoken"
    String labelurnbase = "urn:cite:hmt:labeltoken"

/*
    def tokenize() {
        TokenizationSystem tokenSystem = new HmtGreekTokenization()
        tokensFile.setText("")


        
        def tabList = 
            tabDir.list({d, f-> f ==~ /.*.txt/ } as FilenameFilter ).toList() 
        
            tabList.each { f ->
            System.err.println "Tokenizing ${f}"
            Integer sequence = 0
            File tabFile = new File(tabDir,f)

            String prevUrnVal = ""
            tokenSystem.tokenize(tabFile, separator).each { tokenPair ->
                sequence++;
                String rawCts = Normalizer.normalize(tokenPair[0],Normalizer.Form.NFKC)
                String ctsval 
                if (rawCts) {
                    ctsval = rawCts.replaceAll(/[ \t\n]/,"")
                }

                boolean urnOK = false
                CtsUrn  urn
                try {
                    urn = new CtsUrn(ctsval)
                    urnOK = true

                } catch (Exception e) {
                    System.err.println "tokenizeTask:  could not make urn from ${ctsval}; unable to process pair ${tokenPair}"
                }

                if (urnOK) {
                    String subref = urn.getSubref1()

                   String currUrnVal = urn.getPassageNode()
                    if (currUrnVal != prevUrnVal) {
                        sequence = 1
                    }
                    prevUrnVal = currUrnVal
                    tokensFile.append("<${ctsval}> hmt:seq ${sequence} .\n", "UTF-8")

                    tokensFile.append("<${ctsval}> hmt:psg <${urn.getUrnWithoutPassage()}:${urn.getRef()}> .\n", "UTF-8")

                    //  HmtGreekTokenization is a white-space tokenizer that 
                    //  keeps punctuation.  For analysis, we will throw out punctuation 
                    //  characters.  ·
                    if (subref) {
                        subref = subref.replaceAll(/^[\(\[]/,"")
                        subref = subref.replaceAll(/[.,;?·]$/, "")
                    }

                    // HERE IS WHERE WE CHECK FOR 2-part ID
                    def firstElem = tokenPair[1]
                    def parts = firstElem.split(":")
                    switch (parts[0]) {
                        case "lexical":
                            tokensFile.append("<${lexurnbase}.${subref}> lex:occursIn <${ctsval}> .\n", "UTF-8")
                        break

                        case "numeric":
                            tokensFile.append("<${numurnbase}.${subref}> lex:occursIn <${ctsval}> .\n", "UTF-8")
                        break


                        case "namedEntity":
                            String nameForm = parts[1]
                        tokensFile.append("<${nameurnbase}.${nameForm}> lex:occursIn <${ctsval}> .\n", "UTF-8")
                        break

                        
                        case "waw":
                            tokensFile.append("<${literalurnbase}.${subref}> lex:occursIn <${ctsval}> .\n", "UTF-8")
                        break
                        
                        default : 
                            System.err.println "Unrecognized token type: ${tokenPair[1]}"
                        break
                    }
                }
   
            }
        }

    }
*/
}

