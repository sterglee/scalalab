## Using the new CUDA 6.5 ##

`It is important to define the environment variable` **`CUDA_PATH_V65`** `to point to the path where CUDA was installed`

# `Introduction` #

`Using the new NVIDIA CUDA 6 is easy on` **`OpenSUSE 13.1 Linux`**`. Installing CUDA on Windows is generally an easy task, but that was not the case for Linux distributions. `
`Now, things seem changed, and on Linux you have ready the whole powerful application environment (i.e. the gcc, g++ compilers etc.)`

`It's rather difficult to install CUDA with the live CDs of Fedora and Ubuntu, since a lot of required packages are missing, and you have to install them separately. Contrary, the DVD distribution of OpenSUSE 13.1 contains all the necessary packages, thus the job is much easier. `

`To me, the OpenSUSE 13.1 is the best development operating system, faster than Windows 8 and Solaris 11, open source and with a lot of development tools!`


`Below the steps to install CUDA 6 on Linux are presented. `

**`Step 1:`** `Make a full installation of OpenSUSE 13.1, i.e. install all the software packages from the installation DVD (I used it from a USB stick.) I don't know if the default packages are sufficient.`

**`Step 2:`** ` Download cuda_6.5.14_linux_64.run from NVIDIA`

**`Step 3:`** `As a supervisor type the command`

`init 3`

`in order to open a plain terminal Linux session (i.e. without the X-server)`


> `Login to the system as root (i.e. supervisor)`


`Execute the CUDA installation script as:`

` sh cuda_6.5.14_linux64.sh`


**`Step 4: `**`Finally,edit your .bashrc  e.g. `

` gedit ~/.bashrc`

`and update the environment variables PATH, LD_LIBRARY_PATH and CUDA_PATH as for example for my configuration `

```
export SCALA_HOME=/home/sterg/Scala/

export JAVA_HOME=/home/sterg/Java/jdk1.8.0_25/
export JDK_HOME=$JAVA_HOME

export CUDA_PATH=/home/sterg/cuda65

export PATH=$JAVA_HOME/bin:$SCALA_HOME/bin:$CUDA_PATH/bin:$PATH


export LD_LIBRARY_PATH=$CUDA_PATH/lib64:$CUDA_PATH/lib:$LD_LIBRARY_PATH


```

`The CUDA_PATH variable is used by ScalaLab and not by CUDA.`

`Now, you can use the CUDA massive parallelism from ScalaLab!! `

`.. and in Linux, it is easy to experiment with the CUDA Samples, only type ` **`make`** ` and run them !! `


