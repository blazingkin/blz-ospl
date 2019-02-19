Version 2..7
======
- Add "did you mean"
- Add "type" field to objects to get constructor name
- Fix bug that stopped process contexts from executing when a constructor is called
- Make object contexts inherit from process context
- string starts_with?
- Flag package
- Socket builtin
- Socket Package
- Fix issue with async that would cause threads to mess with each other
(Need to add hash documentation)
(Need to add string documentation)


Version 2.6
=====
- Add `blz -p list` command line option to list loaded packages
- Add `exit(exit_code)` to specify an exit code
- Add `raw_print(char)` to print bytes
- Changes to how packages are loaded. First try BPM packages, then BLZPACKES, then installation directory
- Complete rewrite of block parsing
- Improve parsing error messages
- Improve runtime error messages
- Functions now return last line evaulated by default
- Allow all Array primitive methods to support `!`
- Fix bug in `ceil` that gave incorrect results
- Allow for overriding of `<` and `==` in constructors, other methods are inferred from these
- The user-defined `show` method will be called on objects when attempting to cast them to strings
- Add `split_on_token` to strings
- Add `to_json` to strings, numbers, booleans, arrays, hashes and nil
- Add `nil?` to objects
- Add `size` and `sample` to arrays
- Try Catch Blocks are now supported
- Add `{arguments}` environment variable
- Add `read`, `read_line`, `read_all`, and `write` to resources (files and network connections)
- Add lambda using `(arg1, arg2, arg3 -> expression)` syntax
- Add proper support for escape characters `\#` and `\"` in strings
- Add string replace method to replace characters
- Add `throw` builtin to throw custom values as errors
- Add lexer to catch more syntax errors and correctly parse everything

Test Mode
=====
- `blz -t source.blz another.blz` runs all methods in the given sources that start with `test`

REPL
=====
- Supports blocks (for, while, if, methods) but not constructors

Test Package
====
- Add `expect`, a testing framework
- Add `Expect::is`, `Expect::is_not`, `Expect::is_less_than`, `Expect::is_greater_than`, `Expect::is_nil`, `Expect::is_false`, `Expect::is_true` (and example is `expect(2 + 2).is(4)`)

JSON Package
=====
- Add `parse_json(json_string)` method that will parse json or throw a JSONError if the json is invalid.

CSV Package
=====
- Add `parse_csv(string)` method that will parse csv

Math Package
=====
- Add trig functions sin, cos, tan, cot, sine, cosine, tangent, cotangent

FileSystem Package
=====
- `open(path, mode)` allows for opening files
- `open_http(path)`, `open_https(path)`, `open_url(path)` allow for opening network resources

Lists Package
=====
- Add LinkedList constructor
- Add `range(start_or_end, end)` method to get easily get all integers in a range

Project Structure / Organization
=====
- Use maven to build
- Add unit tests for many ASTNodes
- Move parse classes to their own package
- Clean up old unused code
- Restructure Tests folder
- Allow for error message testing

Other
=====
- Function names can now have `:` in the middle of them (please don't use this in production)
- Add *EXPERIMENTAL* `async` builtin (please don't use this in production)

