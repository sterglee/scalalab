package rsyntaxEdit;

import scalaExec.Interpreter.GlobalValues;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.fife.ui.autocomplete.*;

// this class supports the "Global" completion mode
public  class GCompletionProvider {

  public static void installScalaCompletion() {
    GlobalValues.provider = createScalaCompletionProvider();
    
    AutoCompletion ac = new AutoCompletion(GlobalValues.provider);
    ac.install(GlobalValues.globalRSyntaxEditorPane);
  }

  public static void installAutoCompletion() {
    GlobalValues.provider = createCompletionProvider();
    
    AutoCompletion ac = new AutoCompletion(GlobalValues.provider);
    ac.install(GlobalValues.globalRSyntaxEditorPane);
  }
    
  // create a completion provider capable of displaying the results of the Scala completer
  static DefaultCompletionProvider createScalaCompletionProvider() {
      DefaultCompletionProvider provider = new DefaultCompletionProvider();

      // add the completion results collected with the Scala completer to the provider
        Iterator <String> elems = scalaExec.Interpreter.GlobalValues.scalaResultsForCompletion.iterator();
        while (elems.hasNext()) {
            String currentCompletion = elems.next();
            provider.addCompletion(new BasicCompletion(provider,  currentCompletion));
        }
        GlobalValues.provider = provider;
        
   AutoCompletion ac = new AutoCompletion(GlobalValues.provider);
    ac.install(GlobalValues.globalRSyntaxEditorPane);
        
        return provider;
  }
  
  
  // create a completion provider that is filled statically with some basic completion strings
    static DefaultCompletionProvider createCompletionProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        
        String JavaCompletions [] = {"abstract", "assert", "break", "case", "catch", "class", 
            "const", "continue", "default", "do", "else", "enum", "extends",
    "final", "finally", "for", "goto", "if", "implements", "import", "instanceof", "interface", "native", "new", "package", "private", "protected",
    "public", "return", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", 
    "throws", "transient", "try", "void", "volatile", "while", "int", "double", "char", "short", "long",
        "println"};
        
        
        String ScalaCompletions [] = { "abstract", "boolean", "byte", "case", "catch", "char", "class", "def", "do", "double", "else", "extends", "false", "final",
    "finally", "float", "for", "forSome", "if", "implicit", "import", "int", "lazy", "long", "match", "new", "null", "object", "override", "package",
    "private", "protected", "requires", "return", "sealed", "short", "super", "this", "throw", "trait", "true", "try", "type", "val", "var", "while", "with", "yield"};
 
 String ScalaSciCompletions [] = { 
     "AAD (\"MatrixSpec: String\") // constructs a RichDouble2DArray from a String description of its values",
     // constructors for Mat
     "Mat(nRows: Int, nCols: int)  ",
     "Mat(nRows: Int, nCols: int, defaultInitValue: Double)",
     "Mat(vals: Array[Array[Double]])",
     "Mat(vals: Array[Double])",
     "Mat(A: Mat)",
           // constructors for Matrix
     "Matrix(nRows: Int, nCols: int)  ",
     "Matrix(nRows: Int, nCols: int, defaultInitValue: Double)",
     "Matrix(vals: Array[Array[Double]])",
     "Matrix(vals: Array[Double])",
     "Matrix(A: Mat)",
            // constructors for RichDouble1DArray
     "RichDouble1DArray( v: Array[Double])", 
          // constructors for RichDouble2DArray
     "RichDouble2DArray( v: Array[Array[Double]])",
     "RichDouble2DArray(nRows: Int, nCols: Int)",
     "RichDouble2DArray( jamaMat: scalaSci.Mat)",
     "RichDouble2DArray( numalMat: scalaSci.Mat)",
     "RichDouble2DArray( ejmlMat: scalaSci.EJML.Mat)",
     "RichDouble2DArray( mtjMat: scalaSci.MTJ.Mat)",
     "RichDouble2DArray( commonsMat: scalaSci.CommonMaths.Mat)",
     
     "toDoubleArray(A: specificMatrix): Array[Array[Double]]",
     
      "sin", "cos", "tan", "tanh", "cosh", "sinh", "log", "exp", "pow", 
      "fill0(nrows: Int, ncols:Int): Mat",
      "ones0(nrows: Int, ncols: Int): Mat", 
      "eye0(nrows: Int, ncols: Int): Mat", 
      "eye1(nrows: Int, ncols: Int): Mat", 
      "eye(nrows: Int, ncols: Int): Mat", 
      "zeros0(nrows: Int, ncols: Int): Mat",
      "rand0(nrows: Int, ncols: Int): Mat",
      "fill(nrows: Int, ncols:Int): RichDouble2DArray",
      "ones(nrows: Int, ncols: Int): RichDouble2DArray", 
      "zeros(nrows: Int, ncols: Int): RichDouble2DArray",
      "rand(nrows: Int, ncols: Int): RichDouble2DArray",
      "vrand(n: Int): Vec",
      "vones(n: Int): Vec", 
      "vzeros(n: Int): Vec", 
      "vfill(n: Int): Vec",
      "vfill(n: Int, value: Double): Vec", 
      "inc(begin: Double, pitch: Double,  end: Double): Vec ", 
      "linspace(startv: Double, endv: Double): Vec", 
       "linspace(startv: Double, endv: Double, nP: Int): Vec",
       "logspace(startOrig: Double, endOrig: Double, nP: Int): Vec",
       "logspace(startOrig: Double, endOrig: Double, nP: Int, logBase: Double): Vec",
       
       // plotting routines
       "plot(double [] x, double [] y, double [] z, int low, int high, Color color, String name)",
       "plot(double [] x, double [] y, double [] z, int low, int high,  String name)",
       "plot(double [] x, double [] y, double [] z, int low, int high)",
       "plot(double [] x, double [] y,  int low, int high,  String name)",
       "plot(double [] x, double [] y,  int low, int high,  Color color, String name)",
       "plot(double [] x, double [] y,  int low, int high)",
       "plot(double [] x, double [] y,  int low, int high, Color color) ",
       "plot3d_line(Object  x,  Object y,  Object z, String name) ",
       "splinePlot( double [] xvals, double [] yvals, int NP, Color color, String name)",
       "splinePlot( double [] xvals, double [] yvals, int NP)",
       "splinePlot( double [] xvals, double [] yvals, int NP, String name)",
       "splinePlot( double [] xvals, double [] yvals, int NP, Color color)",
       "plot(double [] x, double [] y, double [] z, Color color, String name)",
       "plotMarkedLine(String name, Color color, double [] x, double [] y)",
       "plotMarkedLine(double [] x, double [] y)",
       "plotMarkedLine(Vec x, Vec y)",
       "plotMarkedLine(Vec x, Vec y, Color c)",
       "plotPoints(String name, Color color, double []x, double []y)",
       "plotPoints(String name, Color color, Vec x, Vec y)",
       "plotPoints(String name, Color color, char mark, Font font, int skipPoints,  double []x, double []y)"+
       "plotPoints(double []x, double []y)",
       "plot(float  [] x, float [] y, float[] z, Color color, String name) ",
       "plot(double [] x, double [] y, double [] z, String name)",
       "plot(float  [] x, float [] y, float[] z, String name) ",
       "plot(double [] x, double [] y, double [] z)",
       "plot(float  [] x, float [] y, float[] z)",
       "plot(double [][] x)",
       "plot(double [][] x, String name)",
       "plot(double [][] x, Color color, String name)",
       "plot(double [][] x, Color color)",
       "plot(float  [] []x)",
       "plot(double [] x, double [] y, double [][] z)",
       "plot(float  [] x, float [] y, float[][] z)",
       "setMarkChar(char ch)",
       "plotMarks(Vec t,  Vec x)",
        "plotMarks(double [] t,  Vec x)",
        "plotMarks(Vec t,  double [] x)",
       "plotMarks(double []  t,  double [] x)",
       "plotMarks(Vec t,  Vec x, Color color, String name)",
       "plotMarks(double []  t,  double [] x, Color color, String name)",
       "plot(Signal sig)",
       "plot(Object  x,  Object y,  Object z, String name)",
       "plot3d_bar(double [] x, double [] y, double [] z, String name)",
       "plot3d_bar(float  [] x, float [] y, float[] z, String name)",
       "surf(double [] x, double [] y, double [][]z,  String name)",
       "surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name)",
       "surf(Vec  x, double [] y, double [][]z,  String name)",
       "surf(double []  x, Vec  y, double [][]z,  String name)",
       "surf(Vec x, Vec  y, double [][]z,  String name)",
       "surf(float  [] x, float [] y, float[] []z, String name)",
       "surf(double [] x, double [] y, double [][]z,  Color color, String name)",
       "surf(float  [] x, float [] y, float[] []z, Color color, String name)",
       "surf(Vec  x, double [] y, double [][]z,   Color color, String name)",
       "surf(double []  x, Vec  y, double [][]z, Color color, String name)",
       "surf(Vec x, Vec  y, double [][]z,  Color color, String name)",
       "surf(double [] x, double [] y, double [][]z)",
       "surf(float  [] x, float [] y, float[] []z)",
       "surf(double [] x, double [] y, double [][]z, Color color)",
       "surf(float  [] x, float [] y, float[] []z, Color color)",
       "plot2d_scatter(Object  x,  Object y, String name)",
       "plot2d_scatter(double []  x,  double [] y, String name)",
       "plot2d_scatter(float  [] x, float [] y, String name)",
       "plot2d_line(Object  x,  Object y, String name)",
       "plot2d_line(double []  x,  double [] y, String name)",
       "plot(Vec x, Vec y, char type, String name)",
       "plot(double [] x, double [] y, char type)",
       "plot(Vec  x, Vec y, char plotType)",
       "plot(Vec x,  Vec y)",
       "plot(Vec x,  double[] y)",
       "plot(double []  x,  Vec y)",
       "plot(double [] x, double [] y, String  typeStr, String name)",
       "plot(double [] x, double [] y, String  typeStr, int lineWidth)",
       "plot(double [] x, double [] y, String  typeStr, String name, int lineWidth)",
       "plot(Vec  x, Vec  y, Color color, String  typeStr, String name, int lineWidth)",
       "plot(double []  x, Vec  y, Color color, String  typeStr, String name, int lineWidth)",
       "plot(Vec  x, double []  y, Color color, String  typeStr, String name, int lineWidth)",
       "plot(double [] x, double [] y, Color color, String  typeStr, String name, int lineWidth)",
       "plot(double [] x,  double []  y, String  typeStr)",
       "plot(Vec x,  Vec  y, String  titleOfPlot)",
       "plot(Vec x,  Vec  y, String  typeStr, String name)",
       "plot(Vec x,  Vec  y, String  typeStr, String name, int lineWidth)",
       "plot(Vec x,  Vec  y, String  typeStr,  int lineWidth)",
       "plot(Vec  x,  Vec  y, boolean dotted)",
       "plot(double [] x, double [] y, double [] z, Color color)",
       "plot(float  [] x, float [] y, float []z, Color color)",
       "plot(Vec x)",
       "plot(Vec x, String title)",
       "plot(Vec x, Color color, String title)",
       "plot(Vec  x,  Color color)",
       "plot( String name, Vec x, Vec y)",
       "plot(Vec x, Vec y, String name, Color color)",
       "plot(Vec x, Vec y, Color color, String name)",
       "plot(Vec x, Vec y, Color color)",
       "plot(Vec x, Vec y, Vec z, Color color, String name)",
       "plot(Vec x, Vec y, Vec z)",
       "plot(Vec x, Vec y, Vec z, Color color)",
       "plot(Mat x)",
       "plot(Mat x, Color color)",
       "plot(Mat x, Mat y)",
       "plot(Mat x, Mat y, String name)",
       "plot(Mat x, Mat y, String name, Color color)",
       "plot(Mat x, Mat y, Color color, String name)",
       "plot(Matrix x)",
       "plot(Matrix x, Color color)",
       "plot(Matrix x, Matrix y)",
       "plot(Matrix x, Matrix y, String name)",
       "plot(Matrix x, Matrix y, String name, Color color)",
       "plot(Matrix x, Matrix y, Color color, String name)",
       "plot( String name, double []  x,  double [] y)",
       "plot(double []  x,  double [] y,  String name, Color color)",
       "plot(float  [] x, float [] y, String name, Color color)",
       "plot(double []  x,  double [] y,  Color color, String name)",
       "plot(float  [] x, float [] y, Color color, String name)",
       "plot(double []  x,  double [] y)",
       "plot(float  [] x, float [] y)",
       "setName(String name)",
       "plot(double []  x,  double [] y, Color color )",
       "plot(float  [] x, float [] y, Color color)",
       "plot(double []  x)",
       "plot(float  [] x)",
       "plot(double []  x, Color color )",
       "plot(float  [] x,  Color color)",
       "plot(double []  x, String name)",
       "rplot(double []  x, String name)",
       "plot(float  [] x, String name)",
       "plot(double []  x,  Color color, String name)",
       "plot(float  [] x, Color color, String name)",
       "plot2d_staircase(Object  x,  Object y, String name)",
       "plot2d_staircase(double []  x,  double [] y, String name)",
       "plot2d_staircase(float  [] x, float [] y, String name)",
       "plot2d_staircase(double []  x,  double [] y, Color color, String name)",
       "plot2d_staircase(float  [] x, float [] y, Color color, String name)",
       "plot2d_bar(double []  x,  double [] y, String name)",
       "plot2d_bar(double []  x,  double [] y, Color color, String name)",
       "plot2d_contour(double [][]XY, String name)",
       "plot2d_countour(float  [][] xy, String name)",
       "plot2d_contour(double [][]XY, Color color, String name)",
       "plot2d_countour(float  [][] xy, Color color, String name)",
       "plot2d_contour(Matrix mXY, String name)",
       "plot2d_contour(Matrix mXY,  Color color, String name)",
       "plot2d_contour(Mat mXY, String name)",
       "plot2d_contour(Mat mXY,  Color color, String name)",
       "plot2d_scalogram(double [][]XY, String name)",
       "plot2d_scalogram(float  [][] xy, String name)",
       "plot2d_scalogram(Matrix mXY, String name)",
       "plot2d_scalogram(Mat mXY, String name)",
       "plot2d_cloud(double [][] sample, int slices_x,int slices_y, String name)",
       "plot2d_cloud(float  [][] sample, int slices_x,int slices_y, String name)",
       "plot2d_cloud(double[][] sample, int slices_x,int slices_y, Color color, String name)",
       "plot2d_cloud(float  [][] sample, int slices_x, int slices_y,  Color color, String name)",
       "plot3d_cloud(Matrix sample, int slices_x,int slices_y,int slices_z, String name)",
       "plot3d_cloud(Matrix sample, int slices_x,int slices_y,int slices_z, Color color, String name)",
       "plot3d_cloud(Mat sample, int slices_x,int slices_y,int slices_z, String name)",
       "plot3d_cloud(Mat sample, int slices_x,int slices_y,int slices_z, Color color, String name)",
       "plot2d_histogram(double [][] xy, int slices_x, String name)",
       "plot2d_histogram(float [][] xy, int slices_x, String name)",
       "plot2d_histogram(double [] x, double [] y, int slices_x, String name)",
       "plot2d_histogram(double [] x, int slices_x, String name)",
       "plot2d_histogram(Vec x, int slices_x, String name)",
       "plot2d_histogram(float [] x, float [] y,  int slices_x, String name)",
       "setAxes(int axe, String axisType)",
       "markX(double  x, double  y)",
       "markX(int x, int y)",
       "markX(double  x, double  y, Color c)",
       "markX(int x, int y, Color c)",
       "mark(char ch, double  x, double  y)",
       "mark(char ch, int x, int y)",
       "mark(char ch, double  x, double  y, Color c)",
       "mark(char ch, int x, int y, Color c)",
       "markX(double  x, double  y, Font f)",
       "markX(int x, int y, Font f)",
       "markX(double  x, double  y, Color c, Font f)",
       "markX(int x, int y, Color c, Font f)",
       "mark(char ch, double  x, double  y, Font f)",
       "mark(char ch, int x, int y, Font f)",
       "mark(char ch, double  x, double  y, Color c, Font f)",
       "mark(char ch, int x, int y, Color c, Font f)",
       "markX(double  x, double  y, int fontSize ) ",
       "markX(int x, int y, int fontSize ) ",
       "markX(double  x, double  y, Color c, int fontSize )",
       "markX(int x, int y, Color c, int fontSize ) ",
       "mark(char ch, double  x, double  y, int fontSize )",
       "mark(char ch, int x, int y, int fontSize ) ",
       "mark(char ch, double  x, double  y, Color c, int fontSize )",
       "mark(char ch, int x, int y, Color c, int fontSize )",
       "plotv(Vector <Double>  x, Vector <Double> y, Vector <Double> z)",
       "plotV(Vector <double [] >  x)",
       "plotv(Vector <Double>  x, Vector <Double> y, Color color)",
       "plotv(Vector <double [] >  x, Color color)",
       "plotv(Vector <Double>  x, Vector <Double> y, Vector <Double> z, String name)",
       "plotV(Vector <double [] >  x, String name)",
       "plotv(Vector <Double>  x, Vector <Double> y, Color color, String name)",
       "plotv(Vector <Double>  x, Vector <Double> y, Vector <Double> z, Color color, String name)",
       "plotV(Vector <double [] >  x, Color color, String name)",
       "fullPlotsOn()  // plots all points thus for large signals can be slow",
       "fastPlotsOn()  // skips points for large signals",
       "setMaximumPlotPoints(int newMaxPointsToPlot)  // sets the limit on the points to plot for large plots ",
       "setMaximumPlotXPoints(int newMaxPointsXToPlot) // controls for 3-D plots the max points at the X-dimension",
       "setMaximumPlotYPoints(int newMaxPointsYToPlot) // controls for 3-D plots the max points at the Y-dimension",
       "getPlot()  //  PlotPanel getPlot()",
       "getXAxis()  //  Axis getXAxis()",
       "getYAxis() //  Axis getYAxis()",
       "getZAxis()  // Axis getZAxis()",
       "scatterPlotsOn() //  turn scatter plots on, in scatter plots points are not connected by a line",
       "linePlotsOn() // turn line plots off, in line plots points are connected by a line",
       "arrToDouble(float [] x)  double [] arrToDouble(float [] x)  // convert float [] array to double []",
       "arrToDoubleDouble(float [][] x) //  double [][] arrToDoubleDouble(float [][] x)  // convert float [][] array to double [][]",
       "subplot( int  p)",
       "subplot2d( int  p)",
       "subplot3d( int  p)",
       "increaseFigTable()  // // increase the size of the global figure table when required",
       "increaseFigTableSpecifiedSize(int specifiedFigNo) //  increase the Figure tables to cover the specified Figure number",
       "initplots()",
       "getFigCount()",
       "newPlot2D()  //  create  a new 2D figure object if we do not focus on an existing one",
       "newPlot3D() // create  a new 3D figure object if we do not focus on an existing one",
       "closeAll()  // closes all the available figure objects",
       "close(String all) // if all==\"all\" closes all figures",
       "getFigureId()  // returns an Id of an unused slot for figure",
       "figure() // constructs a 2d figure object. Returns the figure id",
       " figure2d() // constructs a new figure 2D object with a single subplot panel, i.e. subplot(1,1,1). Returns the figure id",
       "figure3d() // constructs a new figure 3D object with a single subplot panel, i.e. subplot(1,1,1). Returns the figure id",
       "figure(int figId)  // focus on the figure with the identifier figId",
       "figure3d(int figId) //  focus on the figure with the identifier figId",
       "hold(boolean newHoldState)",
       "hold(String newHoldState)",
       "title(String titleStr)",
       "title(int figId, String titleStr)",
       "clf(int figId) clears all the plots from the figure with figId",
       "clf(PlotPanel plotPanel) //  clears all the plots from the reference plotPanel",
       "clf(int figId, int xId, int yId, int plotId)  // clears all the plot with identifier plotId, from the figure with identifier figId, at subplot id: [xId, yId] .The numbering of plot ids starts at 1 (as the fig ids)",
       "clf(int figId, int plotId)  // clears the plot with identifier plotId, from the figure with identifier figId",
       "clf(int figId, int xId, int yId) // clears the plot with identifier plotId, from the figure with identifier figId, at subplot id: [xId, yId]", 
       "createSubplot2D(int rows, int cols, int focusSubPlot, int figureId)",
       "createSubplot3D(int rows, int cols, int focusSubPlot, int figureId)",
       "Subplot2D(int rows, int cols, int focusSubPlot)",
       "Subplot3D(int rows, int cols, int focusSubPlot)",
       "subplot(int rows, int cols, int focusSubPlot)",
       "subplot2D(int rows, int cols, int focusSubPlot)",
       "subplot2d(int rows, int cols, int focusSubPlot)",
       "subplot3D(int rows, int cols, int focusSubPlot)",
       "subplot3d(int rows, int cols, int focusSubPlot)",
       "close()  // closes the current  figure",
       "close(int figId)  // close an explicitly requested figure id. It focuses automatically on the previous figure object which it returns",
       "setColor(int red, int green, int blue)",
       "xlabel(String xLabelStr)",
       "ylabel(String yLabelStr)",
       "zlabel(String zLabelStr)",
       "setLatexColor(Color col) // set the color for LaTex text and return the previous setting",
       "latexLabel(String latex)",
       "latexLabel(String latex, int coordx, int coordy)",
       "latexLabel(String latex, int size,  int coordx, int coordy)",
       "latexLabel(String latex, double coordx, double  coordy)",
       "latexLabel(String latex, int size,  double  coordx, double  coordy)",
       "latexLabel3d(String latex, double nw1, double nw2, double nw3, double se1, double se2, double se3, double sw1, double sw2, double sw3)",
       "latexLabel3d(String latex, int xcoord, int ycoord)",
       "latexRender(String latex)",
       "nplot(xvec: Vec=null, yvec: Vec=null,  xdd: Array[Double]=null, ydd: Array[Double]=null,  yname: String = \"x-y plot\", plotType: String = \"l\", color: Color = Color.GREEN, lineWidth: Int=1 )",
       "splot(expr: String, low: Double, high: Double, color: java.awt.Color=java.awt.Color.RED, linePlotsFlag: Boolean=true, nP: Int = 2000)  // // evaluate and plot the function y=f(x), given by the expression within [low, high]. Note that the variable should be named 'x' '",
       "splot2d(expr: String, lowx: Double, highx: Double, lowy: Double, highy: Double, color: java.awt.Color = java.awt.Color.GREEN,  drawLines: Boolean = true,  nPx: Int = 100, nPy: Int=100)",
       "fplot(f1d:  Double=> Double,  low: Double, high: Double, color:java.awt.Color= java.awt.Color.RED, linePlotsFlag: Boolean=true, nP:Int=2000 )",
       "fplot2d(f2d:  (Double, Double) => Double,  lowx: Double, highx: Double, lowy: Double, highy: Double,  color: java.awt.Color = java.awt.Color.GREEN,  drawLines: Boolean = true,  nPx :Int=100, nPy:Int=100, title: String = \"Surface Plot of function \")",
       
        
        "digamma(xx: Double)   // numerics.Numerics.digamma(xx: Double)  // the standard digamma function ",
       "Bandec(final double[][] a, final int mm1, final int mm2) / /  com.nr.la.Bandec, Object for solving linear equations A*x = b for a band-diagonal matrix A  using LU decomposition",
       "banmul(final double[][] a, final int m1, final int m2, final double[] x, final double[] b)", 
       "solve(final double[] b, final double[] x),  // com.nr.la.Bandec.solve(),  Given a right-hand side vector b[0..n-1], solves the band-diagonal linear equations A*x = b. The solution vector x is returned as x[0..n-1]. ",
       
       "avevar(final double[] data, final doubleW ave, final doubleW var)   //  com.nr.stat.avevar,  given array data[0..n-1], returns its mean as ave and its variance as var",
       "gaussj(final double[][] a, final double[][] b)   // com.nr.la.gaussj(), Linear equation solution by Gauss-Jordan elimination,  The input matrix is a[0..n-1][0..n-1]. b[0..n-1][0..m-1] is   input containing the m right-hand side vectors. On output, a is replaced by its matrix inverse, and b is replaced by  the corresponding set of solution vectors.  ",
         
       
       "tic", "toc",  "sum", "prod", "cumsum", "cumprod",
            "inverse", "inv",  "solve", "rank", "norm1", "norm2", "normF", "dot",
            "Cholesky_L", "Cholesky_SPD", "QR_decomposition", 
            "LU_L", "LU_U", "LU_P", 
            "LU_solve", "det", 
            "vfigure", "vsubplot", "eig", 
            "identity", "diagonal(int, double)", "diagonal(double[])",
             " random(int, int)",
            "fill0", "ones0", "zeros0", 
            "random(int, int, double min, double max)",
            "randomDirac(int m, int n, double[] values, double[] prob) ",
            "randomNormal(int m, int n, double mu, double sigma)",
             "randomChi2(int m, int n, int d) ",
            "randomLogNormal(int m, int n, double mu, double sigma)  ",
            "randomExponential(int m, int n, double lambda)",  
            "randomTriangular(int m, int n, double min, double max)",  
            "randomTriangular(int m, int n, double min, double med, double max) ",
            "randomBeta(int m, int n, double a, double b)",
            "randomCauchy(int m, int n, double mu, double sigma)",
            "randomWeibull(int m, int n, double lambda, double c)  ",
            
            "svd(X: Mat)",
            
           
          "correlation(v1: Mat, v2: Mat): Mat ",
          "corr(v1: Mat, v2: Mat): Mat",
          "covariance(v1: Mat, v2: Mat): Mat",
          "cov(v1: Mat, v2: Mat): Mat",
          
          
          
          
          "readD1Ascii(asciiDataFile: String)",
         "readD2Ascii(asciiDataFile: String)",
          "getFile(String initialDirectory, [String messageToUser])",
          "getFile( String messageToUser, boolean paramIsMessage)",
          "getDouble(String messageOfDialogBox, [double defaultValue])",
          "getInt(String messageOfDialogBox, [int defaultValue]): Int",
          "getString(String messageOfDialogBox, [String defaultValue]): String",
          "load(fileName: String) : Int",
          "save(fileName: String, variableToSave: Double, variableName: String): Boolean",
          "save(fileName: String, variableToSave: RichDouble2DArray, variableName: String): Boolean",
          "matVars()",
          "getMatScalar(scalarName: String): Double",
          "getMatArray(arrayName: String): Array[Array[Double]]",
            
           "dir():Unit",
           "dirR():Unit",
            "cd(\"directory\"):Unit",
            "md(\"directory\")",
            "mkdir(\"directory\")",
            "pwd(): Unit",
            "cls()",
            "whos()",
             "whosv()",
             "whoc()",
                
             "tic()",
             "toc()",
                
           "save(String fileName): Boolean :      // writes to the Matlab .mat file the contents of the scalaSci workspace",
           "save(String fileName, String variableNameToSave): Boolean",
           "format(int decPoints): Int  // controls how many decimal points to display for doubles, sets to decPoints, returns previous setting",
           "setprecision(decPoints: Int): Unit"

 } ;

 
    for (String javaId: JavaCompletions)
          provider.addCompletion(new BasicCompletion(provider,  javaId));
    
    for (String scalaId: ScalaCompletions)
          provider.addCompletion(new BasicCompletion(provider,  scalaId));
    
    for (String scalaSciId: ScalaSciCompletions)
          provider.addCompletion(new BasicCompletion(provider,  scalaSciId));
    
       /*
       try {
           int nclassesScanned =  scanBuiltInLibraryClassesForEditor(provider, 
           * "no/uib", JavaGlobals.mtjColtSGTFile);
           System.out.println("nclasses = "+nclassesScanned);
        } catch (IOException ex) {
            System.out.println("exception in scanBuiltInLibraryClassesForEditor");
            System.out.println(ex.getMessage());
        }
        */
  return provider;
    
  }
    
    public  static int  scanBuiltInLibraryClassesForEditor(DefaultCompletionProvider provider, String libDir, String jarFileName)    throws IOException 
  {
    String  LibDir = libDir;
    int libPathLen = LibDir.length();  // the length of the prefix
    int numLibAllClasses = 0;       // the number of classes scanned from the library
    int numLibAllMethods=0;   // the number of methods from all the classes of the library
    //  get reference to the global Hashtable that holds all loaded classes
  JarEntry je;
  JarInputStream jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
  
  while ((je = jis.getNextJarEntry()) != null)
      {   // while jar file has entries unprocessed
       String nameOfEntry = je.toString();
       
       int strLen = nameOfEntry.length();
       if (strLen > libPathLen) {  // strLen > libPathLen
        nameOfEntry = nameOfEntry.substring(0, libPathLen);   
       if (nameOfEntry.equalsIgnoreCase(LibDir))  { // scan only  LibDir  subdirectory
// make sure we have only slashes, i.e. use Unix path conventions
	String name ='/'+je.getName().replace('\\', '/');
        String   remainingClassName = name.substring(libPathLen+1, name.length());
        
            String javaName = name.replace('/','.');
            int idx = javaName.lastIndexOf(".class");
            int javaNameLen = javaName.length();
            boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
            if (idx != -1 && !classStringIsWithinName) {  // a class file
            javaName = javaName.substring(1, idx);    // remove the first '.'
       
        
               Class  foundClass = null;  
                   try {
           foundClass = GlobalValues.extensionClassLoader.loadClass(javaName);
             }
             catch (ClassNotFoundException e) {
                 foundClass = null;
             }

           if (javaName.indexOf("$") == -1 && javaName.toLowerCase().contains("test")==false) {
        String smallName = javaName.substring(javaName.lastIndexOf(".")+1, javaName.length());
        String nameToInsert = smallName+GlobalValues.smallNameFullPackageSeparator+javaName+"."+smallName;
        provider.addCompletion(new BasicCompletion(provider, nameToInsert)); //smallNamenameToInsert));
        numLibAllClasses++;
           }

        if (foundClass != null)  {
            Method [] classMethods = foundClass.getDeclaredMethods();
            for (Method currentMethod: classMethods) {
                if (Modifier.isPublic(currentMethod.getModifiers() )) {
        String fullName = currentMethod.toString();            
        String methodAndParams =fullName.substring(fullName.indexOf(javaName), fullName.length());
        String methodName = currentMethod.getName()+GlobalValues.smallNameFullPackageSeparator+methodAndParams;
        
        provider.addCompletion(new BasicCompletion(provider, methodName)); //smallNamenameToInsert));
        numLibAllMethods++;
                         
               }
          }
        } 
            }   // a class file
            
         }   // scan only  LibDir  subdirectory
       
    } // strLen > libPathLen
  } // while jar file has entries unprocessed
  
 jis.close();
  
 return numLibAllMethods;  // return the number of methods
 }
 

}



