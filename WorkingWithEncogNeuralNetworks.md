# Introduction #

`ScalaLab can be used to work more effectively and easily with Java libraries. For example the ` **`Encog`** `library of Jeff Heaton, (http://code.google.com/p/encog-java/), is a superb neural network library. We explain how we can utilize it better from ScalaLab. This document refers to the Encog2 classes, that are retained since they are described also in Jeff Heaton's book.  It is a useful exercise also for demonstrating how to install directories that have compiled class files at the classpath of ScalaLab. However, the`  **`Encog3`** `system, is more updated and also easier to install. We present some examples of Encog3 in other pages.`


# Updating the Classpath #

`The first essential step is to update the classpath of the Scala Interpreter, in order to include the Encog classes. This is accomplished easily as: `

  1. `Download the ` _`encogClasses.zip`_ `file that is included for download as ScalaLab toolbox.`
  1. `Unzip` _`encogClasses.zip`_ `to a folder, for example to: /home/sp/NBProjects/encog/  It is important to note that since encog packages start with `_`org, `_  _`org`_ `should correspond to a top-level folder, i.e. /home/sp/NBProjects/encog/org`
  1. `Make the folder visible in ScalaLabExplorer, one way to achieve this, is to type the name of the folder in the "Specify Path" textfield, and then clicking "Browse"`
  1. `Right click the mouse over the "/home/sp/NBProjects/encog/" node in order to open the popup menu. Then select "Paths" => "Append the path to the scalaSci Paths"
  1. `Finally create a new Scala Interpreter that includes the updated set of scalaSci paths in its classpath, by opening the pop-up menu of the ScalaLab Console, and using for example the "Reset Scala Interpreter using ScalaLab default imports" option`

`At this point the displayed list of classpath components of the Scala Interpreter should include /home/sp/NBProjects/encog/. If so, we can proceed to explore the Encog library from ScalaLab as the examples below illustrate.`

# Examples #

`The examples are adapted from the Java code created by Jeff Heaton. Before its example we give it the same title as the original Java code had'

# Solve XOR with RPROP #

```

import org.encog.neural.activation.ActivationSigmoid
import org.encog.neural.data.NeuralData
import org.encog.neural.data.NeuralDataPair
import org.encog.neural.data.NeuralDataSet
import org.encog.neural.data.basic.BasicNeuralDataSet
import org.encog.neural.networks.BasicNetwork
import org.encog.neural.networks.layers.BasicLayer
import org.encog.neural.networks.training.Train
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation
import org.encog.util.logging.Logging


var XOR_INPUT = M("0.0, 0.0; 1.0 0.0; 0.0 1.0; 1.0 1.0").getv
var XOR_IDEAL = M("0.0;  1.0;  1.0;  0.0" ).getv


Logging.stopConsoleLogging

var network = new BasicNetwork
network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 2))
network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 4))
network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1))

network.getStructure().finalizeStructure()

network.reset

var trainingSet = new BasicNeuralDataSet( XOR_INPUT, XOR_IDEAL)

// train the neural network
var train = new ResilientPropagation(network, trainingSet)

var epoch = 1
 do {
       train.iteration()
       println("Epoch #"+epoch+" Error: "+train.getError())
       epoch += 1
    }
 while (train.getError() > 0.01)
 
   // test the neural network
println(" Neural Network Results: ")
var iter = trainingSet.iterator 
while (iter.hasNext()) {
    var pair = iter.next.asInstanceOf[NeuralDataPair]
    var output = network.compute(pair.getInput)
    println(pair.getInput().getData(0)+","+pair.getInput().getData(1)+ 
          ", actual = "+output.getData(0)+", ideal = "+pair.getIdeal().getData(0))
       }
    
              
```