
package scalaSci.math.io

object ioUtils {

  

def saveAscii(d: Array[Double], fileName: String): Unit  =  {
               
    var sepChar = ' '
    var textWr = new JSci.io.TextWriter(fileName, sepChar)
    textWr.write(d)
    textWr.close
    }
    
  
def saveAscii(dd: Array[Array[Double]], fileName: String, sepChar: Char= ' '): Unit  =  {
    var textWr = new JSci.io.TextWriter(fileName, sepChar)
    textWr.write(dd)
    textWr.close
    }
    
    
def readD2Ascii( fileName: String): Array[Array[Double]] =  {
    scalaSci.math.io.files.ASCIIFile.readDouble2DData(new java.io.File(fileName))
    }
    
def readD1Ascii( fileName: String): Array[Double] =  {
    scalaSci.math.io.files.ASCIIFile.readDouble1DArray(new java.io.File(fileName))
    }
  
  
    
}
