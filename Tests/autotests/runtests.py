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
			err = call("java -jar ../../bin/blz-ospl.jar "+ self.source +" < "+ self.input + " > " + self.output + "ran", shell=True)
		else:
			err = call("java -jar ../../bin/blz-ospl.jar "+ self.source + " > "+self.output+"ran", shell=True)
		end = time.time()
		print("Test took: " + str(round((end-start) * 1000) / 1000) + " seconds")
		if (self.output != None):
			testout = open(self.output + "ran", "r").read()
			gt = open(self.output, "r").read()
			if testout != gt:
				print(bcolors.FAIL + "Failed" + bcolors.ENDC)
				print("Output differs from test: " + self.output + " vs " + self.output+"ran")
				sys.exit(1)
			elif err != 0:
				print(bcolors.FAIL + "Failed" + bcolors.ENDC)
				print("Program returned exit code: "+str(err))
				sys.exit(1)
			else:
				print(bcolors.OKGREEN + "Passed" + bcolors.ENDC)
		print()

for file in os.listdir("."):
    if file.endswith(".blz"):
    	src = file
    	inp = None
    	output = None
    	if os.access(file.split(".")[0] + ".in", os.R_OK):
    		inp = file.split(".")[0] + ".in"
    	if os.access(file.split(".")[0] + ".out", os.R_OK):
    		output = file.split(".")[0] + ".out"
    	tf = TestFile(src, output, inp)
    	tf.test()
