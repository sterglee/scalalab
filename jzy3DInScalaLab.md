# Introduction #

`The jzy3D project system allows high quality plotting of data. While the default plotting routines (adapted jMathPlot) are sufficient for some 3-D plots, jzy3D offers far more extensive support for 3-D graphs. Moreover, jzy3D builds upon JOGL (i.e. the Java Bindings for OpenGL) and therefore is fast. In ScalaLab it is easy to use that system and to combine it with the other libraries. It is integrated with ScalaLab210. Here, we describe the few steps required for installation at previous versions (ScalaLab292, ScalaLab282) and we present examples of its use. `


# `Installation in ScalaLab` #

`The jzy3D system is supported by default with ` **`ScalaLab210`**` . The following discussion concerns ScalaLab292 and ScalaLab282.`

`The file ` **`jzy3DToolboxAndNativeLibs.zip`** ` contains within two files, each of which is handled as we describe below: `

`a. the ` **`jzy3D.jar`** `which is the Java part of the jzy3D system. That requires installation as any other ScalaLab toolbox (see the appropriate wiki page on how to install any ScalaLab toolbox. `

`b. the ` **`jzy3DNativeLibs.zip`** `is the file that contains the machine dependent parts, since jzy3D is based on OpenGL. You should extract the relevant files for your platform at the same directory as you have the jzy3D.jar  file (e.g. for 64-bit Linux the files are: gluegen-rt-natives-linux-amd64.jar and jogl-all-natives-linux-amd64.jar)`

# Examples #

` If you performed the above two steps (e.g. jar toolbox installation and native libraries extraction to the proper directory] here are some superb plots with jzy3D in ScalaLab. `


## 3-D Plotting of a function ##

```


import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.colors.ColorMapper
import org.jzy3d.colors.colormaps.ColorMapRainbow
import org.jzy3d.demos.AbstractDemo
import org.jzy3d.demos.Launcher
import org.jzy3d.maths.Range
import org.jzy3d.plot3d.builder.Builder
import org.jzy3d.plot3d.builder.Mapper
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid
import org.jzy3d.plot3d.primitives.Shape
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend

	
// define a function to plot
var  mapper = new Mapper(){
            def  f(x: Double,  y: Double )  = {
                10*Math.sin(x/10)*Math.cos(y/20)*x
            }
        }

// define range and precision fort the function to plot
var range = new Range(-150, 150)

var steps = 50

// create the object to represent the function over the given range
var surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper).asInstanceOf[Shape]

surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(),
new Color(1,1,1, 0.5f)))

surface.setFaceDisplayed(true)

surface.setWireframeDisplayed(true)
surface.setWireframeColor(Color.BLACK)

// create a chart and add surface
var chart = new Chart(org.jzy3d.plot3d.rendering.canvas.Quality.Advanced)

chart.getScene().getGraph().add(surface)

// setup a colorbar
var  cbar = new ColorbarLegend(surface, chart.getView().getAxe().getLayout())
surface.setLegend(cbar)

org.jzy3d.ui.ChartLauncher.openChart(chart)



```


## `Histogram Demo` ##

```


import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.demos.AbstractDemo
import org.jzy3d.demos.Launcher
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.AbstractDrawable
import org.jzy3d.plot3d.primitives.HistogramBar
import org.jzy3d.plot3d.rendering.canvas.Quality
import org.jzy3d.plot3d.rendering.scene.Scene



	
class  HistogramDemo  extends AbstractDemo {
   var chart = new Chart(Quality.Advanced)
   var scene = chart.getScene()
   for(  i <- 0 until 200 by 50)
        for ( j <-0 until 200 by 50)			
		 scene.add( addBar(i,j,Math.random()*2) )
	
		
	def  addBar(x: Double, y: Double, height: Double): AbstractDrawable  = {
		var  color = Color.random()
		color.a = 0.55f

		var  bar = new HistogramBar()
		bar.setData(new Coord3d(x, y, 0), height.asInstanceOf[Float], 10, color)
		bar.setWireframeDisplayed(false)
		bar.setWireframeColor(Color.BLACK)
		bar
	}

	
	override def  getPitch() = {
		"Draws a bar chart";
	}
	
	def  getChart() = {
		chart
	}
	
}

Launcher.openDemo(new HistogramDemo())

```

## `Matlab-like plotting with jzy3d ` ##

`A Matlab-like interface for plotting with jzy3D is in development, implemented with the jzy3dPlot.jzy3dPlot class. This is in a very early stage, however here is a simple plotting example using this class. `

```

var N = 200
var x =  linspace(0, 10, N)
var F1 = 3.3; var F2 = 4.5; var A1= 2.3; var A2 = 0.44;
var y = A1*sin(F1*x)+A2*cos(A2*x)
var z =  cos(0.678*y)

plot3d(x.getv, y.getv, z.getv)
```

`And another example for scatter plots`

```
val N = 2000
val x =  linspace(0, 10, N)
val  F1 = 3.3; val  F2 = 4.5;  val  A1= 2.3;  val A2 = 0.44;
val y = A1*sin(F1*x)+A2*cos(A2*x)
val ZF1 = 3.3;  val ZF2 = 4.5; val ZA1= 2.3; val ZA2 = 0.44;
val  z = ZA1*sin(ZF1*x)+ZA2*cos(ZA2*x)
scatter3d(x.getv(), y.getv(), z.getv(), new java.awt.Rectangle(100, 100, 500, 500), "Scatter Plot", 1)

```


## `3-D Scatter Plot` ##

```
import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d


var size = 100000
var  points = new Array[Coord3d](size)

var colors = new Array[Color](size)

var x=0.0f; var y=0.0f; var z=0.0f; var a = 0.0f
for(i<-0 until size){
        x =  Math.random().asInstanceOf[Float] - 0.5f
        y = Math.random().asInstanceOf[Float] - 0.5f
        z = Math.random().asInstanceOf[Float] - 0.5f
        points(i) = new Coord3d(x, y, z)
        a = 0.25f + (points(i).distance(Coord3d.ORIGIN)/Math.sqrt(1.3)).asInstanceOf[Float] / 2
        colors(i) = new Color(x, y, z, a)
}

var scatter = new org.jzy3d.plot3d.primitives.Scatter(points, colors)
var chart = new Chart()
chart.getScene().add(scatter)

org.jzy3d.ui.ChartLauncher.openChart(chart)

```

## `Multicolor 3-D Scatter Plot` ##
```
import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d
import org.jzy3d.colors.ColorMapper
import org.jzy3d.colors.colormaps.ColorMapRainbow


var size = 100000
var  points = new Array[Coord3d](size)

var colors = new Array[Color](size)

var x=0.0f; var y=0.0f; var z=0.0f; var a = 0.0f
for(i<-0 until size){
        x =  Math.random().asInstanceOf[Float] - 0.5f
        y = Math.random().asInstanceOf[Float] - 0.5f
        z = Math.random().asInstanceOf[Float] - 0.5f
        points(i) = new Coord3d(x, y, z)
        a = 0.25f + (points(i).distance(Coord3d.ORIGIN)/Math.sqrt(1.3)).asInstanceOf[Float] / 2
        colors(i) = new Color(x, y, z, a)
}

var scatter = new org.jzy3d.plot3d.primitives.MultiColorScatter( points, new ColorMapper( new ColorMapRainbow(), -0.5f, 0.5f ) )

var chart = new Chart()
chart.getScene().add(scatter)

org.jzy3d.ui.ChartLauncher.openChart(chart)


```

## Executing a Java example (with F9 keystroke) ##

`Java examples of jzy3D can be executed easily from the editor with the F9 keystroke. Java classes should not be placed in a package. We present some examples.`

### `Animated Surface (Java code - F9 keystroke to execute) ` ###

```


import org.jzy3d.demos.animation.ParametrizedMapper;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IRunnableDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.TicToc;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.plot3d.rendering.view.Renderer2d;


public class AnimatedSurfaceDemo extends AbstractDemo implements IRunnableDemo{

	public static void main(String[] args) throws Exception{
		IRunnableDemo demo = new AnimatedSurfaceDemo();
		Launcher.openDemo(demo);
		demo.start();
	}
	
	public AnimatedSurfaceDemo(){
		mapper = new ParametrizedMapper(0.9){
			public double f(double x, double y) {
				return 10*Math.sin(x*p)*Math.cos(y*p)*x;
			}
		};
		Range range = new Range(-150,150);
		int steps   = 50;
		
		// Create the object to represent the function over the given range.
		surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		
		// Create a chart 
		chart = new Chart("awt");
		surface.setLegend(new ColorbarLegend(surface, 
							chart.getView().getAxe().getLayout().getZTickProvider(), 
							chart.getView().getAxe().getLayout().getZTickRenderer()));
		chart.getScene().getGraph().add(surface);

		// display FPS
		fpsText = "";
		chart.addRenderer(new Renderer2d(){
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(java.awt.Color.BLACK);
				g2d.drawString(fpsText, 50, 50);
			}
		});
	}
	
	public Chart getChart(){
		return chart;
	}
	
	public void start(){
		fpsText = "";
		t = new Thread(){
			TicToc tt = new TicToc();
			@Override
			public void run() {
				while(true){
					try {
						sleep(25);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tt.tic();
					mapper.setParam( mapper.getParam() + 0.0001 );
					remap(surface, mapper);
					//chart.render();
					tt.toc();
					fpsText = Utils.num2str(1/tt.elapsedSecond(), 4) + " FPS";
				}
			}
		};
		t.start();
	}
	
	public void stop(){
		if(t!=null)
			t.interrupt();
	}
	
	protected void remap(Shape shape, Mapper mapper){
		List<AbstractDrawable> polygons = shape.getDrawables();		
		for(AbstractDrawable d: polygons){
			if(d instanceof Polygon){
				Polygon p = (Polygon) d;				
				for(int i=0; i<p.size(); i++){
					Point pt = p.get(i);
					Coord3d c = pt.xyz;
					c.z = (float) mapper.f(c.x, c.y);
				}
			}
		}
	}
	
	protected Chart chart;
	protected Shape surface;
	protected ParametrizedMapper mapper;	
	protected String fpsText;
	
	protected Thread t;
}


```


### `Contour Demo  (Java code - F9 keystroke to execute) ` ###

```




import org.jzy3d.chart.Chart;

import org.jzy3d.colors.ColorMapper;

import org.jzy3d.colors.colormaps.ColorMapRainbow;

import org.jzy3d.contour.MapperContourPictureGenerator;

import org.jzy3d.demos.AbstractDemo;

import org.jzy3d.demos.Launcher;

import org.jzy3d.maths.Coord3d;

import org.jzy3d.maths.Range;

import org.jzy3d.plot3d.builder.Mapper;

import org.jzy3d.plot3d.primitives.MultiColorScatter;

import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;





public class Contour3DDemo extends AbstractDemo{

	public static void main(String[] args) throws Exception{

		Launcher.openDemo(new Contour3DDemo());

	}



	public Contour3DDemo(){

		// Define a function to plot

		Mapper mapper = new Mapper(){

			public double f(double x, double y) {

				return 10*Math.sin(x/10)*Math.cos(y/20)*x;

			}

		};



		// Define range and precision for the function to plot

		Range xrange = new Range(-100,100);   //To get some more detail.

		Range yrange = new Range(-100,100);   //To get some more detail.

		

		// Compute an image of the contour

		MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);

		int nPoints= 1000;

		double[][] contours= contour.getContourMatrix(nPoints, nPoints, 40);

		

		// Create the dot cloud scene and fill with data

		int size = nPoints*nPoints;

		Coord3d[] points = new Coord3d[size];

		

		for (int x = 0; x < nPoints; x++)

			for (int y = 0; y < nPoints; y++){

				if (contours[x][y]>-Double.MAX_VALUE){ // Non contours points are -Double.MAX_VALUE and are not painted

					points[x*nPoints+y] = new Coord3d((float)x,(float)y,(float)contours[x][y]);									

				}

				else

					points[x*nPoints+y] = new Coord3d((float)x,(float)y,(float)0.0);  									

//				points[x*400+y] = new Coord3d((float)x,(float)y,(float)mapper.f(x, y));				

			}



		chart = new Chart();

		MultiColorScatter scatter = new MultiColorScatter( points, new ColorMapper( new ColorMapRainbow(), -600.0f, 600.0f ) );

		chart.getScene().add(scatter);

		scatter.setLegend( new ColorbarLegend(scatter, 

				chart.getView().getAxe().getLayout().getZTickProvider(), 

				chart.getView().getAxe().getLayout().getZTickRenderer()) );

		scatter.setLegendDisplayed(true);

	}

	

	public Chart getChart(){

		return chart;		

	}

	protected Chart chart;

}

```


### `Contour Plots Demo   (Java code - F9 keystroke to execute) ` ###
```




import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.factories.JzyFactories;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeFactory;
import org.jzy3d.plot3d.primitives.axes.ContourAxeBox;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class ContourPlotsDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		Launcher.openDemo(new ContourPlotsDemo());
	}

	public ContourPlotsDemo(){
		// Define a function to plot
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};

		// Define range and precision for the function to plot
		Range xrange = new Range(50,100);
		Range yrange = new Range(50,100);
		int steps   = 50;
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
		ColorMapper myColorMapper=new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)); 
		surface.setColorMapper(myColorMapper);
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);

		// Create a chart with contour axe box, and attach the contour picture
		JzyFactories.axe = new AxeFactory(){
			@Override
			public IAxe getInstance() {
				return new ContourAxeBox(box);
			}
		};
		chart = new Chart(); //TODO: Quality.Advanced contour buggy with axe box 
		ContourAxeBox cab = (ContourAxeBox)chart.getView().getAxe();
		MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);
		cab.setContourImg( contour.getContourImage(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 10), xrange, yrange);
		
		// Add the surface and its colorbar
		chart.addDrawable(surface);
		surface.setLegend(new ColorbarLegend(surface, 
				chart.getView().getAxe().getLayout().getZTickProvider(), 
				chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
	}
	public Chart getChart(){
		return chart;
	}
	protected Chart chart;
}


```

### `FilledContoursDemo   (Java code - F9 keystroke to execute)` ###

```


import java.awt.Rectangle;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.factories.JzyFactories;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeFactory;
import org.jzy3d.plot3d.primitives.axes.ContourAxeBox;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.ui.ChartLauncher;


public class FilledContoursDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		IDemo demo = new FilledContoursDemo();
		ChartLauncher.openImagePanel( ((ContourAxeBox)demo.getChart().getView().getAxe()).getContourImage(), new Rectangle(600,0,400,400) );
		Launcher.openDemo(demo);
	}

	public FilledContoursDemo(){
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};
		Range xrange = new Range(50,100);   //To get some more detail.
		Range yrange = new Range(50,100);   //To get some more detail.
		int steps   = 50;
		
		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
		ColorMapper myColorMapper=new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)); 
		surface.setColorMapper(myColorMapper);
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);

		// Compute an image of the contour
		MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);
		
		// Create a chart with contour axe box, and attach the contour picture
		JzyFactories.axe = new AxeFactory(){
			@Override
			public IAxe getInstance() {
				return new ContourAxeBox(box);
			}
		};
		chart = new Chart(Quality.Advanced);
		ContourAxeBox cab = (ContourAxeBox)chart.getView().getAxe();
		cab.setContourImg( contour.getFilledContourImage(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 10), xrange, yrange);
		
		// Add the surface and its colorbar
		chart.addDrawable(surface);
		surface.setLegend(new ColorbarLegend(surface, 
				chart.getView().getAxe().getLayout().getZTickProvider(), 
				chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
	}

	public Chart getChart(){
		return chart;
	}
	
	protected Chart chart;
}



```

### `HeightMapDemo  (Java code - F9 keystroke to execute) ` ###

```


import java.awt.Rectangle;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.factories.JzyFactories;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeFactory;
import org.jzy3d.plot3d.primitives.axes.ContourAxeBox;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.primitives.axes.layout.providers.RegularTickProvider;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.ui.ChartLauncher;


public class HeightMapDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		IDemo demo = new HeightMapDemo();
		ChartLauncher.openImagePanel( ((ContourAxeBox)demo.getChart().getView().getAxe()).getContourImage(), new Rectangle(600,0,400,400) );
		Launcher.openDemo(demo);
	}

	public HeightMapDemo(){
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};
		Range xrange = new Range(50,100);   //To get some more detail.
		Range yrange = new Range(50,100);   //To get some more detail.
		int steps   = 50;
		
		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
		ColorMapper myColorMapper=new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)); 
		surface.setColorMapper(myColorMapper);
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);

		// Compute an image of the contour
		MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);
		
		// Create a chart with contour axe box, and attach the contour picture
		JzyFactories.axe = new AxeFactory(){
			@Override
			public IAxe getInstance() {
				return new ContourAxeBox(box);
			}
		};
		chart = new Chart(Quality.Intermediate);
		ContourAxeBox cab = (ContourAxeBox)chart.getView().getAxe();
		cab.setContourImg( contour.getHeightMap(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 10), xrange, yrange);
		
		// Add the surface and its colorbar
		chart.addDrawable(surface);
		surface.setLegend(new ColorbarLegend(surface, 
				new RegularTickProvider(10), 
				chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
	}
	public Chart getChart(){
		return chart;
	}
	protected Chart chart;
}


```


### `UserChoosenContoursDemo  (Java code - F9 keystroke to execute)` ###

```



import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.factories.JzyFactories;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeFactory;
import org.jzy3d.plot3d.primitives.axes.ContourAxeBox;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.ui.ChartLauncher;


public class UserChosenContoursDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		IDemo demo = new UserChosenContoursDemo();
		ChartLauncher.openImagePanel( ((ContourAxeBox)demo.getChart().getView().getAxe()).getContourImage(), new Rectangle(600,0,400,400) );
		Launcher.openDemo(demo);
	}
	
	public UserChosenContoursDemo(){
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};
		Range xrange = new Range(50,100);
		Range yrange = new Range(50,100);
		int steps    = 50;
		
		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
		ColorMapper myColorMapper=new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)); 
		surface.setColorMapper(myColorMapper);
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);

		// Compute an image of the contour
		MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);
		
		// Create a chart with contour axe box, and attach the contour picture
		JzyFactories.axe = new AxeFactory(){
			@Override
			public IAxe getInstance() {
				return new ContourAxeBox(box);
			}
		};
		chart = new Chart(Quality.Advanced);
		ContourAxeBox cab = (ContourAxeBox)chart.getView().getAxe();
		
		//Define the array with the heights at which we want a contour line drawn. Numbers must be ordered from smaller to bigger.
		double sortedContourLevels[]={-500.0,-200.0,0.0, 100.0, 300.0, 400.0};
		
		//Compute the user-defined contours.
		BufferedImage img = contour.getContourImage(new DefaultContourColoringPolicy(myColorMapper), 400, 400, sortedContourLevels);
		cab.setContourImg(img, xrange, yrange);
		
		// Add the surface and its colorbar
		chart.addDrawable(surface);
		surface.setLegend(new ColorbarLegend(surface, 
				chart.getView().getAxe().getLayout().getZTickProvider(), 
				chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
	}

	public Chart getChart(){
		return chart;
	}
	
	protected Chart chart;
}


```