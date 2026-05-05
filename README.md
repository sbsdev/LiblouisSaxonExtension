Liblouis Saxon Extension
========================

This project provides a Saxon extension that allows translating text nodes to braille using liblouis from within XSLT.

It does this by providing a specialized `javax.xml.transform.sax.SAXTransformerFactory`
that can be used to configure applications that respect the
system property `javax.xml.transform.sax.SAXTransformerFactory`.

It uses [Saxon][] with a [Java extension][java extension]
that offers translating text into braille using [liblouis][].

Usage `org.liblouis.LouisExtensionTransformerFactoryImpl`
--------------------------------------------------------

    java -Djavax.xml.transform.sax.SAXTransformerFactory=org.liblouis.transformerfactory.LouisExtensionTransformerFactoryImpl YourAppThatUsesJaxp

Usage `org.liblouis.LouisTransform`
-----------------------------------

    java org.liblouis.LouisTransform -s:yourSource.xml -xsl:yourXSLT.xsl

Debugging your XSLT
-------------------

To print debug info, use Saxon's TraceListener mechanism:

    java org.liblouis.LouisTransform -s:yourSource.xml -xsl:yourXSLT.xsl \
                                     -T:org.liblouis.LiblouisTraceListener \
                                     2> file/to/redirect/stderr/to

Prerequisites
-------------

* [Java][] 11 or later
* [Maven][]
* [liblouis][] (e.g. `apt install liblouis-dev liblouis-data`)

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
-------

Copyright 2011 SBS.

Licensed under GNU Lesser General Public License as published by the Free Software Foundation,
either [version 3](https://www.gnu.org/licenses/lgpl-3.0.html) of the License, or (at your option) any later version.


[java extension]: https://github.com/sbsdev/LiblouisSaxonExtension
[Saxon]: https://www.saxonica.com/
[Java]: https://www.java.com
[Maven]: https://maven.apache.org/
[liblouis]: https://liblouis.io/
