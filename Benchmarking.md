# Introduction #

`In this page we present benchmark examples.`

## QR decomposition with various libraries ##

```

   var N = 1500

    // construct an MTJ matrix
  var Amtj = scalaSci.MTJ.StaticMathsMTJ.rand0(N, N)
   // perform a QR decomposition
  tic
  var (qmtj, rmtj) = Amtj.QR
  var tmMTJ = toc
  var orthQmtj = qmtj*(qmtj~)  // test that the Q matrix is orthogonal
  var shouldBeZeroMTJ = qmtj*rmtj-Amtj   // test the validity of the decomposition, that matrix should be zero
  
      
     // construct an EJML matrix
   var Aejml = scalaSci.EJML.StaticMathsEJML.rand0(N, N)
     // perform a QR decomposition
   tic
   var (qejml, rejml)  = Aejml.QR
    
   var tmEJML = toc
   var orthQejml = qejml*(qejml~)   // test that the Q matrix is orthogonal
   var shouldBeZeroejml = qejml*rejml-Aejml   // test the validity of the decomposition, that matrix should be zero

    
    // construct a JAMA matrix
   var Ajama = scalaSci.StaticMaths.rand0(N, N)
    // perform a QR decomposition
   tic
   var (qjama, rjama) = Ajama.qr   
   var tmJama = toc
  var orthJama = qjama*(qjama~)  // test that the Q matrix is orthogonal
  var shouldBeZeroJama = qjama*rjama-Ajama   // test the validity of the decomposition, that matrix should be zero
  
```
## Fib.scala ##

```

object Fib {
    
    def fibIf(n: Int): Int =
  if (n >= 2)
    fibIf(n-1)+fibIf(n-2) 
  else
    1
    }
    
class Fib {
    
   def fibIf(n: Int): Int =
    if (n>=2)  fibIf(n-1)+fibIf(n-2)
      else
     1
     }
     
var start = System.currentTimeMillis()
Fib.fibIf(40)
var end = System.currentTimeMillis()
var timeStatic = end-start
println("time static = "+timeStatic)

start = System.currentTimeMillis()
new Fib().fibIf(40)
end = System.currentTimeMillis()
var timeInstance = end-start
println("time instance = "+timeInstance)

  /*
time static = 932
time instance = 927   
   */  
```

## `BinaryTrees.scala ` ##

```


class TreeNode(item: Int)
{
private var  left, right: TreeNode = _


def this( left: TreeNode, right: TreeNode, item: Int)=
{
this(item)
this.left = left;
this.right = right;

}

def itemCheck():Int = {
// if necessary deallocate here
if (left==null)
                item;
else {
                item + left.itemCheck() - right.itemCheck();
            }
}
}


object TreeNode {
def bottomUpTree(item: Int, depth: Int): TreeNode = {
  if (depth>0){
 new TreeNode(bottomUpTree(2*item-1, depth-1), bottomUpTree(2*item, depth-1), item)
}
else {
 new TreeNode(item);
}
}
}

object BinaryTrees {
  import TreeNode._
  
    var minDepth = 4
    
    def main(args: Array[String]) = {
        var millis = System.currentTimeMillis()
        
        var n = 20
        if (args.length > 0) n = Integer.parseInt(args(0))
        
           var maxDepth = if (minDepth + 2 > n)  minDepth+2
                                else n
                                
           var stretchDepth = maxDepth+1
           

           var check = (TreeNode.bottomUpTree(0, stretchDepth)).itemCheck()
        println("stretch tree of depth "+stretchDepth+"\t check: " + check);


       var  longLivedTree = TreeNode.bottomUpTree(0,maxDepth)

  var depth = minDepth
  while (depth<=maxDepth) {
    var  iterations = 1 << (maxDepth - depth + minDepth);
    check = 0;

for (i<-1 to iterations){
  check += (TreeNode.bottomUpTree(i,depth)).itemCheck();
  check += (TreeNode.bottomUpTree(-i,depth)).itemCheck();
}
  println((iterations*2) + "\t trees of depth " + depth + "\t check: " + check);

  depth += 2
}

  println("long lived tree of depth " + maxDepth + "\t check: "+ longLivedTree.itemCheck());

   var total = System.currentTimeMillis() - millis;
   println("[Binary Trees-" + System.getProperty("project.name")+ " Benchmark Result: " + total + "]");
 }
}

var args = Array("20")
BinaryTrees.main(args)



/*
stretch tree of depth 21	 check: -1
2097152	 trees of depth 4	 check: -2097152
524288	 trees of depth 6	 check: -524288
131072	 trees of depth 8	 check: -131072
32768	 trees of depth 10	 check: -32768
8192	 trees of depth 12	 check: -8192
2048	 trees of depth 14	 check: -2048
512	 trees of depth 16	 check: -512
128	 trees of depth 18	 check: -128
32	 trees of depth 20	 check: -32
long lived tree of depth 20	 check: -1
[Binary Trees-null Benchmark Result: 7822]
*/
```