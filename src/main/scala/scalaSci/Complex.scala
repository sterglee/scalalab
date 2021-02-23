package scalaSci


import java.lang.Math._


// a Complex class
class Complex(Re: Double, Im: Double) 
  {
 import Complex._
 
  // public access of the object fields for efficiency, var access about 3 times faster than final def access 
    var re = this.Re
    var im = this.Im
    final def getReal() = re
    final def getIm() = im
    final def length() = 2
    final def size() = 2
  
  
  // create a complex number given only the real part
    final def this(real: Double) = this(real, 0)
    
    final def - (that: Complex)
        = new Complex(Re - that.re, Im - that.im)
  
    final def unary_- = new Complex(-Re, -Im)
  
   final def * (that: Complex)
        = new Complex(re*that.re - Im*that.im,
                      Im*that.re + Re*that.im)
  
    final def - (that: Double)
        = new Complex(Re - that, Im)
 
   final def * (that: Double)
        = new Complex(re*that - Im,
                      Im*that + Re*that)
      
  final def ^ (exponent: Int): Complex
        = if(exponent == 0) new Complex(1.0, 0.0)
          else if(exponent == 1) this
          else this * (this^(exponent-1))
    final def toPolar() = (radius, theta)
    final def radius() = java.lang.Math.sqrt(Re*Re + Im*Im)
    final def theta() = java.lang.Math.atan2(Re, Im)

  
final def abs(): Double  =   {
    Math.sqrt(Re*Re+Im*Im)
    }
    
    final def + (that: Complex)
        = new Complex(Re + that.re, Im + that.im)
    
  final def + (that: Double)
        = new Complex(Re + that, Im )

   final def  multiply(factor: Complex) =  {
        new  Complex(Re * factor.re - Im * factor.im,
                             Re * factor.im + Im * factor.re)
    }

    final def  multiply(factor: Double) =  {
        new Complex(Re * factor, Im * factor)
    }
    
// return the conjugate of this complex number
  final def conjugate =  new Complex(Re, -Im)
  final def conj = conjugate
  
final def /(divisor: Complex): Complex = {
     var a = Re; var b = Im; var c = divisor.re; var d = divisor.im
      var denom = c*c+d*d
          
      return new Complex( (a*c+b*d)/denom, (b*c-a*d)/denom)
 
  }
                        
  
  final def /  (divisor: Double) =  {
        new Complex(Re / divisor, Im  / divisor)
    }

 
  final def reciprocal(): Complex  =  {
        var denom = Re*Re+Im*Im
        new Complex(Re/denom, -Im/denom)
        }
  
  override final def equals(that : Any) = that match {
    case that : Complex => this.re == that.re && this.im == that.im
    case real : Double => this.re == real && this.im == 0
    case real : Int => this.re == real && this.im == 0
    case real : Short => this.re == real && this.im == 0
    case real : Long => this.re == real && this.im == 0
    case real : Float => this.re == real && this.im == 0
    case _ => false
  }


    /**
     * Get a hashCode for the complex number.
     * Any {@code Double.NaN} value in real or imaginary part produces
     * the same hash code {@code 7}.
     *
     * @return a hash code value for this object.
     */
    override final def  hashCode(): Int  = {
        return 37 * (17 * org.apache.commons.math3.util.MathUtils.hash(im) +org.apache.commons.math3.util.MathUtils.hash(re))
    }
    
  
  override final def toString = {
      (re, im) match {
        case (0, 0) => "zero"
        case (re, 0) => re.toString
        case (0, im) => im + "i"
        case _ => if (im > 0.0)  re + "+"  + im + "i" 
                       else
                          re+" "+im+"i"
      }
    }
   
     final def  add(addend: Complex) =  {
         new  Complex(Re + addend.re, Im + addend.im)
    }
   
    final def  add(addend: Double) =  {
        new  Complex(Re + addend, Im)
    }

    final def  subtract(subtrahend: Complex ): Complex  = {
        return new Complex(Re - subtrahend.re, Im - subtrahend.im)
    }

    final def subtract(subtrahend: Double): Complex =  {
        return  new  Complex(Re - subtrahend, Im)
    }

    final def  negate() =  {
        new  Complex(-Re, -Im)
    }


  final def  sqrt1z() = {
        new  Complex(1.0, 0.0).subtract(this.multiply(this)).sqrt()
      }
   
   final def  acos() =  {
      this.add(this.sqrt1z().multiply(I)).log().multiply(I.negate())
    }

    final def asin() = {
        sqrt1z().add(this.multiply(I)).log().multiply(I.negate())
    }

    
    final def  atan() =  {
  this.add(I)./(I.subtract(this)).log().multiply(I./(new  Complex(2.0, 0.0)))
    }

    final def  cos() =  {
      new Complex(Math.cos(Re) * Math.cosh(Im),
                             -Math.sin(Re) * Math.sinh(Im))
    }

    final def cosh() =  {
        new  Complex(Math.cosh(Re) * Math.cos(Im),
                             Math.sinh(Re) * Math.sin(Im))
    }

    final def  exp() =  {
        var  expReal = Math.exp(Re)
        new Complex(expReal *  Math.cos(Im),
                             expReal * Math.sin(Im))
  }
    
    final def  log() =  {
        new Complex(Math.log(abs()),  Math.atan2(Im,  Re))
    }
    
  
    final def  pow(x: Complex ) =   {
        this.log().multiply(x).exp()
    }

     final def pow(x: Double) =  {
         this.log().multiply(x).exp()
    }

    final def sin() =  {
        new   Complex(Math.sin(Re) * Math.cosh(Im),
                             -Math.cos(Re) * Math.sinh(Im))
    }

    final def sinh() =  {
       new Complex(Math.sinh(Re) * Math.cos(Im),
            Math.cosh(Re) * Math.sin(Im))
    }

    final def  sqrt(): Complex  = {
        if (Re == 0.0 && Im== 0.0) {
            return new Complex(0.0, 0.0)
        }

        var  t = Math.sqrt((Math.abs(Re) + abs()) / 2.0)
        if (Re >= 0.0) {
            return new Complex(t, Im / (2.0 * t))
        } else {
            return new  Complex(Math.abs(Im) / (2.0 * t),
                                 Math.copySign(1d, Im) * t)
        }
    }

    final def  tan(): Complex  = {
        if (Im > 20.0) {
            return new  Complex(0.0, 1.0)
        }
        if (Im < -20.0) {
            return new  Complex(0.0, -1.0)
        }

        var real2 = 2.0 * Re
        var imaginary2 = 2.0 * Im
        var  d = Math.cos(real2) + Math.cosh(imaginary2)

        return new  Complex(Math.sin(real2) / d, Math.sinh(imaginary2) / d)
    }

    
  final def tanh(): Complex  = {
        if (Re> 20.0) {
            return new Complex(1.0, 0.0)
        }
        if (Re < -20.0) {
            return new Complex(-1.0, 0.0)
        }
        var  real2 = 2.0 * Re
        var  imaginary2 = 2.0 * Im
        var  d = Math.cosh(real2) + Math.cos(imaginary2)

        return new  Complex(Math.sinh(real2) / d,
                             Math.sin(imaginary2) / d)
    }
  
 final def  getArg() =  {
        org.apache.commons.math3.util.FastMath.atan2(Im, Re)
    }

  final def  getArgument() =  {
        org.apache.commons.math3.util.FastMath.atan2(Im, Re)
    }

 final def nthRoot(n: Int): java.util.ArrayList[Complex] =  {

        var  result = new java.util.ArrayList[Complex]();

        var  nthRootOfAbs = Math.pow(abs(), 1.0 / n)

        // Compute nth roots of complex number with k = 0, 1, ... n-1
        var  nthPhi = getArgument() / n
        var  slice = 2 * Math.PI / n
        var  innerPart = nthPhi
        var k=0
        while (k< n) {
            // inner part
            var realPart = nthRootOfAbs *  Math.cos(innerPart)
            var  imaginaryPart = nthRootOfAbs *  Math.sin(innerPart)
            result.add(new Complex(realPart, imaginaryPart))
            innerPart += slice
            k += 1
        }

        return result
    }


    }





  object Complex{

   // the square root of -1. A number representing "0.0+1.0i"
 val  I = new Complex(0.0, 1.0)
 // a complex number representing "1.0+0.0*i"
 val ONE = new Complex(1.0, 0.0)
 // a complex number representing "0.0+0.0*i"
 val ZERO = new Complex(0.0, 0.0)
 
                       
  final def apply(re: Double,  im: Double) = new Complex(re, im)
   
  final def sqrt(x: Complex) = x.sqrt
  final def sqrt1z(x: Complex) = x.sqrt1z
  final def abs(x: Complex) = x.abs
  final def sin(x: Complex) = x.sin
  final def asin(x: Complex) = x.asin
  final def sinh(x: Complex) = x.sinh
  final def cos(x: Complex) = x.cos
  final def acos(x: Complex) = x.acos
  final def cosh(x: Complex) = x.cosh
  final def tan(x: Complex) = x.tan
  final def atan(x: Complex) = x.atan
  final def tanh(x: Complex) = x.tanh
  final def exp(x: Complex) = x.exp
  final def log(x: Complex) = x.log
  final def pow(x: Complex, y: Double) = x.pow(y)
  final def getArg(x: Complex) = x.getArg
  final def getArgument(x: Complex) = x.getArgument
 
  }


