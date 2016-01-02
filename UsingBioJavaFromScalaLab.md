# `Introduction` #

**`BioJava`** `is a powerful system for Bioinformatics research. We compiled the sources as a bundle of .jar files suitable for installation as ScalaLab toolboxes. These toolboxes are available for download at the page: `

https://sourceforge.net/projects/scalalab/files/?source=navbar

`They can be installed easily by placing them at the ` **`DefaultToolboxes`** ` ScalaLab folder.`

`We present below some code examples using the BioJava toolbox.`

`Java codes and detailed explanations of these examples (and many more) can be found from the excellent book: `

**`BioJava: A Programming Guide`**

**`Kaladhar D.S.V.G.K.`**

**`LAMBERT Academic Publishing, 2012`**



# Examples #

```

import org.biojava3.alignment.template.GapPenalty;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.alignment._
import org.biojava3.alignment.SubstitutionMatrixHelper
import org.biojava3.alignment.SubstitutionMatrixHelper._


	var PRECISION = 0.00000001;
	
    var   query = new ProteinSequence("AERNDKK")
    var   target = new ProteinSequence("ERDNKGFPS")
    var 	gaps = new SimpleGapPenalty( 2, 1)
    var   blosum62 = SubstitutionMatrixHelper.getBlosum62()
    
   		var  alig = new SmithWaterman[ProteinSequence, AminoAcidCompound]()
        	alig.setQuery(query)
        	alig.setTarget(target)
        	alig.setGapPenalty(gaps)
        	alig.setSubstitutionMatrix(blosum62)
        	var ag = alig.getPair().toString()

   
```

## `Getting a DNA, RNA or Protein Alphabet` ##

`BioJava Alphabets are collections of Symbols. Some of the common biological alphabets (DNA, RNA, protein, etc. ) are registered with BioJava AlphabetManager and can be accessed by name. The DNA, RNA, and protein alphabets can also be accessed with convenient static methods of the DNATools, RNAtools and ProteinTools classes. `

```

import java.util._
import org.biojava.bio.seq._
import org.biojava.bio.symbol._

var dna = AlphabetManager.alphabetForName("DNA")

var rna = AlphabetManager.alphabetForName("RNA")

var prtn = AlphabetManager.alphabetForName("PROTEIN")

var prtn = AlphabetManager.alphabetForName("PROTEIN-TERM")

var dna = DNATools.getDNA

var rna = RNATools.getRNA

var prtn = ProteinTools.getAlphabet

var prtnt = ProteinTools.getTAlphabet   // one with the * symbol



```


## `Demonstration of whether two Symbols or Alphabets are equal` ##

`The same Alphabets and Symbols are canonical based on how they are constructed or from where they come from. This means that if two DNA alphabets (or Symbols from those alphabets) are instantiated at different times are equal via both the .equals() and == methods. Symbols from the PROTEIN and the PROTEINTERM alphabets are canonical as are Symbols from IntegerAlphabet and SubIntegerAlphabet. `

```
import org.biojava.bio.seq._
import org.biojava.bio.symbol._

var alph1 = DNATools.getDNA  // get the DNA alphabet

var alph2 = AlphabetManager.alphabetForName("DNA") // get the DNA alphabet

var shouldBeEquals = alph1.equals(alph2)

var shouldBeCanonical = alph1==alph2


var rnaalph = AlphabetManager.alphabetForName("RNA")  // get the RNA alphabet

var shouldNotBeEqual = alph1.equals(rnaalph)

var shouldNotBeCanonical = rnaalph == alph1

```


## `Custom Alphabet from custom Symbols` ##

`The following script demonstrates the creation of a "binary" alphabet that contains two Symbols, "zero" and "one". This custom alphabet can then be used. `

```
import java.util._
import org.biojava.bio.seq._
import org.biojava.bio.Annotation
import org.biojava.bio.symbol._


// make "zero" Symbol with no annotation
var zero = AlphabetManager.createSymbol("zero", Annotation.EMPTY_ANNOTATION)

// make "one" symbol with no annotation
var one = AlphabetManager.createSymbol("one", Annotation.EMPTY_ANNOTATION)

var Symbols = new HashSet[org.biojava.bio.symbol.AtomicSymbol]()   // collect Symbols in a set

Symbols.add(zero)  
Symbols.add(one)

var binary = new SimpleAlphabet(Symbols, "Binary")  // create the "Binary" Alphabet
var iter = binary.iterator
while (iter.hasNext) {
	 var sym = iter.next.asInstanceOf[Symbol]
	 println( sym.getName)
 }


// register newly created Alphabets

// the newly created Alphabet is registered with the AlphabetManager under the name "Binary",
// it should be canonical with the previous instance with that name

AlphabetManager.registerAlphabet(binary.getName(), binary)
var alpha = AlphabetManager.alphabetForName("Binary")

println(alpha == binary)   // check canonical status
```



## `Make an ambiguous Symbol like Y or R` ##

`The IBU defines standard codes for symbols which are ambiguous such as Y to indicate C or T and R to indicate G or C or N to indicate any nucleotide. BioJava signify these Symbols as the BasisSymbols. BasisSymbol objects can contain one or more component Symbols with valid members of the same Alphabet as the BasisSymbol and are therefore capable of being ambiguous. `

```
import java.util._
import org.biojava.bio.seq._
import org.biojava.bio.symbol._

var dna = DNATools.getDNA   // get the DNA alphabet
// make the 'Y' symbol
var symbolsThatMakeY = new HashSet[org.biojava.bio.symbol.AtomicSymbol]()

symbolsThatMakeY.add(DNATools.c)
symbolsThatMakeY.add(DNATools.t)
var y = dna.getAmbiguity(symbolsThatMakeY)

println("Formal name of `Y` is: "+y.getName())   
println("Class type of `Y` is: "+y.getClass().getName())

```



## `Building a Sequence from a String or make a Sequence Object back into a String` ##

`Sequence is represented as a String of characters, e.g. DNA as "aagttgcca", RNA as "aauugcc" and protein as "cdaafgklmnpq". It is a convenient method for viewing and representing complex biological polymers. BioJava uses SymbolLists and Sequences to represent these biological polymers as Objects. Sequences extend SymbolLists and provide extra methods to store things like the name of the sequence."

**`String to SymbolList`**
```
import org.biojava.bio.seq._
import org.biojava.bio.symbol._

var dna = DNATools.createDNA("atgcatgc")   // create a DNA SymbolList from String

var rna = RNATools.createRNA("augcaugc")  // create an RNA SymbolList from String

var aa = ProteinTools.createProtein("AGFS")  // create a Protein SymbolList from String


```

**`String to Sequence`**
```
import org.biojava.bio.seq._
import org.biojava.bio.symbol._

var dna1 =  DNATools.createDNASequence("atgcatgc", "dna_1")   // create a DNA Sequence from String

var rna2 = RNATools.createRNASequence("augcaugc", "rna_1")  // create an RNA Sequence from String

var aa = ProteinTools.createProteinSequence("AGFS", "prot_1")  // create a Protein Sequence from String

```

**`SymbolList to String`**

```
import org.biojava.bio.seq._
import org.biojava.bio.symbol._


var symL = RNATools.createRNA("augucca")
var s = symL.seqString

```


## `Subsection of a Sequence` ##
`Programmers might only be interested in examining subsections of a sequence, e.g. the first 15 bases, or they might want to get a region between two points. BioJava uses a biological coordinate system for identifying bases with the first base numbered as 1 and the last base index is equal to the length of the sequence.`

```
import org.biojava.bio.seq._
import org.biojava.bio.symbol._


var symL = RNATools.createRNA("augucca")

var sym = symL.symbolAt(1)  // get the first symbol

var symL2 = symL.subList(1, 3)  // get the first three bases

var symL3 = symL.subList(symL.length()-3, symL.length()) // get the last three bases


```


## Transcribing a DNA Sequence to an RNA Sequence ##

`BioJava contains DNA and RNA Sequences and SymbolLists that are made using different Alphabets. Conversion from DNA to RNA can be done using the static method transcribe() in RNATools. `

```
import org.biojava.bio.seq._
import org.biojava.bio.symbol._


var symL = DNATools.createDNA("atgcagacca")

var symLRNA = RNATools.transcribe(symL)

println(symLRNA.seqString())


```


## `Reverse Complement of a Sequence or SymbolList` ##

`To reverse complement a DNA SymbolList or Sequence, use the DNATools.reverseComplement(sl: SymbolList) method. An equivalent method is found in RNATools for performing the same operation on RNA based Sequences and SymbolLists. `

```
import org.biojava.bio.seq._
import org.biojava.bio.symbol._


var symL = DNATools.createDNA("atgcagacca")

var symLRevCompl = DNATools.reverseComplement(symL)

println(symLRevCompl.seqString())


```


## Change a Sequence name ##

`BioJava Sequence objects are immutable and contain safety features to prevent changes that corrupt the integrity of the data. A consequence of this is that there is no setName() method in Sequence. One way to change a "view" of a Sequence is to make a ViewSequence using the original Sequence as argument in the constructor. The ViewSequence wrapper intercepts with some of the method calls to the underlying Sequence that gives the possibility of changing the name.`


## `Motif into a regular expression` ##

`One of the interested things you can do with BioJava's MotifTools is to make a String into a regular expression Pattern. You can then use this Pattern to search a SymbolList object for the existence of that Pattern. The generated Pattern can even be from an ambiguous sequence such as "acgytnwacrs". To accomplish this task, BioJava contains a wrapper to Java's built-in regular expression functionality.`

```

import org.biojava.bio.seq._
import org.biojava.bio.symbol._
import org.biojava.utils.regex._


var IUPAC = DNATools.getDNA
var workingSequence = DNATools.createDNA("aattaggattacgcgcccgtaaattaggattacgcccgtagta")

var FACTORY = PatternFactory.makeFactory(IUPAC)  // pattern using pattern factory

var pattern = FACTORY.compile("wtagn")

println("Searching for: "+ pattern.patternAsString)

var occurences = pattern.matcher(workingSequence)

while (occurences.find()) {
	println("Match: "+"\t"+workingSequence+"\n"+occurences.start()+"\t"+occurences.group().seqString())
}


```


# `Translation and Proteomics` #

## `Translate a SymbolList or Sequence` ##

```

import org.biojava.bio.seq._
import org.biojava.bio.symbol._


var symL = DNATools.createDNA("atatatatatat")  // create a DNA SymbolList

var symLRNA = DNATools.toRNA(symL) // transcribe to RNA

var symLProtein = RNATools.translate(symLRNA)  // translate to protein

println(symLProtein.seqString)   

```