Liblouis Saxon Extension
========================

This project provides a saxon extension that allows to translate text nodes to braille using liblouis from within xsl. 

It does this by providing a specialized `javax.xml.transform.sax.SAXTransformerFactory`
that can be used to configure applications that respect the
System property `"javax.xml.transform.TransformerFactory"`.

It uses [saxon][] with a [java extension](https://github.com/sbsdev/LiblouisSaxonExtension)
that offers translating text into braille using [liblouis].

Usage `org.liblouis.LouisExtensionTransformerFactoryImpl`
--------------------------------------------------------

    java -Djavax.xml.transform.sax.SAXTransformerFactory YourAppThatUsesJaxp

Examples can be found in the xsl tests which are performed using
[utf-x][] (we're using the svn version, which has been ported to work with saxon9he).
See utfx.sh shell script in the project [dtbook2sbsform][].

Usage `org.liblouis.LouisTransform`
-----------------------------------

    java org.liblouis.LouisTransform -s:yourSource.xml -xsl:yourXSLT.xsl

An example can be found in the dtbook2sbsform.sh shell script.

Debugging your XSLT
-------------------

To print debug info, use Saxon's TraceListener mechanism:

    java org.liblouis.LouisTransform -s:yourSource.xml -xsl:yourXSLT.xsl \
                                     -T:org.liblouis.LiblouisTraceListener \
                                     2> file/to/redirect/stderr/to

Prerequisite installs
------------------------

* [java][]
* [liblouis][]

Authors
-------

**Christian Egli**

+ https://github.com/egli

**Bernhard Wagner**

+ http://xmlizer.net
+ http://github.com/bwagner

**Bert Frees**

+ https://github.com/bertfrees

License
---------------------

Copyright 2011 SBS.

Licensed under GNU Lesser General Public License as published by the Free Software Foundation,
either [version 3](http://www.gnu.org/licenses/gpl-3.0.html) of the License, or (at your option) any later version.


[java extension]
[saxon]: http://saxon.sourceforge.net/
[java]: http://java.sun.com
[liblouis]: http://code.google.com/p/liblouis/
[dtbook2sbsform]: https://github.com/sbsdev/dtbook2sbsform
[utf-x]: http://utf-x.sourceforge.net/
