package com.blazingkin.interpreter.repl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

import in.blazingk.blz.packagemanager.ImportPackageInstruction;

public class REPL {
	
	public static Context replContext = new Context();
	public static void immediateModeLoop(InputStream is){
		Executor.getEventHandler().print("blz-ospl "+Variable.getEnvVariable(SystemEnv.version).value +" running in immediate mode:\n");
		Executor.getEventHandler().print("Type 'exit' to exit\n");
		ArrayList<String> inputBuffer = new ArrayList<String>();
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
						while (Interpreter.thrownErrors.peek().getMessage() == null &&
							Interpreter.thrownErrors.size() > 1){
							Interpreter.thrownErrors.pop();
						}
						Executor.getEventHandler().err(Interpreter.thrownErrors.peek().getMessage());
						Executor.getEventHandler().err("\n");
						continue;
					}
					else if (in.equals("exit") || in.equals("quit")){
						break;
					}
					else if (in.equals("")){
						continue;
					}
					else if (in.startsWith("import ")) {
						String packageName = in.replaceFirst("import ", "");
						importPackage(packageName);
						continue;
					}
					else if (in.startsWith("require ")) {
						String fileName = in.replaceFirst("require ", "");
						importFile(fileName);
						continue;
					}
					inputBuffer.add(in);
					try {
						SplitStream<String> stream = new SplitStream<String>(inputBuffer);
						ArrayList<Either<String, ParseBlock>> parsed = BlockParser.parseBody(stream);
						Executor.getEventHandler().print(
							new BlockNode(parsed, true).execute(replContext).toString()
						);
						Executor.getEventHandler().print("\n");
						inputBuffer.clear();
						System.gc();
					}catch(SyntaxException e){
						if (!e.getMessage().equals(BlockParser.blocksUnclosedErrorMessage)){
							Executor.getEventHandler().err(e.getMessage() + "\n");
							inputBuffer.clear();
						}
						/* Block was incomplete */
					}
				}catch(BLZRuntimeException e){
					/* Runtime exception in their code */
					Interpreter.throwError(e.getMessage());
					inputBuffer.clear();
				}catch(NoSuchElementException e){
					/* Input closed */
					in = "exit";
				}catch(Exception e){
					/* IO or other exception */
					e.printStackTrace();
					Interpreter.throwError(e.getMessage());
					inputBuffer.clear();
				}
			}while (in.toLowerCase() != "exit");
		}finally{
			sc.close();
		}
	}
	
	private static void importFile(String fileName) throws Exception {
		if (!fileName.endsWith(".blz")) {
			fileName = fileName + ".blz";
		}
		/* Try absolute path */
		com.blazingkin.interpreter.executor.sourcestructures.Process p = in.blazingk.blz.packagemanager.FileImportManager.importFile(Paths.get(fileName));
		if (p == null) {
			/* Try path relative to CWD */
			Path path = Paths.get("");
			path = Paths.get(path.toString(), fileName);
			p = in.blazingk.blz.packagemanager.FileImportManager.importFile(path);
		}
		for (MethodNode m : p.methods) {
			Variable.setValue(m.getStoreName(), Value.method(m), replContext);
		}
		for (Constructor c : p.constructors) {
			Variable.setValue(c.name, Value.constructor(c), replContext);
		}
	}
	
	private static void importPackage(String packageName) throws IOException, Exception {
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		in.blazingk.blz.packagemanager.Package p = new in.blazingk.blz.packagemanager.Package(importer.findPackage(packageName));
		for (MethodNode m : p.getAllMethodsInPackage()) {
			Variable.setValue(m.getStoreName(), Value.method(m), replContext);
		}
		for (Constructor c : p.getAllConstructorsInPackage()) {
			Variable.setValue(c.name, Value.constructor(c), replContext);
		}
	}

}
