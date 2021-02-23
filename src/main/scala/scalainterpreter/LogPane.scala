
package  scalainterpreter

import java.awt.{ BorderLayout, Color }
import java.io.{ IOException, OutputStream, Writer }
import javax.swing.{ AbstractAction, JPanel, JScrollPane, JTextArea, ScrollPaneConstants }
import ScrollPaneConstants._

class LogPane( rows: Int = 10, columns: Int = 60 )
extends JPanel with CustomizableFont {
   pane =>

   private val textPane   = new JTextArea( rows, columns ) {
      private var totalLength = 0

      override def append( str: String ) {
         super.append( str );
         totalLength += str.length
         updateCaret
      }

      override def setText( str: String ) {
         super.setText( str )
         totalLength = if( str == null ) 0 else str.length
      }

      private def updateCaret {
         try {
            setCaretPosition( math.max( 0, totalLength - 1 ))
         }
         catch { case _:Throwable => /* ignore */ }
      }
   }

   def init {
      textPane.setFont( createFont )
      textPane.setEditable( false )
      textPane.setLineWrap( true )
      textPane.setBackground( Color.black )
      textPane.setForeground( Color.white )
      val ggScroll   = new JScrollPane( textPane, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER )
//      ggScroll.putClientProperty( "JComponent.sizeVariant", "small" )
      setLayout( new BorderLayout() )
      add( ggScroll, BorderLayout.CENTER )
   }

   def clear {
      textPane.setText( null )
   }

   // ---- Writer ----
   object writer extends Writer {
      def close {}
      def flush {}

      @throws( classOf[ IOException ])
      def write( ch: Array[ Char ], off: Int, len: Int ) {
         val str = new String( ch, off, len );
         textPane.append( str )
      }
   }

   // ---- Writer ----
   object outputStream extends OutputStream {
      @throws( classOf[ IOException ])
      override def write( b: Array[ Byte ], off: Int, len: Int ) {
         val str = new String( b, off, len );
         textPane.append( str )
      }

      @throws( classOf[ IOException ])
      def write( b: Int ) {
         write( Array( b.toByte ), 0, 1 )
      }
   }
}