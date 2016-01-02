# Introduction #

`The Java Neural Network Framework (http://neuroph.sourceforge.net/) can be readily installed as a ScalaLab toolbox (see downloads). We present here some examples in Java . `

`You should install toolbox neuroph-2.6.jar and run the examples with F9 within the jsyntaxpane editor.`


# Perceptron Sample #

```

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Perceptron;

/**
 * This sample shows how to create, train, save and load simple Perceptron neural network
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class PerceptronSample {

    /**
     * Runs this sample
     */
    public static void main(String args[]) {
 
            // create training set (logical AND function)
            TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(2, 1);
            trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 0}, new double[]{0}));
            trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 1}, new double[]{0}));
            trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 0}, new double[]{0}));
            trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 1}, new double[]{1}));

            // create perceptron neural network
            NeuralNetwork myPerceptron = new Perceptron(2, 1);
            // learn the training set
            myPerceptron.learn(trainingSet);
            // test perceptron
            System.out.println("Testing trained perceptron");
            testNeuralNetwork(myPerceptron, trainingSet);
            // save trained perceptron
            myPerceptron.save("mySamplePerceptron.nnet");
            // load saved neural network
            NeuralNetwork loadedPerceptron = NeuralNetwork.load("mySamplePerceptron.nnet");
            // test loaded neural network
            System.out.println("Testing loaded perceptron");
            testNeuralNetwork(loadedPerceptron, trainingSet);

    }

    /**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, TrainingSet<SupervisedTrainingElement> trainingSet) {

        for(SupervisedTrainingElement trainingElement : trainingSet.elements()) {
            neuralNet.setInput(trainingElement.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString(trainingElement.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput) );
        }
    }


```

## XOR Multilayer Perceptron ##

```

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 * This sample shows how to create, train, save and load simple Multi Layer Perceptron for the XOR problem.
 * This sample shows basics of Neuroph API.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class XorMultiLayerPerceptronSample {

    /**
     * Runs this sample
     */
    public static void main(String[] args) {
    	
        // create training set (logical XOR function)
        TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(2, 1);
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 0}, new double[]{0}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 1}, new double[]{1}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 0}, new double[]{1}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 1}, new double[]{0}));

        // create multi layer perceptron
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);

        // enable batch if using MomentumBackpropagation
        if( myMlPerceptron.getLearningRule() instanceof MomentumBackpropagation )
        	((MomentumBackpropagation)myMlPerceptron.getLearningRule()).setBatchMode(true);

        // learn the training set
        System.out.println("Training neural network...");
        myMlPerceptron.learn(trainingSet);

        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(myMlPerceptron, trainingSet);

        // save trained neural network
        myMlPerceptron.save("myMlPerceptron.nnet");

        // load saved neural network
        NeuralNetwork loadedMlPerceptron = NeuralNetwork.load("myMlPerceptron.nnet");

        // test loaded neural network
        System.out.println("Testing loaded neural network");
        testNeuralNetwork(loadedMlPerceptron, trainingSet);
    }

    /**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, TrainingSet<SupervisedTrainingElement> trainingSet) {

        for(SupervisedTrainingElement trainingElement : trainingSet.elements()) {
            neuralNet.setInput(trainingElement.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString( trainingElement.getInput() ) );
            System.out.println(" Output: " + Arrays.toString( networkOutput) );
        }
    }

}

```

## XOR Resilient Propagation Sample ##

```

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 * This sample trains Multi Layer Perceptron network using Resilient Propagation
 * learning rule for the XOR problem.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class XorResilientPropagationSample {

    /**
     * Runs this sample
     */
    public static void main(String[] args) {
    	
        // create training set (logical XOR function)
        TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(2, 1);
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 0}, new double[]{0}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 1}, new double[]{1}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 0}, new double[]{1}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 1}, new double[]{0}));

        // create multi layer perceptron
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 3, 1);
        // set ResilientPropagation learning rule
        myMlPerceptron.setLearningRule(new ResilientPropagation()); 
       
        // learn the training set
        System.out.println("Training neural network...");
        myMlPerceptron.learn(trainingSet);

        int iterations = ((SupervisedLearning)myMlPerceptron.getLearningRule()).getCurrentIteration();        
        System.out.println("Learned in "+iterations+" iterations");
        
        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(myMlPerceptron, trainingSet);

    }

    /**
     * Prints network output for each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, TrainingSet<SupervisedTrainingElement> trainingSet) {

        for(SupervisedTrainingElement trainingElement : trainingSet.elements()) {
            neuralNet.setInput(trainingElement.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString( trainingElement.getInput() ) );
            System.out.println(" Output: " + Arrays.toString( networkOutput) );
        }
    }

}


```