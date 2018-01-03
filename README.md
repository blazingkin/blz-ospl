The blazingkin open source programming language (blz-ospl)
===

[![Build Status](https://travis-ci.org/blazingkin/blz-ospl.svg?branch=master)](https://travis-ci.org/blazingkin/blz-ospl)

blz is an easy to use, fast, portable, and open scripting language.

The language is currently developing feature-completeness, and may change dramatically as that happens.

It currently runs on top of the JVM. There are plans to move it to the [GraalVM](https://github.com/graalvm/graal) using Truffle.

Language Homepage
===

Check out the language homepage at [blazingk.in/blz-ospl](http://blazingk.in/blz-ospl).


Dependencies:
===
* JRE 7+


How to setup and use:
===
## Windows

1.) Install the Java [JDK](http://www.oracle.com/technetwork/java/javase/downloads)

2.) Add the Java bin folder to your PATH

3.) Clone the blz-ospl project from github

4.) Add the bin folder to your PATH

5.) From the command line, run

> blz ExampleCode.blz

## Unix

Use the installation script available [here](http://blazingk.in/install_blz.sh). (Always read scripts you download before running them).

*Or*

1.) Install Java

2.) Clone the blz-ospl project from github

3.) Add the blz-ospl 'bin' folder to your path

> export PATH=$PATH:"INSTALLDIRECTORY/bin"

Where INSTALLDIRECTORY is the directory where you saved blz-ospl

4.) From the terminal, run

> $ blz ExampleCode.blz

Contact and License
===
For more information concerning this project, please email me at blazingkin [at] gmail [dot] com or visit [my website](http://www.blazingk.in/)

This work includes a [Java BigMath library by Richard Mathar](https://arxiv.org/abs/0908.3030v3). It is in the org folder.

See the Extras folder for the full license

Copyright © 2017 Alex Gravenor under the GNU GPL V3 License
