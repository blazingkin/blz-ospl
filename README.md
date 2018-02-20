The blazingkin open source programming language (blz-ospl)
===

Stable - [![Build Status](https://travis-ci.org/blazingkin/blz-ospl.svg?branch=master)](https://travis-ci.org/blazingkin/blz-ospl)

Nightly - [![Build Status](https://travis-ci.org/blazingkin/blz-ospl.svg?branch=v2.6)](https://travis-ci.org/blazingkin/blz-ospl/branches)

blz is an easy to use, fast, and portable scripting language implemented using the JVM.

The goal is to provide programmers with high levels of abstraction to increase developer velocity.

Getting Started
====

Directions for installing the language are available on the [wiki page](https://github.com/blazingkin/blz-ospl/wiki/Installation)


Example Code
====

Hello World
```
print("Hello World!")
```

Factorial
```
:main
	print("What number would you like the factorial of?")
	input = number_input()
	print(fact(input))
end

:fact(num)
	if num <= 1
		return 1
	end
	return num * fact(num - 1)
end
```

Constructors / Objects
```
:main
	# Make a new Ball and name it red_ball
	red_ball = Ball("red", 2)
	
	# Examine its properties
	print(red_ball.name())
	print(red_ball.volume())
	
end

# Constructors can take parameters (that automatically become properties)
constructor Ball(color, radius)
	
	# Objects can also have functions (closures)
	:volume
		return 4/3 * {pi} * (radius ** 3)
	end
	:name
		return "a " + color + " ball with radius " + radius
	end
	
end
```


Reference Material
====

Check out the [wiki on GitHub](https://github.com/blazingkin/blz-ospl/wiki) for details about the language and its standard library


Language Hompage
===

Check out the language homepage at [blazingk.in/blz-ospl](http://blazingk.in/blz-ospl).

Contact and License
===
For more information concerning this project, please email me at blazingkin [at] gmail [dot] com or visit [my website](http://www.blazingk.in/)

This work includes a [Java BigMath library by Richard Mathar](https://arxiv.org/abs/0908.3030v3). It is in the org folder.

See the Extras folder for the full license

Copyright © 2015-2018 Alex Gravenor under the GNU GPL V3 License
