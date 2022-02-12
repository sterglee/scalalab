
// helps to perform conveniently imports for basic application types
package scalaExec.Interpreter

object importHelper {
  // Java standard UI and graphics support
  def importJavaSwing() = { 
    GlobalValues.globalInterpreter.interpret("""
    import _root_.java.awt._ ; 
    import _root_.java.awt.event._ ;
    import _root_.javax.swing._ ;
    import _root_.javax.swing.event._;
""")
  }

  def injectJavaSwing()  = {
      val javaSwingStr = "\n importJavaSwing \n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(javaSwingStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }

   def injectDirectlyJavaSwing()  = {
      val javaSwingStr = """
      import _root_.java.awt._ ; 
    import _root_.java.awt.event._ ;
    import _root_.javax.swing._ ;
    import _root_.javax.swing.event._; 
    """
    scalaExec.Interpreter.GlobalValues.editorPane.setText(javaSwingStr + GlobalValues.editorPane.getText)
    }

   def importBasicPlots() = {
     GlobalValues.globalInterpreter.interpret("""
        import _root_.java.awt._ ; 
        import _root_.java.awt.event._ ;
        import _root_.javax.swing._ ;
        import _root_.javax.swing.event._;
          import _root_.scalaSci.math.plot._;
          import _root_.scalaSci.math.plot.canvas._
          import _root_.scalaSci.math.plot.plotObjects._
          import _root_.scalaSci.math.plot.plots._; 
          import _root_.scalaSci.math.plot.plot._
          import _root_.scalaSci.math.plot.plotTypes._
          import _root_.scalaSci.math.plot.PlotFunctional._
          import _root_.scalaSci.math.plot.PlotAdaptiveFunctional._
                                             
          import _root_.scalaSci.math.plot.namedPlotsObj._
    """)
   }
   
  def injectBasicPlots()  = {
      val basicPlotsStr = "\n importBasicPlots \n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(basicPlotsStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
  }

   def injectBasicPlotsDirectly()  = {
      val basicPlotsStr = """
       import _root_.java.awt._ 
       import _root_.java.awt.event._ 
       import _root_.javax.swing._ ;
       import _root_.javax.swing.event._
       import _root_.scalaSci.math.plot._
       import _root_.scalaSci.math.plot.canvas._
       import _root_.scalaSci.math.plot.plotObjects._
       import _root_.scalaSci.math.plot.plots._
       import _root_.scalaSci.math.plot.plot._
       import _root_.scalaSci.math.plot.plotTypes._
       import _root_.scalaSci.math.plot.PlotFunctional._
       import _root_.scalaSci.math.plot.PlotAdaptiveFunctional._
          
                                             
       import _root_.scalaSci.math.plot.namedPlotsObj._
    """
    scalaExec.Interpreter.GlobalValues.editorPane.setText(basicPlotsStr + GlobalValues.editorPane.getText)
    }

  // the NUMAL library related staff
  def importNumAl() =  {
    GlobalValues.globalInterpreter.interpret("""
    import _root_.java.util.Vector ; 
    import _root_.numal._ ; 
    import _root_.numal.Algebraic_eval._;
    import _root_.numal.Analytic_eval._
    import _root_.numal.Analytic_problems._
    import _root_.numal.Approximation._
    import _root_.numal.Basic._;
    import _root_.numal.FFT._;
    import  _root_.numal.Linear_algebra._; 
    import _root_.numal.Special_functions._;
    import java.text.DecimalFormat 
 """
    )
  }

  def injectNumAl()  = {
      val numAlStr = "\n importNumAl \n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(numAlStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }
    
  
   def injectNumAlDirectly()  = {
      val numAlStr = """
       import _root_.java.util.Vector ; 
    import _root_.numal._ ; 
    import _root_.numal.Algebraic_eval._;
    import _root_.numal.Analytic_eval._
    import _root_.numal.Analytic_problems._
    import _root_.numal.Approximation._
    import _root_.numal.Basic._;
    import _root_.numal.FFT._;
    import  _root_.numal.Linear_algebra._; 
    import _root_.numal.Special_functions._;
    import java.text.DecimalFormat 
    """
    scalaExec.Interpreter.GlobalValues.editorPane.setText(numAlStr + GlobalValues.editorPane.getText)
    }


   def importScalaSciDefaultMat() = {
     GlobalValues.globalInterpreter.interpret("""
        import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.Mat ; 
        import _root_.scalaSci.Mat._ ; 
        import _root_.scalaSci.StaticMaths._ ; 
                  """)
   }

   def injectScalaSciDefaultMat()  = {
      val str = "\n importScalaSciDefaultMat\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(str+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }

  def injectScalaSciDefaultMatDirectly() = {
    val defaultMatStr =   """
import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.Mat ; 
        import _root_.scalaSci.Mat._ ; 
        import _root_.scalaSci.StaticMaths._ ; 
        """
    scalaExec.Interpreter.GlobalValues.editorPane.setText(defaultMatStr + GlobalValues.editorPane.getText)
  }
  
   def importScalaSciEJMLMat() = {
     GlobalValues.globalInterpreter.interpret("""
        import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.EJML.Mat ; 
        import _root_.scalaSci.EJML.BMat ; 
        import _root_.scalaSci.EJML.Mat._ ; 
        import _root_.scalaSci.EJML.BMat._ ; 
        import _root_.scalaSci.EJML.StaticMathsEJML._ ; 
                      """)
   }

   def injectScalaSciEJMLMat()  = {
      var str = "\n importScalaSciEJMLMat\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(str+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }
    
  def injectScalaSciEJMLMatDirectly() = {
    val EJMLStr =   """
        import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.EJML.Mat ; 
        import _root_.scalaSci.EJML.BMat ; 
        import _root_.scalaSci.EJML.Mat._ ; 
        import _root_.scalaSci.EJML.BMat._ ; 
        import _root_.scalaSci.EJML.StaticMathsEJML._ ; 
                """
    scalaExec.Interpreter.GlobalValues.editorPane.setText(EJMLStr + GlobalValues.editorPane.getText)
  }
  
   def importScalaSciMTJMat() = {
     GlobalValues.globalInterpreter.interpret("""
        import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.MTJ.Mat ; 
        import _root_.scalaSci.MTJ.Mat._ ; 
        import _root_.scalaSci.MTJ.StaticMathsMTJ._ ;
                                                        
                      """)
   }

   def injectScalaSciMTJMat()  = {
      val str = "\n importScalaSciMTJMat\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(str+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }
    
    def injectScalaSciMTJMatDirectly = {
      val mtjStr = """
        import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.MTJ.Mat ; 
        import _root_.scalaSci.MTJ.Mat._ ; 
        import _root_.scalaSci.MTJ.StaticMathsMTJ._ ;
"""
      scalaExec.Interpreter.GlobalValues.editorPane.setText(mtjStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    
    }

  def importScalaSciCommonMathsMat() = {
     GlobalValues.globalInterpreter.interpret("""
        import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.CommonMaths.Mat ; 
        import _root_.scalaSci.CommonMaths.Mat._ ; 
        import _root_.scalaSci.CommonMaths.StaticMathsCommonMaths._ ; 
          """)
   }

   def injectScalaSciCommonMathsMat()  = {
      val str = "\n importScalaSciCommonMathsMat \n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(str+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }
  
  def injectScalaSciCommonMathsMatDirectly() = {
    val cmStr = """
        import _root_.scalaSci.Vec;
        import _root_.scalaSci.Matrix ; 
        import _root_.scalaSci.Vec._ ; 
        import _root_.scalaSci.RichNumber; 
        import _root_.scalaSci.Matrix._ ; 
        import _root_.scalaSci.RichDouble1DArray ; 
        import _root_.scalaSci.RichDouble2DArray ;
        import _root_.scalaSci.RichDouble1DArray._ ; 
        import _root_.scalaSci.RichDouble2DArray._ ;
        import _root_.scalaSci.CommonMaths.Mat ; 
        import _root_.scalaSci.CommonMaths.Mat._ ; 
        import _root_.scalaSci.CommonMaths.StaticMathsCommonMaths._ ; 
        """  
scalaExec.Interpreter.GlobalValues.editorPane.setText(cmStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)    

  }
  
  def importJPlots() = {
    GlobalValues.globalInterpreter.interpret( """
             import  _root_.JFplot._;
             import _root_.JFplot.jFigure._;
             """
    )
  }
  
    def injectJPlots()  = {
      val jplotStr = "\n importJPlots\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(jplotStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }
    
  def injectJPlotsDirectly = {
    val jplotsStr = """
            import  _root_.JFplot._;
            import _root_.JFplot.jFigure._;

"""
      scalaExec.Interpreter.GlobalValues.editorPane.setText(jplotsStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
  }

  
  def importIO() = {
    GlobalValues.globalInterpreter.interpret("""
            import java.text.DecimalFormat;
            import System.out._;
            import scalaSci.math.io.XMLMethods._
            import  _root_.scalaSciCommands.BasicCommands; 
            import  _root_.scalaSciCommands.BasicCommands._; 
            import _root_.scalaSci.math.io.MatIO._ ; 
            import _root_.scalaSci.math.io.ioUtils._ ; 
            """)
  }
          
  
  def injectIO()  = {
      val ioStr = "\n importIO\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(ioStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }
    
 def injectIODirectly() = {
   val ioStr = """
            import java.text.DecimalFormat;
            import System.out._;
            import scalaSci.math.io.XMLMethods._
            import  _root_.scalaSciCommands.BasicCommands; 
            import  _root_.scalaSciCommands.BasicCommands._; 
            import _root_.scalaSci.math.io.MatIO._ ; 
            import _root_.scalaSci.math.io.ioUtils._ ; 
   """
    scalaExec.Interpreter.GlobalValues.editorPane.setText(ioStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
 } 

def importLAPACK() = {
  GlobalValues.globalInterpreter.interpret("""
     import _root_.org.netlib.lapack.LAPACK;
     import _root_.scalaSci.ILapack._;
     import _root_.scalaSci.ILapack.Eig; 
      """
    );
}

  
  def injectLAPACK()  = {
      val LAPACKStr = "\n importLAPACK\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(LAPACKStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }

  def injectLAPACKDirectly = {
     val LAPACKStr = """
      import _root_.org.netlib.lapack.LAPACK
     import _root_.scalaSci.ILapack._
     import _root_.scalaSci.ILapack.Eig
      """
      scalaExec.Interpreter.GlobalValues.editorPane.setText(LAPACKStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    
  }
  
  def importComputerAlgebra() = {
  GlobalValues.globalInterpreter.interpret("""
           import _root_.scala._;
           import  _root_.scala.collection._; 
           import numerics.Numerics._; 
           import org.matheclipse.core.eval.EvalUtilities;
           import org.matheclipse.core.expression.F;
           import org.matheclipse.core.form.output.OutputFormFactory;
           import org.matheclipse.core.form.output.StringBufferWriter;
           import org.matheclipse.core.interfaces.IExpr;
            import _root_.scalaSci.Complex; 
            import _root_.scalaSci.Complex._; 
            import _root_.PatRec.PatternRecognition._; 
            """)
   
  }  
  
  
  def injectComputerAlgebra()  = {
      val computerAlgebraStr = "\n importComputerAlgebra\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(computerAlgebraStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }

  def injectComputerAlgebraDirectly() = {
    val computerAlgebraStr = """
             import _root_.scala._;
           import  _root_.scala.collection._; 
           import numerics.Numerics._; 
           import org.matheclipse.core.eval.EvalUtilities;
           import org.matheclipse.core.expression.F;
           import org.matheclipse.core.form.output.OutputFormFactory;
           import org.matheclipse.core.form.output.StringBufferWriter;
           import org.matheclipse.core.interfaces.IExpr;
            import _root_.scalaSci.Complex; 
            import _root_.scalaSci.Complex._; 
            import _root_.PatRec.PatternRecognition._; 
  """
      scalaExec.Interpreter.GlobalValues.editorPane.setText(computerAlgebraStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    
  }
    
  def importApacheCommons() = {
      GlobalValues.globalInterpreter.interpret(GlobalValues.ApacheCommonsImports)
    }
    
  
  def injectApacheCommons()  = {
      val apacheCommonsStr = "\n importApacheCommons\n\n"
      scalaExec.Interpreter.GlobalValues.editorPane.setText(apacheCommonsStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    }

  def injectApacheCommonsDirectly = {
    val apacheCommonsStr = GlobalValues.ApacheCommonsImports
      scalaExec.Interpreter.GlobalValues.editorPane.setText(apacheCommonsStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    
  }  
  
  def injectBioJavaDirectly = {
    var biojavaStr = " \n import org.biojava.nbio.alignment.Alignments \n"+
"import org.biojava.nbio.alignment.Alignments.PairwiseSequenceAlignerType \n"+
"import org.biojava.nbio.alignment.SimpleGapPenalty \n"+
"import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper \n"+
"import org.biojava.nbio.alignment.template.GapPenalty \n"+
"import org.biojava.nbio.alignment.template.PairwiseSequenceAligner \n"+
"import org.biojava.nbio.core.alignment.template.SequencePair \n"+
"import org.biojava.nbio.core.alignment.template.SubstitutionMatrix \n"+
"import org.biojava.nbio.core.sequence.ProteinSequence \n"+
"import org.biojava.nbio.core.sequence.compound.AminoAcidCompound \n"+
"import org.biojava.nbio.core.sequence.io.FastaReaderHelper\n"


    scalaExec.Interpreter.GlobalValues.editorPane.setText(biojavaStr+scalaExec.Interpreter.GlobalValues.editorPane.getText)
    
  }  
}            
            
    



