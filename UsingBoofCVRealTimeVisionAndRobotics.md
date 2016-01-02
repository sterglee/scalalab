# Introduction #

`BoofCV is an open source Java library for real-time computer vision and robotics applications developed by Peter Abeles (http://www.boofcv.org/). It is available as a ScalaLab toolbox and can be installing, as usually, i.e. by placing the boofCV.jar at the classpath of the Scala interpreter. We present here examples of using BoofCV as a ScalaLab toolbox.`

# Example 1 #
`This is a Java example taken from the source of Peter Abeles, that can be executed from the ScalaLab editor with F9. This example verifies that the toolbox is placed correctly at the classpath. However, ScalaLab can exploit  more easily and effectively the algorithms of the toolbox by means of Scala code.`

```


import boofcv.abst.filter.blur.BlurFilter;
import boofcv.abst.filter.derivative.ImageGradient;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.alg.filter.derivative.GImageDerivativeOps;
import boofcv.alg.filter.derivative.GradientSobel;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.core.image.border.BorderType;
import boofcv.core.image.border.FactoryImageBorder;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.factory.filter.derivative.FactoryDerivative;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageUInt8;

import java.awt.image.BufferedImage;

/**
 * An introductory example designed to introduce basic BoofCV concepts.  Each function
 * shows how to perform basic filtering and display operations using different techniques.
 *
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class ImageFilterExample {

	public static void procedural( ImageUInt8 input )
	{
		ImageUInt8 blurred = new ImageUInt8(input.width,input.height);
		ImageSInt16 derivX = new ImageSInt16(input.width,input.height);
		ImageSInt16 derivY = new ImageSInt16(input.width,input.height);

		// Gaussian blur: Convolve a Gaussian kernel with a width of 5 pixels
		BlurImageOps.gaussian(input,blurred,-1,2,null);

		// Calculate image's derivative
		GradientSobel.process(blurred, derivX, derivY, FactoryImageBorder.extend(input));

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeSign(derivX,null,-1);
		ShowImages.showWindow(outputImage,"Procedural Fixed Type");
	}

	public static <T extends ImageBase, D extends ImageBase>
	void generalized( T input )
	{
		Class<T> inputType = (Class<T>)input.getClass();
		Class<D> derivType = GImageDerivativeOps.getDerivativeType(inputType);

		T blurred = GeneralizedImageOps.createImage(inputType,input.width, input.height);
		D derivX = GeneralizedImageOps.createImage(derivType,input.width, input.height);
		D derivY = GeneralizedImageOps.createImage(derivType,input.width, input.height);

		// Gaussian blur: Convolve a Gaussian kernel with a width of 5 pixels
		GBlurImageOps.gaussian(input, blurred, -1, 2, null);

		// Calculate image's derivative
		GImageDerivativeOps.sobel(blurred, derivX, derivY, BorderType.EXTENDED);

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeSign(derivX,null,-1);
		ShowImages.showWindow(outputImage,"Generalized "+inputType.getSimpleName());
	}

	public static <T extends ImageBase, D extends ImageBase>
	void filter( T input )
	{
		Class<T> inputType = (Class<T>)input.getClass();
		Class<D> derivType = GImageDerivativeOps.getDerivativeType(inputType);

		T blurred = GeneralizedImageOps.createImage(inputType, input.width, input.height);
		D derivX = GeneralizedImageOps.createImage(derivType, input.width, input.height);
		D derivY = GeneralizedImageOps.createImage(derivType, input.width, input.height);

		// declare image filters
		BlurFilter<T> filterBlur = FactoryBlurFilter.gaussian(inputType, -1, 2);
		ImageGradient<T,D> gradient = FactoryDerivative.sobel(inputType, derivType);

		// process the image
		filterBlur.process(input,blurred);
		gradient.process(blurred,derivX,derivY);

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeSign(derivX,null,-1);
		ShowImages.showWindow(outputImage,"Filter "+inputType.getSimpleName());
	}

	public static void nogenerics( ImageBase input )
	{
		Class inputType = input.getClass();
		Class derivType = GImageDerivativeOps.getDerivativeType(inputType);

		ImageBase blurred = GeneralizedImageOps.createImage(inputType,input.width, input.height);
		ImageBase derivX = GeneralizedImageOps.createImage(derivType,input.width, input.height);
		ImageBase derivY = GeneralizedImageOps.createImage(derivType,input.width, input.height);

		// Gaussian blur: Convolve a Gaussian kernel with a width of 5 pixels
		GBlurImageOps.gaussian(input, blurred, -1, 2, null);

		// Calculate image's derivative
		GImageDerivativeOps.sobel(blurred, derivX, derivY, BorderType.EXTENDED);

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeSign(derivX,null,-1);
		ShowImages.showWindow(outputImage,"Generalized "+inputType.getSimpleName());
	}

	public static void main( String args[] ) {

                                    String  imageFile = javax.swing.JOptionPane.showInputDialog(null, "Please specify the full path name of your image (e.g. /home/sp/NBProjects/BoofCV/data/outdoors01.jpg)");
		BufferedImage image = UtilImageIO.loadImage(imageFile);

		// produces the same results
		procedural(ConvertBufferedImage.convertFrom(image,null,ImageUInt8.class));
		generalized(ConvertBufferedImage.convertFrom(image, null, ImageUInt8.class));
		filter(ConvertBufferedImage.convertFrom(image, null, ImageUInt8.class));
		nogenerics(ConvertBufferedImage.convertFrom(image, null, ImageUInt8.class));

		// try another image input type
		generalized(ConvertBufferedImage.convertFrom(image,null,ImageFloat32.class));
	}
}

```