
package scalaSci.Search

object SearchLibs {
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import scalaExec.Interpreter.GlobalValues


import java.io._
import javax.swing._ 
import java.awt._
import java.awt.event._

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane

 val   jf = new JFrame("Search keywords in libraries")
 val   querySpec = new  JTextField(50)
 val   jtxtpane = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
 val jscroll = new RTextScrollPane(jtxtpane)
 val   buttonSearch = new JButton("Search (NUMAL, MTJ, Colt, EJML, Apache Commons)")
 val   buttonSearchMain = new JButton("Search Main ScalaLab .jar file")
 val   buttonSearchToolboxes = new JButton("Search  the installed Toolboxes")
  
 val dpanel = new JPanel
 val topPanel = new JPanel
 val examplePanel = new JPanel
 val exampleLabel = new JLabel("Example of query that you can write at the textbox below :")
 val exampleTextField = new JTextField("svd & solve")
 val matchCaseCheckBox = new JCheckBox("Match Case", matchCase)
 var  matchCase = true
 var  inspectMethodsAlso = false

  
def  GUISearchLibs() = {
  
  querySpec.setToolTipText("type here keywords separated by &, e.g. Eig & ejml and then press the Search button to serach")
  jtxtpane.setFont(new Font(GlobalValues.paneFontName, Font.PLAIN, GlobalValues.paneFontSize))
      
  jtxtpane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
  jtxtpane.setCodeFoldingEnabled(true)

  	
  buttonSearch.setToolTipText("Press to perform the search")
buttonSearch.addActionListener ( new ActionListener {
  def actionPerformed( e: ActionEvent ) {
javax.swing.SwingUtilities.invokeLater(  new Runnable {
                     def run {
             
   val   keywords  = getKeywords()
   
   var  results = searchNumalEJMLMTJApacheColt(keywords)
   jtxtpane.setText(results)     
                     }
})
   }
}     
)


  	
  buttonSearchMain.setToolTipText("Press to perform the search")
buttonSearchMain.addActionListener ( new ActionListener {
  def actionPerformed( e: ActionEvent ) {
   javax.swing.SwingUtilities.invokeLater(  new Runnable {
                     def run {
      val   keywords  = getKeywords()

      var  results = searchMain(keywords)
      jtxtpane.setText(results)     
                     }
   })
   }
}     
)

  	
  buttonSearchToolboxes.setToolTipText("Press to perform the search to the installed toolboxes")
buttonSearchToolboxes.addActionListener ( new ActionListener {
  def actionPerformed( e: ActionEvent ) {
javax.swing.SwingUtilities.invokeLater(  new Runnable {
                     def run {
             
   val   keywords  = getKeywords()
   var  results = searchToolboxes(keywords)
   jtxtpane.setText(results)     
                     }
})
   }
}     
)

  
    matchCaseCheckBox.addActionListener ( new ActionListener {
  def actionPerformed( e: ActionEvent ) {
   matchCase = matchCaseCheckBox.isSelected
    }
}     
)

val inspectMethodsAlsoCheckBox = new JCheckBox("Inspect Methods Also", inspectMethodsAlso)
inspectMethodsAlsoCheckBox.setToolTipText("Inspecting methods requires class loading and is wasteful")
 inspectMethodsAlsoCheckBox.addActionListener ( new ActionListener {
  def actionPerformed( e: ActionEvent ) {
   inspectMethodsAlso = inspectMethodsAlsoCheckBox.isSelected
    }
}     
)
 

 
  examplePanel.add(exampleLabel)
  examplePanel.add(exampleTextField)
  topPanel.setLayout(new GridLayout(7,1))
  topPanel.add(buttonSearch)
  topPanel.add(buttonSearchToolboxes)
  topPanel.add(buttonSearchMain)
  topPanel.add(matchCaseCheckBox)
  topPanel.add(inspectMethodsAlsoCheckBox )
  topPanel.add(examplePanel)
  topPanel.add(querySpec)
  

  dpanel.setLayout(new GridLayout(2,1))
  dpanel.add(topPanel)
  dpanel.add(jscroll)

  jf.add(dpanel)

  jf.pack()
  jf.setVisible(true)

}


 // returns the keywords that the user entered seperated by &
 def getKeywords() = {
     val   userQuery  =  querySpec.getText
     var   strTok = new java.util.StringTokenizer(userQuery, "&")
       // count number of conditions
     var condCnt = 0
     while (strTok.hasMoreTokens()) { condCnt += 1; strTok.nextToken }

       // now fill the list of keywords
     var keywords = new Array[String](condCnt)
     strTok = new java.util.StringTokenizer(userQuery, "&")
     condCnt = 0
     while (strTok.hasMoreTokens()) { 
        keywords(condCnt) = strTok.nextToken.trim
        condCnt += 1
          }
          keywords
 }
  
// true if target contains all the keywords
def  containsAllKeywords(target: String, keywords: Array[String], matchCase: Boolean)  = {
	var containsAll = true
	for (sstr <- keywords)
	  if (matchCase) {
	    if (target.contains(sstr)==false)
	      containsAll = false
	  }
	   else {
	   	if (target.toLowerCase.contains(sstr.toLowerCase) == false)
	   	 containsAll = false
	   }
	      
   containsAll	     
}

def searchNumalEJMLMTJApacheColt(keywords: Array[String]) = {
	import scalalab.JavaGlobals
	// construct an array of .jar files over which the search will be performed
	var jarFileNamesOfLibs = new Array[String](4)
	jarFileNamesOfLibs(0) = JavaGlobals.numalFile
	jarFileNamesOfLibs(1) = JavaGlobals.mtjColtSGTFile
	jarFileNamesOfLibs(2) = JavaGlobals.ejmlFile
	jarFileNamesOfLibs(3) = JavaGlobals.ApacheCommonsFile
          
// now perform the search 
	searchAllLibs(jarFileNamesOfLibs, keywords)
	
}


def searchToolboxes(keywords: Array[String]) = {
	import scalalab.JavaGlobals
	

var numToolboxes = scalaExec.Interpreter.GlobalValues.ScalaSciClassPathComponents.size
 
var jarFileNamesOfLibs = new Array[String](numToolboxes)

   var idx =  GlobalValues.fullJarFilePath.lastIndexOf("/")
   
var defaultToolboxesFolder = GlobalValues.fullJarFilePath.substring(0, idx).replace("jar:file:", "")

for (tb<-0 until numToolboxes) {
	var currentToolbox = defaultToolboxesFolder+"/"+scalaExec.Interpreter.GlobalValues.ScalaSciClassPathComponents.get(tb)
	
	jarFileNamesOfLibs(tb) =  currentToolbox
                     
	
}
	
	searchAllLibs(jarFileNamesOfLibs, keywords)
	
}

// search over all ScalaLab libraries for items relevant with the array of keywords
def searchAllScalaLabLibs(keywords: Array[String]) = {
	import scalalab.JavaGlobals
	// construct an array of .jar files over which the search will be performed
	var jarFileNamesOfLibs = new Array[String](4)
	jarFileNamesOfLibs(0) = JavaGlobals.numalFile
      	jarFileNamesOfLibs(1) = JavaGlobals.mtjColtSGTFile
	jarFileNamesOfLibs(2) = JavaGlobals.ejmlFile
	jarFileNamesOfLibs(3) = JavaGlobals.ApacheCommonsFile

//jarFileNamesOfLibs(4) = JavaGlobals.jarFilePath
	jarFileNamesOfLibs(4) = JavaGlobals.jsciFile
	//jarFileNamesOfLibs(5) = JavaGlobals.JASFile
	
	// now perform the search 
	searchAllLibs(jarFileNamesOfLibs, keywords)
	
}


// search rhe Main ScalaLab .jar for items relevant with the array of keywords
def searchMain(keywords: Array[String]) = {
	import scalalab.JavaGlobals
	// construct an array of .jar files over which the search will be performed
	var jarFileNamesOfLibs = new Array[String](1)
	jarFileNamesOfLibs(0) = JavaGlobals.jarFilePath
	
	// now perform the search 
	searchAllLibs(jarFileNamesOfLibs, keywords)
	
}

// search all the .jar files of the libraries for patterns that contain all the keywords
def searchAllLibs(jarFileNamesOfLibs: Array[String],  keywords: Array[String])  = { 
        // results will be accumulated with a StringBuilder
   var relevantItems = new StringBuilder
  var myJarLoader = scalaExec.Interpreter.GlobalValues.globalInterpreter.classLoader

  for (jarFileName  <- jarFileNamesOfLibs)   {   // for all library .jar file names
      var jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)))

	var entryCnt = 0
	var je = jis.getNextJarEntry()
	while (je != null)
                {   // while jar file has entries unprocessed
     entryCnt += 1
     var nameOfEntry =  je.toString()    
     
   	var  name = '/' + je.getName().replace('\\', '/')   // replace any Windows  path conventions
   	var  javaName = name.replace('/','.')   // transform to Java path name format

 
    if (javaName.endsWith(".class"))  { // a class file
        var  idx = javaName.lastIndexOf(".class")
        javaName = javaName.substring(1, idx)    // remove the first '.' and the ".class" suffix

 if (containsAllKeywords(javaName,  keywords, matchCase))
         relevantItems.append(javaName+"\n")

   if  (inspectMethodsAlso)  {  // inspectMethodsAlso
         if (javaName.indexOf("$") == -1) {   // not a special class

          // try to load the Java class with a Java classloader
       
       try {
        var foundClass = myJarLoader.loadClass(javaName)
 

         if (foundClass != null)  {   // class loaded successfully
         	     // retrieve the declared methods of the class
//println("found javaName = "+ javaName)
       // record the item since it is relevant
     
         	  var classMethods = foundClass.getDeclaredMethods()
         	  
         	  for (currentMethod <- classMethods) {   // for all methods of the class
         	  	
         	  	if (Modifier.isPublic(currentMethod.getModifiers())) {   // if the method is public
         	  		
        var methodName = javaName+"."+currentMethod.getName()
     if (containsAllKeywords(methodName,  keywords, matchCase))  {   // relevant method
         	   var returnType = currentMethod.getReturnType()   // return type of the method
         	   var paramTypes = currentMethod.getParameterTypes()  // parameter types of the method
         	   var paramStr = "("
         	   var firstParam = true
         	   for (param <- paramTypes) {  // construct a parameter list description
         	   	if (firstParam) 
         	   	   paramStr += convertScalaLike(param.getName)
         	   	else
         	   	   paramStr += (","+convertScalaLike(param.getName))

         	   	firstParam = false
         	    }
         	    paramStr += ("): "+convertScalaLike(returnType.getName)+"\n")   // complete with the description of return type 

             relevantItems.append( methodName + paramStr )
                  }   // relevant method      
         	  	}  // method is public

         	  }   // for all methods of the class  	
         
         } // class loaded successfully

        }
       catch  {
       	case _:Throwable => 
       }
          	  		
         	  	}   // not a special class
   } //  inspectMethodsAlso	  
     		}   // a class file
   je = jis.getNextJarEntry()

      } // while jar file has entries unprocessed          

  }   // for all library .jar file names
	
 var finalItems = relevantItems.toString            
                 

  finalItems 
}
 


def convertScalaLike(s: String) = {
  s  match  {
  	case "I" => "Int"
  	case "F" => "Float"
  	case "Z" => "Boolean"
  	case "C" => "Char"
  	case "Ljava/lang/String" => "String"
  	case "[Ljava/lang/String" => "Array[String]"
  	case "[I" => "Array[Int]"
  	case "[[I" => "Array[Array[Int]]"
  	case "[D" => "Array[Double]"
  	case "[[D" => "Array[Array[Double]]"
  	
  	case "void" => "Unit"
  	
  	case _ => s	                 
           } 
      }            

    }
  