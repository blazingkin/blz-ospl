package com.blazingkin.interpreter.executor.executionorder;

import java.util.Arrays;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.variables.Variable;
@Deprecated
public class NonEquivalentJump implements InstructionExecutor {
	/*	NonEquivalentJump
	 * 	If the variables are not equivalent then it will jump to the specified function
	 */
	
	public void run(String[] args){
		if (Integer.parseInt(Variable.parseString((args[1]))) != Integer.parseInt(Variable.parseString((args[2])))){
			String fName = args[0];
			if (Method.contains(Executor.methods, fName) != null){
				int start = -1;
				for (int i = 0; i < args.length; i++){
					if (args[i].charAt(0) == '('){
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
