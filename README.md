ComeOn !
========

A tool to upload pictures to Wikimedia Commons with extended support for JPEG metadata and its use in page templates. It is written in Java and the template engine is the old-but-reliable [Apache Velocity](https://velocity.apache.org/engine/releases/velocity-1.7/user-guide.html).

## Building

Building ComeOn! and its dependencies requires Oracle JDK 7 and Maven 3. Running ComeOn! requires Oracle Java 7 and, for the braves, a working WebStart plugin.

For the moment, ComeOn! uses a custom fork of [the Java MediaWiki client API](https://github.com/wikimedia/java-mwapi) and depends on the barely-maintained [Simple Validation library from Project Kenai](https://kenai.com/projects/simplevalidation/). You will need to clone and Maven-install both these repositories :
* https://github.com/edouardhue/java-mwapi
* https://github.com/edouardhue/simplevalidation

Simply run
```shell
mvn clean install -DskipTests=true
```
on each. As java-mwapi requires a running MediaWiki instance during unit-tests (sic), you will have to skip tests for this project.

##  Running

ComeOn! may be launched with Java WebStart from [http://edouardhue.github.io/comeon/jnlp-report.html]. This requires at least Java 7, but 8 is ok. The developer not having any decent code signing certificate, the Java plugin will refuse to run ComeOn! until you lower its security level (see configuration panel or wherever your operating system hides this) and acknowledge each security warning.

### First run

ComeOn! uses templates to generate description pages. A basic set of templates may be found at [https://github.com/edouardhue/comeon-templates]. Dowload the archive or this repository somewhere on your computer before running ComeOn!. Once in ComeOn!, navigate to *Edit* > *Preferences* > *Templates* dialog and add these templates you want. All of these templates use the Velocity engine and are encoded in UTF-8.

At the first run, you will also need to set your Commons credentials. Navigate to *Edit* > *Preferences* > *Wikis* then select the *Commons* entry and enter your username, password and your identity as you want it to appear in attribution templates.

Once you have set these preferences, get back to the main window. Pick some files with *File* > *Open pictures*, then select the template you want to use to generate description pages and click OK. You can preview and edit the generated description page. Modifying the template at runtime is not supported yet. Unwanted pictures may be removed with pressing Ctrl and left-clicking on their thumbnail in the lower carousel. The left panel lists all the available information found in the file's metadata. Any piece of information may be used in the templates (see examples). When ready, click *File* > *Upload pictures* and confirm to start the transfer.

ComeOn! has the ability to load external metadata from a CSV file (this file should have a header line with column titles). When picking files, tick the *Use external metadata* checkbox, pick the CSV file and indicate how data from the file should be matched against metadata from the pictures. The droplist on the left lists the columns found in the CSV file. In the right field, indicate the metadata key whose value will match the CSV column's value (*Iptc.ObjectName* for example). Data from the file may then be used in the templates like this: *$external.columnName*.

Report issues in [here on Github](https://github.com/edouardhue/comeon/issues). Please attach faulty pictures to ease reproducig and fixing bugs.

Good luck !
