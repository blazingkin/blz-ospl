import os, sys
import filecmp
import time
from subprocess import call
""" This is a python program that runs all the .blz files in the "Testing directory"
*	It will pipe in input from the file with the same name, but the .in extension
*	It will check output against the file with the same name, but with the .out extension
"""

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

exitcode = 0

class TestFile:
	def __init__(self, source, output=None, inp=None):
		self.source = source
		self.output = output
		self.input = inp
		self.error = None

	def set_error(self, err):
		self.error = err

	def test(self):
		print("Running " + self.source)
		err = 0
		command = "java -jar ../bin/blz-ospl.jar "+self.source
		if (self.input != None):
			command = command + " < " + self.input
		if self.output != None:
			command = command + " > " + self.output + "ran"
		if (self.error != None):
			command = command + " 2> "+self.error+"ran"
		start = time.time()
		err = call(command, shell=True)
		end = time.time()
		print("Test took: " + str(round((end-start) * 1000) / 1000) + " seconds")
		if (self.output != None):
			testout = open(self.output + "ran", "r").read().replace("\r", "")
			gt = open(self.output, "r").read().replace("\r", "")
			if testout != gt:
				print(bcolors.FAIL + "Failed" + bcolors.ENDC)
				print("Output differs from test: " + self.output + " vs " + self.output+"ran")
				print("")
				return 1
		if self.error != None:
			testerr = open(self.error + "ran", "r").read().replace("\r", "")
			expectederr = open(self.error, "r").read().replace("\r", "")
			if testerr != expectederr:
				print(bcolors.FAIL + "Failed" + bcolors.ENDC)
				print("Error output differs from test: " + self.error + " vs " + self.error+"ran")
				print("")
				return 1
		if (err != 0 and self.error == None) or (err == 0 and self.error != None):
			print(bcolors.FAIL + "Failed" + bcolors.ENDC)
			print("Program returned exit code: "+str(err))
			print("")
			return 1
		print(bcolors.OKGREEN + "Passed" + bcolors.ENDC)
		print("")
		return 0

def run_tests():
	result = 0
	for folder in os.listdir("."):
		if os.path.isdir(folder):
			for file in os.listdir(folder):
				if file.endswith(".blz") and folder != "tmp":
					src = os.path.join(folder, file)
					
					# Set input and output files if they exist
					inp = None
					output = None
					if os.access(src.split(".")[0] + ".in", os.R_OK):
						inp = src.split(".")[0] + ".in"
					if os.access(src.split(".")[0] + ".out", os.R_OK):
						output = src.split(".")[0] + ".out"
					tf = TestFile(src, output, inp)

					# Set an error file if it exists
					if os.access(src.split(".")[0]+".err", os.R_OK):
						tf.set_error(src.split(".")[0]+".err")
					# Run the test, set the error flag if it failed
					if tf.test() == 1:
						result = 1
	if (result == 0):
		print("All tests passed!")
	else:
		print("Some tests failed")
	sys.exit(result)

if __name__ == '__main__':
	run_tests()
