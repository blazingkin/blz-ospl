package com.blazingkin.interpreter.executor;

import com.blazingkin.interpreter.executor.data.MoveData;
import com.blazingkin.interpreter.executor.data.Peek;
import com.blazingkin.interpreter.executor.data.Pop;
import com.blazingkin.interpreter.executor.data.Push;
import com.blazingkin.interpreter.executor.data.RandomImplementor;
import com.blazingkin.interpreter.executor.data.Set;
import com.blazingkin.interpreter.executor.executionorder.ChangeProcess;
import com.blazingkin.interpreter.executor.executionorder.Define;
import com.blazingkin.interpreter.executor.executionorder.End;
import com.blazingkin.interpreter.executor.executionorder.Exit;
import com.blazingkin.interpreter.executor.executionorder.ForLoop;
import com.blazingkin.interpreter.executor.executionorder.Goto;
import com.blazingkin.interpreter.executor.executionorder.IfBlock;
import com.blazingkin.interpreter.executor.executionorder.JumpReturn;
import com.blazingkin.interpreter.executor.executionorder.LoopEnd;
import com.blazingkin.interpreter.executor.executionorder.ReturnProcess;
import com.blazingkin.interpreter.executor.executionorder.WhileLoop;
import com.blazingkin.interpreter.executor.input.FileInput;
import com.blazingkin.interpreter.executor.input.NumInput;
import com.blazingkin.interpreter.executor.input.StringInput;
import com.blazingkin.interpreter.executor.listener.Listener;
import com.blazingkin.interpreter.executor.math.AddVars;
import com.blazingkin.interpreter.executor.math.Ceiling;
import com.blazingkin.interpreter.executor.math.Decrement;
import com.blazingkin.interpreter.executor.math.DivideVars;
import com.blazingkin.interpreter.executor.math.ExponentVars;
import com.blazingkin.interpreter.executor.math.Floor;
import com.blazingkin.interpreter.executor.math.Increment;
import com.blazingkin.interpreter.executor.math.Logarithm;
import com.blazingkin.interpreter.executor.math.LogicalAnd;
import com.blazingkin.interpreter.executor.math.ModVars;
import com.blazingkin.interpreter.executor.math.MultiplyVars;
import com.blazingkin.interpreter.executor.math.Round;
import com.blazingkin.interpreter.executor.math.SubVars;
import com.blazingkin.interpreter.executor.math.TrigFunctions;
import com.blazingkin.interpreter.executor.math.Trigonometry;
import com.blazingkin.interpreter.executor.math.Unsign;
import com.blazingkin.interpreter.executor.output.BLZLogging;
import com.blazingkin.interpreter.executor.output.Echo;
import com.blazingkin.interpreter.executor.output.FileOutput;
import com.blazingkin.interpreter.executor.output.SameLineEcho;
import com.blazingkin.interpreter.executor.output.graphics.GraphicsExecutor;
import com.blazingkin.interpreter.executor.output.graphics.GraphicsTask;
import com.blazingkin.interpreter.executor.string.ConcatenateStrings;
import com.blazingkin.interpreter.executor.string.Length;
import com.blazingkin.interpreter.executor.string.Substring;
import com.blazingkin.interpreter.executor.tensor.GetTensorValue;
import com.blazingkin.interpreter.executor.tensor.IntTensor;
import com.blazingkin.interpreter.executor.tensor.SetTensorValue;
import com.blazingkin.interpreter.executor.timing.Wait;

@SuppressWarnings("deprecation")
public enum Instruction {
	INVALID("","INVALID", null),													// INVALID - Not for use
	ECHO("ECHO", "ECHO", new Echo()),												// Echo - prints a replacing string
	SAMELINEECHO("SECHO", "SAME LINE ECHO", new SameLineEcho()),
	STORE("SET", "STORE", new Set()),												// Store - Stores an int as a variable
	GOTO("GOTO", "GOTO", new Goto()),												// Goto - Goes to a line number
	ADDVARIABLE("ADD", "ADD VARIABLES", new AddVars()),								// Add - adds two replacing strings and sets a variable to them
	SUBTRACTVARIABLE("SUB", "SUBTRACT VARIABLES", new SubVars()),					// Sub - subtract two replacing strings and sets a variable to them
	MULTIPLYVARIABLE("MUL", "MULTIPLE VARIABLES", new MultiplyVars()),				// Mul - multiply two replacing strings and sets a variable to them
	DIVIDEVARIABLE("DIV", "DIVIDE VARIABLES", new DivideVars()),					// Div - divide two replacing strings and sets a variable to them
	POWVARIABLE("POW", "EXPONENTIAL VARIABLES", new ExponentVars()),				// Pow - put one replacing string to the power of another replacing string and sets a variable to them
	NUMINPUT("NIN", "NUM INPUT", new NumInput()),									// Number input - inputs a string and parses to an int stores to a variable
	EXIT("EXT", "EXIT", new Exit()),												// Exit - ends the process
	MOVE("MOV", "MOVE", new MoveData()),											// Move data - copies a variable to another address
	LOGICALAND("AND", "LOGICAL AND", new LogicalAnd()),								// logical and - bitwise and, writes to a variable
	DECREMENT("DEC", "DECREMENT", new Decrement()),									// decrement - decrements a variable by one
	INCREMENT("INC", "INCREMENT", new Increment()),									// increment - increments a variable by one
	PUSH("PUSH", "PUSH", new Push()),												// push - push an integer onto the stack
	POP("POP", "POP", new Pop()),													// pop - pops an integer into a variable
	PEEK("PEEK", "PEEK", new Peek()),												// peek - peeks an integer into a variable
	RANDOM("RAND", "RANDOM", new RandomImplementor()),								// rand - puts a random number 0-99 into a variable
	STRINGINPUT("STRIN", "STRINGIN", new StringInput()),								// Input String - gets a string through input
	UNSIGN("UNS", "UNSIGN", new Unsign()),											// Unsign - Absolute values a variable
	MODULUS("MOD", "MODULUS", new ModVars()),										// Modulus - Gets the remainder of a difference
	RETURNJUMP("RJP", "RETURN JUMP", new JumpReturn()),								// Return Jump - Jumps, then returns after this at the relevant END
	END("END", "END STATEMENT", new End()),											// End - Ends a return jump statement
	CONCATENATE("CON", "CONCATENATE", new ConcatenateStrings()),					// Concatenate Strings - Concatenate Strings, removes "'s
	IF("IF", "IF STATEMENT", new IfBlock()),
	GRAPHICSTEST("GRT", "GRAPHICS TEST", new GraphicsExecutor(GraphicsTask.init)),
	WAIT("WAIT", "WAIT", new Wait()),												//Wait - Waits a given time on this instruction
	SETGRAPHICSSIZE("GRASS", "GRAPHICS SET SIZE", new GraphicsExecutor(GraphicsTask.setSize)),	//Graphics Set Size - Sets the size of the window
	ADDTEXT("TEXT", "ADD TEXT", new GraphicsExecutor(GraphicsTask.drawText)),					//Add Text - Draws some text on the screen
	TESTDRAW("TDRAW", "Test Draw", new GraphicsExecutor(GraphicsTask.draw)),
	CLEAR("CLEAR", "Clear Graphics", new GraphicsExecutor(GraphicsTask.clear)),					//Clear - clears the window
	SETGRAPHICSPROPERTY("GRAPHICSPROPERTY", "Set Graphics Property", new GraphicsExecutor(GraphicsTask.setProperty)),	//Graphics Property - Sets a property of the graphics system
	ADDLISTENER("ADDLISTENER", "Add Event Listener", new Listener()),									//Add listener - Registers an event listener
	CLEARLAST("CLEARLAST", "Clear Lst Graphics Element", new GraphicsExecutor(GraphicsTask.clearLast)),
	ADDPROCESS("ADDPROCESS", "Add Process", new ChangeProcess()),								//Add Process - Execute code from an external file
	RETURNPROCESS("RETPRO", "Return Process", new ReturnProcess()),								//Return Process - Return to the previous process
	ROUND("ROUND", "Round", new Round()),
	FLOOR("FLOOR", "Floor", new Floor()),
	CEILING("CEIL", "Ceiling", new Ceiling()),
	FILEINPUT("FILEIN", "File In", new FileInput()),
	FILEOUTPUT("FILEOUT", "File Out", new FileOutput()),
	STRINGLENGTH("SLEN", "String Length", new Length(true)),
	ARRAYLENGTH("ALEN", "Array Length", new Length(false)),
	SUBSTRING("SUBS", "Substring", new Substring()),
	Logarithm("LOG", "Logarithm", new Logarithm()),
	SIN("SIN", "Sine", new Trigonometry(TrigFunctions.SIN)),
	COS("COS", "Cosine", new Trigonometry(TrigFunctions.COS)),
	TAN("TAN", "Tangent", new Trigonometry(TrigFunctions.TAN)),
	CSC("CSC", "Cosecant", new Trigonometry(TrigFunctions.CSC)),
	SEC("SEC", "Secant", new Trigonometry(TrigFunctions.SEC)),
	COT("COT", "Cotangent", new Trigonometry(TrigFunctions.COT)),
	ARCSIN("ARCSIN", "Inverse Sine", new Trigonometry(TrigFunctions.ARCSIN)),
	ARCCOS("ARCCOS", "Inverse Cosine", new Trigonometry(TrigFunctions.ARCCOS)),
	ARCTAN("ARCTAN", "Inverse Tangent", new Trigonometry(TrigFunctions.ARCTAN)),
	FORLOOP("FOR", "For", new ForLoop()),
	WHILELOOP("WHILE", "While", new WhileLoop()),
	ENDLOOP("ENDLOOP", "End Loop", new LoopEnd()),
	INTTENSOR("TENSORI", "DECLARE INTEGER TENSOR", new IntTensor()),
	SETTENSORVALUE("TENSORSET", "SET TENSOR VALUE", new SetTensorValue()),
	GETTENSORVALUE("TENSORGET", "GET TENSOR VALUE", new GetTensorValue()),
	TOGGLELOGGING("BLZLOG", "TOGGLE LOGGING", new BLZLogging()),
	DEFINE("DEFINE", "DEFINE LAMBDA EXPRESSION", new Define(true)),
	LAMBDA("LAMBDA", "RETURN LAMBDA EXPRESSION", new Define(false));
	
	
	private Instruction(final String ins, final String name, final InstructionExecutor executor){
		this.instruction = ins;
		this.name = name;
		this.executor = executor;	
	}
	
	public final InstructionExecutor executor;
	public final String instruction;
	public final String name;

}
