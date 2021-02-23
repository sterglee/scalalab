package  scalainterpreter

import java.awt.{ Font, GraphicsEnvironment }

trait CustomizableFont {

   protected def createFont : Font = {
    
      new Font( scalaExec.Interpreter.GlobalValues.paneFontName, Font.PLAIN,  scalaExec.Interpreter.GlobalValues.paneFontSize)
               
}}