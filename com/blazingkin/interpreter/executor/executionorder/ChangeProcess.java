package com.blazingkin.interpreter.executor.executionorder;

import java.io.File;
import java.io.FileNotFoundException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.executor.Process;

public class ChangeProcess implements InstructionExecutor {

	@Override
	public void run(String[] args) {	//File Path, Function Name
		String s = "";
		for (int i = 0; i < args.length-1; i++){
			s = s+ args[i]+" ";
		}
		s = s.substring(0, s.length()-1);
		String path = Variable.parseString(s);
		File f = new File(path);
		if (!f.exists()){
			Interpreter.throwError("Could not find file at path: "+path);
		}
		boolean found = false;
		Process p = null;
		for (int i = 0; i < Process.processes.size(); i++){
			if (Process.processes.get(i).readingFrom.getPath().equals(f.getPath())){
				p = Process.processes.get(i);
				found = true;
			}
		}
		if (!found){
			try {
				p = new Process(f);
			} catch (FileNotFoundException e) {
				
			}
		}
		p.setLine(Executor.methods.get(Executor.methods.size()-1).lineNumber);
		Executor.addProcess(p);
		
		
	}

}
