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
import com.blazingkin.interpreter.library.BlzEventHandler;
import com.blazingkin.interpreter.library.StandAloneEventHandler;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.MethodBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.BLZRational;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import in.blazingk.blz.packagemanager.ImportPackageInstruction;

public class REPL {
	
	public static Context replContext = new Context();
	public static void immediateModeLoop(InputStream is){
		Executor.setEventHandler(new StandAloneEventHandler());
		BlzEventHandler eventHandler = Executor.getEventHandler();
		eventHandler.print("blz-ospl "+Variable.getEnvVariable(SystemEnv.version).value +" running in immediate mode:\n");
		eventHandler.print("Type 'exit' to exit\n");
		ArrayList<String> inputBuffer = new ArrayList<String>();
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			eventHandler.err(e.getMessage());
			eventHandler.exitProgram("Failed to import Core");
		}
		String in = "";
		Scanner sc = new Scanner(is);
		MethodBlockParser methodParser = new MethodBlockParser();
		Executor.immediateMode.set(true);
		Interpreter.thrownErrors.add(new Exception("There have been no exceptions"));
		try{
			do{
				try{
					eventHandler.print("> ");
					in = sc.nextLine();
					if (in.equals("err")){
						while (Interpreter.thrownErrors.peek().getMessage() == null &&
							Interpreter.thrownErrors.size() > 1){
							Interpreter.thrownErrors.pop();
						}
						eventHandler.err(Interpreter.thrownErrors.peek().getMessage());
						eventHandler.err("\n");
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
						ArrayList<Either<SourceLine, ParseBlock>> parsed = BlockParser.parseBody(stream, 1);

						// If there is a single block, then it could be a method or a constructor. We need special cases to handle those
						if (parsed.size() == 1 && parsed.get(0).isRight()) {
							ParseBlock bl = parsed.get(0).getRight().get();
							// Check for method
							if (methodParser.shouldParse(bl.getHeader())) {
								MethodNode method = (MethodNode) methodParser.parseBlock(bl);
								replContext.setValueInPresent(method.getStoreName(), Value.method(method));							
								continue;
							} else if (bl.isConstructor()){
								eventHandler.err("Constructors are currently not supported in immediate mode");
							}
							// Check for constructor
							// Currently difficult to do since the REPL doesn't have a process
						}

						// Execute the block if it wasn't a method or constructor
						Value result = new BlockNode(parsed, true).execute(replContext);
						if (result.type == VariableTypes.Rational) {
							BLZRational r = (BLZRational) result.value;
							eventHandler.print(result.toString() + " (" + r.toDecimalString() + ")");
						} else {
							eventHandler.print(result.toString());
						}

						eventHandler.print("\n");
						inputBuffer.clear();
						System.gc();
					}catch(SyntaxException e){
						if (!e.getMessage().equals(BlockParser.blocksUnclosedErrorMessage)){
							eventHandler.err(e.getMessage() + "\n");
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
				}catch(IOException e){
					Interpreter.throwError(e.getMessage());
					inputBuffer.clear();
				}catch(Exception e){
					/* Some error in my code, might want to get a stacktrace */
					e.printStackTrace();
					Interpreter.throwError(e.getMessage());
					eventHandler.err("An exception occured in the blz runtime. Please report this issue to a developer");
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
			replContext.setValueInPresent(m.getStoreName(), Value.method(m));
		}
		for (Constructor c : p.constructors) {
			replContext.setValueInPresent(c.name, Value.constructor(c));
		}
	}
	
	private static void importPackage(String packageName) throws IOException, Exception {
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		in.blazingk.blz.packagemanager.Package p = new in.blazingk.blz.packagemanager.Package(importer.findPackage(packageName));
		for (MethodNode m : p.getAllMethodsInPackage()) {
			replContext.setValueInPresent(m.getStoreName(), Value.method(m));
		}
		for (Constructor c : p.getAllConstructorsInPackage()) {
			replContext.setValueInPresent(c.name, Value.constructor(c));
		}
	}

}
