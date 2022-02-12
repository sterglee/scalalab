
package scalaSci

// constructs and returns a matrix object of type T and size Nrows X Ncols
object MatrixFactory {

  def apply(T: AnyRef, Nrows: Int, Ncols: Int) =     {
// construct a matrix of a type according  to the T parameter     
    T match {
      case x: scalaSci.RichDouble2DArray =>   new scalaSci.RichDouble2DArray(Nrows, Ncols)
      case x: scalaSci.D2Das1DMat =>   new scalaSci.D2Das1DMat(Nrows, Ncols)
      case x: scalaSci.Matrix =>   new scalaSci.Matrix(Nrows, Ncols)
      case x: scalaSci.Mat =>  new scalaSci.Mat(Nrows, Ncols)
      case x: scalaSci.CommonMaths.Mat =>  new scalaSci.CommonMaths.Mat(Nrows, Ncols)   
      case x: scalaSci.MTJ.Mat =>    new scalaSci.MTJ.Mat(Nrows, Ncols)
      case x: scalaSci.EJML.Mat =>  new scalaSci.EJML.Mat(Nrows, Ncols)
      case x: scalaSci.JBLAS.Mat =>  new scalaSci.JBLAS.Mat(Nrows, Ncols)
          
        // companion object class is passed as type
      case x:Any=>  
          val className = x.getClass.toString
          
          if  (className == "class scalaSci.StaticMaths$")  
              new scalaSci.Mat(Nrows, Ncols) 
          else   
          if (className == "class scalaSci.StaticMathsD2Das1D$")
              new scalaSci.D2Das1DMat(Nrows, Ncols)
          else
         if (className == "class scalaSci.CommonMaths.StaticMathsCommonMaths$")  
              new scalaSci.CommonMaths.Mat(Nrows, Ncols)
         else  
          if (className == "class scalaSci.MTJ.StaticMathsMTJ$")
             new scalaSci.MTJ.Mat(Nrows, Ncols)  
         else   
         if (className == "class scalaSci.EJML.StaticMathsEJML$")
              new scalaSci.EJML.Mat(Nrows, Ncols)
         else 
         if  (className == "class scalaSci.JBLAS.StaticMathsJBLAS$") 
              new scalaSci.JBLAS.Mat(Nrows, Ncols)
          else   {
        println("other of type "+ x.getClass);  
       new scalaSci.Mat(Nrows, Ncols)  
          }  
         
    }  
  } 
}
