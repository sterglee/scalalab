# Simple File/Directory related commands #

`ScalaLab has some simple commands for handling files/directories that can be useful. We describe some of them by means of examples.`

`To display and return the ` _`current working directory`_ `type: `

```
var wd = pwd
```

`To display the list of files/folders of the current working directory and to return the list of files and the list of folders, you can type: `

```
var (fileList, folderList) = dir
```

`To use a Scala regular expression in order to filter the returned names use:`

```
var (fileList, folderList) = xdir("Run\\w*")
```

`The above command returns all the file names starting with "Run" `

`To change directory use the cd command, e.g. `

```

cd("..")   // changes the current working directory to the parent directory

cd("ScalaLab210M6Aug26") // changes to the ScalaLab210M6Aug26 folders if it exists

cd("/home/sterg/NBProjects")  // changes to the absolute directory /home/sterg/NBProjects

```

`To create a directory use the ` _`md`_ `command, e.g. `

```
md("temp2")  // make a new folder named temp2
cd("temp2")  // change to temp2
dir // display the contents, should be empty
```

