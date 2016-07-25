# Digital Scholarly Editions #

We can find the default image for a text-bearing surface.


Using  the data
<a href="../../../resources/test/data/venA-folioToImage.csv" concordion:set="#csv = setHref(#HREF)">this .csv file</a>
, we find for folio <strong concordion:set="#urn">urn:cite:hmt:msA.80v</strong>  the default image <strong assertEquals="imgForTbs(#urn, #csv)">urn:cite:hmt:vaimg.VA080VN-0583</strong>.


Using the index in this repository at 
<strong concordion:set="#scholImg">/testdata/dse/venA-extraIliadic.csv</strong>,
we can find texts mapped to an image.  For <strong concordion:set="#img">urn:cite:hmt:vaimg.VA080VN-0583</strong>, we get the list 

<!--<strong concordion:assertEquals="textImgMap(#img, #scholImg)"></strong> -->