package com.blazingkin.interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import com.blazingkin.interpreter.compilation.Translator;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Variable;

public class Interpreter {
	public static void main(String args[]){
		try{
			switch(args.length){
			case 0:
				throwError("Not Enough Arguments");
				break;
			default: 
				new Interpreter().run(args);
			break;
			}
		}catch(Exception e){throwError("Unhandled Exception Within Java\nErrorDump:"); e.printStackTrace();}
		
	}
	
	
	public void run(String args[]) throws FileNotFoundException{
		try{
			for (int i = 0; i < args.length; i++){
				if (args[i].charAt(0) == '-'){
					switch(args[i].charAt(1)){
					case 'c':						// - c *PATHNAME*	COMPILE
					String path = args[i+1];
					File pth = new File(path);
					
					int z = i+2;
					List<String> arg = new LinkedList<String>();
					while (z < args.length){
						arg.add(args[z]);
						z++;
					}
					runCompiler(pth, arg);
						break;
					case 'e':						// - e *PATHNAME*	EXECUTE
						String paths= args[i+1];
						File pths = new File(paths);
						int f = i+2;
						List<String> rg = new LinkedList<String>();
						while (f < args.length){
							rg.add(args[f]);
							f++;
						}
						runExecutor(pths, rg);
						break;
					}
				}
			}
		}catch(Exception e){e.printStackTrace();throwError("Error, Executor was on line "+(Integer)(Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value) + " in file: "+Executor.runningProcesses.peek().readingFrom.getAbsolutePath());}
	}
	
	public void runCompiler(File path, List<String> args) throws Exception {
		Translator.run(path, args);
	}
	
	public void runExecutor(File path, List<String> args) throws Exception{
		Executor.run(path, args);
	}
	
	public static void throwError(String error){
		System.err.println(error);
		System.exit(0);
	}

}
