# Introduction #

`This tutorial presets examples on Network Programming adapted from the excellent book: ` **`"Java Network Programming"`** _`, Elliotte Rusty Harold, 4th edition, O'Reilly`_


# Getting the Internet Address #

`The ` **`InetAddress`** ` class has static factory methods that connect to a DNS server to resolve a hostname. The most common is ` **`InetAddress.getByName()`**`. For example, this is how we look up ` _`www.oreilly.com:`_

```


import  java.net._


var  address = InetAddress.getByName("www.oreilly.com")
var hostName = address.getHostName   // get host name from the address
var ipAddress = address.getHostAddress  // get IP address


```

`The ` _`getByName()`_ `method does not merely set a private` _`String`_ `field in the ` _`InetAddress`_ `class. It actually makes a connection to the local DNS server to look up the name and the numeric address. If the DNS server can't find the address, this method throws an` _`UnknownHostException`_`, a subclass of ` _`IOException.`_

`We can also do a reverse lookup by IP address. For example, if we want the hostname for the address 208.2239.100, we pass the dotted quad address to ` _`InetAddress.getByName(): `_

```


import  java.net._

var address = InetAddress.getByName("208.201.239.100")

var hostName = address.getHostName

```

`If the address we look up does not have a hostname, ` _`getHostName()`_ ` simply returns the dotted address we supplied. `


`The ` _`www.oreilly.com`_ ` actually has two addresses. Which one ` _`getHostName()`_`returns is indeterminate. If, for some reason, we need a the addresses of a host, we call ` _`getAllByName()`_ ` instead, which returns an array: `

```


import  java.net._

var addresses = InetAddress.getAllByName("www.oreilly.com")

addresses foreach println

```



`Finally, the ` _`getLocalHost()`_`method returns an ` _`InetAddress`_ `object for the host on which your code is running: `

```


import  java.net._


var me = InetAddress.getLocalHost

```

`This method tries to connect to DNS to get a real hostname and IP address, but if that fails it may return the ` _`loopback`_ `address instead.`

`If we know a numeric address, we can create an ` _`InetAddress`_ `object from that address without talking to DNS using ` _`InetAddress.getByAddress().`_ `This method can create addresses for hosts that do not exist or cannot be resolved: `

```


import  java.net._


var  address = Array(107.toByte, 23.toByte, 216.toByte, 196.toByte)

var  lessWrong = InetAddress.getByAddress(address)
 
var  lessWrongWithname = InetAddress.getByAddress("lesswrong.com", address)

```

`Unlike the other factory methods , these two methods make no guarantees that such a host exists or that the hostname is correctly mapped to the IP address. They throw an ` _`UnknownHostException`_ `only if a byte array of an illegal size (neither 4 nor 16 bytes long) is passed as the ` _`address`_ ` argument. This could be useful if a domain name server is not available or might have inaccurate information. `


`The ` _`getHostName()`_ ` method returns a ` _`String`_ ` that contains the name of the host with the IP address represented by an ` _`InetAddress`_ ` object. If the machine in question doesn't have a hostname or if the security manager prevents the name from being determined, a dotted quad format of the numeric IP address ireturned. For example: `

```

import  java.net._


var machine = InetAddress.getLocalHost
var localHost = machine.getHostName

```

`The ` _`getCanonicalHostName()`_ ` method is similar, but it's a bit more agressive about contacting DNS, ` _`getHostName()`_ ` will only call DNS if it doesn't think it already knows the hostname. ` _`getCanonicalHostName()`_ ` calls DNSif it can, and may replace the resltng cached hostname. For example:`

```

import  java.net._


var machine = InetAddress.getLocalHost
var localHost = machine.getCanonicalHostName
```

`The ` _` getCanonicalHostName()`_ ` method is particularly useful when you're starting with a dotted quad IP address rather than the hostname. The following example converts the dotted quad address 208.201.239.37 into a hostname by usig ` _`InetAddress.getByName()`_ ` and then applying ` _` getCanonicalHostName()`_ ` on the resulting object. `


```

import  java.net._

var ia = InetAddress.getByName("208.201.239.100")

println(ia.getCanonicalHostName)


```

`The ` _`getHostAddress()`_ ` method returns a string containing the dotted quad format of the IP address. The following example uses this method to print the IP address of the local machine in the customary format. `

```

import  java.net._

var machine = InetAddress.getLocalHost
var dottedQuad = machine.getHostAddress
println("My address is "+dottedQuad)

```

`If you want to know the IP address of a machine, then use the ` _`getAddress()`_ ` method, which returns an IP address as an array of bytes in network byte order. The most significant byte (i.e. the first byte in the address's dotted quad form) is the first byte in the array, or element zero. To be ready for IPv6 addresses, try not to assume anything about the length of the array,use the array's` _` length `_ ` field:`

```

import  java.net._

var me = InetAddress.getLocalHost
var addresses = me.getAddress
```

`The bytes returned are unsigned, which poses a problem. Unlike C, Java and Scala do not have an unsigned byte primitive da type. Bytes with values higher than 127 are treated as negative numbers. Therefore, if you to do anyhing with the bytes returned by` _`getAddress()`_ ` you need to promote the bytes to ` _`int`_`s and make appropriate adjustments. Here's one way to do it:`

```
var signedByte = -5
var unsignedByte = if (signedByte < 0) signedByte+256 else signedByte
```

`One reason to look at the raw bytes of an IP address is to determine the type of the address. Test the number of bytes in the array returned by ` _`getAddress()`_ ` to determine whether you're dealing with an IPv4 or IPv6 address. The following example demonstrates. `

```

import  java.net._



var me = InetAddress.getLocalHost
var addresses = me.getAddress

if (addresses.length == 4) 
  println("IPv4")
 else 
  if (addresses.length == 6)
  println("IPv6")

```

# Sockets #

## Getting the time for the NIST time server ##

```

import java.net._
import java.io._

var socket = new Socket("time.nist.gov", 13)

socket.setSoTimeout(15000)

var in = socket.getInputStream
var time = new StringBuilder

var reader = new InputStreamReader(in, "ASCII")
var c = reader.read
while (c != -1) 
 {
    time.append( c.toChar)
    c = reader.read
    }
    
    println( time)
     
```