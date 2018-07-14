package com.blazingkin.interpreter.executor.instruction;

import java.util.HashMap;

import com.blazingkin.interpreter.executor.async.Async;
import com.blazingkin.interpreter.executor.data.HashGetKeys;
import com.blazingkin.interpreter.executor.data.RandomImplementor;
import com.blazingkin.interpreter.executor.data.Rebind;
import com.blazingkin.interpreter.executor.executionorder.Break;
import com.blazingkin.interpreter.executor.executionorder.Continue;
import com.blazingkin.interpreter.executor.executionorder.Exit;
import com.blazingkin.interpreter.executor.executionorder.ReturnValue;
import com.blazingkin.interpreter.executor.filesystem.CloseResource;
import com.blazingkin.interpreter.executor.filesystem.CreateResource;
import com.blazingkin.interpreter.executor.filesystem.CreateResourceFromFilePath;
import com.blazingkin.interpreter.executor.filesystem.OpenResource;
import com.blazingkin.interpreter.executor.filesystem.ScannerHasNext;
import com.blazingkin.interpreter.executor.filesystem.ScannerReadNext;
import com.blazingkin.interpreter.executor.filesystem.WriteToResource;
import com.blazingkin.interpreter.executor.input.NumInput;
import com.blazingkin.interpreter.executor.input.StringInput;
import com.blazingkin.interpreter.executor.output.BLZLogging;
import com.blazingkin.interpreter.executor.output.Echo;
import com.blazingkin.interpreter.executor.output.FailTest;
import com.blazingkin.interpreter.executor.output.FileOutput;
import com.blazingkin.interpreter.executor.output.RawEcho;
import com.blazingkin.interpreter.executor.output.SameLineEcho;
import com.blazingkin.interpreter.executor.string.Length;
import com.blazingkin.interpreter.executor.timing.Wait;

import in.blazingk.blz.packagemanager.ImportPackageInstruction;

public enum Instruction {
	INVALID("","INVALID", null),													// INVALID - Not for use
	ECHO("ECHO", "Echo", new Echo()),												// Echo - prints a replacing string
	SAMELINEECHO("SECHO", "Echo no newline", new SameLineEcho()),
	RAWECHO("BLZINTERNALRAWECHO", "Raw echo", new RawEcho()),
	NUMINPUT("NIN", "Number input from stdin", new NumInput()),									// Number input - inputs a string and parses to an int stores to a variable
	RANDOM("RAND", "Random", new RandomImplementor()),								// rand - puts a random number 0-99 into a variable
	STRINGINPUT("STRIN", "Read stdin", new StringInput()),								// Input String - gets a string through input
	WAIT("WAIT", "Wait", new Wait()),												//Wait - Waits a given time on this instruction
	FILEOUTPUT("FILEOUT", "File out", new FileOutput()),
	STRINGLENGTH("SLEN", "String length", new Length(true)),
	ARRAYLENGTH("ALEN", "Array length", new Length(false)),
	TOGGLELOGGING("BLZLOG", "Toggle logging", new BLZLogging()),
	BREAK("BREAK", "Break from loop", new Break()),
	CONTINUE("CONTINUE", "Continue in loop", new Continue()),
	RETURN("RETURN", "Return value", new ReturnValue()),
	IMPORTPACKAGE("IMPORT", "Import package", new ImportPackageInstruction()),
	EXIT("BLZINTERNALEXIT", "Exit with code", new Exit()),
	HASHGETKEYS("BLZINTERNALHASHGETKEYS", "Hash get key", new HashGetKeys()),
	ARRAYREBIND("BLZINTERNALREBIND", "Array rebind", new Rebind()),
	CREATERESOURCE("BLZINTERNALCREATERESOURCE", "Create resource", new CreateResource()),
	OPENRESOURCE("BLZINTERNALOPENRESOURCE", "Open resource", new OpenResource()),
	GETRESOURCEFROMFILEPATH("BLZINTERNALBLZGETFILEATPATH", "Get resource at path", new CreateResourceFromFilePath()),
	CLOSERESOURCE("BLZINTERNALCLOSERESOURCE", "Close resource", new CloseResource()),
	SCANNERHASNEXT("BLZINTERNALSCANNERHASNEXT", "Scanner has next byte", new ScannerHasNext()),
	SCANNERREADNEXT("BLZINTERNALSCANNERREADNEXT", "Scanner read next byte", new ScannerReadNext()),
	WRITETORESOURCE("BLZINTERNALWRITETORESOURCE", "Write to resource", new WriteToResource()),
	FAILTEST("FAILTEST", "Fail test", new FailTest()),
	ASYNC("ASYNC", "Spawn async", new Async());
	
	
	private Instruction(final String ins, final String name, final InstructionExecutor executor){
		this.instruction = ins;
		this.name = name;
		this.executor = executor;	
	}
	
	public final InstructionExecutor executor;
	public final String instruction;
	public final String name;


	public static Instruction getInstructionType(String s){		//gets the instruction based on the function call name
		try{
			return instructions.get(s.toUpperCase());
		}catch(Exception e){
		}
		return Instruction.INVALID;
	}
	
	// This hashmap is statically loaded with all instructions from the Instruction enum
	public static HashMap<String, Instruction> instructions;
	static {
		instructions = new HashMap<String, Instruction>();
		for (Instruction i: Instruction.values()){
			instructions.put(i.instruction, i);
		}
	}

}
