package com.blazingkin.interpreter.repl;

import java.io.InputStream;
import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Variable;

public class REPL {
	
	public static void immediateModeLoop(InputStream is){
		Executor.getEventHandler().print("blz-ospl "+Variable.getEnvVariable(SystemEnv.version).value +" running in immediate mode:\n");
		Executor.getEventHandler().print("Type 'exit' to exit\n");
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			Executor.getEventHandler().err(e.getMessage());
			Executor.getEventHandler().exitProgram("Failed to import Core");
		}
		String in = "";
		Scanner sc = new Scanner(is);
		Executor.immediateMode = true;
		Interpreter.thrownErrors.add(new Exception("There have been no exceptions"));
		try{
			do{
				try{
					Executor.getEventHandler().print("> ");
					in = sc.nextLine();
					if (in.equals("err")){
						if (Interpreter.thrownErrors.peek().getMessage() == null &&
							Interpreter.thrownErrors.size() > 1){
							Interpreter.thrownErrors.pop();
						}
						Executor.getEventHandler().err(Interpreter.thrownErrors.peek().getMessage());
						Executor.getEventHandler().err("\n");
						continue;
					}
					if (in.equals("exit") || in.equals("quit")){
						break;
					}
					if (in.equals("")){
						continue;
					}
					Executor.getEventHandler().print(ExpressionExecutor.parseExpression(in).toString());
					Executor.getEventHandler().print("\n");
				}catch(Exception e){
					e.printStackTrace();
					Interpreter.throwError(e.getMessage());
				}
			}while (in.toLowerCase() != "exit");
		}finally{
			sc.close();
		}
	}

}
