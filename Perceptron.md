# `The Percepton algorithm in  ScalaLab` #

`The ` _`Perceptron`_ `is a classical introductory algorithm in Neural Networks. We illustrate an implementation of it in ScalaLab. `

```
def perce( X: Array[Array[Double]], y: Array[Double], w_init: Array[Double]): Array[Double] =
   {
  val (l, nPoints) = X.size
  val  max_iter = 10000 // maximum allowable number of iterations
  val rho = 0.05 // Learning rate
  var w = w_init   // initialization of the paramater vector
  var iter = 0  // iteration counter
  var mis_class = nPoints  // # number of missclassified vectors
  var gradi = vzeros(l).getv

  var clsign = 0.0
 
   while ((mis_class > 0) && (iter < max_iter)) {
     iter += 1
     mis_class = 0
    for (i<-0 until nPoints) {
      clsign = (X(::, i) dot w) * y(i)

      if (clsign < 0)   {  // misclassification
        mis_class += 1
        gradi = gradi + rho*(-y(i)*X(::, i))
      }
   }
 w = w - rho*gradi
 }
 w
}

var Nt1 = 50
var Nt2 = 50
var Xtrain = Array.ofDim[Double](2, Nt1+Nt2)
var Y = new Array[Double](Nt1+Nt2)

// form seperatable data sets first
for (k<-0 until Nt1) {
   Xtrain(0)(k) = -5.0*(1+rand)
   Xtrain(1)(k) = -5.0*(1+rand)
   Y(k) = -1.0
 }
for (k<-Nt1 until Nt1+Nt2) {
   Xtrain(0)(k) = 5.0*(1+rand)
   Xtrain(1)(k) = 5.0*(1+rand)
   Y(k) = 1.0
 }


figure(1); title("Separatable patterns case")
scatterPlotsOn
plot(Xtrain(::, 0, Nt1-1), Color.GREEN, "Class 1")
plot(Xtrain(::, Nt1, Nt1+Nt2-1), Color.BLUE ,  "Class 2")
    
var winit = Array(-3.0, 1.0)
var wlsep = perce( Xtrain, Y,  winit)

var Np = 200
var xvals = linspace(-6, 6, Np)

hold("on")
linePlotsOn
var ylineOrig = xvals map (x => winit(0) + winit(1)*x)  // i.e. w0+w1*x
plot(xvals, ylineOrig, Color.BLACK, "Separating Line formed by the initial weights of the perceptron")

var yvals = xvals map  (wlsep(0) + wlsep(1) *_ )  // i.e. w0 + w1*x

plot(xvals, yvals, Color.RED, "Perceptron separating line")



// Non-Separatable patterns

var Xtrain_nonsep = Array.ofDim[Double](2, Nt1+Nt2)
var Y_nonsep = new Array[Double](Nt1+Nt2)

// form non-seperatable data sets first
for (k<-0 until Nt1) {
   Xtrain_nonsep(0)(k) = -1.0 + 6* rand
   Xtrain_nonsep(1)(k) = -1.0 + 6* rand
   Y_nonsep(k) = -1.0
 }
for (k<-Nt1 until Nt1+Nt2) {
   Xtrain_nonsep(0)(k) = 1.0 + 6*rand
   Xtrain_nonsep(1)(k) = 1.0 + 6*rand
   Y_nonsep(k) = 1.0
 }
 
 figure(2); title("Non-Separatable patterns case")
scatterPlotsOn
plot(Xtrain_nonsep(::, 0, Nt1-1), Color.GREEN, "Class 1")
plot(Xtrain_nonsep(::, Nt1, Nt1+Nt2-1), Color.BLUE, "Class 2")
    
var winit_nonsep = Array(-3.0, 1.0)
var wlsep_nonsep = perce( Xtrain_nonsep, Y_nonsep,  winit)

var Np_nonsep = 200
var xvals_nonsep = linspace(-6, 6, Np)

hold("on")
linePlotsOn
var ylineOrig_nonsep = xvals map (x => winit_nonsep(0) + winit_nonsep(1)*x)  // i.e. w0+w1*x
plot(xvals_nonsep, ylineOrig_nonsep, Color.BLACK, "Separating Line formed by the initial weights of the perceptron (non-separable case) ")

var yvals_nonsep = xvals_nonsep map  (wlsep_nonsep(0) + wlsep_nonsep(1) *_ )  // i.e. w0 + w1*x

plot(xvals_nonsep, yvals_nonsep, Color.RED, "Perceptron separating line for the non-separable case")


```