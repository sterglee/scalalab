# Introduction #

`WEKA can be installed easily as a ScalaLab toolbox. Specifically, we can download the weka.jar file, and using the "ScalaSci Toolboxes" tab, we can specify it (with "Specify toolboxes") and then load it (with "Import toolboxes"). Here, we present some examples of using WEKA with ScalaLab scripting code`


# WEKA Examples #

# Multilayer Perceptron #

```
 
// this script  requires  WEKA toolbox to be first installed
import weka.classifiers.functions._
import weka.classifiers.Classifier
import weka.core._
import java.io._
import weka.core.converters.ConverterUtils._

var dataFile = getFile("Please specify your data file")   // get the datafile from the user

   //  read the datafile
var allData = DataSource.read(dataFile)
allData.setClassIndex(allData.numAttributes()-1)



// construct the training set and testing sets
var trainSet = new Instances(allData, 0)  // create an initial empty training set
var testSet = new Instances(allData, 0)    // create an initial empty testing set

var UseInTrain = true  // controls whether to add the Instance at the training set or at the testing set
var enumInstances = allData.enumerateInstances()
while  (enumInstances.hasMoreElements) {
 var currInstance  = enumInstances.nextElement.asInstanceOf[Instance]
 if (UseInTrain)
    trainSet.add(currInstance)
 else
   testSet.add(currInstance)
 UseInTrain = !UseInTrain
}

  // construct an MLP classifier and train it
var MLPNet =  new MultilayerPerceptron // create a WEKA MLP structure

MLPNet.buildClassifier(trainSet)   // build an MLP classifier on the training set
// test the classifier on the testing set
// extract the class labels
var enumTestInstances = testSet.enumerateInstances()  
var numTestingInstances = testSet.numInstances()
var classLabels = new Array[Double](numTestingInstances)
var predictedLabels  = new Array[Double](numTestingInstances)

var cnt=0
var classIdx = testSet.classIndex   // get class index
while (enumTestInstances.hasMoreElements) {   // for all the elements of the testing set
   var currInstance = enumTestInstances.nextElement.asInstanceOf[Instance]
   var distForInstance = MLPNet.distributionForInstance(currInstance)
   var classOfInstance =  currInstance.toDoubleArray.apply(classIdx)
   classLabels(cnt) = classOfInstance
   predictedLabels(cnt) = distForInstance(0)
   cnt += 1
}
   
figure(1)
linePlotsOn
hold("on")
plot(predictedLabels, Color.RED, "predicted");
plot(classLabels, Color.BLUE, "actual class"); title("MLP Network prediction")

```


# Association Rules with the APriori Algorithm #

```
var dataFile = getFile("Please specify your data file");

var fr = new java.io.FileReader(dataFile);

 // get the instances from the data file
var instances = new weka.core.Instances(fr);

// construct a WEKA Apriori associator object
var aprioriObj = new weka.associations.Apriori()

aprioriObj.buildAssociations(instances);

// get the extracted association rules

var aprioriRules = aprioriObj.toString();
  // send the extracted rules to the Console
println ("Extracted rules :  \n\n"+aprioriRules)

//  display them also in a JTextArea
var frame = new JFrame("Apriori Rules");
var rulesArea = new JTextArea(aprioriRules);
frame.add(rulesArea);
frame.setSize(500, 500);
frame.setVisible(true);

```