ComeOn !
========

A tool to upload pictures to Wikimedia Commons with extended support for JPEG metadata and its use in page templates. It is written in Java and Swing.

## Building and running

Building ComeOn! and its dependencies requires Oracle JDK 7 and Maven 3. Running ComeOn! requires Oracle Java 7 and, for the braves, a working WebStart plugin.

For the moment, ComeOn! uses a custom fork of [https://github.com/wikimedia/java-mwapi](the Java MediaWiki client API) and depends on the barely-maintained [Simple Validation library from Project Kenai](https://kenai.com/projects/simplevalidation/). You will need to clone and Maven-install both these repositories :
* https://github.com/edouardhue/java-mwapi
* https://github.com/edouardhue/simplevalidation

Simply run
```shell
mvn clean install
```
on each. As java-mwapi requires a running MediaWiki instance during unit-tests (sic), you will probably have to skip tests for this project.

ComeOn! is meant to be run with Java WebStart from http://edouardhue.github.io/comeon/jnlp-report.html. The developer not having any decent code signing certificate, Java will bug you with security warnings. WebStart being what it is, you may prefer to build and run ComeOn! locally. You may use the jar profile for this purpose :

```shell
mvn -Pjar clean package
```

and then unzip the archive in target/ to your favorite location and run the application with the included shell script :

```shell
./comeon.sh
```

ComeOn! uses templates to generate description pages. A basic set of templates may be found at https://github.com/edouardhue/comeon-templates. Clone this repository somewhere on your computer before running ComeOn!. Once in ComeOn!, go to the preferences / templates dialog and add these templates you want.

Good luck !

