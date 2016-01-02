# Introduction #

` OS Sim (Operating System Concepts Simulator) is a simulator in Java of the basic Operating Systems structures. It can be used to practice with the basic Operating Systems algorithms and structures and to evaluate the performance of algorithmic and design alternatives. It supports also graphical presentations of the simulations. It can be useful for the computer science students learning process and also for an Operating Systems researcher.`

` The project's site is: `


https://sourceforge.net/projects/oscsimulator/


`OS Sim  can be installed easily as a ScalaLab toolbox. We present examples, illustrating that ScalaLab can be a useful tool, for studing Operating Systems concepts with OS Sim.`

## `Installation of OSSim toolbox` ##

`The installation process is very simple, simply download the ` _`OSSim.jar`_ ` and install it as any ScalaLab toolbox. `


## Examples ##

`Let now work with OSSim in ScalaLab. We recommend preferring RSyntaxArea editor, thus you can press Ctrl-R or switch from the Edit menu item. `

`Also useful is the code completion feature, which should be switched to Scala completion from the ` _`Completion`_ ` menu of the RSyntaxArea based ScalaLab editor. `


### Creating a process ###

`We can create a process with the following code snippet. `


```


import edu.upc.fib.ossim.process.model.Process
import java.util.Vector

 // prepare the parameters for constructing a process
var pid = 1      //	 process identifier
var pname = "Pr1"  //  process name	
var prio = 4  // process priority
var timesubmission = 10  // process initial time. (entering ready queue)
var periodic = false  // true means endless process that repeats burst cycle indefinitely, otherwise	only once
var burstsCycle = new Vector[Integer](3)  //  process bursts vector. CPU or I/O bursts, values 0 or 1  
burstsCycle.insertElementAt(0, 0)  // CPU burst
burstsCycle.insertElementAt(1, 1) // IO burst
burstsCycle.insertElementAt(2, 0)   // CPU burst
var color = Color.RED

// create the process
var p1 = new Process(pid, pname, prio, timesubmission, periodic, burstsCycle, color)

```

`Now the ` _`p1`_ ` variable corresponds to a newly created process. The code completion feature of ScalaLab can help to discover easily its fields. We can type: ` _`p1.`_ `and then ` ` CTRL-SPACE` ` in order ScalaLab to present the available fields/methods for the process. Similarly, if you type ` _`p1.get`_`CTRL-SPACE`_` a list of all fields/methods starting with `_`get`_` is displayed. `_


## `Evaluating a scheduling policy` ##

`The following code creates some processes and evaluates a scheduling policy`

```

import edu.upc.fib.ossim.process.model.Process
import java.util.Vector

import edu.upc.fib.ossim.process.model.ContextProcess
import edu.upc.fib.ossim.process.model.ProcessStrategyFCFS
import edu.upc.fib.ossim.process.model.ProcessStrategyPrio
import edu.upc.fib.ossim.process.model.ProcessStrategyRR
import edu.upc.fib.ossim.process.model.ProcessStrategySJF

 // prepare the parameters for constructing a process
var pname = "Pr"  //  process name     
var prio = 4  // process priority
var timesubmission = 0  // process initial time. (entering ready queue)
var periodic = false  // true means endless process that repeats burst cycle indefinitely, otherwise    only once
var color = Color.RED
var preemptive = true
 
 //var context = new ContextProcess(new ProcessStrategyPrio(preemptive))
 var context = new ContextProcess(new ProcessStrategySJF(preemptive))
 
 var maxDuration=20
 var numProcess = 10
 
 for (pr<-0 until numProcess-1) {
   var pid = pr      //      process identifier
   var numBursts = pr+1 // (Math.random()*maxDuration).toInt+1
   var burstsCycle = new Vector[Integer](numBursts)  //  process bursts vector. CPU or I/O bursts, values 0 or 1  
   for (b<- 0 until numBursts)   // add the bursrs
      burstsCycle.add(0)
   
     
   var cp = new Process(pid, pname+pr, prio, timesubmission, periodic, burstsCycle, color)
   context.addProcess(cp, 0)
   }
   
     /* Forwards simulation time 1 unit. Common tasks such as initial (time 0) state back up, increment all processes 
     * waiting time in the ready queue, look at arriving queue to move processes to the ready queue and 
     * move finished processes to the finished queue are implemented here, algorithm concrete tasks such as 
     * execute running process 1 time unit are implemented into concrete strategies.  Returns true when simulation ends 
     * (no more processes in any queue)
     * 
     * @param time			current simulation time
     * @param multiprogram  scheduling is multiprogramming 
     * @param preemptive	scheduling is preemptive
     * @param quantum		scheduling quantum size
     * 
     * @return simulation ends 
     * 
     * @see ProcessStrategy
     */
     var multiprogram = true; preemptive = false; var quantum = 1
     context.setAlgorithm(new ProcessStrategySJF(preemptive))
 var time = 0
 while ( context.forwardTime(time,  multiprogram,  preemptive,  quantum) != true) {
      time += 1
     }
     
     context.getProcessCount
     context.avgResponseTime
     context.avgTurnaroundTime
     context.avgWaitingTime


```

`This example can also be written in Java like style, using the GroovyLab mode of ScalaLab: `

```

import edu.upc.fib.ossim.process.model.Process
import java.util.Vector

import edu.upc.fib.ossim.process.model.ContextProcess
import edu.upc.fib.ossim.process.model.ProcessStrategyFCFS
import edu.upc.fib.ossim.process.model.ProcessStrategyPrio
import edu.upc.fib.ossim.process.model.ProcessStrategyRR
import edu.upc.fib.ossim.process.model.ProcessStrategySJF

 // prepare the parameters for constructing a process
pname = "Pr"  //  process name     
prio = 4  // process priority
timesubmission = 0  // process initial time. (entering ready queue)
periodic = false  // true means endless process that repeats burst cycle indefinitely, otherwise    only once
color = Color.RED
preemptive = true
 
 //var context = new ContextProcess(new ProcessStrategyPrio(preemptive))
 context = new ContextProcess(new ProcessStrategySJF(preemptive))
 
 maxDuration=20
 numProcess = 10
 
 for (pr in 0..numProcess-1) {
    pid = pr      //      process identifier
    numBursts = pr+1 // (Math.random()*maxDuration).toInt+1
    burstsCycle = new Vector<Integer>(numBursts)  //  process bursts vector. CPU or I/O bursts, values 0 or 1  
   for (b in 0..numBursts)   // add the bursrs
      burstsCycle.add(0)
   
     
   cp = new Process(pid, pname+pr, prio, timesubmission, periodic, burstsCycle, color)
   context.addProcess(cp, 0)
   }
   
     /* Forwards simulation time 1 unit. Common tasks such as initial (time 0) state back up, increment all processes 
     * waiting time in the ready queue, look at arriving queue to move processes to the ready queue and 
     * move finished processes to the finished queue are implemented here, algorithm concrete tasks such as 
     * execute running process 1 time unit are implemented into concrete strategies.  Returns true when simulation ends 
     * (no more processes in any queue)
     * 
     * @param time			current simulation time
     * @param multiprogram  scheduling is multiprogramming 
     * @param preemptive	scheduling is preemptive
     * @param quantum		scheduling quantum size
     * 
     * @return simulation ends 
     * 
     * @see ProcessStrategy
     */
      multiprogram = true; preemptive = false; quantum = 1
     context.setAlgorithm(new ProcessStrategySJF(preemptive))
 time = 0
 while ( context.forwardTime(time,  multiprogram,  preemptive,  quantum) != true) {
      time += 1
     }
     
     context.getProcessCount()
     context.avgResponseTime()
     context.avgTurnaroundTime()
     context.avgWaitingTime()

```

`The GroovyLab style can be executed within ScalaLab, using the ` **`F8`** ` key. `