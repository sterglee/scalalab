# Introduction #

`This page aims to demonstrate data mining techniques on microRNA data using ScalaLab with the WEKA toolbox. Currently, the page is in very initial stage. `

`You can download the file ` **`Nature_miR_classify_cancer.zip`** `that contains data in .arff format and accompanied with a research paper that describes them.`

`Then you should install the weka.jar toolbox.`

`You can process the miRNA.arff file using the following code, that performs imputation of missing values and applies the Expectation Maximization (EM) clustering of WEKA: `

```

// object that implements a simple imputation policy,
// of replacing a missing value with the mean of its row
object  ImputeMissing {
        
def isMissing (x: Double) = x == 5 // define what a missing entry means
// compute the mean of x avoiding the missing entries
def meanExceptMissing(x: RichDoubleArray, isMissing: Double =>Boolean) =  {
        var sm = 0.0
        var cnt = 0
        for (k<-0 until x.length) 
         if  (!isMissing(x(k)))
          {
                sm += x(k)
                cnt += 1
          }
          sm/cnt
}



// replace missing values where if a value is missing is determined by the isMissing function
def replaceMissingValues( A: RichDoubleDoubleArray, isMissing: Double => Boolean)  = {
        var cnt = 0 // count of replaced values
        for (row <-0 until A.numRows) {
// compute the mean of the row, not counting the missing values
          var rowMean = meanExceptMissing(A(row, ::), isMissing )
          for (col <- 0 until A.numColumns) 
            if (isMissing(A(row, col)))   {  // missing entry
              A(row, col) = rowMean
              cnt += 1
            }
        } 
        cnt      
 }

}

// this file performs data mining using the WEKA toolbox at miRNA data set

// this example, can take some time to perform data mining, so please wait!!
// (alternatively, you can run it in an independent thread, 
// with Shift-F6, with the risk of corrupting the Swing, since it is not thread safe)

import weka.core._
import weka.core.converters.ConverterUtils._

var currentWorkingDirectory = pwd  // get the current working directory
var dataFile = getFile(currentWorkingDirectory, "Specify miRNA data")
   
   //  Read the datafile, allData will be  an Instances object
var allData = DataSource.read(dataFile)


// construct the training set 
var trainSet = new Instances(allData, 0)  // create an initial empty training set


var enumInstances = allData.enumerateInstances()
while  (enumInstances.hasMoreElements) {
 var currInstance  = enumInstances.nextElement.asInstanceOf[Instance]
   trainSet.add(currInstance)
 }

var nInstances = trainSet.numInstances  // get number of instances
var firstInstance = trainSet.firstInstance
var nattributes = firstInstance.numAttributes  // get number of attributes

var attributeInfo = new weka.core.FastVector
var attributeNames = new Array[String](nattributes)   // keep the names of the attributes
// read the attributes
var enumAttributes = allData.enumerateAttributes()
var geneNameAttrib =  enumAttributes.nextElement  // the first attribute is the gene name
var descrAttribb = enumAttributes.nextElement  // the next attribute is the gene description
var k = 0
while (enumAttributes.hasMoreElements) {
           var currentAttribute = enumAttributes.nextElement.asInstanceOf[Attribute]
           attributeNames(k) = currentAttribute.name  // get attribute name
        attributeInfo.addElement(currentAttribute)
        k += 1
    }
   
       

// first two attributes are name and description thus keep nattributes-2 space
var trainSetRDA = new RichDoubleDoubleArray(nInstances, nattributes-2)
var geneNames = new Array[String](nInstances)
var description = new Array[String](nInstances)
for (k<-0 until nInstances) {
    var currInstance = trainSet.instance(k)

    geneNames(k) = currInstance.stringValue(0)   // get the gene name
    description(k) = currInstance.stringValue(1)  // get the description
    for (attr<-2 until nattributes) 
        trainSetRDA(k, attr-2) = currInstance.value(attr)
    }


var imputedTrainData = trainSetRDA.copy   // copy of training data in which we impute missing values

import  ImputeMissing._    // import the imputation routines
var cntReplaced = replaceMissingValues(imputedTrainData, isMissing)

var capacity = nInstances
attributeInfo.size
var  missingImputedInstances = new Instances( "imputedRNA", attributeInfo, capacity)


 var weight = 1.0
 for (inst<-0 until nInstances)  {
   var attValues = imputedTrainData(inst,::)
   var nInst = new Instance(weight, attValues)
   nInst.setDataset(missingImputedInstances)
   missingImputedInstances.add(nInst)
}


// now test WEKA clusterers

var trainSetImputed = missingImputedInstances
// start with the EM clusterer
var  emWEKA = new weka.clusterers.EM()   // create a WEKA clusterer
emWEKA.buildClusterer(trainSetImputed)    //  build the clusterer with EM,   this statement takes some time!

// print information about the WEKA clusterer
println("\n\n emWEKA clusterer \n\n"+ emWEKA.toString())

var numClusters = emWEKA.numberOfClusters()   // return the created number of clusters

println("By cross validation WEKA constructed "+numClusters +" clusters ")
// report some information on the created clusters
println("Some information about the WEKA EM clusterer "+ emWEKA.toString)

// get the normal estimators for each cluster
var clusterModelsCenters = emWEKA.getClusterModelsNumericAtts

clusterModelsCenters(0).size
// take the "means"  of the clusters
var emMeans = new RDDA(numClusters,  nattributes-2)  

for (k<-0 until numClusters) {   // for all computed clusters
           // extract the cluster params, for each attribute WEKA outputs the mean, the standard deviation, and a "weight" parameter, I don't know up to now what it means
        var currentClusterParams = new RDDA(clusterModelsCenters(k))
     for (attr<-2 until nattributes-3)   // first two attributes are gene name and description
        emMeans(k, attr-2) = currentClusterParams(attr, 0)
}


// plot the computed means with the WEKA EM clusterer
figure(3); title("Computed means by the EM clusterer")
hold("on")
for (k<-0 until numClusters)
  plot(emMeans(k, ::),  newColor, "cluster "+k)
  
//  print the first center parameteres
var firstCluster = new RDDA(clusterModelsCenters(0))
firstCluster.print

```

## Tissue classification ##

`We can perform other experiments by using the same miRNA data with in a transposed style, i.e. with rows being the tissues and genes the attributes. `

`Here is some code that process the data by transposing them in order the rows (instances) to correspond to tissues and the columns (attributes) to genes. The code applies also   EM clustering. `

```
// object that implements a simple imputation policy,
// of replacing a missing value with the mean of its row
object  ImputeMissing {
        
def isMissing (x: Double) = x == 5 // define what a missing entry means
// compute the mean of x avoiding the missing entries
def meanExceptMissing(x: RichDoubleArray, isMissing: Double =>Boolean) =  {
        var sm = 0.0
        var cnt = 0
        for (k<-0 until x.length) 
         if  (!isMissing(x(k)))
          {
                sm += x(k)
                cnt += 1
          }
          sm/cnt
}



// replace missing values where if a value is missing is determined by the isMissing function
def replaceMissingValues( A: RichDoubleDoubleArray, isMissing: Double => Boolean)  = {
        var cnt = 0 // count of replaced values
        for (row <-0 until A.numRows) {
// compute the mean of the row, not counting the missing values
          var rowMean = meanExceptMissing(A(row, ::), isMissing )
          for (col <- 0 until A.numColumns) 
            if (isMissing(A(row, col)))   {  // missing entry
              A(row, col) = rowMean
              cnt += 1
            }
        } 
        cnt      
 }

}


 

// this file performs data mining using the WEKA toolbox at miRNA data set

// this example, can take some time to perform data mining, so please wait!!
// (alternatively, you can run it in an independent thread, 
// with Shift-F6, with the risk of corrupting the Swing, since it is not thread safe)

import weka.core._
import weka.core.converters.ConverterUtils._

var currentWorkingDirectory = pwd  // get the current working directory
var dataFile = getFile(currentWorkingDirectory, "Specify miRNA data")
   
   //  Read the datafile, allData will be  an Instances object
var allData = DataSource.read(dataFile)


// construct the training set 
var trainSet = new Instances(allData, 0)  // create an initial empty training set


var enumInstances = allData.enumerateInstances()
while  (enumInstances.hasMoreElements) {
 var currInstance  = enumInstances.nextElement.asInstanceOf[Instance]
   trainSet.add(currInstance)
 }

var nInstances = trainSet.numInstances  // get number of instances
var firstInstance = trainSet.firstInstance
var nattributes = firstInstance.numAttributes  // get number of attributes

var attributeInfo = new weka.core.FastVector
var attributeNames = new Array[String](nattributes)   // keep the names of the attributes
// read the attributes
var enumAttributes = allData.enumerateAttributes()
var geneNameAttrib =  enumAttributes.nextElement  // the first attribute is the gene name
var descrAttrib = enumAttributes.nextElement  // the next attribute is the gene description
var k = 0
while (enumAttributes.hasMoreElements) {
           var currentAttribute = enumAttributes.nextElement.asInstanceOf[Attribute]
           attributeNames(k) = currentAttribute.name  // get attribute name
        attributeInfo.addElement(currentAttribute)
        k += 1
    }
   
       

// first two attributes are name and description thus keep nattributes-2 space
var trainSetRDA = new RichDoubleDoubleArray(nInstances, nattributes-2)
var geneNames = new Array[String](nInstances)
var description = new Array[String](nInstances)
for (k<-0 until nInstances) {
    var currInstance = trainSet.instance(k)

    geneNames(k) = currInstance.stringValue(0)   // get the gene name
    description(k) = currInstance.stringValue(1)  // get the description
    for (attr<-2 until nattributes) 
        trainSetRDA(k, attr-2) = currInstance.value(attr)
    }

import ImputeMissing._
var imputedTrainData = trainSetRDA.copy   // copy of training data in which we impute missing values

var cntReplaced = replaceMissingValues(imputedTrainData, isMissing)
println("imputed "+cntReplaced+" missing values") 

var tissuesAsRows  = imputedTrainData~  // transpose data 
// geneNames now will be the new attribte set
var ngenes = geneNames.size  // number of genes
//var genesAsAttributesAndClass = new Array[String](ngenes+1)
var genesAsAttributesAndClass = new Array[String](ngenes)

for (geneId<-0 until ngenes)  // genes will beocme attributes
   genesAsAttributesAndClass(geneId) = geneNames(geneId)
//genesAsAttributesAndClass(ngenes) = "Classification"   // last attribute is classificaction

// construct instances now. Rows will be tissue types, i.e. the previous attributes
var ngenesAndClass = genesAsAttributesAndClass.length
var fv_genesAsAttributesAndClass = new FastVector(ngenesAndClass)
for (genesAndClassId <- 0 until ngenesAndClass)
 fv_genesAsAttributesAndClass.addElement( new Attribute(genesAsAttributesAndClass(genesAndClassId)))

var weight = 1.0
var ntissues = tissuesAsRows.size._1
var capacity = ntissues
var  tissuesAsRowsDataSet = new Instances( "tissuesAsRows", fv_genesAsAttributesAndClass, capacity)

var tissueTypes = attributeNames
	
for (tissueId<- 0 until ntissues) {
	var attValues = tissuesAsRows(tissueId, ::)
	var attValuesAndClassif = new Array[Double](ngenesAndClass)
	for (gid<-0 until ngenesAndClass-1)
 	  attValuesAndClassif(gid) = attValues(gid)
//	var classif = -1.0  // defaullt tumor type
//	if (tissueTypes(tissueId)(0)=='N')   // normal tissue
//	  classif = 1.0
//	 attValuesAndClassif( ngenesAndClass-1 ) = classif
	  
     var nInst = new Instance(weight, attValuesAndClassif)
     nInst.setDataset(tissuesAsRowsDataSet)
     tissuesAsRowsDataSet.add(nInst)
   }
 
 

// now test WEKA clusterers

// start with the EM clusterer
var  emWEKA = new weka.clusterers.EM()   // create a WEKA clusterer
emWEKA.setNumClusters(2)
emWEKA.buildClusterer(tissuesAsRowsDataSet)    //  build the clusterer with EM,   this statement takes some time!

// print information about the WEKA clusterer
println("\n\n emWEKA clusterer \n\n"+ emWEKA.toString())

var numClusters = emWEKA.numberOfClusters()   // return the created number of clusters

println("By cross validation WEKA constructed "+numClusters +" clusters ")
// report some information on the created clusters
println("Some information about the WEKA EM clusterer "+ emWEKA.toString)

// get the normal estimators for each cluster
var clusterModelsCenters = emWEKA.getClusterModelsNumericAtts


// take the "means"  of the clusters
var emMeans = new RDDA(numClusters,  nattributes-2)  

for (k<-0 until numClusters) {   // for all computed clusters
           // extract the cluster params, for each attribute WEKA outputs the mean, the standard deviation, and a "weight" parameter, I don't know up to now what it means
        var currentClusterParams = new RDDA(clusterModelsCenters(k))
     for (attr<-2 until nattributes-3)   // first two attributes are gene name and description
        emMeans(k, attr-2) = currentClusterParams(attr, 0)
}


// plot the computed means with the WEKA EM clusterer
figure(3); title("Computed means by the EM clusterer, tissues as rows, genes as attributes")
hold("on")
for (k<-0 until numClusters)
  plot(emMeans(k, ::),  newColor, "cluster "+k)
  
//  print the first center parameteres
var firstCluster = new RDDA(clusterModelsCenters(0))
firstCluster.print
```