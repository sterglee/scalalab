package scalaExec.Interpreter
import scala.concurrent._

object ConcurrencyUtils {

    def execute( body: => Unit) = ExecutionContext.global.execute(
        new Runnable {  def run() = body }  
  )
  
}
