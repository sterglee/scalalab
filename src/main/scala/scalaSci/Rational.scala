package scalaSci

class Rational(n: Int, d: Int) {

    final def length() = 2
    final def size() = 2
  
  require(d != 0)

  private val g = gcd(n.abs, d.abs)
  val numer = n / g
  val denom = d / g

  def this(n: Int) = this(n, 1)

  def + (that: Rational): Rational =
    new Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )

  def + (i: Int): Rational =
    new Rational(numer + i * denom, denom)

  def - (that: Rational): Rational =
    new Rational(
      numer * that.denom - that.numer * denom,
      denom * that.denom
    )

  def - (i: Int): Rational =
    new Rational(numer - i * denom, denom)

  def * (that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  def * (i: Int): Rational =
    new Rational(numer * i, denom)

  def / (that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)

  def / (i: Int): Rational =
    new Rational(numer, denom * i)

  override def toString = numer +"/"+ denom

  private def gcd(a: Int, b: Int): Int = 
    if (b == 0) a else gcd(b, a % b)
}

object Rational {
implicit def intToRational(x: Int) = { new scalaSci.Rational(x) }
}

object Main {

    implicit def intToRational(x: Int) = new Rational(x) 
         //
  def main(args: Array[String]) {
    val x = new Rational(2, 3)
    println("x [" + x + "]")
    println("x * x [" + (x * x) + "]")
    println("x * 2 [" + (x * 2) + "]")

    implicit def intToRational(x: Int) = new Rational(x)
    val r = new Rational(2,3)
    println("2 * r [" + (2 * r) + "]")
  }
}
