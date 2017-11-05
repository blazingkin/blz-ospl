package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.executor.sourcestructures.Method;
import com.blazingkin.interpreter.variables.Variable;

public class JumpReturn implements InstructionExecutorStringArray {
	/*	JumpReturn
	 * 	Jumps to a function and then returns on the following line at the next end
	 * 	Args are fName, [(args, to, pass)]
	 */
	public void run(String[] args) {
		String fName = args[0];
		Executor.getCurrentProcess().lineReturns.add(Executor.getLine());
		if (Method.contains(Executor.getMethods(), fName) != null){
			Method m = Executor.getMethodInCurrentProcess(fName);
			if (m.takesVariables){
				if (args.length != 2){
					Interpreter.throwError("Was expecting arguments for function: "+fName);
				}
				args[1] = args[1].replace("(", "").replace(")", "").replace(",", "");
				String[] vars = args[1].split(" ");
				Executor.executeMethod(Executor.getMethodInCurrentProcess(fName), Variable.getValuesFromList(vars));
				return;
			}
				Executor.executeMethod(Executor.getMethodInCurrentProcess(fName));
				return;			
		}
	}
}