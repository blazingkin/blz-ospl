import os
import filecmp
import time
from subprocess import call
""" This is a python program that runs all the .blz files in the "Testing directory"
*	It will pipe in input from the file with the same name, but the .in extension
*	It will check output against the file with the same name, but with the .out extension
"""

class TestFile:
	def __init__(self, source, output=None, inp=None):
		self.source = source
		self.output = output
		self.input = inp

	def test(self):
		print("Running " + self.source)
		start = time.time()
		if (self.input != None):
			call("java -jar ../../bin/blz-ospl.jar "+ self.source +" < "+ self.input + " > " + self.output + "ran", shell=True)
		else:
			call("java -jar ../../bin/blz-ospl.jar "+ self.source + " > "+self.output+"ran", shell=True)
		end = time.time()
		if (self.output != None):
			if not filecmp.cmp(self.output+"ran", self.output):
				print("Failed")
				print("Output differs from test: " + self.output + " vs " + self.output+"ran")
			else:
				print("Passed")
		print("Test took: " + str(end-start) + " seconds")

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