package com.blazingkin.interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.blazingkin.interpreter.compilation.Translator;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.library.BlzEventHandler;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Variable;

@SuppressWarnings("deprecation")
public class Interpreter {
	public static boolean logging = true;
	
	public static void main(String args[]){
		try{
			switch(args.length){
			case 0:
				printHelp();
				System.exit(-1);
				break;
			default: 
				new Interpreter().run(args);
			break;
			}
		}catch(Exception e){throwError("Unhandled Exception Within Java\nErrorDump:"); e.printStackTrace();}
		
	}
	
	public static void printHelp(){
		System.out.println("How to use BLZ-OSPL:");
		System.out.println("The language home page is at http://blazingk.in/blz-ospl");
/*		System.out.println();
		System.out.println("Compile a pre-blz file (typically .pblz extension)");
		System.out.println("blz-ospl -c *INPATH* *OUTPATH*");
		This feature is deprecated, but will be recreated at a later date*/
		System.out.println();
		System.out.println("Execute a blz file (typically .blz extension)");
		System.out.println("blz-ospl *PATH*");
		System.out.println();
		System.out.println("Run in immediate mode");
		System.out.println("blz-ospl -i");
		System.out.println();
		System.out.println("Print version number");
		System.out.println("blz-ospl -v");
		System.out.println();
		System.out.println("See this help message");
		System.out.println("blz-ospl -h");
	}
	
	
	public void run(String args[]) throws FileNotFoundException{
		try{
			if (args.length == 0){
				Interpreter.printHelp();
				System.exit(0);
			}
			if (args[0].charAt(0) == '-'){
				switch(args[0].charAt(1)){
					case 'h':
						Interpreter.printHelp();
						System.exit(0);
						break;
					case 'i':
						Executor.immediateModeLoop(System.in);
						break;
					case 'c':						// - c *INPUT* *OUTPUT*	COMPILE
						String path = args[1];
						File pth = new File(path);
						int z = 2;
						List<String> arg = new LinkedList<String>();
						while (z < args.length){
							arg.add(args[z]);
							z++;
						}
						runCompiler(pth, arg);
						break;
					case 'v':
						System.out.println("blz-ospl v"+Variable.getEnvVariable(SystemEnv.version).value);
						break;
				}
				System.exit(0);
			}
			String paths= args[0];
			File pths = new File(paths);
			int f = 1;
			List<String> rg = new LinkedList<String>();
			while (f < args.length){
				rg.add(args[f]);
				f++;
			}
			runExecutor(pths, rg);
		}catch(Exception e){
			e.printStackTrace();
			if (!Executor.getCurrentProcess().runningFromFile){
				throwError("Error, Executor was on line "+ Executor.getLine()+" in a software environment");
			}else{
				throwError("Error, Executor was on line "+Executor.getLine() + " in file: "+Executor.getRunningProcesses().peek().readingFrom.getAbsolutePath());
				}		
			}
	}
	
	
	//This is for when you want to run blz code from another program
	public static void executeCodeAsLibrary(ArrayList<String> code, List<String> args, BlzEventHandler eventHandler) throws Exception{
		try{
			Executor.run(code, args, eventHandler);
		}catch(Exception e){
			if (!Executor.isCloseRequested()){
				throw e;
			}
		}
	}
	
	public void runCompiler(File path, List<String> args) throws Exception {
		Translator.run(path, args);
	}
	
	public static void terminate(){
		Executor.setCloseRequested(true);
	}
	
	public void runExecutor(File path, List<String> args) throws Exception{
		Executor.run(path, args);
	}
	
	public static void throwError(String error){
		if (logging){
			StackTraceElement[] elements = Thread.currentThread().getStackTrace();
			  for (int i = 2; i < elements.length; i++) {
			    StackTraceElement s = elements[i];
			    System.err.println("\tat " + s.getClassName() + "." + s.getMethodName()
			        + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
			  }
			System.err.println(error);
			if (!Executor.getRunningProcesses().isEmpty()){
				System.err.println("Error occurred on line: "+Executor.getLine());
			}
			if (!Executor.isImmediateMode()){
				Executor.getEventHandler().exitProgram("An Error Occured");
			}
			thrownErrors.add(new Exception(error));
		}
	}

	
	public static Stack<Exception> thrownErrors = new Stack<Exception>();
}
