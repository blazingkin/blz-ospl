package com.blazingkin.interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import in.blazingk.blz.packagemanager.FileImportManager;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;
import com.blazingkin.interpreter.library.BlzEventHandler;
import com.blazingkin.interpreter.repl.REPL;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Variable;

public class Interpreter {
	public static boolean logging = true;
	public static int exitCode = 0;

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
		}catch(Exception e){
			throwError("Unhandled Exception Within Java\nErrorDump:"); 
			e.printStackTrace();
		}
		System.exit(exitCode);
	}
	
	public static void printHelp(){
		System.out.println("How to use BLZ-OSPL:");
		System.out.println("The language home page is at http://blazingk.in/blz-ospl");
		System.out.println();
		System.out.println("Execute a blz file (typically .blz extension)");
		System.out.println("blz PATH [-m MAIN]");
		System.out.println();
		System.out.println("Run in immediate mode");
		System.out.println("blz -i[mmediate]");
		System.out.println();
		System.out.println("Manage Packages");
		System.out.println("blz -p[ackage] [help | list]");
		System.out.println();
		System.out.println("Print version number");
		System.out.println("blz -v[ersion]");
		System.out.println();
		System.out.println("See this help message");
		System.out.println("blz -h[elp]");
	}
	
	public void selectOption(String option, String[] fullArgs){
		if (option.equals("h") || option.equals("help")){
			Interpreter.printHelp();
		}else if (option.equals("i") || option.equals("immediate")){
			REPL.immediateModeLoop(System.in);
		}else if (option.equals("v") || option.equals("version")){
			System.out.println("blz-ospl v"+Variable.getEnvVariable(SystemEnv.version).value);
		}else if (option.equals("p") || option.equals("package")){
			FileImportManager.handlePackageInstruction(fullArgs);
		}else{
			Interpreter.printHelp();
			System.err.println("Unrecognized option: "+option);
		}
		System.exit(exitCode); 
	}
	
	public void run(String args[]) throws FileNotFoundException{
		try{
			if (args.length == 0){	
				Interpreter.printHelp();
				System.exit(0);
			}
			int fileArg = 0;
			for (; fileArg < args.length; fileArg++){
				if (args[fileArg].charAt(0) != '-'){
					break;
				}
				selectOption(args[fileArg].substring(1).toLowerCase(), args);
			}
			String paths= args[fileArg];
			File pths = new File(paths);
			
			/* If it can't be found, try adding the extension */
			if (!pths.exists() && !paths.endsWith(".blz")) {
				pths = new File(paths + ".blz");
			}
			
			int programArgsIndex = fileArg + 1;
			List<String> programArgs = new LinkedList<String>();
			while (programArgsIndex < args.length){
				programArgs.add(args[programArgsIndex]);
				programArgsIndex++;
			}
			runExecutor(pths, programArgs);
		}catch(Exception e){
			e.printStackTrace();
			if (!Executor.getCurrentProcess().runningFromFile){
				throwError("Error, Executor was on line "+Executor.getLine()+" in a software environment");
			}else{
				throwError("Error, Executor was on line "+Executor.getLine()+ " in file: "+Executor.getRunningProcesses().peek().readingFrom.getAbsolutePath());
				}		
			}
	}
	
	
	//This is for when you want to run blz code from another program
	public static void executeCodeAsLibrary(String[] code, List<String> args, BlzEventHandler eventHandler) throws Exception{
		try{
			Executor.run(code, args, eventHandler);
		}catch(Exception e){
			if (!Executor.isCloseRequested()){
				throw e;
			}
		}
	}
	
	public static void terminate(){
		Executor.setCloseRequested(true);
	}

	public static void setExitCode(int val){
		exitCode = val;
	}
	
	public void runExecutor(File path, List<String> args) throws Exception{
		Executor.run(path, args);
	}
	
	public static void throwError(String error){
		thrownErrors.add(new Exception(error));
		if (Executor.isImmediateMode()){
			Executor.getEventHandler().err("There was an issue running your last command\n");
			Executor.getEventHandler().err("Type 'err' to see the error");
			return;
		}
		if (logging){
			if (!Executor.getRunningProcesses().isEmpty()){
				Stack<RuntimeStackElement> reverse = new Stack<RuntimeStackElement>();
				while (!RuntimeStack.runtimeStack.isEmpty()) {
					reverse.push(RuntimeStack.runtimeStack.pop());
				}
				System.err.println("Stack:");
				while (!reverse.isEmpty()) {
					RuntimeStackElement rse = reverse.pop();
					System.err.println((rse.getLineNum() == -1 ? "" : "Line " + rse.getLineNum()) + "\t" + rse.toString());
				}
				System.err.println("Error occurred on line: "+Executor.getLine());
			}
			System.err.println(error);
		}
		Executor.getEventHandler().exitProgram("An Error Occured");
	}

	
	public static Stack<Exception> thrownErrors = new Stack<Exception>();
}
