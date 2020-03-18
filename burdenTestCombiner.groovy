import java.io.File




int readFile(LinkedHashMap geneMap, String fileName){
    String testToRecognize = "BURDEN_FIRTH"
    String transcriptToRecognize = "Best"
    File file = new File(fileName)
    String line
    int noOfLines = 0
    file.withReader { reader ->
        while ((line = reader.readLine()) != null) {
           List elems = line.split('\t')
           String varGroup = elems[0]
           String gene = elems[1]
           String transcript =  elems[2]
           String testStr = elems[7]
           String pValue = elems[8]
           String beta = elems[14]
           String se = elems[15]
           if ( (  testStr == testToRecognize ) && ( transcript == transcriptToRecognize ) ) {
              if ( !geneMap.containsKey(gene) ){
                 geneMap[gene] = new ArrayList()  
              }
              LinkedHashMap recForVarGroup =  new LinkedHashMap()
              recForVarGroup['varGroup'] = varGroup
              recForVarGroup['pValue'] = (pValue!="NA")?Float.parseFloat(pValue):1
              recForVarGroup['beta'] = beta
              recForVarGroup['se'] = se
              geneMap[gene] << recForVarGroup   
            }         
            noOfLines++
        }
    }
     return noOfLines
}

LinkedHashMap geneMap = new LinkedHashMap()

readFile(geneMap,"c:/Users/balexand/dev/extractor/geneData/burden_loftee.gassoc")
println("number genes = ${geneMap.keySet().size()}");
readFile(geneMap,"c:/Users/balexand/dev/extractor/geneData/burden_ns_severe.gassoc")
println("number genes = ${geneMap.keySet().size()}");
readFile(geneMap,"c:/Users/balexand/dev/extractor/geneData/burden_ns_strict.gassoc")
println("number genes = ${geneMap.keySet().size()}");
readFile(geneMap,"c:/Users/balexand/dev/extractor/geneData/burden_ns_strict_fp_ptvs.gassoc")
println("number genes = ${geneMap.keySet().size()}");
readFile(geneMap,"c:/Users/balexand/dev/extractor/geneData/burden_ns_strict_ns_1pct.gassoc")
println("number genes = ${geneMap.keySet().size()}");
readFile(geneMap,"c:/Users/balexand/dev/extractor/geneData/burden_ns_strict_ns_broad_1pct.gassoc")
println("number genes = ${geneMap.keySet().size()}");
readFile(geneMap,"c:/Users/balexand/dev/extractor/geneData/burden_ns_stringent.gassoc")
println("number genes = ${geneMap.keySet().size()}");

int noOfRecords = 0
String outputFileName = "c:/Users/balexand/dev/extractor/bestBurdenTest.json"


ArrayList allRecs = []
for (Map.Entry<String, ArrayList<LinkedHashMap>> entry : geneMap){
    String gene = entry.getKey();
    ArrayList<LinkedHashMap> recordArray = entry.getValue();
    noOfRecords++
   // if (noOfRecords<10) {
       ArrayList sortedRecs = recordArray.sort{x,y -> x.pValue<=>y.pValue}
       LinkedHashMap rec = sortedRecs.first()
       allRecs << """{
                       "gene": "${gene}",
                       "varGroup": "${rec.varGroup}",
                       "pValue": "${rec.pValue}",
                       "beta": "${rec.beta}",
                       "se": "${rec.se}"
                       }""".toString()
   // }
}
File outFile = new File(outputFileName)
outFile << "["
outFile << allRecs.join(",")
outFile << "]"

