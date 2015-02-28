# Digital Scholarly Editions #

We can find the default image for a text-bearing surface.


Using the image index file in this repository at <strong concordion:set="#tbsImage">/testdata/dse/iliad6/venA-folioToImage.csv</strong>, we find
for folio <strong concordion:set="#urn">urn:cite:hmt:msA.80v</strong>  the default image <strong concordion:assertEquals="imgForTbs(#urn, #tbsImage)">urn:cite:hmt:vaimg.VA080VN-0583</strong>.


Using the index in this repository at 
<strong concordion:set="#scholImg">/testdata/dse/venA-extraIliadic.csv</strong>,
we can find texts mapped to an image.  For <strong concordion:set="#img">urn:cite:hmt:vaimg.VA080VN-0583</strong>, we get the list 

<!--<strong concordion:assertEquals="textImgMap(#img, #scholImg)"></strong> -->