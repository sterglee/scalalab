package scalaSci

import scalaSci.math.LinearAlgebra.LinearAlgebra
import JSci.maths.wavelet.Signal
import scalaExec.Interpreter.GlobalValues
import scalaSci.math.array.DoubleArray
import java.util._
import Jama._



object StaticMathsD2Das1D extends AnyRef with scalaSci.StaticScalaSciCommonOps[scalaSci.D2Das1DMat]  with scalaSci.StaticScalaSciGlobal  {
  
  // SOS:  these implementations are stub for now!
  def LU_solve(A: scalaSci.D2Das1DMat,b: scalaSci.D2Das1DMat): scalaSci.D2Das1DMat = new scalaSci.D2Das1DMat(1,1)
  def corr(A: scalaSci.D2Das1DMat,B: scalaSci.D2Das1DMat): scalaSci.D2Das1DMat = new scalaSci.D2Das1DMat(1,1)
  def correlation(A: scalaSci.D2Das1DMat,B: scalaSci.D2Das1DMat): scalaSci.D2Das1DMat = new scalaSci.D2Das1DMat(1,1)
  def cov(A: scalaSci.D2Das1DMat,B: scalaSci.D2Das1DMat): scalaSci.D2Das1DMat = new scalaSci.D2Das1DMat(1,1)
  def covariance(A: scalaSci.D2Das1DMat,B: scalaSci.D2Das1DMat): scalaSci.D2Das1DMat = new scalaSci.D2Das1DMat(1,1)
  def find(A: scalaSci.D2Das1DMat): Array[Array[Int]] = Array.ofDim[Int](1,1)

  
}
