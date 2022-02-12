package  scalainterpreter

import java.awt.{ BorderLayout, Dimension, Font, GraphicsEnvironment, Toolkit }
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.{ AbstractAction, Box, JComponent, JEditorPane, JLabel, JPanel, JProgressBar, JScrollPane, KeyStroke, OverlayLayout, ScrollPaneConstants, SwingWorker , JList}
import ScrollPaneConstants._

import jsyntaxpane.{ DefaultSyntaxKit, SyntaxDocument }
import scalaExec.Interpreter.GlobalValues
import scalaExec.gui.AutoCompletionFrame
import tools.nsc.{ ConsoleWriter, Interpreter => NewLinePrintWriter,Settings }
import java.io.{ File, PrintWriter, Writer }
import java.awt.event.{InputEvent, ActionEvent, KeyEvent, KeyListener}

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.Point
import javax.swing.text.Document

import scalaExec.Interpreter.ControlInterpreter._

class EditorPane  extends  JPanel with CustomizableFont {
   pane =>

     var editorTextSavedFlag= true    // flag that tracks whether editor text has been saved
     
     var textLen = 0   // the text length
      // used to specify a block of text
     var fromLoc = 0  
     var toLoc = 0
       // the Scala Interpreter that the pane uses
     var interpreterVar: Option[ scala.tools.nsc.interpreter.IMain ] = None  
       // the currently edited Syntax Document
     
     var editorPane: JEditorPane = null
     var docVar: Option[ SyntaxDocument ] = None
     var F6_consumed = false
     var F7_consumed = false
     var F2_consumed = false
     var F11_consumed = false
     var F10_consumed = false
   

  def editorTextSaved() = editorTextSavedFlag
  def getPane() = editorPane
  

  
  // update fields denoting the document in the editor, necessary when a new document is edited
  def  updateDocument  = {
         
          editorPane.setContentType( "text/scala" )
            
          docVar = editorPane.getDocument() match {
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
    editorPane.requestFocus
  }
  
  
  def myProcessingKeys(ke: KeyEvent): KeyEvent = {
    val keycode = ke.getKeyCode  
    keycode   match {
      case KeyEvent.VK_F5 =>   GlobalValues.consoleOutputWindow.resetText(" ");  ke
       
      case KeyEvent.VK_F9 => javax.swing.JOptionPane.showMessageDialog(null, "You can compile Java code by placing the code within the main window!! "); ke
         
      
      case KeyEvent.VK_F1 => 
                 val selectedStr = editorPane.getSelectedText
                 if (selectedStr != null)  {
                 if (GlobalValues.autocompletionEnabled == false)   {  // enable autocompletion on the first F1 press
                       GlobalValues.autocompletionEnabled = true
                       scalaExec.scalaLab.scalaLab.initAutocompletion()
                   }
               
        val matches = scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci.getMatchedObj(selectedStr)
                 if (matches != null) {
                 val topLevelResultsList = new javax.swing.JList(matches)
                 val detailHelpAdapter = new scalaExec.gui.autoCompleteListHandler
                 topLevelResultsList.addKeyListener(detailHelpAdapter)
                 topLevelResultsList.addListSelectionListener( new ListSelectionListener() {
                   override def valueChanged(lse: ListSelectionEvent) {
                     var selValue = topLevelResultsList.getSelectedValue().toString()
                     selValue = selValue.substring(selValue.indexOf(GlobalValues.smallNameFullPackageSeparator)+3, selValue.length())
                     GlobalValues.selectedStringForAutoCompletion = selValue
                   }
                 })
                 
       GlobalValues.autoCompletionFrame = new AutoCompletionFrame("scalalabConsole AutoCompletion ( F1 for detailed help on the selected entry)")
       val xloc = editorPane.getCaret.getMagicCaretPosition.x
       val yloc = editorPane.getCaret.getMagicCaretPosition.y
       GlobalValues.autoCompletionFrame.setLocation(xloc, yloc)
       GlobalValues.autoCompletionFrame.displayMatches(topLevelResultsList)
       GlobalValues.autoCompletionFrame.requestFocus
                  }
                 }
                 
                 ke    
                   
        case KeyEvent.VK_F10 => 
         if (F10_consumed == false) {
           var etext = scalaExec.Interpreter.GlobalValues.editorPane.getSelectedText
          if (etext != null) {
          var inspectCommand = "scalaSciCommands.BasicCommands.inspectg("+etext+")  "
          GlobalValues.globalInterpreter.interpret(inspectCommand)
          }
          
          ke.consume
          F10_consumed = !F10_consumed
         }
         else
           F10_consumed = !F10_consumed
         ke
      
              case KeyEvent.VK_F7  =>   // use Scala autocompletion to complete the user expression
                 
               ke
      
              case KeyEvent.VK_F11  =>   // use Scala autocompletion to complete the user expression
                 
        /* SOS-Sterg
        if (F11_consumed == false) {
                     F11_consumed = !F11_consumed
                     var cstr = editorPane.getSelectedText
                        // get the starting point of the current command start                    
                    var  lastNewLineCommandStartIdx = cstr.lastIndexOf('\n')+1
                    var  frozeneditorPaneContents = editorPane.getSelectedText
                         
             // check for multiple commands per line       
                    val endCommandChar = ';'   // character denoting the end of the previous command
                    val previousSemicolonCommandStartIdx = cstr.lastIndexOf(endCommandChar)+1
                    if (previousSemicolonCommandStartIdx > lastNewLineCommandStartIdx)  {  // multiple commands per line separated by ';''
                        lastNewLineCommandStartIdx = previousSemicolonCommandStartIdx  // update index after ';'
                     }
                    var  posAutoCompletion = lastNewLineCommandStartIdx
                    val   toksToCheckForLastIndex = "= +*-"
                    val   toksCnt = toksToCheckForLastIndex.length
                    for ( k <- 0 until toksCnt)  {
                      var tokIndex = cstr.lastIndexOf(toksToCheckForLastIndex.charAt(k))+1  // index of token  (in order to match the text after it)
                    if (tokIndex > lastNewLineCommandStartIdx)  {
                        posAutoCompletion = tokIndex
                        lastNewLineCommandStartIdx = tokIndex
                      }
                    }
                   
                     cstr = cstr.substring(posAutoCompletion, cstr.length()).trim()
                     
                     // check for '.' that marks that the user requests to know the members of a structure
                     var    iString = cstr   //= text.substring(startPos, text.length());
                     var  afterDot=""
                     var  membersRequested = false;  // when the user presses dot, we imply that the completion should provide the members of a structure
       
                     var  dotIndex = cstr.indexOf('.')
                     if (dotIndex != -1)    {    // members of a structure should be provided
                         membersRequested = true
                         var  tabDotIndex = frozeneditorPaneContents.lastIndexOf('.')
                         frozeneditorPaneContents = frozeneditorPaneContents.substring(0, tabDotIndex+1)
                         afterDot = iString.substring(dotIndex+1, iString.length());    // get member typed after dot
                         afterDot = afterDot.trim();   // strip any blanks
                         iString = iString.substring(0, dotIndex)  // remove the dot
                     }
                     var  afterDotCharsExist = false
                     if (afterDot.length() > 0)
                         afterDotCharsExist = true

                    // create a completion object                    
                 var completion = new  scala.tools.nsc.interpreter.JLineCompletion(scalaExec.Interpreter.GlobalValues.globalInterpreter);
                 
                    if (membersRequested == false)   {   // provide the top level elements
               var   ps = Parsed.apply(iString)
               var   scalaResultsList = completion.topLevelFor(ps)
               var   matchedSize = scalaResultsList.length
               if (matchedSize > 0) {  // top level elements exist, edit them
                     val    dmatches =  new Array[Object](matchedSize)
                     val    complResults = scalaResultsList.iterator
                     var    k=0
                     while (complResults.hasNext)  {   // collect the matched results
                         var  currentCompletion =  complResults.next().asInstanceOf[String]
                         dmatches(k) = currentCompletion
                         k += 1
                  }
                    
                var topLevelResultsList = new JList(dmatches)
                topLevelResultsList.addListSelectionListener(new ListSelectionListener() {
                     override def  valueChanged(lse: ListSelectionEvent ) {
                             
                             var selValue = topLevelResultsList.getSelectedValue().toString()
                             
                             var  currentText = editorPane.getText()
                             var  posPreviousText  =  currentText.lastIndexOf('\n') + 1
                             var  posEndPreviousCommand = currentText.lastIndexOf(endCommandChar)+1
                             if  (posEndPreviousCommand > posPreviousText)
                                 posPreviousText = posEndPreviousCommand;
                             //int lastEqualsIdx = currentText.lastIndexOf('=')+1;
                             if (posAutoCompletion > posPreviousText)
                                 posPreviousText = posAutoCompletion
                             editorPane.setText(currentText.substring(0,posPreviousText)+selValue)
                             GlobalValues.selectedStringForAutoCompletion = selValue
                             editorPane.setCaretPosition(editorPane.getText().length())   	// set cursor at the end of the text area
                             
	                 }
                     }
         );
        
           GlobalValues.autoCompletionFrame = new AutoCompletionFrame("scalalabConsole AutoCompletion, Workspace variables");
           GlobalValues.autoCompletionFrame.displayMatches(topLevelResultsList);
                     }     //  top level elements exist, edit them
                    }   //  // provide the top level elements
                   
                    else   {   // a dot pressed which implies that the completion should provide members of a structure
                        
                    //  see if we have methods available
                    var  scalaComplResultsList = completion.completions(iString)

                   var classOfItem = scalaExec.Interpreter.GlobalValues.globalInterpreter.classOfTerm(iString);
                   
                        if (classOfItem.toString().equalsIgnoreCase("None") == false )  {
                    // completion.
                    var  ccomplResults = scalaComplResultsList.iterator
                    var  membersSize = ccomplResults.length
                    if (membersSize > 0)  {   // member elements exist, edit them 
                        var  memberMatches = new java.util.Vector[String]()
                        var  complResults = scalaComplResultsList.iterator
                        while (complResults.hasNext)  {   // collect the matched results
                            var  currentCompletion =  complResults.next().asInstanceOf[String]
                            currentCompletion = currentCompletion.trim()
                            if (afterDotCharsExist)  {   // user typed after "."
                             if (currentCompletion.startsWith(afterDot))  {
                                var  classMatchedMethods =  scalaExec.gui.ConsoleKeyHandler.getMethodsOfClass(classOfItem, currentCompletion)
                                var  classMatchedMethodsLen = classMatchedMethods.length
                                
                                for ( matchedCnt <- 0  until classMatchedMethodsLen)
                                   memberMatches.add(classMatchedMethods(matchedCnt).asInstanceOf[String])
                                }
                            }
                            else  {
                                var  classMatchedMethods =  scalaExec.gui.ConsoleKeyHandler.getMethodsOfClass(classOfItem, currentCompletion)
                                var  classMatchedMethodsLen = classMatchedMethods.length
                                
                                for ( matchedCnt <- 0  until classMatchedMethodsLen)
                                  memberMatches.add(classMatchedMethods(matchedCnt).asInstanceOf[String])
                              }
                          }   // collect the matched results
                        
                    var membersResultList = new JList(memberMatches)
                    membersResultList.addListSelectionListener(new ListSelectionListener() {
                override  def  valueChanged(lse: ListSelectionEvent ) {
                          var  selValueObj = membersResultList.getSelectedValue()
                          if (selValueObj != null)  {
                            var  selValue =  selValueObj.asInstanceOf[String]
                            editorPane.setText(frozeneditorPaneContents+selValue)
                            GlobalValues.selectedStringForAutoCompletion = selValue;
                            editorPane.setCaretPosition(editorPane.getText().length());   	// set cursor at the end of the text area  
                          }
                          
                    }
                 }
         );             
             GlobalValues.autoCompletionFrame = new AutoCompletionFrame(" Members of "+iString);
             GlobalValues.autoCompletionFrame.displayMatches(membersResultList);
                            }   // a dot pressed which implies that the completion should provide members of a structure
                        }   // member elements exist, edit them 
                   }    // a dot pressed which implies that the completion should provide members of a structure
         }  // F3_consumed==false   
  
                 else
                   F11_consumed = !F11_consumed
                 */
        ke
      
                 
               
   /*   case KeyEvent.VK_PAGE_DOWN =>
          var currLoc = editorPane.getCaretPosition
          editorPane.setCaretPosition(currLoc+20)
          ke
        */
       
        
      case KeyEvent.VK_F2 =>   
        var etext = editorPane.getText
        var  currentTextLen = etext.length
        if (currentTextLen != textLen)   // text altered at the time betwenn F2 clicks
           {
          fromLoc = 0  // reset 
          }
                if (F2_consumed == false)  {
          var cursorLoc = editorPane.getCaretPosition
         if (cursorLoc < toLoc)  {
           // reset if cursor is within the already executed part
            fromLoc = 0
         }
         toLoc = cursorLoc
         var textToExec =  etext.substring(fromLoc, toLoc)
        
         editorPane.setSelectionStart(fromLoc)
         editorPane.setSelectionEnd(toLoc)
         editorPane.setSelectedTextColor(java.awt.Color.RED)
         
         textToExec = textToExec.substring(0, textToExec.lastIndexOf("\n"))
         fromLoc += textToExec.length
         
          
                 var r =   new Runnable {
                     def run {
             recordAndInterpret(textToExec )
                        }
                   }
                var execThread =  new Thread(r)
                execThread.start
                
               F2_consumed = !F2_consumed
               
               ke.consume
         }
         else  F2_consumed = !F2_consumed
         
        textLen = currentTextLen 
       var mem =  GlobalValues.availMemFormat.format( GlobalValues.rt.freeMemory().asInstanceOf[Double]/1000000)
       var  dispStr = "Available memory : "+mem+"MB. "
       ggStatus.setText(dispStr)
                    
        ke 
        
      
      case KeyEvent.VK_F6 =>  
         if (ke.isShiftDown()) {
                 if (F6_consumed == false) {
                 var r =   new Runnable {
                     def run {
            getSelectedTextOrCurrentLine.foreach( recordAndInterpret( _ ))
                        }
                   }
                var execThread =  new Thread(r)
                execThread.start
                
          
                 
            F6_consumed = !F6_consumed
            ke.consume
    }
    else  F6_consumed = !F6_consumed
    
       var mem =  GlobalValues.availMemFormat.format( GlobalValues.rt.freeMemory().asInstanceOf[Double]/1000000)
       var  dispStr = "Available memory : "+mem+"MB. "
       ggStatus.setText(dispStr)
                    
         }
         else
           { // F6 executes at the Event Dispatch Thread
                 if (F6_consumed == false) {
             javax.swing.SwingUtilities.invokeLater(  new Runnable {
                     def run {
                       
            getSelectedTextOrCurrentLine.foreach( recordAndInterpret( _ ))
                                }
             })
           
            F6_consumed = !F6_consumed
            ke.consume
                 }      
         else {
            F6_consumed = !F6_consumed
         }
         var mem =  GlobalValues.availMemFormat.format( GlobalValues.rt.freeMemory().asInstanceOf[Double]/1000000)
       var  dispStr = "Available memory : "+mem+"MB. "
       if (GlobalValues.explorerVisible)  
            ggStatus.setText(dispStr)

         }    // F6 executes at the Event Dispatch Thread     
        
          
            ke
               case _ =>  editorTextSavedFlag = false;   ke
            }
   
  }
   var customKeyMapActions:    Map[ KeyStroke, Function0[ Unit ]] = Map.empty
   // the custom processing function
   var customKeyProcessAction: Option[ Function1[ KeyEvent, KeyEvent ]] = Some(myProcessingKeys)
   var initialText = ""
   if (GlobalValues.startupHelpFlag)
     initialText = """ // Type ScalaLab code here (use `Configuration` menu to control the presentation of this startup text).
// Press  F6  to execute selected text  or current line (within Event Dispatch Thread)
// Press Shift-F6 to execute  selected text or current line in a separate thread
// Press Ctrl-F6 to cancel the currently executed command
// Press CTRL-SPACE  for   code completion 
// Press TAB expands the abbreviation, e.g. bo<TAB> expands to Boolean
 // Mouse Double Click on an identifier: Displays its value
//  Press F2 to execute code up to cursor position     
  // Press F5 to clear the output console
 // Press  Shift-F4 on an identifier (either selected or not). Presents a completion list, if a dot exists the results are filtered, e.g.   with:  "jf.setV", filters only methods starting with "setV"
  // Press F4 on a type (e.j. javax.swing.JFrame). Presents the constructors, methods and fields of the type with a JTree)
  // Select a keyword (e.g. "fft") and press F1 for obtaining help on the selected identifier using Java reflection  // Select a keyword (e.g. "fft") and press F1 for obtaining help on the selected identifier using Java reflection 
"""
   private val ggStatus = new JLabel( "Initializing..." )

   editorPane      = new JEditorPane() {
      override protected def processKeyEvent( e: KeyEvent ) {
         super.processKeyEvent( customKeyProcessAction.map( fun => {
            fun.apply( e )
         }) getOrElse e )
      }
      setFont(new Font(scalaExec.Interpreter.GlobalValues.paneFontName, Font.PLAIN,  scalaExec.Interpreter.GlobalValues.paneFontSize))
   }
   private val progressPane      = new JPanel()
   private val ggProgress        = new JProgressBar()
   private val ggProgressInvis   = new JComponent {
      override def getMinimumSize   = ggProgress.getMinimumSize
      override def getPreferredSize = ggProgress.getPreferredSize
      override def getMaximumSize   = ggProgress.getMaximumSize
   }

   def interpreter: Option[ scala.tools.nsc.interpreter.IMain ] = interpreterVar
   def doc: Option[ SyntaxDocument ] = docVar
   var syntaxDocument: SyntaxDocument = _   // TODO: expand popup menu
       
  def getInterpreter = scalaExec.Interpreter.GlobalValues.globalInterpreter   // returns the Scala interpreter instance

  def init {
      // spawn interpreter creation
      (new SwingWorker[ Unit, Unit ] {
         override def doInBackground {
            
            DefaultSyntaxKit.initKit()
            
          editorPane.addMouseListener(
   new MouseListener() {
     override def  mouseClicked(e: MouseEvent) = {
       if (e.getClickCount()>=2)  {  //only on ndouble-clicks}
       var editor = e.getSource().asInstanceOf[JEditorPane]
       var  pt = new Point(e.getX(), e.getY())
       var pos = editor.viewToModel(pt)
       var doc = editor.getDocument()
       
       var exited = false
       var wb = ""
       var offset = pos
       while (offset >= 0 && exited==false) {
         var ch = doc.getText(offset, 1).charAt(0)
         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_')
         if (!isalphaNumeric)  exited=true
          else {
           wb = wb + ch
           offset -= 1
          }
          }
       var wa = ""
       var docLen = doc.getLength()
       offset = pos+1
       exited = false
       while (offset < docLen && exited==false) {
         var ch = doc.getText(offset, 1).charAt(0)
         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_')
         if (!isalphaNumeric)  exited=true
           else {
         wa = wa + ch
         offset += 1
           }
         }
        
         var wordAtCursor = wb.reverse+wa       
          
                // .scalainterpreter.displayWatchedVars watchVar(wordAtCursor)
          GlobalValues.globalInterpreter.interpret(wordAtCursor)
       }
      }
              
     override def  mousePressed(e: MouseEvent) = {
      
     }
     override def  mouseReleased(e: MouseEvent) = {
      
     }
     override def  mouseEntered(e: MouseEvent) = {
      
     }
     override def  mouseExited(e: MouseEvent) = {
      
     }
    });
  
           editorPane.addMouseMotionListener(
   new MouseMotionListener() {
     override def  mouseMoved(e: MouseEvent) = {
      //println("mouse moved")
     }
     override def  mouseDragged(e: MouseEvent) = {
       //println("mouse dragged")
     }
     });
  

            interpreterVar = Some( scalaExec.Interpreter.GlobalValues.globalInterpreter )
            
          
         }

          
          
        
         override protected def done {
            ggProgressInvis.setVisible( true )
            ggProgress.setVisible( false )
            editorPane.setContentType( "text/scala" )
            //editorPane.setText( initialText )
            docVar = editorPane.getDocument() match {
               case sdoc: SyntaxDocument => Some( sdoc )
               case _ => None
            }
            syntaxDocument = docVar.get  // TODO: expand popup menu
   

            editorPane.setFont( createFont )
            editorPane.setEnabled( true )
            editorPane.requestFocus
            status( "Ready." )
         }
      }).execute()

      val ggScroll   = new JScrollPane( editorPane, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS  )
//      ggScroll.putClientProperty( "JComponent.sizeVariant", "small" )

      ggProgress.putClientProperty( "JProgressBar.style", "circular" )
      ggProgress.setIndeterminate( true )
      ggProgressInvis.setVisible( false )
      editorPane.setEnabled( false )

      val imap = editorPane.getInputMap( JComponent.WHEN_FOCUSED )
      val amap = editorPane.getActionMap()
      
     
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

      progressPane.setLayout( new OverlayLayout( progressPane ))
      progressPane.add( ggProgress )
      progressPane.add( ggProgressInvis )
      ggStatus.putClientProperty( "JComponent.sizeVariant", "small" )
      val statusPane = Box.createHorizontalBox()
      statusPane.add( Box.createHorizontalStrut( 4 ))
      statusPane.add( progressPane )
      statusPane.add( Box.createHorizontalStrut( 4 ))
      statusPane.add( ggStatus )

//      setLayout( new BorderLayout )
      setLayout( new BorderLayout() )
      add( ggScroll, BorderLayout.CENTER )
      add( statusPane, BorderLayout.SOUTH )
   }   // init

   def getSelectedText : Option[ String ] = {
      val txt = editorPane.getSelectedText
      if( txt != null ) Some( txt ) else None
   }

   def getCurrentLine : Option[ String ] =  
      docVar.map( _.getLineAt( editorPane.getCaretPosition ))
      

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
     import scalaExec.Interpreter.GlobalValues._
      interpreterVar.foreach( globalInterpreter => {
         status( null )
         try { globalInterpreter.interpret( code ) match {
            case scala.tools.nsc.interpreter.Results.Error       => status( "! Error !" )
            case scala.tools.nsc.interpreter.Results.Success     => status( "Ok. <" + scalaExec.Interpreter.GlobalValues.globalInterpreter.mostRecentVar + ">" )
            case scala.tools.nsc.interpreter.Results.Incomplete  => status( "! Code incomplete !" )
            case _ =>
         }}
         catch { case e:Throwable => e.printStackTrace() }
      })
   }
   
  
  






           

}
   
  
  
