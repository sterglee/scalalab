# Introduction #

**`Data for these examples can be downloaded with the "SampleBioinformaticsData.zip" file at the SourceForge ScalaLab downloads`**

`BioJava is a powerful framework for Bioinformatics. BioJava3 is improved and is not directly compatible with earlier versions. To experiment with the latest version you can download the ` **`biojava3Alljars.zip`** ` file. Then you should unzip the BioJava3  libraries that it contains (it has 10 .jar libraries) within the ` **`defaultToolboxes`** ` ScalaLab folder. Then we can try some examples. These examples are adapted to ScalaLab code from the original Java based BioJava tutorial.`

## Creation of Protein and DNA sequences ##

`BioJava3 makes it easy to create sequences and what could be easier than using a String. `

```
 
 import org.biojava3.core.sequence._
 
 var proteinSequence = new ProteinSequence("ARNDCEQGHILKMFPSTWYVBZJX");
 var dnaSequence = new DNASequence("ATCG");

 println("Protein Sequence = "+proteinSequence.toString +", DNA sequence = "+dnaSequence.toString)

```

`The storage of the sequence data is defined by the ` _`Sequence`_ `interface which allows for some interesting and we hope useful abstraction. The simplest Sequence interface to represent a sequence as a String is the ` _`ArrayListSequenceReader`_ `and is the default data store when creating a sequence from a string. For large genomic data you can create a`  _`ChromosomeSequence`_ `from a ` _`TwoBitSequenceReader`_ ` or `_`FourBitSequenceReader`_ `and reduce the in memory storage requirements. By using the Sequence Interface we can easily extend the concept of local sequence storage in a fasta file to loading the sequence from`  _`Uniprot`_ `or NCBI based on an accession ID. The following is a simple example of creating a ProteinSequence using a Uniprot ID where the ` _`UniprotProxySequenceReader`_ `implements the Sequence interface and knows how to take the Uniprot ID and retrieve the sequence data from Uniprot. The ` _`UniprotProxySequenceReader`_ `can implement other feature interfaces and using the XML data that describes the Protein Sequence we can give a list of known mutations or mutagenenis studies with references to papers. This also allows us to link the Uniprot ID to the NCBI ID and retrieve the gene sequence data from NCBI via the` _`NCBIProxySequenceReader`_` . We are still in the early stages of extending these sequence relationships and expect some api changes. The abstraction of the sequence storage to an interface allows for a great deal of flexibility but has also added some challenges in how to handle situations when something goes wrong and you need to throw an exception. By introducing the ability to load sequences from remote URLs when the internet is not working or you have implemented a lazy instantiation approach to loading sequence data we have made it difficult to handle error conditions without making every method throw an exception. This a design work in progress as we get feedback from developers and expect some level of api changes as we improve the overall design.`

```
 
 import org.biojava3.core.sequence._
 import org.biojava3.core.sequence.compound._
 import org.biojava3.core.sequence.loader._
 
 
 var  uniprotSequence = new UniprotProxySequenceReader[AminoAcidCompound]("YA745_GIBZE", AminoAcidCompoundSet.getAminoAcidCompoundSet())
 var  proteinSequence = new ProteinSequence(uniprotSequence)
 
```

`The use of the` _`SequenceCreator`_ `interface also allows us to address large genomic data sets where the sequence data is loaded from a fasta file but done in a way where the sequence is loaded in a lazy fashion when the appropriate method for sequence data or sub-sequence data is needed. The` _`FileProxyProteinSequenceCreator`_ `implements the` _`Sequence`_ `interface but is very specific to learning the location of the sequence data in the file. `

```

 import org.biojava3.core.sequence._
 import org.biojava3.core.sequence.compound.AminoAcidCompoundSet
 import org.biojava3.core.sequence.loader._
 
 import org.biojava3.core.sequence.compound._

 import org.biojava3.core.sequence.io.FastaReader
 import org.biojava3.core.sequence.io.GenericFastaHeaderParser
 import org.biojava3.core.sequence.io.FileProxyProteinSequenceCreator

 import org.biojava3.core.sequence.io.ProteinSequenceCreator 
 
 import org.biojava3.core.sequence.io.FastaSequenceParser
 import java.io.File
 
 var inputFile = "prSmall.seq"
 var file = new File(inputFile)
 
 var aminoacidCompoundSet = new AminoAcidCompoundSet
 
 var fastaHeaderParser = new GenericFastaHeaderParser [ProteinSequence,AminoAcidCompound]()
 
 var sequenceParser = new FastaSequenceParser
 
 var proteinSeqCreator = new FileProxyProteinSequenceCreator(file, aminoacidCompoundSet, sequenceParser)
 
 var  fastaProxyReader = new FastaReader[ProteinSequence,AminoAcidCompound](file, fastaHeaderParser, proteinSeqCreator)
 
 
 var proteinProxySequences = fastaProxyReader.process()
                    
 var ks = proteinProxySequences.keySet

 ks.forEach { key:String => 
    var proteinSequence = proteinProxySequences.get(key)
    println(key) 
    println(proteinSequence.toString)
    }
    

```



`In the above example a` _`FastaReader`_ `class is created where we abstract out the code that is used to parse the Fasta Header and use` _`FileProxyProteinSequenceCreator`_ `to learn the beginning and ending offset location of each protein sequence. When the fasta file is parsed instead of loading the sequence data for each sequence into a` _`ProteinSequence`_ `using an` _`ArrayListSequenceReader`_   ` a ` _`SequenceFileProxyLoader`_ `is used instead. A ` _`SequenceFileProxyLoader`_ `is created for each sequence and stores the beginning and ending index of each sequence in the fasta file. When sequence data is needed for a` _`ProteinSequence`_ `then` _`SequenceFileProxyLoader`_ `will use Random I/O and seek to the offset position and return the sequence data. The current implementation of` _`SequenceFileProxyLoader`_ `will load the protein sequence data when needed and retain in memory which works great if you are only interested in a subset of sequences. If the application using the API is going to iterate through all sequences in a large fasta file then in the end all sequence data would be loaded into memory. The ` _`SequenceFileProxyLoader`_ `could be easily extended to maintain a max number of sequences loaded or memory used and free up sequence data that is loaded into memory. This way you can implement the appropriate cacheing algorithm based on the usage of the sequence data.`


### Helper Classes make it easy ###

`In an effort to provide a flexible and modular api the abstraction can often make it difficult for someone getting started with the api to know what to use. We are implementing a set of classes that have the word Helper in them to hide the abstraction and at the same time provide examples on how to use the underlying API. Typically the helper methods will be static methods and generally should be a small block of glue code. The following code shows the use of` _`FastaReaderHelper`_ ` and ` _`FastaWriterHelper`_

```
 
 
 import org.biojava3.core.sequence.io.FastaReaderHelper
 import org.biojava3.core.sequence.io.FastaWriterHelper
 
 import java.io.File
 
 var fileName = "dnaSmall.seq"
 var file = new File(fileName)
 
 var dnaSequences = FastaReaderHelper.readFastaDNASequence(file)
 
 FastaWriterHelper.writeNucleotideSequence(new File("outDNA.fna"),dnaSequences.values())

```


### Working with Sequence Objects ###

`When working with Sequence objects it is important to keep a number of points in mind`
  1. You must never rely on the backing storage of a Sequence
  1. Never perform operations on the String form of a Sequence since this will incur a performance penalty
  1. Sequences are iterable and can be stepped through using Java5's foreach loop construct
  1. All Sequences have a generic capture of the type of compound they contain. Learn to use this capture to increase or decrease the types of Sequence you want to process
  1. If you need to change the contents of a Sequence investigate the views used by Translation on how to avoid costly and unnecessary construction costs
  1. Review SequenceMixin on how to write for Sequences in a very generic manner



### Indexing Sequences By Length ###

`Sometimes it is useful to index a set of sequences by their length. Avoid using any kind of String method to do this since String operations are costly in BioJava (due to the String conversion that must be applied). Here is an example on how to do it for any Sequence object. `

```
List<Sequence<AminoAcidCompound>> translations = populateFromSomewhere();
Collections.sort(translations, new Comparator<Sequence<? extends Compound>>() {
 public int compare(Sequence<? extends Compound> o1, Sequence<? extends Compound> o2) {
   Integer o1Length = o1.getLength();
   Integer o2Length = o2.getLength();
   return o1Length.compareTo(o2Length);
 }
});
```

`Note our usage of the generic type to capture Sequence objects of any type since the assessment of length is something which can be applied to any Sequence not just AminoAcidCompound sequences.`


## DNA Translation ##

`DNA translation follows the normal biological flow where a portion of DNA (assumed to be CDS) is translated to mRNA. This is translated to a protein sequence using codons. All parts of the translation process are configurable including:`
  * `CompoundSets used in the Sequence objects`
  * `The SequenceCreator used`
  * `The Frame and direction of translation`
  * `Trimming stop codons`
  * `IUPAC codon tables `


### Quick and Dirty ###

`The following translates the given DNASequence to a peptide using the non-ambiguity CompoundSets with Codon table 1 in Frame 1 in the forward orientation.`

```
import org.biojava3.core.sequence.DNASequence
 
var protein = new DNASequence("ATG").getRNASequence().getProteinSequence()
```


### Translating in a Different Frame ###

`A common feature of transcription is the ability to specify the base at which we start translating from DNA to RNA which in turn has an effect on how we convert the resulting RNA into a protein. This can be the difference between a working translation and one full of gibberish. The Frame enum provides all 6 available frames which can be given to the DNA object when we request the RNA. Multiple frames of translations are possible but see later on. `

```
 
 import org.biojava3.core.sequence.DNASequence
 import org.biojava3.core.sequence.transcription.Frame
  
var  dna = new DNASequence("AATG")
var  rna = dna.getRNASequence(Frame.TWO)
var  protein = rna.getProteinSequence()

```