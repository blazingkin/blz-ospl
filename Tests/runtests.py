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

	def test(self):
		print("Running " + self.source)
		start = time.time()
		err = 0
		if (self.input != None):
			err = call("java -jar ../bin/blz-ospl.jar "+ self.source +" < "+ self.input + " > " + self.output + "ran", shell=True)
		else:
			err = call("java -jar ../bin/blz-ospl.jar "+ self.source + " > "+self.output+"ran", shell=True)
		end = time.time()
		print("Test took: " + str(round((end-start) * 1000) / 1000) + " seconds")
		if (self.output != None):
			testout = open(self.output + "ran", "r").read().replace("\r", "")
			gt = open(self.output, "r").read().replace("\r", "")
			if testout != gt:
				print(bcolors.FAIL + "Failed" + bcolors.ENDC)
				print("Output differs from test: " + self.output + " vs " + self.output+"ran")
				print()
				return 1
			elif err != 0:
				print(bcolors.FAIL + "Failed" + bcolors.ENDC)
				print("Program returned exit code: "+str(err))
				print()
				return 1
			else:
				print(bcolors.OKGREEN + "Passed" + bcolors.ENDC)
		print()

def run_tests():
	result = 0
	for folder in os.scandir("."):
		if folder.is_dir():
			for file in os.listdir(folder.name):
				if file.endswith(".blz") and folder.name != "tmp":
					src = os.path.join(folder.name, file)
					inp = None
					output = None
					if os.access(src.split(".")[0] + ".in", os.R_OK):
						inp = src.split(".")[0] + ".in"
					if os.access(src.split(".")[0] + ".out", os.R_OK):
						output = src.split(".")[0] + ".out"
					tf = TestFile(src, output, inp)
					if tf.test() == 1:
						result = 1
	if (result == 0):
		print("All tests passed!")
	else:
		print("Some tests failed")
	sys.exit(result)

if __name__ == '__main__':
	run_tests()
