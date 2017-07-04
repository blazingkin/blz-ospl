package com.blazingkin.interpreter.executor.executionorder;

import java.io.File;
import java.io.FileNotFoundException;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.Process;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class ChangeProcess implements InstructionExecutor {

	@Override
	public void run(String[] args) {	//File Path, Function Name
		String s = "";
		for (int i = 0; i < args.length-1; i++){
			s = s+ args[i]+" ";
		}
		s = s.substring(0, s.length()-1);
		String path = Variable.contains(s)?(String)Variable.getValue(s).value:s;
		path = path.contains(".blz")?path:path+".blz";	//If the file extension is not stated, add it
		File f = new File(path);
		if (!f.exists()){
			Interpreter.throwError("Could not find file at path: "+path);
		}
		boolean found = false;
		Process p = null;
		for (int i = 0; i < Process.processes.size(); i++){
			if (Process.processes.get(i).runningFromFile && Process.processes.get(i).readingFrom.getPath().equals(f.getPath())){
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
		Executor.addProcess(p);
		Executor.executeMethod(Executor.getMethod(args[args.length-1], p.UUID));
	}

}
