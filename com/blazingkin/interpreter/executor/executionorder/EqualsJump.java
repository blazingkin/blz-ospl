package com.blazingkin.interpreter.executor.executionorder;

import java.util.Arrays;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.variables.Variable;

@Deprecated
public class EqualsJump implements InstructionExecutor {

	public void run(String[] args) {
		/*	Equals Jump
		 * 	If the variables passed are equals, then it will jump to the function
		 * 	DEPRICATED
		 */
		if ((Variable.parseString((args[1]))).equals((Variable.parseString((args[2]))))){
			String fName = args[0];
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
