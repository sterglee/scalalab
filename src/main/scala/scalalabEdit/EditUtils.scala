
package scalalabEdit

object EditUtils {

//  build our abbreviations map from the contents of the file fileName.
//  the abbreviations file is comma separated:
//  we list first the abbreviation, then a comma (i.e. ",") and then the replacement, e.g.
//     aad, Array[Array[Double]]
  def buildAbbreviationsMap( fileName: String ) = {
    import scala.io.Source

    println("fileName = "+fileName)
    
    val source = Source.fromFile(fileName, "UTF-8")
    
    val lineIterator = source.getLines

    var abbreviationMap = Map[String, String]()

    for (lines  <- lineIterator)  {
        var currentLineTokens =  lines split ","  // split the comma separating 
        abbreviationMap += (currentLineTokens(0).trim -> currentLineTokens(1).trim)   // add the abbreviations to our map
    }
    
    // return the abbreviations map
    abbreviationMap
    }
    
  
}
