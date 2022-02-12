package wavelets;


import java.io.Serializable;

// Continuous Morlet Wavelet Transform
// Copyright (C)  Richard Buessow
// richard.buessow@tu-berlin.de
//import java.lang.Math.*;
import java.io.Serializable;
public class CWT implements Serializable{
	public  double[][] Re;
	public  double[][] Im;
	private int Ny;
	public double[] f;
	public double[] t;
	public double[] deltaStept;
	
        public CWT(double[] y){
		   this(y,y.length,(double) y.length/4,100,"log",3,3);
		}	
	public CWT(double[] y,int fs){
		   this(y,fs,(double) fs/4,100,"log",3,3);
		}	
	public CWT(double[] y,int fs,double fmax){
	   this(y,fs,fmax,100,"log",3,3);
	}
	public CWT(double[] y,int fs,double fmax, int maxNf){
		   this(y,fs,fmax,maxNf,"log",3,3);
		}
	public CWT(double[] y,int fs,double fmax, int maxNf, int dstepfac){
		   this(y,fs,fmax,maxNf,"log",dstepfac,3);
		}
	public CWT(double[] y,int fs,double fmax, int maxNf, int dstepfac, int df0){
		   this(y,fs,fmax,maxNf,"log",dstepfac,df0);
		}
        
        
        
        
 // fs: the sampling frequency        
 // fmax: the maximum frequency of the analysis       
// maxNf: the maximum number of frequencies for which analysis is performed
// stepfac: controls the change of the step size, i.e. "frequency adaptive" change at step
// df0: starting frequency f0        
	public CWT(double[] y,int fs,double fmax, int maxNf, String linlog, int stepfac, int df0){
		// Calculation of the transform
		// used to calc between a fictiv T=1s and the real
		double ffac = (double) fs/y.length; 
		// maximum number of dyades: 2^6<n<2^7 -> maxNd = 6
		int maxNd = (int) (Math.log((double)y.length)/Math.log(2.));
		// 
		//System.out.println("maxNd: " + maxNd);
                // f0 is the starting frequency of analysis
		double fmor, f0=pow2(df0), fd=0;
		int Nd,len,startw,starty,maxNb,index=0,Ninner=0,stepLen=1;
		boolean lin=false,stepch=true;
		//	int stopw,stopy;
		// check maximum freq		
		if (fmax>fs/2) fmax=fs/2;   // analyze up to the Nyquist frequency
		//
		// how many dyadic steps have actually to be performed to reach fmax
		for (Nd=0;Nd<maxNd;Nd++)
			if(f0*ffac*pow2(Nd)>=fmax) break; 
		//System.out.println("Nd= " +Nd);
		//
		// actually only stepfac that are dyadic are used
		int dstepfac;// = (int) (Math.log((double)stepfac)/Math.log(2.));
		int MinStepLen;
		//
		// no change of the step size - not recommended
		if (stepfac==0){
			stepch = false;
			dstepfac=0;
			MinStepLen=1;
			// System.out.println("Stepsize always:" + stepLen);
		}
		else{
			dstepfac = (int) (Math.log((double)stepfac)/Math.log(2.));
			MinStepLen = Math.max(1,pow2(maxNd-Nd-df0-dstepfac+1));
		}
		// System.out.println("dstepfac: " + dstepfac);
		stepfac = pow2(dstepfac);
		// System.out.println("stepfac: " + stepfac);
		//
		// smallest step Length 
		stepLen=MinStepLen;
		// System.out.println("stepLen: " + stepLen);
		// 
		// linear frequency axis - not recommended
		if(linlog.equals("lin"))
			lin = true;		
		//
		// 
		Re = new double[maxNf][];
		Im = new double[maxNf][];
		f = new double[maxNf];
		deltaStept = new double[maxNf];     // time step for the corresponding frequency
		maxNb = y.length/MinStepLen;    // maximum number of steps
                Ninner = (int) Math.ceil((double) maxNf/Nd);	
		t = new double[maxNb];
		for (int i=0;i<maxNb;i++) 
                    t[i]= (double) i*MinStepLen/fs;    // time axis
		
                // calc Transform
		// outer dyadic loop
		outer:    // Nd is the number of dyadic steps actually to be performed to reach fmax
		for (int dy=0;dy<Nd;dy++){ //dy=Nd-1, the inner loop calcs until fmax
			//System.out.println("dy: " + dy);
			len= pow2(maxNd - dy);
			//System.out.println("len: " + len);
			// calculate actual step length 
			if(stepch)
				stepLen = Math.max(MinStepLen,len/(int) f0/stepfac);
			// System.out.println("stepLen: " + stepLen);			
			// maximal Number translation parameters b
			maxNb = y.length/stepLen;
			//System.out.println("stepLen: " + stepLen + " len: " + len + " maxNb " + maxNb + " stepfac " + stepfac);
			// calc the wavelet
			Morlet morlet = new Morlet(len,f0);	
			// for linear freq axis
			if(lin) Ninner = (int) Math.pow(2,dy+fd); // Ninner controls the number of frequency steps to take		
        		// inner frequency loop
			for (int inner=0;inner< Ninner;inner++){
				// frequency in the fictivous 1s window
				if(lin)
					fmor = f0+f0*inner/Ninner;
				else  // on each step double the analysis frequency
					fmor = f0*Math.pow(2,( double)inner/Ninner); 
				// real frequency
				f[index]=(fmor*pow2(dy)*ffac);
				deltaStept[index] = (double) stepLen/fs;
				//  System.out.println("deltaStept["+index+"]: " + deltaStept[index]);
				// change freq of morlet
				morlet.setF0(fmor);
				Re[index] = new double[maxNb];
				Im[index] = new double[maxNb];
				// translation loop
				for (int b=0;b<maxNb;b++){
		            startw=Math.max(0,len/2-b*stepLen);
		            starty=Math.max(0,b*stepLen-len/2);;
		            // do not calc at the boarder
		           // if(b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner)&&y.length-b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner))	            	
		            if(b*stepLen>len/f0/Math.pow(2.,(double)inner/Ninner)&&y.length-b*stepLen>len/f0/Math.pow(2.,(double)inner/Ninner))	            	
		            for (int i=0;i<Math.min(len,y.length+len/2-b*stepLen)-startw;i++){
//		            		System.out.println("Re[index][b]: " +Re[index][b]);
//		            		System.out.println("y[starty+i]: " +y[starty+i]);
//		            		System.out.println("morlet.getRe(startw+i): " + morlet.getRe(startw+i));
		            		Re[index][b] += y[starty+i]*morlet.getRe(startw+i);
		            		Im[index][b] += y[starty+i]*morlet.getIm(startw+i);
		            	}
		            // multiply with 1/sqrt(a) and dt = 1/fs
		            Re[index][b] *= Math.sqrt(f[index])/fs;
		            Im[index][b] *= Math.sqrt(f[index])/fs;
				}
				// :end translation loop
				index++;
				//System.out.println(index);
				if(index==maxNf)
					break outer; // breack if number of frequency points is reached
			} // :end inner frequency loop
		} // :end outer frequency loop
		//System.out.println("index= " +index);
	}
	

      public CWT(double [][] yv, int fs, double fmax,  int maxNf, String linlog, int stepfac, int df0){
            int rows = yv.length;
            int cols = yv[0].length;
            double [] yvec= null; 
            if (cols > rows)   //  a row vector was passed
            {
                yvec = new double[cols];
                for (int colId=0; colId < cols; colId++)
                    yvec[colId] = yv[0][colId];
            }
            else  {  // a column vector was passed
                yvec = new double[rows];
                for (int rowId=0; rowId<rows; rowId++)
                    yvec[rowId] = yv[rowId][0];
            }
        
            // Calculation of the transform
		// used to calc between a fictiv T=1s and the real
		double ffac = (double) fs/yvec.length; 
		// maximum number of dyades: 2^6<n<2^7 -> maxNd = 6
		int maxNd = (int) (Math.log((double)yvec.length)/Math.log(2.));
		// 
		//System.out.println("maxNd: " + maxNd);
		double fmor,f0=pow2(df0),fd=0;
		int Nd,len,startw,starty,maxNb,index=0,Ninner=0,stepLen=1;
		boolean lin=false,stepch=true;
		//	int stopw,stopy;
		// check maximum freq		
		if (fmax>fs/2) fmax=fs/2;
		//
		// how many dyadic steps have actually to be performed to reach fmax
		for (Nd=0;Nd<maxNd;Nd++)
			if(f0*ffac*pow2(Nd)>=fmax) break; 
		//System.out.println("Nd= " +Nd);
		//
		// actually only stepfac that are dyadic are used
		int dstepfac;// = (int) (Math.log((double)stepfac)/Math.log(2.));
		int MinStepLen;
		//
		// no chang of the step size - not recommended
		if (stepfac==0){
			stepch = false;
			dstepfac=0;
			MinStepLen=1;
			// System.out.println("Stepsize always:" + stepLen);
		}
		else{
			dstepfac = (int) (Math.log((double)stepfac)/Math.log(2.));
			MinStepLen = Math.max(1,pow2(maxNd-Nd-df0-dstepfac+1));
		}
		// System.out.println("dstepfac: " + dstepfac);
		stepfac = pow2(dstepfac);
		// System.out.println("stepfac: " + stepfac);
		//
		// smallest step Length 
		stepLen=MinStepLen;
		// System.out.println("stepLen: " + stepLen);
		// 
		// linear frequency axis - not recommended
		if(linlog.equals("lin"))
			lin = true;		
		//
		// 
		Re = new double[maxNf][];
		Im = new double[maxNf][];
		f = new double[maxNf];
		deltaStept = new double[maxNf];
		maxNb = yvec.length/MinStepLen;
		Ninner = (int) Math.ceil((double) maxNf/Nd);	
		t = new double[maxNb];
		for (int i=0;i<maxNb;i++) t[i]= (double) i*MinStepLen/fs;
		// calc Transform
		// outer dyadic loop
		outer:
		for (int dy=0;dy<Nd;dy++){ //dy=Nd-1, the inner loop calcs until fmax
			//System.out.println("dy: " + dy);
			len= pow2(maxNd - dy);
			//System.out.println("len: " + len);
			// calculate actual step length 
			if(stepch)
				stepLen = Math.max(MinStepLen,len/(int) f0/stepfac);
			// System.out.println("stepLen: " + stepLen);			
			// maximal Number translation parameters b
			maxNb = yvec.length/stepLen;
			//System.out.println("stepLen: " + stepLen + " len: " + len + " maxNb " + maxNb + " stepfac " + stepfac);
			// calc the wavelet
			Morlet morlet = new Morlet(len,f0);	
			// for linear freq axis
			if(lin) Ninner = (int) Math.pow(2,dy+fd);
			// inner frequency loop
			for (int inner=0;inner< Ninner;inner++){
				// frequency in the fictivous 1s window
				if(lin)
					fmor = f0+f0*inner/Ninner;
				else
					fmor = f0*Math.pow(2,( double)inner/Ninner);
				// real frequency
				f[index]=(fmor*pow2(dy)*ffac);
				deltaStept[index] = (double) stepLen/fs;
				//  System.out.println("deltaStept["+index+"]: " + deltaStept[index]);
				// change freq of morlet
				morlet.setF0(fmor);
				Re[index] = new double[maxNb];
				Im[index] = new double[maxNb];
				// translation loop
				for (int b=0;b<maxNb;b++){
		            startw=Math.max(0,len/2-b*stepLen);
		            starty=Math.max(0,b*stepLen-len/2);;
		            // do not calc at the boarder
		           // if(b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner)&&y.length-b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner))	            	
		            if(b*stepLen>len/f0/Math.pow(2.,(double)inner/Ninner)&&yvec.length-b*stepLen>len/f0/Math.pow(2.,(double)inner/Ninner))	            	
		            for (int i=0;i<Math.min(len,yvec.length+len/2-b*stepLen)-startw;i++){
//		            		System.out.println("Re[index][b]: " +Re[index][b]);
//		            		System.out.println("y[starty+i]: " +y[starty+i]);
//		            		System.out.println("morlet.getRe(startw+i): " + morlet.getRe(startw+i));
		            		Re[index][b] += yvec[starty+i]*morlet.getRe(startw+i);
		            		Im[index][b] += yvec[starty+i]*morlet.getIm(startw+i);
		            	}
		            // multiply with 1/sqrt(a) and dt = 1/fs
		            Re[index][b] *= Math.sqrt(f[index])/fs;
		            Im[index][b] *= Math.sqrt(f[index])/fs;
				}
				// :end translation loop
				index++;
				//System.out.println(index);
				if(index==maxNf)
					break outer; // breack if number of frequency points is reached
			} // :end inner frequency loop
		} // :end outer frequency loop
		//System.out.println("index= " +index);
      }
        
        
	// help methods 
	// returning the absolute value
	public CWT(double[] y,int fs, double[] f, int dstepfac, int df0){
		this.Ny = y.length;
		this.f = f;
		// Calculation of the transform
		// used to calc between a fictiv T=1s and the real
		//double ffac = (double) fs/y.length; 
		// maximum number of dyades: 2^6<n<2^7 -> maxNd = 6
		int maxNd = (int) (Math.log((double)y.length)/Math.log(2.));
		double ffac = (double) fs/pow2(maxNd);
		//System.out.println("Neu, ffac: " + ffac + " alt: " + (double) fs/y.length);
		// 
		//System.out.println("maxNd: " + maxNd);
		double fmor,f0=pow2(df0),fmax;//,fd=0;
		int Nd,len,startw,starty,maxNb,index=0,Ninner=0,stepLen=1,maxNf;
		boolean lin=false,stepch=true;
		//	int stopw,stopy;
		// check maximum freq
		fmax = f[f.length-1];
		if (fmax>fs/2) fmax=fs/2;
		//
		// how many dyadic steps have actually to be performed to reach fmax
		for (Nd=0;Nd<maxNd;Nd++)
			if(f0*ffac*pow2(Nd)>=fmax) break; 
		//System.out.println("Nd= " +Nd);
		//
		// actually only stepfac that are dyadic are used
		//int dstepfac;// = (int) (Math.log((double)stepfac)/Math.log(2.));
		int stepfac;
		int MinStepLen=1;
		//
		// no chang of the step size - not recommended
		if (dstepfac==0){
			stepch = false;
			// MinStepLen=1;
			// System.out.println("Stepsize always:" + stepLen);
		}
		else{
			//dstepfac = (int) (Math.log((double)stepfac)/Math.log(2.));
			MinStepLen = Math.max(1,pow2(maxNd-Nd-df0-dstepfac+1));
			//System.out.println("MinStepLen:" + stepLen);
		}
		//System.out.println("dstepfac: " + dstepfac);
		stepfac = pow2(dstepfac);
		//System.out.println("stepfac: " + stepfac);
		//
		// smallest step Length 
		stepLen=MinStepLen;
		// System.out.println("stepLen: " + stepLen);
		// 
		// linear frequency axis - not recommended	
		//
		// 
		maxNf = f.length;
		Re = new double[maxNf][];
		Im = new double[maxNf][];
		deltaStept = new double[maxNf];
		maxNb = y.length/MinStepLen;
		Ninner = (int) Math.ceil((double) maxNf/Nd);
		//System.out.println("Ninner: " + Ninner);	
		t = new double[maxNb];
		for (int i=0;i<maxNb;i++) t[i]= (double) i*MinStepLen/fs;
		// calc Transform
		// outer dyadic loop
		//for(int i=0;i<f.length;i++) System.out.println(f[i]);
		for (int dy=0;dy<Nd;dy++){ //dy=Nd-1, the inner loop calcs until fmax
			//System.out.println("dy: " + dy);
			if (f0*pow2(dy+1)*ffac<f[index])
				dy++;
			len= pow2(maxNd - dy);
			//System.out.println("len: " + len);
			// calculate actual step length 
			if(stepch)
				stepLen = Math.max(MinStepLen,len/(int) f0/stepfac);
			// System.out.println("stepLen: " + stepLen);			
			// maximal Number translation parameters b
			maxNb = y.length/stepLen;
			//System.out.println("stepLen: " + stepLen + " len: " + len + " maxNb " + maxNb + " stepfac " + stepfac);
			// calc the wavelet
			Morlet morlet = new Morlet(len,f0);	
			// for linear freq axis
			Ninner = 0;
			int tempindex = index;
			while(f[tempindex]<f0*pow2(dy+1)*ffac){
			//	System.out.println("f["+tempindex+"]" + f[tempindex] + " " + f0*pow2(dy+1)*ffac);
				tempindex++;
				Ninner++;
				if(tempindex==f.length)
					break;
			}
			System.out.println("dy: "+ dy +" Ninner: "+Ninner);
			// inner frequency loop
			for (int inner=0;inner< Ninner;inner++){
				// real frequency
				//System.out.println(fmor);
				fmor = f[index]/pow2(dy)/ffac;
				deltaStept[index] = (double) stepLen/fs;
				//  System.out.println("deltaStept["+index+"]: " + deltaStept[index]);
				// change freq of morlet
				morlet.setF0(fmor);
				Re[index] = new double[maxNb];
				Im[index] = new double[maxNb];
				// translation loop
				for (int b=0;b<maxNb;b++){
		            startw=Math.max(0,len/2-b*stepLen);
		            starty=Math.max(0,b*stepLen-len/2);;
		            // do not calc at the boarder
		            //if(b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner)&&y.length-b*stepLen>len/stepfac/Math.pow(2.,(double)inner/Ninner))	            	
		            //if(b*stepLen>len/f0/Math.pow(2.,(double)inner/Ninner)&&y.length-b*stepLen>len/f0/Math.pow(2.,(double)inner/Ninner))	            	
		            for (int i=0;i<Math.min(len,y.length+len/2-b*stepLen)-startw;i++){
//		            		System.out.println("Re[index][b]: " +Re[index][b]);
//		            		System.out.println("y[starty+i]: " +y[starty+i]);
//		            		System.out.println("morlet.getRe(startw+i): " + morlet.getRe(startw+i));
		            		Re[index][b] += y[starty+i]*morlet.getRe(startw+i);
		            		Im[index][b] += y[starty+i]*morlet.getIm(startw+i);
		            	}
		            // multiply with 1/sqrt(a) and dt = 1/fs
		            Re[index][b] *= Math.sqrt(f[index])/fs;
		            Im[index][b] *= Math.sqrt(f[index])/fs;
				}
				// :end translation loop
				index++;
				//System.out.println(index);
				//if(index==maxNf)
				//	break outer; // breack if number of frequency points is reached
			} // :end inner frequency loop
		} // :end outer frequency loop
		//System.out.println("index= " +index);	
	}
	public double[][] getAbs(){
		double out[][] = new double[Re.length][];

		for (int i=0;i<out.length;i++){
			out[i] = new double[Re[i].length];
			for (int j=0;j<out[i].length;j++)
				out[i][j] = Math.pow(Re[i][j],2.) + Math.pow(Im[i][j],2.);
			}
		return out;
	}
	// by cols
	public double[] getAbs(int i){
		System.out.println(i);
		double out[] = new double[Re[i].length];
			for (int j=0;j<out.length;j++)
				out[j] = Math.pow(Re[i][j],2.) + Math.pow(Im[i][j],2.);
		return out;
	}
	// :end returning the absolute value
	// returning delta f for interp 
	public double df(int i){
		double out;
		if (i==0)
			out = 0.5*(f[1] - f[0]);
		else if (i==f.length-1)
			out = 0.5*(f[f.length-1]-f[f.length-2]);
		else
			out = 0.5*(f[i+1] - f[i-1]);
		return out;
	}
	// : help methods
	//
	// output methods
	// 
	public double[][] pd(){
		// power density
		// all values
		double out[][] = new double[f.length][t.length];

		for (int i=0;i<f.length;i++)
			out[i] = Numerics.interp(getAbs(i),df(i)/Ny,t.length);
		return out;
	}
	public double[][] pd(int [] cols){
		// power density
		// only cols values  
		double out[][] = new double[f.length][cols.length];
		for (int i=0;i<f.length;i++)
			out[i] = Numerics.interp(getAbs(i),df(i)/Ny,t.length,cols);
		return out;
	}
	
	public double[][] ed(){
		// energy density
		// all values
		System.out.println(f.length);
		double out[][] = new double[f.length][t.length];
		for (int i=0;i<f.length;i++)
			out[i] = Numerics.interp(getAbs(i),t.length);
		return out;
	}
	public double[][] ed(int [] cols){
		// energy density
		// only cols value
		double out[][] = new double[f.length][cols.length];
		for (int i=0;i<f.length;i++)
			out[i] = Numerics.interp(getAbs(i),1,t.length,cols);	
		return out;
	}
	public double[] et(){
		// energy over time
		double out[] = new double[t.length];
		double temp[][] = new double[f.length][t.length];
		double tempv[] = new double[f.length];
		for (int i=0;i<f.length-1;i++)
			temp[i] = Numerics.interp(getAbs(i),t.length);
		for (int j=0; j<t.length; j ++){
			for (int i=0;i<f.length-1;i++)
				tempv[i] = temp[i][j];
			out[j] = Numerics.trapez(tempv,f);
		}		
		return out;
	}

	public double[] ef(){
		// energy over frequency
		double out[] = new double[f.length];
		for (int i=1;i<f.length;i++)
			out[i] = Numerics.simpson(getAbs(i),deltaStept[i]);
		return out;
	}
	public double e(){
		// total energy
		double out;
		double temp[] = new double[f.length];
		for (int i=1;i<f.length;i++)
			temp[i] = Numerics.simpson(getAbs(i),deltaStept[i]);
		out = Numerics.trapez(temp, f);
		return 2*out; // 2*: negative part of the f-axis
	}	
	public double[] pf(){
		// energy over frequency
		double out[] = new double[f.length];
		double T=t[t.length-1];
	//	System.out.println("T: " + T);
		for (int i=1;i<f.length;i++)
			out[i] = Numerics.simpson(getAbs(i),df(i)/T*deltaStept[i]);
		return out;
	}	
	public static int pow2(int n){
		int out=1;
		for(int i=0;i<n;i++)
			out *=2;
		return out;
	}
	
	public static class Numerics{
		
	public static double simpson(double[] y){
		return simpson(y,1);
	}
	public static double simpson(double[] y , double dx){
		// Simpson's Rule: step size equal 
		double out=0;
			for (int i=2;i<y.length; i+=2)
				out += y[i-2]/6. + y[i]/6. + 2./3.*y[i-1];
		return out*2*dx;
	}	
	public static double trapez(double [] y, double[] x){
		// Trapez step size changes
		double out=0;
		for (int i=1;i<y.length; i++)
			out += (y[i-1] + y[i])*(x[i] - x[i-1])/2.;
	return out;		
	}
	public static double[] interp(double[] y, double a, int len){
		// interpol y on len
		double[] out = new double[len];
		//int step = len/y.length;
		if(y.length<len){
			int step = pow2((int) (Math.log((double)len/y.length)/Math.log(2.)));
//		System.out.println("step= "+step);
			for (int j=0;j<y.length-1;j++)
				for (int i=0;i<step;i++)
					out[j*step+i] = a*(y[j] +(double) i/step* (y[j+1] -y[j]));
			for (int i=0;i<(len-(y.length-1)*step);i++)
				out[(y.length-1)*step+i] = a*y[y.length-1];		
			return out;
		}
		else{
			for (int i=0;i<y.length;i++)
				y[i] *= a;
			return y;
		}
	}
	public static double[] interp(double[] y, int len){
		return interp(y,1,len);
	}
	public static double[] interp(double[] y, double a, int len, int[] index){
		double[] out = new double[index.length];
		double[] temp = new double[len];
	    if(y.length<len){
		    int step = pow2((int) (Math.log((double)len/y.length)/Math.log(2.)));		
			for (int j=0;j<y.length-1;j++)
				for (int i=0;i<step;i++)
					temp[j*step+i] = a*(y[j] +(double) i/step* (y[j+1] -y[j]));
		// alles nach dem letzten wird der letzte Wert zugewiesen
		for (int i=0;i<step;i++)
			temp[(y.length-1)*step+i] = a*y[y.length-1];
		for (int i=0;i<index.length;i++)
			out[i] = temp[index[i]+1];
	    }
		else{
			for (int i=0;i<index.length;i++)
				out[i] = a*y[index[i]+1];
		}
	    return out;
	} //:end method interp
	
	} //:end Class Numerics

	public class Morlet{
		private int np;
		private double f0,dt;
		private double beta=2;
		private double[][] vals;
		// Constructor
		public Morlet(int np,double f0){
			this.dt = 1 / (double) np;
			this.np=np;
			this.f0=f0;
			vals = new double[np][2];
			calcMorlet();
		}
		// private Functions
		private void calcMorlet(){
			double t;
			for (int it=0;it<np;it++){
				t = (it*dt-.5)*f0;
				vals[it][0] = Math.sqrt(Math.sqrt(beta/Math.PI))*Math.exp(-beta/2*t*t)*Math.cos(2*Math.PI*t);
				vals[it][1] = Math.sqrt(Math.sqrt(beta/Math.PI))*Math.exp(-beta/2*t*t)*Math.sin(2*Math.PI*t);
			//	vals[it][0] = Math.sqrt(beta/Math.PI)*Math.exp(-beta/2*t*t)*Math.cos(2*Math.PI*t);
			//	vals[it][1] = Math.sqrt(beta/Math.PI)*Math.exp(-beta/2*t*t)*Math.sin(2*Math.PI*t);
					
			}		
		}
		// public Functions
		public void setBeta(double beta){
			if (beta!=this.beta){
				this.beta=beta;
				calcMorlet();
			}
		}
		public void setF0(double f0){
			if (f0!=this.f0){
				this.f0=f0;
				calcMorlet();
			}
		}
		public double getRe(int index){
			return vals[index][0];
		}
		public double getIm(int index){
			return vals[index][1];
		}
			
	}

} // :end CWT