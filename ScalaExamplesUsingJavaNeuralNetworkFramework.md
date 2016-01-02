# Introduction #

`ScalaSci is much more convenient than Java for writing scientific code. Here we present examples of neural network models using the Neuroph toolbox in scalaSci. The prerequisite of course is to install the Neuroph toolbox. `


# Perceptron example #

```

import java.util.Arrays

import org.neuroph.core.NeuralNetwork
import org.neuroph.core.learning.SupervisedTrainingElement
import org.neuroph.core.learning.TrainingSet
import org.neuroph.nnet.Perceptron

 import collection.JavaConversions._

    /**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
   def  testNeuralNetwork(neuralNet: NeuralNetwork, trainingSet: TrainingSet[SupervisedTrainingElement]) = {

        for(trainingElement <- trainingSet.elements()) {
            neuralNet.setInput(trainingElement.getInput(): _*)
            neuralNet.calculate()
            var  networkOutput = neuralNet.getOutput()

            print("Input: " + Arrays.toString(trainingElement.getInput()) )
            println(" Output: " + Arrays.toString(networkOutput) )
        }
    }

           var trainingSet = new TrainingSet[SupervisedTrainingElement]("AND")

           // create training set (logical AND function)
           trainingSet.addElement( new SupervisedTrainingElement(Array(0.0, 0.0), Array(0.0)))
           trainingSet.addElement( new SupervisedTrainingElement(Array(0.0, 1.0), Array(0.0)))
           trainingSet.addElement( new SupervisedTrainingElement(Array(1.0, 0.0), Array(0.0)))
           trainingSet.addElement( new SupervisedTrainingElement(Array(1.0, 1.0), Array(1.0)))
           
            // create perceptron neural network
            var  myPerceptron = new Perceptron(2, 1)
            // learn the training set
            myPerceptron.learn(trainingSet)
            // test perceptron
            println("Testing trained perceptron")
            testNeuralNetwork(myPerceptron, trainingSet)
            // save trained perceptron
            myPerceptron.save("mySamplePerceptron.nnet")
            // load saved neural network
            var  loadedPerceptron = NeuralNetwork.load("mySamplePerceptron.nnet")
            // test loaded neural network
            println("Testing loaded perceptron")
            testNeuralNetwork(loadedPerceptron, trainingSet)

    

    
```