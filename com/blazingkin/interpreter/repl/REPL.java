package com.blazingkin.interpreter.repl;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.sourcestructures.Method;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import in.blazingk.blz.packagemanager.ImportPackageInstruction;
import in.blazingk.blz.packagemanager.Package;

public class REPL {
	
	public static void immediateModeLoop(InputStream is){
		Executor.getEventHandler().print("blz-ospl "+Variable.getEnvVariable(SystemEnv.version).value +" running in immediate mode:\n");
		Executor.getEventHandler().print("Type 'exit' to exit\n");
		String in = "";
		Scanner sc = new Scanner(is);
		Executor.immediateMode = true;
		importCore();
		Interpreter.thrownErrors.add(new Exception("There have been no exceptions"));
		try{
			do{
				try{
					Executor.getEventHandler().print("> ");
					in = sc.nextLine();
					if (in.equals("err")){
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
	
	private static void importCore(){
		Set<File> packagesToImport = new HashSet<File>();
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		try{
			packagesToImport.add(importer.findPackage("Core"));	
			for (File f : packagesToImport){
				Package p = new in.blazingk.blz.packagemanager.Package(f);
				for (Method m : p.getAllMethodsInPackage()){
					Variable.setValue(m.functionName, new Value(VariableTypes.Method, m));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			Interpreter.throwError(e.getMessage());
		}
	}

}
