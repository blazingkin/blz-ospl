## How to contribute to blz-ospl

#### **Did you find a bug?**


* **Ensure the bug was not already reported** by searching on GitHub under [Issues](https://github.com/blazingkin/blz-ospl/issues).

* If you're unable to find an open issue addressing the problem, [open a new one](https://github.com/blazingkin/blz-ospl/issues/new). Be sure to include a **title and clear description**, as much relevant information as possible, and a **code sample** or an **executable test case** demonstrating the expected behavior that is not occurring.


#### **Did you write a patch that fixes a bug? or do you have a new feature**

* Open a new GitHub pull request with the patch.

* Ensure the PR description clearly describes the problem and solution. Include the relevant issue number if applicable.


#### **Do you want to contribute to the blz documentation?**

* Please contribute to the [blz-docs](https://github.com/blazingkin/blz-docs) repository.


## How to write a patch

#### I have an issue with a standard library feature (i.e. a .blz file)

1. Check out the latest version of blz. This will not be the master branch. It should be named `v[develpment version number]`

2. Look under the `Packages` folder for the package that has the issue.

3. Find the relevant file, and make changes

4. Go into the `Tests` folder, and add a new test. You will need to change a `.blz` file and the corresponding `.out` file

5. Make sure you run the tests, by using the `runtests.py` script

6. Create a fork of blz, and push your changes there

7. Submit a pull request, making sure to merge into the latest branch.

#### I have an issue with the language behavior

1. Check out the latest version of blz. This will not be the master branch. It should be named `v[develpment version number]`

2. Look under `language/src/main` for the part of the language that you want to modify. Make your changes

3. Add tests in the `language/src/test` folder.

4. Make sure everything correctly compiles, and the tests pass using the `mvn package` command (You will need to install maven in order to build the language)

5. Make sure that the integration tests still pass. (Run the testing script `Tests/runtests.py`)

6. Create a fork of blz, and push your changes there

7. Submit a pull request, making sure to merge into the latest branch.

#### I have an issue with the documentation

* Please contribute to the [blz-docs](https://github.com/blazingkin/blz-docs) repository.

Thanks so much for your contribution!