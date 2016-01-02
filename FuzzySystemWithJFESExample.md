# Introduction #

`The JFES toolbox provides a framework for building both boolean rule-based and fuzzy rule-based expert systems and control systems. We describe some basic aspects of JFES by providing the implementation of a simple fuzzy control system that controls the movement of a simple car. The fuzzy controller of that car takes two variables as input: ` **`speed`** `and ` **`distance`** `(from some obstacle) and computes a decision to the variable ` **`control`** `that is either an acceleration or deceleration signal.`

`In particular the rules of the example fuzzy control system are the following:`

```

AccelerateRule: IF Distance is Large
     AND Speed is Low
     THEN Control is Accelerate
DecelerateRule: IF Distance is Small
     AND Speed is High
     THEN Control is Decelerate
```

# Implementation of the fuzzy control system #

`The former fuzzy control system can be implemented from ScalaLab using directly the API of JFES. The prerequisite is of course to install the JFES.jar file as a ScalaLab toolbox. `

`The ScalaLab code that constructs the fuzzy control system and demonstrates the fuzzy inference is as follows: `

```

import JFES.Fuzzy._

// construct a fuzzy rule base named "Driver"
var  rb = new FuzzyRuleBase("Driver")

// now define some continuous fuzzy rule variables
/** Creates a new variable with the specified initial values.
    base        the FuzzyRuleBase for this variable
    id          the integer identifier for this variable
    name        the String that contains the name of this variable
    discourseLo the double that contains the low end of the universe of discourse
    discourseHi the double that contians the high end of the universe of discourse 
   isSymbolic: flag that controls whether to handle the variable as Symbolic */

// def ContinuousFuzzyRuleVariable(base: FuzzyRuleBase, name: String, discourseLo: double, discourseHi: double, isSymbolic: boolean) 
var  distance = new ContinuousFuzzyRuleVariable(rb, "Distance", 0, 120, false)
var  speed = new ContinuousFuzzyRuleVariable(rb, "Speed", 0, 150, false)
var  control = new ContinuousFuzzyRuleVariable(rb, "Control", -2, 2, false)
 
var  alphaCut = 0.1
//   def ShoulderFuzzySet( parentVar: ContinuousFuzzyRuleVariable, name: String alphaCut: Double, ptBeg: Double, ptEnd: Double, setDirection: Int ) 
var smallDistance = new ShoulderFuzzySet(distance, "Small", alphaCut, 0, 120,  FuzzyDefs.LEFT)
var smallMFDistanceDomain =  smallDistance.getDomain
var smallMFDistanceValues = smallDistance.truthVector


var largeDistance = new ShoulderFuzzySet(distance, "Large", alphaCut, 0, 120, FuzzyDefs.RIGHT)
var largeMFDistanceDomain = largeDistance.getDomain
var largeMFDistanceValues = largeDistance.truthVector

figure(1); subplot(311); plot(smallMFDistanceDomain, smallMFDistanceValues, Color.RED, "Small"); xlabel("Distance ")
plot(largeMFDistanceDomain, largeMFDistanceValues, Color.GREEN, "Large");


var lowSpeed = new ShoulderFuzzySet(speed, "Low", alphaCut, 0, 150,  FuzzyDefs.LEFT)
var lowMFSpeedDomain =  lowSpeed.getDomain
var lowMFSpeedValues = lowSpeed.truthVector

var highSpeed = new ShoulderFuzzySet(speed, "High", alphaCut, 0, 150, FuzzyDefs.RIGHT)
var highMFSpeedDomain = highSpeed.getDomain
var highMFSpeedValues = highSpeed.truthVector

subplot(312); plot(lowMFSpeedDomain, lowMFSpeedValues, Color.GREEN, "Low"); xlabel("Speed ")
plot(highMFSpeedDomain, highMFSpeedValues, Color.RED, "High")

var decelerateControl = new GaussianFuzzySet(control, "Decelerate", alphaCut, -1, 0.5)
var decelerateControlDomain =  decelerateControl.getDomain
var decelerateControlValues = decelerateControl.truthVector

var accelerateControl = new GaussianFuzzySet(control, "Accelerate", alphaCut, 1, 0.5)
var accelerateControlDomain =  accelerateControl.getDomain
var accelerateControlValues = accelerateControl.truthVector

subplot(313); plot(decelerateControlDomain, decelerateControlValues, Color.GREEN, "Decelerate"); xlabel("Control ")
plot(accelerateControlDomain, accelerateControlValues, Color.RED, "Accelerate")


var speedIsHigh = new FuzzyClause(speed, FuzzyOperator.CmpIs, highSpeed)
var speedIsLow = new FuzzyClause(speed, FuzzyOperator.CmpIs, lowSpeed)

var distanceIsLarge = new FuzzyClause(distance, FuzzyOperator.CmpIs, largeDistance)
var distanceIsSmall = new FuzzyClause(distance, FuzzyOperator.CmpIs, smallDistance)

var controlIsAccelerate = new FuzzyClause(control, FuzzyOperator.AsgnIs, accelerateControl)
var controlIsDecelerate = new FuzzyClause(control, FuzzyOperator.AsgnIs, decelerateControl)


var accClauses = new Array[FuzzyClause](2)
accClauses(0) = distanceIsLarge
accClauses(1) = speedIsLow
var accelerateRule = new FuzzyRule( rb, "AccelerateRule", accClauses, controlIsAccelerate)

var decClauses = new Array[FuzzyClause](2)
decClauses(0) = distanceIsSmall
decClauses(1) = speedIsHigh
var decelerateRule = new FuzzyRule( rb, "DecelerateRule", decClauses, controlIsDecelerate)


// display the constructed fuzzy rules of the rule base
var gt = new JTextArea; gt.setFont(new Font("Arial", Font.PLAIN, 20))
rb.displayRules(gt)
var jf = new JFrame("rr"); jf.add(gt)
jf.setSize(600, 300)
jf.setVisible(true)


// demonstrate a fuzzy inference
rb.reset()

distance.setNumericValue(50.0)
speed.setNumericValue(80.0)

rb.forwardChain()
var sigControl = control.getNumericValue()
 

```