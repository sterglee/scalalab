  
      /*
 * This code is adapted from: 
 *  ScalaInterpreterPane.scala
 *  (ScalaInterpreterPane)
 *
 *  Copyright (c) 2010-2012 Hanns Holger Rutz. All rights reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 3 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package  scalainterpreter

import java.awt.{ BorderLayout, Dimension, Font, GraphicsEnvironment, Toolkit }
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.{ AbstractAction, Box, JComponent, JEditorPane, JLabel, JPanel, JProgressBar, JScrollPane, KeyStroke, OverlayLayout, ScrollPaneConstants, SwingWorker }
import ScrollPaneConstants._


import tools.nsc.interpreter.{Results, NamedParam, IMain}

import scala.tools.nsc.interpreter.{IMain, Repl, ReplCore}
import scala.tools.nsc.interpreter.shell.{ILoop, ReplReporterImpl, ShellConfig}
import scala.tools.nsc.reporters.Reporter


import jsyntaxpane.{ DefaultSyntaxKit, SyntaxDocument }

import scalaExec.Interpreter.GlobalValues
import scala.tools.nsc.{ ConsoleWriter,Interpreter => IR, NewLinePrintWriter, Settings }
import java.io.{ File, PrintWriter, Writer }
import java.awt.event.{InputEvent, ActionEvent, KeyEvent, KeyListener}

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.Point
import javax.swing.text.Document

import scalaExec.Interpreter.ControlInterpreter._

import scala.concurrent._
import ExecutionContext.Implicits.global

object ScalaInterpreterPaneGlobals {
    var completer = new scala.tools.nsc.interpreter.shell.ReplCompletion(GlobalValues.globalInterpreter)
    var defaultImportsProcessed = false    // default imports have been processed by the interpreter
    var defaultCursor = scalaExec.Interpreter.GlobalValues.editorPane.getCursor
    var lastResult = scala.tools.nsc.interpreter.Results.Success    // last result evalutaed by the Scala interpreter
    var commandFuture: Future[String] = Future {
       " "
    }  // the future that executes our current command 
}

class ScalaInterpreterPane  extends  JPanel with CustomizableFont {
   pane =>
   
     private  var textLen = 0   // the text length
      // used to specify a block of text
     private var fromLoc = 0  
     private var toLoc = 0
       // the Scala Interpreter that the pane uses
     private var interpreterVar: Option[ scala.tools.nsc.interpreter.IMain ] = None  
       // the currently edited Syntax Document
    private  var docVar: Option[ SyntaxDocument ] = None
    
  def getDoc = docVar
  
  // update fields denoting the document in the editor, necessary when a new document is edited
  def  updateDocument  = {
         
          scalaExec.Interpreter.GlobalValues.editorPane.setContentType( "text/scala" )
            
          docVar = scalaExec.Interpreter.GlobalValues.editorPane.getDocument() match {
               case sdoc: SyntaxDocument => Some( sdoc )
               case _ => None
            }
            syntaxDocument = docVar.get  // TODO: expand popup menu
   
        }
        
 
  // subclasses may override this
   var executeKeyStroke = {
      val ms = Toolkit.getDefaultToolkit.getMenuShortcutKeyMask
      KeyStroke.getKeyStroke( KeyEvent.VK_E, if( ms == InputEvent.CTRL_MASK ) ms | InputEvent.SHIFT_MASK else ms )
   }

   // subclasses may override this
   var initialCode: Option[ String ] = None
  
   // subclasses may override this
   var out: Option[ Writer ] = None

  def resetInterpreter(inter: scala.tools.nsc.interpreter.IMain) = { interpreterVar = Some(inter) }
    
  def getFocusOnInterpreterPane  = { 
    scalaExec.Interpreter.GlobalValues.editorPane.requestFocus
  }
  
  def myProcessingKeys(ke: KeyEvent): KeyEvent = {
    val keycode = ke.getKeyCode  
    keycode   match {
               case _ =>  GlobalValues.editorTextSaved = false;   ke
            }
   
  }
  
  var customKeyMapActions:    Map[ KeyStroke, Function0[ Unit ]] = Map.empty
   // the custom processing function
  var customKeyProcessAction: Option[ Function1[ KeyEvent, KeyEvent ]] = Some(myProcessingKeys)
   
  var initialText = ""
   if (GlobalValues.startupHelpFlag)
     initialText = "       // Type ScalaLab code here (use `Configuration` menu to control the presentation of this startup text). \n"+
"//  Press  F6 (or Ctrl-E, or CTRL-X, or select the right mouse-click popup menu execute option) to execute selected text  or current line (within the Event Dispatch Thread - Is more reliable but Swing is frozen until command completes) \n"+
"//  Press Shift-F6     to execute selected text  or current line in non-blocking mode (i.e. not in the Event Dispatch Thread) \n"+
"//  Press F5 to clear the output console \n"+
"//  Mouse Double Click on an identifier: Displays its value \n"+
"//  Mouse cursor over an identifier displays information for that identifier \n"+
"//   Press F7 uses code completion of the Scala Completer, can be used also for package completion, e.g. javax.<F7> displays the contents of package javax \n"+
"//   Press F7-F7 (i.e. two times F7) after a method name displays the full method's signature \n"+
"//  Press  F4 on an identifier. Presents a completion list, if a dot exists the results are filtered, \n"+ 
"//    e.g.   with:  \"var jf=new JFrame(\"Frame\"); jf.setV\", filters only methods starting with \"setV\" \n"+
"//  Press Shift-F4 on a type (e.j. javax.swing.JFrame). Presents the constructors, methods and fields of the type with a JTree) \n"+
"//  Selecting text (e.g. \"svd\") and pressing F1, displays a completion frame on available relevant methods \n"+
"//  Press Ctrl-F6 to cancel the currently executed command  \n"+
"//  Press F2 to execute code up to cursor position     \n"+
"//  Press Control-Space  uses code completion of jsyntaxpane \n"+
"//  Press TAB expands the abbreviation internal to the jsyntaxpane, e.g. bo<TAB> expands to Boolean (F8 displays these abbreviations) \n"+
"//  Press F11: expands the selected text with the abbreviations specified with the Abbreviations.txt file   \n"+
"//  Press F12 to close all displayed figures \n\n";

 
   private val ggStatus = new JLabel( "Initializing..." )

  // the main editor pane for editing
   scalaExec.Interpreter.GlobalValues.editorPane      = new JEditorPane() {
     
      override protected def processKeyEvent( e: KeyEvent ) {
         super.processKeyEvent( customKeyProcessAction.map( fun => {
            fun.apply( e )
         }) getOrElse e )
      }
   }
   
  
    private val progressPane      = new JPanel()
       
    GlobalValues.ggProgress        = new JProgressBar()  // progress pane for JSyntaxPane
    GlobalValues.ggProgressInvis   = new JComponent {   // hide progress pane for JSyntaxPane
    override def getMinimumSize   = GlobalValues.ggProgress.getMinimumSize  
    override def getPreferredSize = GlobalValues.ggProgress.getPreferredSize
    override def getMaximumSize   = GlobalValues.ggProgress.getMaximumSize
   }

    
   def interpreter : Option[ scala.tools.nsc.interpreter.IMain ] = interpreterVar
   def doc: Option[ SyntaxDocument ] = docVar
   var syntaxDocument: SyntaxDocument = _   // TODO: expand popup menu
       
  def getInterpreter = scalaExec.Interpreter.GlobalValues.globalInterpreter   // returns the Scala interpreter instance

  
  def appendBasicPathsToSettings(settings: Settings) = {
    
    //  append to the classpath the main ScalaLab libraries
      settings.classpath.append(scalalab.JavaGlobals.jarFilePath)  // the Scalalab .jar file by itself
      settings.classpath.append(scalalab.JavaGlobals.compFile)  // Scala Compiler
      settings.classpath.append(scalalab.JavaGlobals.libFile)     // Scala Libraries
      settings.classpath.append(scalalab.JavaGlobals.reflectFile)  // scala-reflect file
      settings.classpath.append(scalalab.JavaGlobals.swingFile)  // Scala Swing

    
      settings.classpath.append(scalalab.JavaGlobals.jfreechartFile)   // ScalaLab JFreeChart library
      settings.classpath.append(scalalab.JavaGlobals.numalFile)  // NUMAL library
      settings.classpath.append(scalalab.JavaGlobals.mtjColtSGTFile)  // MTJ, Colt, SGT libraries
      settings.classpath.append(scalalab.JavaGlobals.ApacheCommonsFile)   // Apache Common Maths current version file
      settings.classpath.append(scalalab.JavaGlobals.ejmlFile)   // EJML file
      settings.classpath.append(scalalab.JavaGlobals.rsyntaxTextAreaFile)  // RSyntaxTextArea file
      settings.classpath.append(scalalab.JavaGlobals.matlabScilabFile)  // MATLAB - SciLab connection file
      
      settings.classpath.append(scalalab.JavaGlobals.jblasFile)  // JBLAS File
      settings.classpath.append(scalalab.JavaGlobals.jsciFile)   // jSci  file
      settings.classpath.append(scalalab.JavaGlobals.javacppFile)
      settings.classpath.append(scalalab.JavaGlobals.JASFile)     // Java Algebra System
      settings.classpath.append(scalalab.JavaGlobals.LAPACKFile)    // LAPACK linear algebra 
      settings.classpath.append(scalalab.JavaGlobals.ARPACKFile)  // ARPACK linear algebra 

    }
  

  
  def init {
    
    scalaExec.Interpreter.GlobalValues.editorPane.getDocument().putProperty(javax.swing.text.DefaultEditorKit.EndOfLineStringProperty, "\n")
    
    var settings = new Settings()   // settings for the main Scala Interpreter
    
    settings.Xexperimental.value=true
 //   settings.Ybackend.value = "GenBCode"
    settings.Ydelambdafy.value = "inline"
    
    
    // make the classpath settable by user
    scalalab.JavaUtilities.setByUser(settings);
    // scalalab.JavaUtilities.useJavaCP(settings);
    
// detect the paths of the core ScalaLab jars at the local file system
   _root_.scalalab.JavaUtilities.detectPaths
      
      
          appendBasicPathsToSettings(settings);
    
         
          println("appending toolboxes")
               // append to classpath the toolboxes installed from previous sessions
          var   classpathScalaSci = GlobalValues.ScalaSciClassPathComponents
           for (k <- 0 until  classpathScalaSci.size) {
              var clsp = classpathScalaSci.elementAt(k).asInstanceOf[String]
              settings.classpath.append(clsp.trim())
              println("appended "+clsp)
              
          }
          
       //  any .jar file in the defaultToolboxes folder is automatically appended to classpath 
          var defaultToolboxesFolder = GlobalValues.scalalabLibPath.replace("lib", "defaultToolboxes")
          println("appending toolboxes of DefaultToolboxes folder:  "+defaultToolboxesFolder)
          
          var toolboxesFolderFiles = (new java.io.File(defaultToolboxesFolder)).listFiles
          if (toolboxesFolderFiles!=null)
            for (file <- toolboxesFolderFiles if file.getName.endsWith(".jar")) {
               var toolboxName = file.toString
               if (GlobalValues.ScalaSciClassPathComponents.contains(toolboxName)==false) {
                   //   append the toolbox to the scalaSciClassPath
                 settings.classpath.append(toolboxName)
                 GlobalValues.ScalaSciClassPathComponents.add(toolboxName)
            
                  if (GlobalValues.interpreterClassPathComponents.contains(toolboxName)==false)
                    GlobalValues.interpreterClassPathComponents.add(toolboxName)
            
                 println("appending from defaultToolboxes folder toolbox: "+file)
               }
          }
        
      // spawn interpreter creation
      (new SwingWorker[ Unit, Unit ] {
         override def doInBackground {
                 
          DefaultSyntaxKit.initKit()
          
          scalaExec.Interpreter.GlobalValues.editorPane.addMouseListener( new PaneMouseListener())
            // MouseMotionListener is used to display values of the variables when mouse cursor is moved over their name 
          if (GlobalValues.mouseMotionListenerForJSyntax == true)
            scalaExec.Interpreter.GlobalValues.editorPane.addMouseMotionListener( new PaneMouseMotionAdapter())

           settings.deprecation.value = false

           if (scalaExec.Interpreter.GlobalValues.compilerOptimizationFlag == true)
             settings.optimise.value = true
           else
             settings.optimise.value = false


           scalaExec.Interpreter.GlobalValues.globalInterpreter = new scala.tools.nsc.interpreter.IMain( settings,  new ReplReporterImpl(settings)){
             override protected def parentClassLoader = pane.getClass.getClassLoader
                        }
            scalaExec.Interpreter.GlobalValues.globalInterpreter.setContextClassLoader()
            
          // interpret the proper imports for each library
            interpretImportsForInterpreterType 

            bindingsCreator.foreach( _.apply( scalaExec.Interpreter.GlobalValues.globalInterpreter ))
            initialCode.foreach( code => recordAndInterpret( code ))
            interpreterVar = Some( scalaExec.Interpreter.GlobalValues.globalInterpreter )
            
       
         }


def interpretImportsForInterpreterType = {
          // interpret the proper imports for each library
          if (ScalaInterpreterPaneGlobals.defaultImportsProcessed == false ) {

            _root_.scalaExec.Interpreter.GlobalValues.interpreterTypeForPane  match {
             
            case  _root_.scalaExec.Interpreter.GlobalValues.EJMLMat => 
               scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)

              if (scalaExec.Interpreter.GlobalValues.userConsole.getText.contains("error: ")) {
                 scalaExec.Interpreter.GlobalValues.userConsole.setText("")
                
              scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
                  
                 println("EJML Interpreter")
               }
               
            
              
              case   _root_.scalaExec.Interpreter.GlobalValues.MTJMat => 
                 scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsMTJScala)
              if (scalaExec.Interpreter.GlobalValues.userConsole.getText.contains("error: ")) {
                 scalaExec.Interpreter.GlobalValues.userConsole.setText("")
                 scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
                 println("MTJ Interpreter")
               }
               
                 
              case   _root_.scalaExec.Interpreter.GlobalValues.D2Das1DMat => 
                  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsD2Das1DScala)
              if (scalaExec.Interpreter.GlobalValues.userConsole.getText.contains("error: ")) {
                 scalaExec.Interpreter.GlobalValues.userConsole.setText("")
                 scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
                 println("D2Das1DMat Interpreter")
                
               }
               
             case  _root_.scalaExec.Interpreter.GlobalValues.ApacheCommonMathsMat =>
                scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsCommonMathsScala)
               if (scalaExec.Interpreter.GlobalValues.userConsole.getText.contains("error: ")) {
                 scalaExec.Interpreter.GlobalValues.userConsole.setText("")
                 scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
                 println("Common Maths Interpreter") 
               }
              
            case   _root_.scalaExec.Interpreter.GlobalValues.JBLASMat =>
                scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsJBLASScala)
               if (scalaExec.Interpreter.GlobalValues.userConsole.getText.contains("error: ")) {
                 scalaExec.Interpreter.GlobalValues.userConsole.setText("")
                 scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
                 println("JBLAS  Interpreter") 
               }
              

            case _ =>
                 scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
        if (scalaExec.Interpreter.GlobalValues.userConsole.getText.contains("error: ")) {
                 scalaExec.Interpreter.GlobalValues.userConsole.setText("")
                 scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
                 println("EJML Interpreter")
               }
              
                                    }
            
            ScalaInterpreterPaneGlobals.defaultImportsProcessed = true
  }
}
          
        
         override protected def done {
            GlobalValues.ggProgressInvis.setVisible( true )
            GlobalValues.ggProgress.setVisible( false )
            scalaExec.Interpreter.GlobalValues.editorPane.setContentType( "text/scala" )
            docVar = scalaExec.Interpreter.GlobalValues.editorPane.getDocument() match {
               case sdoc: SyntaxDocument => Some( sdoc )
               case _ => None
            }
            syntaxDocument = docVar.get  // TODO: expand popup menu
   

            scalaExec.Interpreter.GlobalValues.editorPane.setFont( createFont )
            scalaExec.Interpreter.GlobalValues.editorPane.setEnabled( true )
            scalaExec.Interpreter.GlobalValues.editorPane.requestFocus
            
            // install the editor's key handlers
      setupKeyActions()
      
          
      scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo()
     
          status( "Ready." )
          
          interpretImportsForInterpreterType
          
           scalaExec.Interpreter.GlobalValues.editorPane.setText( initialText )
    
         }
      }).execute()

      val ggScroll   = new JScrollPane( scalaExec.Interpreter.GlobalValues.editorPane, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS  )
//      ggScroll.putClientProperty( "JComponent.sizeVariant", "small" )

      GlobalValues.ggProgress.putClientProperty( "JProgressBar.style", "circular" )
      GlobalValues.ggProgress.setIndeterminate( true )
      GlobalValues.ggProgressInvis.setVisible( false )
      scalaExec.Interpreter.GlobalValues.editorPane.setEnabled( false )

      val imap = scalaExec.Interpreter.GlobalValues.editorPane.getInputMap( JComponent.WHEN_FOCUSED )
      val amap = scalaExec.Interpreter.GlobalValues.editorPane.getActionMap()
        // define the execution action (triggered with F6)
      imap.put( executeKeyStroke, " .exec" )
      amap.put( " .exec", new AbstractAction {
         def actionPerformed( e: ActionEvent ) {
                 var r =   new Runnable {
                     def run {
            getSelectedTextOrCurrentLine.foreach( recordAndInterpret( _ ))
                     }
                   }
         
                var execThread =  new Thread(r)
                execThread.start
             }
          
          })  
         
      
      progressPane.setLayout( new OverlayLayout( progressPane ))
      progressPane.add( GlobalValues.ggProgress )
      progressPane.add( GlobalValues.ggProgressInvis )
      ggStatus.putClientProperty( "JComponent.sizeVariant", "small" )
      val statusPane = Box.createHorizontalBox()
      statusPane.add( Box.createHorizontalStrut( 4 ))
      statusPane.add( progressPane )
      statusPane.add( Box.createHorizontalStrut( 4 ))
      statusPane.add( ggStatus )

      setLayout( new BorderLayout() )
      add( ggScroll, BorderLayout.CENTER )
      add( statusPane, BorderLayout.SOUTH )
   }

   def getSelectedText : Option[ String ] = {
      val txt = scalaExec.Interpreter.GlobalValues.editorPane.getSelectedText
      if( txt != null ) Some( txt ) else None
   }

   def getCurrentLine : Option[ String ] =  
      docVar.map( _.getLineAt( scalaExec.Interpreter.GlobalValues.editorPane.getCaretPosition ))
      

   def getSelectedTextOrCurrentLine : Option[ String ] = 
       getSelectedText.orElse( getCurrentLine )
      
   /**
    *    Subclasses may override this to
    *    create initial bindings for the interpreter.
    *    Note that this is not necessarily executed
    *    on the event thread.
    */
   var bindingsCreator: Option[ Function1[ scala.tools.nsc.interpreter.IMain, Unit ]] = None

   protected def status( s: String ) {
      ggStatus.setText( s )
   }

   def interpret( code: String ) {
      interpreterVar.foreach( globalInterpreter => {
         status( null )
         try { globalInterpreter.interpret( code ) match {
           /* case IR.Error       => status( "! Error !" )
            case IR.Success     => status( "Ok. <" + scalaExec.Interpreter.GlobalValues.globalInterpreter.mostRecentVar + ">" )
            case IR.Incomplete  => status( "! Code incomplete !" ) */
            case _ =>
         }}
         catch { case e :Throwable => e.printStackTrace() }
      })
   }
   
  
  



def setupKeyActions() = {
  
      val imap = scalaExec.Interpreter.GlobalValues.editorPane.getInputMap( JComponent.WHEN_FOCUSED )
      val amap = scalaExec.Interpreter.GlobalValues.editorPane.getActionMap()
      imap.put( executeKeyStroke, " .exec" )
      amap.put( " .exec", new AbstractAction {
         def actionPerformed( e: ActionEvent ) {
                 var r =   new Runnable {
                     def run {
            getSelectedTextOrCurrentLine.foreach( recordAndInterpret( _ ))
                     }
                   }
                  var execThread =  new Thread(r)
                  execThread.start
             }
          }
        )  
        
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "expandAbbreviation")
        amap.put("expandAbbreviation", new AbstractAction {
            scalaExec.Interpreter.GlobalValues.editorPane.setCursor(ScalaInterpreterPaneGlobals.defaultCursor)
            def actionPerformed(e: ActionEvent) {
               scalainterpreter.AbbreviationHandler.detectAndReplaceWordAtCaret
              }
              })
         
    imap.put( KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0), "clearConsole")
    amap.put( "clearConsole",  new AbstractAction {
             def actionPerformed(e: ActionEvent)  {
                 scalaExec.Interpreter.GlobalValues.editorPane.setCursor(ScalaInterpreterPaneGlobals.defaultCursor)
                 GlobalValues.consoleOutputWindow.resetText(" "); 
             }
          })
         
    
        imap.put(KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0), "compileJava")
        amap.put("compileJava", new AbstractAction {
          def actionPerformed(e: ActionEvent) {
            scalaExec.Interpreter.GlobalValues.editorPane.setCursor(ScalaInterpreterPaneGlobals.defaultCursor)
            new scalaExec.gui.CompileExecutePaneActionJava().executeTextJava() 
          }
        })
      
        imap.put(KeyStroke.getKeyStroke( KeyEvent.VK_F9, InputEvent.SHIFT_MASK), "compileJavaEmbedded")
        amap.put("compileJavaEmbedded", new AbstractAction {
          def actionPerformed(e: ActionEvent) {
            scalaExec.Interpreter.GlobalValues.editorPane.setCursor(ScalaInterpreterPaneGlobals.defaultCursor)
            new scalaExec.gui.CompileExecutePaneActionJava().executeTextJavaEmbedded() 
          }
        })
        
  
    imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.CTRL_MASK), "stop")
      amap.put("stop", new AbstractAction {
        def actionPerformed(e: ActionEvent)   {
          scalaExec.Interpreter.GlobalValues.editorPane.setCursor(ScalaInterpreterPaneGlobals.defaultCursor)
          if (GlobalValues.currentThread != null) {
             println("Stopping current thread")
            GlobalValues.currentThread.stop
            GlobalValues.currentThread = null
             }  
          else
            println("no computation thread exists")
        }
      }
    )


  imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7,  0), "complete")
  amap.put("complete", new AbstractAction {
    def actionPerformed(e: ActionEvent) {
      new CompletionAction( ScalaInterpreterPaneGlobals.completer).complete()
    }
  }
  )


      imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "EDTexecute")
      imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK), "EDTexecute")  
    
      amap.put("EDTexecute", new AbstractAction {
           def actionPerformed(e:ActionEvent) {
             
          if (ScalaInterpreterPaneGlobals.commandFuture.isCompleted) {
              javax.swing.SwingUtilities.invokeLater(  new Runnable {
                     def run {
            ScalaInterpreterPaneGlobals.commandFuture = Future 
                {
                 
            var cr = new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR)
            scalaExec.Interpreter.GlobalValues.editorPane.setCursor(cr)
            
               
            getSelectedTextOrCurrentLine.foreach( recordAndInterpret( _ ))
             
                  var mem =  GlobalValues.availMemFormat.format( GlobalValues.rt.freeMemory().asInstanceOf[Double]/1000000)
       var  dispStr = "Available memory : "+mem+"MB. "
       if (GlobalValues.explorerVisible)  
            ggStatus.setText(dispStr)
      
            if (GlobalValues.watchVariablesFlag) {
        scalaSciCommands.WatchInterpreterState.displayUserNamesAndValues
        GlobalValues.editorPane.transferFocus

             }
             
                    scalaExec.Interpreter.GlobalValues.editorPane.setCursor(ScalaInterpreterPaneGlobals.defaultCursor)
              
                    ScalaInterpreterPaneGlobals.lastResult.toString()
                    
                    
            }  // Future
            } // run
                             
              } // Runnable
           )
        }  // previous commandFuture is completed
      }  // actionPerformed
      }  // AbstractAction
        )

    //ScalaInterpreterPaneGlobals.commandFuture foreach {
        //case success => println("success")
        //scala.tools.nsc.interpreter.Results.Success.toString
    //}
    //ScalaInterpreterPaneGlobals.commandFuture.failed foreach {
     // println ("failed")
      //new scala.tools.nsc.interpreter.Results.Error.toString 
   // }
           

          
    imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.SHIFT_MASK), "execute")
      amap.put("execute", new AbstractAction {
          def actionPerformed(e:ActionEvent) {
                   try {
          var r =   new Runnable {
                     def run {
                       
            getSelectedTextOrCurrentLine.foreach( recordAndInterpret( _ ))
                 
               }
             }
             var execThread = new Thread(r)
             execThread.start
              // add the future to the recent future list
            
            if (execThread.getState() == Thread.State.RUNNABLE)
           scalaExec.Interpreter.PendingThreads.addThread(execThread);

                
       var mem =  GlobalValues.availMemFormat.format( GlobalValues.rt.freeMemory().asInstanceOf[Double]/1000000)
       var  dispStr = "Available memory : "+mem+"MB. "
       if (GlobalValues.explorerVisible)  
            ggStatus.setText(dispStr)
      
        
            }
            catch {
              case ex: Exception => println("exception")
            }
          }
      })
          
   
    imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "inspectCompletionList")
    amap.put("inspectCompletionList", new inspectCompletionListAction())
    
   
        
    
    imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.SHIFT_MASK), "inspectCompletionListClass")
    amap.put("inspectCompletionListClass", new inspectClassCompletionListAction())
    
    
      imap.put(KeyStroke.getKeyStroke( KeyEvent.VK_F12, 0), "closePlots")
      amap.put("closePlots", new AbstractAction {
        def actionPerformed(e: ActionEvent) {
          scalaSci.math.plot.plot.closeAll()
          }
      })
    
    
    imap.put(KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0), "globalAutoCompletionHelp")
    amap.put("globalAutoCompletionHelp", new globalAutoCompletionAction()) 
    
    
    
    installScalaCompletion()
    
               
    imap.put(KeyStroke.getKeyStroke( KeyEvent.VK_F2, 0), "executeToCursor")
    amap.put("executeToCursor", new AbstractAction {
      def actionPerformed(e: ActionEvent) {
        var etext = scalaExec.Interpreter.GlobalValues.editorPane.getText
        var  currentTextLen = etext.length
        if (currentTextLen != textLen)   // text altered at the time betwenn F2 clicks
           {
          fromLoc = 0  // reset 
          }
                
          var cursorLoc = scalaExec.Interpreter.GlobalValues.editorPane.getCaretPosition
         if (cursorLoc < toLoc)  {
           // reset if cursor is within the already executed part
            fromLoc = 0
         }
         toLoc = cursorLoc
         var textToExec =  etext.substring(fromLoc, toLoc)
        
         scalaExec.Interpreter.GlobalValues.editorPane.setSelectionStart(fromLoc)
         scalaExec.Interpreter.GlobalValues.editorPane.setSelectionEnd(toLoc)
         scalaExec.Interpreter.GlobalValues.editorPane.setSelectedTextColor(java.awt.Color.RED)
         
         textToExec = textToExec.substring(0, textToExec.lastIndexOf("\n"))
         fromLoc += textToExec.length
         
          
                 var r =   new Runnable {
                     def run {
             recordAndInterpret(textToExec )
                        }
                   }
                var execThread =  new Thread(r)
                execThread.start
                }
      });
      
      customKeyMapActions.iterator.zipWithIndex.foreach( tup => {
         val (spec, idx) = tup
         val name = " .user" + idx
         imap.put( spec._1, name )
         amap.put( name, new AbstractAction {
            def actionPerformed( e: ActionEvent ) {
               spec._2.apply()
            }
         })
      })

  }



def installScalaCompletion() = {
 /*   val imap = scalaExec.Interpreter.GlobalValues.editorPane.getInputMap( JComponent.WHEN_FOCUSED )
    val amap = scalaExec.Interpreter.GlobalValues.editorPane.getActionMap()
      
    imap.put(KeyStroke.getKeyStroke( KeyEvent.VK_F7, 0), "scalaCompletionHelp")
    amap.put("scalaCompletionHelp",  new CompletionAction( ScalaInterpreterPaneGlobals.completer))
   */
}

  
  
}
   
  


 
      
      
      
      
      
      
