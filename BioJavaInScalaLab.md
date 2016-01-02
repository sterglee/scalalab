# Introduction #

`ScalaLab offers an easier and more productive way to do Bioinformatics with BioJava. All that its is required is to install the .jars of BioJava (available at the Downloads section) as ScalaLab toolboxes. `

`We provide some examples of using BioJava routines.`

## Example 1 ##

```

import java.io.FileInputStream
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType
import org.biojava3.alignment.template.Profile
import org.biojava3.alignment.template.SequencePair
import org.biojava3.alignment.template.SubstitutionMatrix
import org.biojava3.core.sequence.DNASequence
import org.biojava3.core.sequence.compound.AmbiguityDNACompoundSet
import org.biojava3.core.sequence.compound.DNACompoundSet
import org.biojava3.core.sequence.compound.NucleotideCompound
import org.biojava3.core.sequence.io.FastaReaderHelper
import org.biojava3.core.util.ConcurrencyTools
import org.biojava3.alignment._

import scala.collection.JavaConversions._

    def getDNAFASTAFile()  =  {

        var  inStream =  new FileInputStream("dna-fasta.txt")
    
        var  fastas = FastaReaderHelper.readFastaDNASequence(inStream)

	   var sequences = new ArrayList[DNASequence]()
	   var kfastas = fastas.keySet
	   var skfastas = kfastas.toSet    // convert to Scala set
	   var sfastas = fastas.toMap  // convert to Scala map

	   for (key<-skfastas) {
	   	var seq = sfastas(key)
	   	sequences.add(seq)
	   }

  sequences 	           

    }

 var  lst = getDNAFASTAFile()

 var profile: Profile[DNASequence, NucleotideCompound] = Alignments.getMultipleSequenceAlignment(lst)



```