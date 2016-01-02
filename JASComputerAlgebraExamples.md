# Big Integer Test #

`The following examples exercise some parts of the internal Java Algebra System of ScalaLab. `

```




import edu.jas.arith.BigInteger
import edu.jas.arith.BigInteger._
import edu.jas.arith.Roots._


import junit.framework.Test
import junit.framework.TestCase
import junit.framework.TestSuite
import junit.framework.Assert._

var  x = new  BigInteger("4567863332")

var r2x =  root(x, 2)  // we take the square root of that BigInteger
var r3x = root(x, 3)


// test random integer
var a = BigInteger.IRAND(500)
var b = new BigInteger(""+a)
var c = BigInteger.IDIF(b, a)
var d = new BigInteger(b.getVal())


// test addition
val bitlen = 100
a = BigInteger.IRAND( bitlen )
b = BigInteger.ISUM(a, a)
c = BigInteger.IDIF(b, a)
d = BigInteger.ISUM(a, BigInteger.ZERO)
d = BigInteger.IDIF(a, BigInteger.ZERO)
d = BigInteger.IDIF(a, a)

// test multiplication
a = BigInteger.IRAND( bitlen )
b = BigInteger.IPROD(a, a)
c = BigInteger.IQ(b, a)
d = BigInteger.IPROD(a, BigInteger.ONE)
d = BigInteger.IQ(a, BigInteger.ONE)

// test distributive law
var fac = new BigInteger()
a = fac.random(bitlen)
b = fac.random(bitlen)
c = fac.random(bitlen)
d = a.multiply( b.sum(c))
var e = a.multiply(b).sum(a.multiply(c))   // should be equal to d
 
 // test gcd
 a = BigInteger.IRAND(bitlen)
 b = BigInteger.IRAND(bitlen)
 c = BigInteger.IGCD(a, b)  // ~1

 var qr = BigInteger.IQR(a, c)
  d = BigInteger.IPROD( qr(0), c)   
  assertEquals("a = gcd(a,b)*q1",a, d)
  assertEquals("a/gcd(a,b) = q*x+0", qr(1), BigInteger.ZERO)

  c = BigInteger.IRAND( bitlen*4 )
  a = BigInteger.IPROD(a, c)
  b = BigInteger.IPROD(b, c)
  c = BigInteger.IGCD(a, b)   // = c

  qr =  BigInteger.IQR(a, c)
  d = BigInteger.IPROD( qr(0), c)
  assertEquals(" a = gcd(a, b)*q1", a, d)
  assertEquals("a/gcd(a, b) = q*x+0", qr(1), BigInteger.ZERO)

  qr = BigInteger.IQR(b, c)
  d = BigInteger.IPROD( qr(0), c)
  assertEquals("b = cda, b)*q1", b, d)
  assertEquals("b/gcd(a, b = q*x+0", qr(1), BigInteger.ZERO)
   
                
```

## Big Rational Test ##

```



import edu.jas.arith.BigRational
import edu.jas.arith.BigRational._
import edu.jas.arith.BigInteger
import edu.jas.arith.BigInteger._
import edu.jas.arith.Roots._


import junit.framework.Test
import junit.framework.TestCase
import junit.framework.TestSuite
import junit.framework.Assert._

// test constants
var a = BigRational.ZERO
var b = BigRational.ONE
var c = BigRational.RNDIF(b, b)

// test constructor
a = new BigRational("6/8")
b = new BigRational("3/4")

assertEquals("6/8 = 3/4", a, b)
var s = "6/1111111111111111111111111111111111111111111"
a = new BigRational(s)
var t = a.toString

a = new BigRational(1)
b = new BigRational(-1)
c = BigRational.RNSUM(b, a)

a.isONE  // should return true
s = "1.500000000"
a = new BigRational( s )
b = new BigRational( "3/2" )
assertEquals("decimalConstr = b ",a,b)

s = "-1.500000000"
a = new BigRational( s )
b = new BigRational( "-3/2" )
assertEquals("decimalConstr = b ",a,b)

s = "0.750000000"
a = new BigRational( s )
b = new BigRational( "3/4" )
assertEquals("decimalConstr = b ",a,b)

s = "0.333333333"
a = new BigRational( s )
t = a.toString(9)
assertEquals("decimalConstr = b " + t,s,t)

s = "-0.000033333"
a = new BigRational( s )
t = a.toString(9)
assertEquals("decimalConstr = b " + t,s,t)


// test random rationals

a = BigRational.RNRAND( 500 )
b = new BigRational(""+a)
c = BigRational.RNDIF(b, a)

assertEquals("a-b = 0",c,BigRational.ZERO)

```





