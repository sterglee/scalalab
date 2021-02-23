package Do

object Do {
  def apply(body: => Unit) = new DoBody(body)
}


class DoBody ( body: => Unit) 
{
  def until(cond: => Boolean) : Unit = 
  {
   body
   while (!cond)
    body
 }
}

