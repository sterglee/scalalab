# Introduction #

`ScalaLab started to support the development of applications intended for the Java Embedded run time.`

`We can produce code for the Embedded Java Runtime using the ` **`Shift-F9`** `keystroke. This executes Java code in the editor's text (i.e. not Scala or ScalaSci code but plain Java classes). The following .jar dependences are currently used from the internal Java compiler of ScalaLab:` **`EJMLFile, jsciFile, mtjColtSGTFile, ApacheCommonsFile, numalFile, LAPACKFile, ARPACKFile.`** `Therefore, to run the produced .class file on the Java Embedded platform and if you use routines from these files, you should place them at the classpath.`

`We provide an example of generating a Java class for the Java Embedded target. Suppose that we have the following code: `

```

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
    
public class testEJ {
 

    public static void main(String [] args)  {

        double mn = 20.0; double sigma = 34.5;
    NormalDistribution nd = new NormalDistribution(mn, sigma);
    
    double xstart = 10; double dx = 0.1; double xend = 30;
    double xc = xstart;
    while (xc < xend)  {
        System.out.println(" ND (  "+xc+" ) = "+ nd.density(xc));
        xc += dx;
    }
    nd.density(mn);
     }
}

```

`The steps to run the code above are simple: `

  * `Compile the code using ` **`Shift-F9`** `or with the relevant menu option from the ` **`Compile`** `menu.`

  * `Execute the produced ` _`testEJ.class`_ `with the command `

`java -cp .:ApacheCommonMaths.jar testEJ`