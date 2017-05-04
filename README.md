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


How to use:
===
## Windows

1.) Add Java to your PATH

2.) Add the folder containing the jar and bat file to your PATH

3.) From the command line, run

> blz-ospl ExampleCode.blz

## Unix

1.) Run the following command with "JARPATH" filled in with the correct path

> java -jar "JARPATH" -e ExampleCode.blz

Contact and License
===
For more information concerning this project, please email me at blazingkin [at] gmail [dot] com or visit [my website](http://www.blazingk.in/)

Copyright © 2017 Alex Gravenor under the GNU GPL V2 License