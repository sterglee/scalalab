package scalaSci.math.plot.plots;

import JSci.awt.ColorScheme;
import java.awt.*;

import javax.swing.*;

import scalaSci.math.plot.*;
import scalaSci.math.plot.canvas.PlotCanvas;
import scalaSci.math.plot.render.*;

public class contourPlot extends Plot implements JSci.awt.ColorScheme {
        double [][] XY;
        private double min, max;
        private int deltay;
        private double[] deltax;
        private float c1=1;
        private float c2=1;
        private float c3=1;
        private int Contourx=1;
        private int Contoury=1;
        private int xCoordMax;
        private int yCoordMax;
        private     ColorScheme     CS;
              
        private int ColorScaleWidth=20;   // the width of the displayed color scale bar
        private String MaxString, MinString, MiddleString;
        
    public contourPlot(String n, Color c, double [][]_XY) {
                super(n, c);
                CS = this;
                setData(_XY);
      }

        	
   public Color getColor(float f) {
      if((f<0)||(f>1))
         throw new IllegalArgumentException("Color are given for values between 0 and 1 : "+f);
    Color color = new Color(f, f, f, f);
    return color; //(Color.getHSBColor(f, f, f));
  }
        
    public void plot(AbstractDrawer draw, Color c) {
            if (!visible)
                return;
                
                AWTDrawer  aDraw = (AWTDrawer)draw;
                Dimension plotCanvasDim = aDraw.canvas.getSize();
                rescale(plotCanvasDim.width, plotCanvasDim.height);  // rescaling to the current plotting canvas
        
                int FontHeight = 12; //draw.getFontMetrics(g.getFont()).getHeight();
		  // build the color scale on the left of the axis
                aDraw.drawRect(Contourx+ColorScaleWidth,Contoury-1,xCoordMax,yCoordMax);
                aDraw.drawRect(Contourx-1,Contoury-1,ColorScaleWidth,yCoordMax);
                aDraw.setClip(Contourx,Contoury,ColorScaleWidth-1,yCoordMax-1);
                double step255;   
                step255 = yCoordMax/255.0;
                for(float y=0;y<yCoordMax;y+=step255) {
                        aDraw.setColor(CS.getColor(y/(float)yCoordMax));
                        aDraw.fillRect(Contourx,Contoury+(int)Math.floor(y),ColorScaleWidth-1,2);
            } 
                
            aDraw.setColor(Color.black);
        
        if (MaxString != null) {
        aDraw.drawString(MaxString, Contourx,+FontHeight+Contoury);
        aDraw.drawString(MiddleString, Contourx,+Contoury+(int)Math.round((yCoordMax+FontHeight)/2.0));
        aDraw.drawString(MinString, Contourx,yCoordMax-1);
    }
		aDraw.setClip(2*Contourx+ColorScaleWidth,Contoury,xCoordMax-1,yCoordMax-1);
    for(int k=0;k<XY.length;k++) {
			for(int l=0;l<XY[k].length;l++) {
                aDraw.setColor(CS.getColor((float)XY[k][l]));
                aDraw.fillRect((int)Math.floor(l*deltax[k])+2*Contourx+ColorScaleWidth,k*deltay+Contoury,
                                        (int)Math.floor((l+1.0)*deltax[k])+Contourx,(k+1)*deltay+Contoury);
			}
    
                 }
             }
     
                


                /**
        * Sets the data plotted by this ContourPlot to the specified data.
        */
	public void setData(double feed[][]) {
    //invert the rows
                double[][] array=new double[feed.length][];
                for(int k=0;k<array.length;k++) {
                        array[k]=feed[array.length-k-1];
                }
		min=array[0][0];
		max=array[0][0];
		XY=new double[array.length][];
		for(int k=0;k<array.length;k++) {
			XY[k]=new double[array[k].length];
			for(int l=0;l<array[k].length;l++) {
				if(array[k][l]>max)
					max=array[k][l];
				if(array[k][l]<min)
					min=array[k][l];
			}
		}
		if(max==min) {
			for(int k=0;k<array.length;k++) {
				for(int l=0;l<array[k].length;l++) {
					XY[k][l]=1;
				}
			}
		} else {
                         for(int k=0;k<array.length;k++) 
			    for(int l=0;l<array[k].length;l++) 
					XY[k][l] = (1-(array[k][l]-min)/(max-min));
                }
                int precision=getPrecision(max-min);
                MaxString=Double.toString(round(max, precision));
                MinString=Double.toString(round(min, precision));
                MiddleString=Double.toString(round((max+min)/2.0,precision));
    
                
	}
        
        private int getPrecision (double d) {
    d=Math.abs(d);
    int ans=0; double compare=1;
    if(d<1) {
      while(compare>d){
        compare/=10;
        ans--;
      }
      return(ans);
    }
    while(compare<d) {
      compare*=10;
      ans++;
    }
    return(ans);
  }

        
  private double round(double d,int k) {
    if(d==0)
      return(0);
    double sign=d/Math.abs(d);
    d=Math.abs(d);
    if(k<0) {
      int k1=k;
      while(k1<0) {
        k1++;
        d*=10;
      }
      d=Math.round(d);
      while(k<0) {
        k++;
        d/=10;
      }
      return(d*sign);
    }
    int k1=k;
    while(k1<0) {
      k1++;
      d/=10;
    }
    d=Math.round(d);
    while(k<0) {
      k++;
      d*=10;
    }
    return(d*sign);

  }
  
  	private void rescale(int  width, int height) {
            int xDim=width-3*Contourx;
            int yDim=height-2*Contoury-ColorScaleWidth;
            deltay=(int) Math.floor(yDim/XY.length);
            yCoordMax=deltay*XY.length;
      //modifi 
	deltax=new double[XY.length];
      //modifi 
		xCoordMax=0;
    int MaxNumberOfElements=XY[0].length;
    for(int k=1;k<XY.length;k++) {
      MaxNumberOfElements=Math.max(XY[k].length,MaxNumberOfElements);
    }
    int UsableWidth= xDim; // (int)Math.floor(xDim/(double)MaxNumberOfElements)*MaxNumberOfElements;
		for(int k=0;k<XY.length;k++) {
			deltax[k]=UsableWidth/(double)XY[k].length;
      xCoordMax=Math.max(xCoordMax,(int)Math.floor(XY[k].length*deltax[k]));
		}
    
		
	}

         
	public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {
		for (int i = 0; i < XY.length; i++) {
			int[] screenCoord = draw.project(XY[i]);

			if ((screenCoord[0] + note_precision > screenCoordTest[0]) && (screenCoord[0] - note_precision < screenCoordTest[0])
					&& (screenCoord[1] + note_precision > screenCoordTest[1]) && (screenCoord[1] - note_precision < screenCoordTest[1]))
				return XY[i];
		}
		return null;
	}

	public double[][] getData() {
		return XY;
	}

private static double[][] createContourData() {
                double data[][]=new double[40][40];
                double x,y;
                for(int i=0;i<data.length;i++) {
                        for(int j=0;j<data[0].length;j++) {
                                x=(i-data.length/2.0)*3.0/data.length;
                                y=(j-data[0].length/2.0)*3.0/data[0].length;
                                data[i][j]=Math.exp(-x*x-y*y);
                        }
                }
                return data;
       }

	public static void main(String[] args) {
		Plot2DPanel p2 = new Plot2DPanel();

                double [][]data = createContourData();
		
                
                p2.addContourPlot("Contour test", data);
                /*JFrame fr = new JFrame();
                fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                fr.add(p2);
                fr.setSize(400, 800);
                fr.setVisible(true); */
                 new FrameView(p2).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
 