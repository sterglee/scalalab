package scalaUtils

import java.net.{ MalformedURLException, URL }
import scala.tools.nsc.util.{ SourceFile, BatchSourceFile, ClassPath }
import java.io.{ File, PrintWriter, StringWriter, Writer }

object ScalaUtilitiesObject {

  def fileToURL(f: File): Option[URL] =
      try { Some(f.toURL) }
      catch { case e: Throwable => Console.println(e); None }
    def paths(str: String): List[URL] =
      for (
        file <- ClassPath.expandPath(str) map (new File(_)) if file.exists;
        val url = fileToURL(file); if !url.isEmpty
      ) yield url.get

  def jars(dirs: String): List[URL] =
      for (
        libdir <- ClassPath.expandPath(dirs) map (new File(_)) if libdir.isDirectory;
        jarfile <- libdir.listFiles if jarfile.isFile && jarfile.getName.endsWith(".jar");
        val url = fileToURL(jarfile); if !url.isEmpty
      ) yield url.get

def thread( body: =>Unit): Thread = {
  val t = new Thread {
    override def run() = body
  }
  t.start()
  t
  }
}

