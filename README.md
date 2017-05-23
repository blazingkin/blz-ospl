blz-ospl
===
An assembly-like interpreted language that implements both imperative and functional programming schema.

Currently implemented in Java, but will eventually transition to being a directly compiled language.

The language will change drastically right now as I build up feature-completeness and polish it.

Hello World Sample
===

:main

ECHO "Hello World!"

END

or

:main

(ECHO "Hello World")

END

*See the examples folder for more*


Dependencies:
===
* JDK 7+


How to setup and use:
===
## Windows

1.) Install Java and make sure it is added to your PATH

2.) Download the blz-ospl project from github

3.) Add the project folder to your PATH

4.) From the command line, run

> blz-ospl ExampleCode.blz

## Unix

1.) Install Java

2.) Download the blz-ospl project from github

3.) Add an alias for blz-ospl, this can be done by adding the following line to your ~/.bashrc

> alias blz-ospl=INSTALLDIRECTORY/blz-ospl.sh

Where INSTALLDIRECTORY is the directory where you saved the project

4.) From the terminal, run

> blz-ospl ExampleCode.blz

Contact and License
===
For more information concerning this project, please email me at blazingkin [at] gmail [dot] com or visit [my website](http://www.blazingk.in/)

Copyright © 2017 Alex Gravenor under the GNU GPL V2 License
