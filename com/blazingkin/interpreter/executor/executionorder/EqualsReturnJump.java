package com.blazingkin.interpreter.executor.executionorder;

import java.util.Arrays;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.variables.Variable;

public class EqualsReturnJump implements InstructionExecutor {
	public final boolean appositive;
	
	
	public EqualsReturnJump(boolean app){
		appositive = app;
	}
	
	public void run(String[] args) {
		/*	Equals Return Jump
		 * 	If the variables are equal then it will jump to the function
		 * 	Then it adds a lineReturn called by the end executor
		 * 
		 */
		if ((Variable.parseString((args[1]))).equals((Variable.parseString((args[2])))) == appositive){
			Executor.getCurrentProcess().lineReturns.add((Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value+2);
			String fName = (String) args[0];
			if (Method.contains(Executor.getMethods(), fName) != null){
				int start = -1;
				for (int i = 0; i < args.length; i++){
					if (args[i].charAt(0) == '('){	//passes arguments
						start = i;
					}
				}
				if (start != -1){
					Executor.executeMethod(Executor.getMethodInCurrentProcess(fName), Variable.getArguments(Arrays.copyOfRange(args, start, args.length)));
					return;		
				}
				Executor.executeMethod(Executor.getMethodInCurrentProcess(fName));
			}
		}
	}

}
