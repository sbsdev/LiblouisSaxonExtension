# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

LiblouisSaxonExtension is a Java library that integrates the [liblouis](http://liblouis.org/) braille translation library with the [Saxon XSLT processor](http://saxon.sourceforge.net/). It exposes a custom Saxon extension function `louis:translate(table, text)` so XSLT stylesheets can perform braille translation inline.

## Build Commands

The project uses Maven:

```bash
mvn package          # Compile and build liblouissaxonx.jar
mvn test             # Run JUnit 5 tests
mvn compile          # Compile only
mvn clean            # Remove build artifacts
mvn clean package    # Full rebuild
```

## Running

**Command-line XSLT transformer:**
```bash
java -cp target/liblouissaxonx.jar:$(mvn dependency:build-classpath -q -DforceStdout) \
     org.liblouis.LouisTransform -s:source.xml -xsl:style.xsl
```

**With trace listener for debugging:**
```bash
java ... org.liblouis.LouisTransform -s:source.xml -xsl:style.xsl -T:org.liblouis.LiblouisTraceListener
```

**As a custom SAX transformer factory (programmatic use):**
```bash
java -Djavax.xml.transform.sax.SAXTransformerFactory=org.liblouis.transformerfactory.LouisExtensionTransformerFactoryImpl YourApp
```

## Architecture

There are two entry points into the extension, both registering the same core function:

- **`LouisTransform`** — CLI entry point; extends Saxon's `Transform` class and registers the extension in `setFactoryConfiguration()`.
- **`LouisExtensionTransformerFactoryImpl`** — Programmatic entry point; extends Saxon's `TransformerFactoryImpl` and registers the extension in both constructors. Activated via the `javax.xml.transform.sax.SAXTransformerFactory` system property.

**Core extension:**

- **`LouisExtensionFunctionDefinition`** — Defines the Saxon 12 extension function `louis:translate(table, text)` via `net.sf.saxon.lib.ExtensionFunctionDefinition`. Calls into `liblouis-java` (`Translator` class) which invokes the native liblouis library (bundled in the liblouis-java JAR — no separate system install needed). Translators are cached by table name within each call expression instance.

**Debugging:**

- **`LiblouisTraceListener`** — Implements Saxon's `net.sf.saxon.lib.TraceListener`; prints template matches and `louis:translate()` calls (with arguments) to stderr.

## Key Dependencies

- `net.sf.saxon:Saxon-HE:12.5` — Saxon 12 XSLT processor; extension API is in `net.sf.saxon.lib` (not `net.sf.saxon.functions` as in Saxon 9)
- `org.liblouis:liblouis-java:5.2.0` — Java binding for liblouis (JNA wrapper). Requires system liblouis installed (e.g. `apt install liblouis-dev liblouis-data`). Translation API: `new Translator(tableFile).translate(text, null, null, null).getBraille()`; throws both `TranslationException` and `DisplayException`.

## Tests

There is one test class: `src/test/java/org/liblouis/LouisTransformTest.java`. It runs an XSLT transformation on `resources/test.xml` using `resources/test.xsl` and asserts the braille output matches the expected Grade 2 English braille string. The test working directory is the project root, so `resources/` paths resolve correctly.
