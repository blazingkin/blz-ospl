package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.executor.data.HashHasKey;
import com.blazingkin.interpreter.executor.data.InitializeObject;
import com.blazingkin.interpreter.executor.data.RandomImplementor;
import com.blazingkin.interpreter.executor.executionorder.Break;
import com.blazingkin.interpreter.executor.executionorder.Continue;
import com.blazingkin.interpreter.executor.executionorder.Else;
import com.blazingkin.interpreter.executor.executionorder.End;
import com.blazingkin.interpreter.executor.executionorder.Exit;
import com.blazingkin.interpreter.executor.executionorder.ForLoop;
import com.blazingkin.interpreter.executor.executionorder.IfBlock;
import com.blazingkin.interpreter.executor.executionorder.ReturnValue;
import com.blazingkin.interpreter.executor.executionorder.WhileLoop;
import com.blazingkin.interpreter.executor.input.FileInput;
import com.blazingkin.interpreter.executor.input.NumInput;
import com.blazingkin.interpreter.executor.input.StringInput;
import com.blazingkin.interpreter.executor.output.BLZLogging;
import com.blazingkin.interpreter.executor.output.Echo;
import com.blazingkin.interpreter.executor.output.FileOutput;
import com.blazingkin.interpreter.executor.output.SameLineEcho;
import com.blazingkin.interpreter.executor.output.RawEcho;
import com.blazingkin.interpreter.executor.string.Length;
import com.blazingkin.interpreter.executor.timing.Wait;

import in.blazingk.blz.packagemanager.ImportPackageInstruction;
import in.blazingk.blz.packagemanager.ImportRelativeInstruction;

public enum Instruction {
	INVALID("","INVALID", null),													// INVALID - Not for use
	ECHO("ECHO", "ECHO", new Echo()),												// Echo - prints a replacing string
	SAMELINEECHO("SECHO", "SAME LINE ECHO", new SameLineEcho()),
	RAWECHO("BLZINTERNALRAWECHO", "RAWECHO", new RawEcho()),
	NUMINPUT("NIN", "NUM INPUT", new NumInput()),									// Number input - inputs a string and parses to an int stores to a variable
	RANDOM("RAND", "RANDOM", new RandomImplementor()),								// rand - puts a random number 0-99 into a variable
	STRINGINPUT("STRIN", "STRINGIN", new StringInput()),								// Input String - gets a string through input
	END("END", "END STATEMENT", new End()),											// End - Ends a return jump statement
	IF("IF", "IF STATEMENT", new IfBlock()),
	WAIT("WAIT", "WAIT", new Wait()),												//Wait - Waits a given time on this instruction
	FILEINPUT("FILEIN", "File In", new FileInput()),
	FILEOUTPUT("FILEOUT", "File Out", new FileOutput()),
	STRINGLENGTH("SLEN", "String Length", new Length(true)),
	ARRAYLENGTH("ALEN", "Array Length", new Length(false)),
	FORLOOP("FOR", "For", new ForLoop()),
	WHILELOOP("WHILE", "While", new WhileLoop()),
	TOGGLELOGGING("BLZLOG", "TOGGLE LOGGING", new BLZLogging()),
	BREAK("BREAK", "Break from loop", new Break()),
	CONTINUE("CONTINUE", "Continue in loop", new Continue()),
	RETURN("RETURN", "Return value", new ReturnValue()),
	INSTANTIATE("NEW", "INSTANTIATE OBJECT", new InitializeObject()),
	IMPORTPACKAGE("IMPORT", "IMPORT PACKAGE", new ImportPackageInstruction()),
	REQUIREPROCESS("REQUIRE", "REQUIRE PROCESS", new ImportRelativeInstruction()),
	ELSE("ELSE", "ELSE BLOCK", new Else()),
	EXIT("BLZINTERNALEXIT", "Exit with code", new Exit()),
	HASHHASKEY("BLZINTERNALHASHHASKEY", "Hash has key", new HashHasKey());
	
	
	private Instruction(final String ins, final String name, final InstructionExecutor executor){
		this.instruction = ins;
		this.name = name;
		this.executor = executor;	
	}
	
	public final InstructionExecutor executor;
	public final String instruction;
	public final String name;

}
