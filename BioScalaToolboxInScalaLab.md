# `Introduction` #

`Scala is a great language to perform Computational Biology and Bioinformatics tasks. The BioScala toolbox of ScalaLab is one way of starting exploring the vast protential of the Scala language in Bioinformatics tasks. We present here some material of tutorial type.`


# `BioScala in ScalaLab` #

` BioScala (https://github.com/bioscala/bioscala) is available as a ScalaLab toolbox  (file: bioScala.jar). Therefore, we can download bioScala.jar, and then install it, as any other ScalaLab toolbox. In addition, the BioJava toolbox, bioJava.jar, also available from ScalaLab's downloads, should be installed. The later is necessary since much functionality of BioScala is build upon BioJava (BioJava can also be used from ScalaLab, and a Scala based DSL for BioJava is in our future plans). After, performing these installation steps we can execute the following examples, that demonstrate some aspects of BioScala. The material in this page has been adapted from the documentation of BioScala.`

`The first step is to perform some imports: `
```
import bio._
```

`Then we can create a Sequence object`
```
val dna = new DNA.Sequence("agctaacg")
```

`Sequence is strongly typed. Creating a DNA sequence from an RNA String will give an error`
```
val dna2 = new DNA.Sequence("agcuaacg")
```

`You can transcribe DNA`
```
val rna = dna.transcribe
```
`rna: bio.RNA.Sequence = agcuaacg`

`Notice that the above is an RNA object now. Nucleotides are proper lists.`
```
val l = RNA.A :: rna.toList
```
`l: List[bio.RNA.Nucleotide] = List(a, a, g, c, u, a, a, c, g)`

`To translate nucleotides to amino acids:`
```
bio.DNA.SequenceTranslation.translate(l)
```
` res0: List[bio.Protein.AASymbol] = List(K, L, T)`

`Create a Sequence with ID`
```
val seq = new DNA.Sequence("ID456", "agctaacg")
seq.id
```

`Create a Sequence with ID and decsription`
```
val seq = new DNA.Sequence("ID456", "My gene", "agctaacg")
seq.description
```

`A Sequence, like in the real world, can have multiple ID's and descriptions.`
```
import bio.attribute._
val seq2 = seq.attrAdd(Id("Pubmed:456"))
seq2.idList

val seq3 = seq2.attrAdd(Description("Another description"))
seq3.descriptionList

```

`Note that Sequence is immutable. Every time you add an attribute a new copy gets created.`


## `CodonSequence` ##

`The CodonSequence makes use of the Attribute list of the standard AminoAcidSequence object. When creating a CodonSequence, e.g.`

```
 val seq = new Protein.CodonSequence("ID356","Describe gene 356","agctgaatc")
 seq.toString
 seq.toDNA
```

`it stored the DNA sequence as a CodonAttribute to the AminoAcid. When you want to fetch the codon sequence of Attribute with the third Amino Acid, you can query for the codon information `
```
seq(2).getCodon
```
`List(a, t , c)`

`and even directly from the CodonSequence`

```
 seq.getCodon(2)
```

`Now we want to delete the middle codon:`
```
  var seq2 = seq.delete(1,1)
  seq2.toDNA
```

`We have another immutable CodonSequence object seq2, which contains a new sequence with matching amino acids and codons:`

```
  seq.toString
  seq.toDNA
```