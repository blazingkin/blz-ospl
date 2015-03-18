package com.blazingkin.interpreter.executor;

import com.blazingkin.interpreter.executor.data.MoveData;
import com.blazingkin.interpreter.executor.data.Peek;
import com.blazingkin.interpreter.executor.data.Pop;
import com.blazingkin.interpreter.executor.data.Push;
import com.blazingkin.interpreter.executor.data.RandomImplementor;
import com.blazingkin.interpreter.executor.data.Set;
import com.blazingkin.interpreter.executor.data.SetString;
import com.blazingkin.interpreter.executor.executionorder.BooleanIfBlock;
import com.blazingkin.interpreter.executor.executionorder.ChangeProcess;
import com.blazingkin.interpreter.executor.executionorder.End;
import com.blazingkin.interpreter.executor.executionorder.EqualsJump;
import com.blazingkin.interpreter.executor.executionorder.EqualsReturnJump;
import com.blazingkin.interpreter.executor.executionorder.EquivalentGoto;
import com.blazingkin.interpreter.executor.executionorder.Exit;
import com.blazingkin.interpreter.executor.executionorder.Goto;
import com.blazingkin.interpreter.executor.executionorder.GreaterThanGoto;
import com.blazingkin.interpreter.executor.executionorder.IfBlock;
import com.blazingkin.interpreter.executor.executionorder.Jump;
import com.blazingkin.interpreter.executor.executionorder.JumpReturn;
import com.blazingkin.interpreter.executor.executionorder.LessThanGoto;
import com.blazingkin.interpreter.executor.executionorder.LessThanJump;
import com.blazingkin.interpreter.executor.executionorder.LessThanReturnJump;
import com.blazingkin.interpreter.executor.executionorder.MoreThanJump;
import com.blazingkin.interpreter.executor.executionorder.MoreThanReturnJump;
import com.blazingkin.interpreter.executor.executionorder.NonEquivalentGoto;
import com.blazingkin.interpreter.executor.executionorder.NonEquivalentJump;
import com.blazingkin.interpreter.executor.executionorder.ReturnProcess;
import com.blazingkin.interpreter.executor.executionorder.While;
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
import com.blazingkin.interpreter.executor.math.LogicalAnd;
import com.blazingkin.interpreter.executor.math.ModVars;
import com.blazingkin.interpreter.executor.math.MultiplyVars;
import com.blazingkin.interpreter.executor.math.Round;
import com.blazingkin.interpreter.executor.math.SubVars;
import com.blazingkin.interpreter.executor.math.Unsign;
import com.blazingkin.interpreter.executor.output.Echo;
import com.blazingkin.interpreter.executor.output.SameLineEcho;
import com.blazingkin.interpreter.executor.output.graphics.GraphicsExecutor;
import com.blazingkin.interpreter.executor.output.graphics.GraphicsTask;
import com.blazingkin.interpreter.executor.string.ConcatenateStrings;
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
	EQUALGOTO("EGO", "Equal GOTO", new EquivalentGoto()),							// Equals goto - If Equals Goto
	NOTEQUALGOTO("NGO", "Nonequal GOTO", new NonEquivalentGoto()),					// Not Equals goto - If not equals goto
	LESSTHANGOTO("LGO", "Less than GOTO", new LessThanGoto()),						// Less Than Goto - If less then goto
	GREATERTHANGOTO("GGO", "Greater than Goto", new GreaterThanGoto()),				// Greater Than Goto - If greater then goto
	NUMINPUT("NIN", "NUM INPUT", new NumInput()),									// Number input - inputs a string and parses to an int stores to a variable
	JUMP("JMP", "JUMP", new Jump()),												// Jump - Jumps to a set point
	EXIT("EXT", "EXIT", new Exit()),												// Exit - ends the process
	EQUALSJUMP("EJP","EJUMP", new EqualsJump()),									// Equals Jump - If equal jump
	NOTEQUALSJUMP("NJP", "NEJUMP", new NonEquivalentJump()),						// Not Equals Jump - If Not Equals Jump
	LESSTHANJUMP("LJP", "LESSJUMP", new LessThanJump()),							// Less Than Jump - If less then jump
	MORETHANJUMP("MJP", "MOREJUMP", new MoreThanJump()),							// More than jump - if more then jump
	MOVE("MOV", "MOVE", new MoveData()),											// Move data - copies a variable to another address
	LOGICALAND("AND", "LOGICAL AND", new LogicalAnd()),								// logical and - bitwise and, writes to a variable
	DECREMENT("DEC", "DECREMENT", new Decrement()),									// decrement - decrements a variable by one
	INCREMENT("INC", "INCREMENT", new Increment()),									// increment - increments a variable by one
	PUSH("PUSH", "PUSH", new Push()),												// push - push an integer onto the stack
	POP("POP", "POP", new Pop()),													// pop - pops an integer into a variable
	PEEK("PEEK", "PEEK", new Peek()),												// peek - peeks an integer into a variable
	RANDOM("RAND", "RANDOM", new RandomImplementor()),								// rand - puts a random number 0-99 into a variable
	STRINGSET("STR", "STRINGSET", new SetString()),									// Set String - Stores a string
	STRINGINPUT("SIN", "STRINGIN", new StringInput()),								// Input String - gets a string through input
	UNSIGN("UNS", "UNSIGN", new Unsign()),											// Unsign - Absolute values a variable
	MODULUS("MOD", "MODULUS", new ModVars()),										// Modulus - Gets the remainder of a difference
	RETURNJUMP("RJP", "RETURN JUMP", new JumpReturn()),								// Return Jump - Jumps, then returns after this at the relevant END
	END("END", "END STATEMENT", new End()),											// End - Ends a return jump statement
	EQUALSRETURNJUMP("ERJ", "EQUALS RETURN JUMP", new EqualsReturnJump(true)),			// Equals Return Jump - Jumps if statement is equal, then returns after this at the relevant END
	NOTEQUALSRETURNJUMP("NRJ", "NOT EQUALS RETURN JUMP", new EqualsReturnJump(false)),// Not Equals Return Jump - Jumps if statement is not equal, then returns after this at the relevant END
	LESSTHANRETURNJUMP("LRJ", "LESS THAN RETURN JUMP", new LessThanReturnJump()),	// Less than return jump - Jumps if statement is less than, then returns after this at the relevant END
	MORETHANRETURNJUMP("MRJ", "MORE THAN RETURN JUMP", new MoreThanReturnJump()),	// More than return jumo - Jumps if statement is more than, then returns after this at the relevant END
	CONCATENATE("CON", "CONCATENATE", new ConcatenateStrings()),					// Concatenate Strings - Concatenate Strings, removes "'s
	IFBLOCK("IFE", "IF BLOCK", new BooleanIfBlock(true)),									// If block - Executes the next line if condition is true
	IF("IF", "IF STATEMENT", new IfBlock()),
	NOTIFBLOCK("IFN", "IF BLOCK", new BooleanIfBlock(false)),								// Not If Block - Executres the next line if conditin is false
	GRAPHICSTEST("GRT", "GRAPHICS TEST", new GraphicsExecutor(GraphicsTask.init)),
	WAIT("WAIT", "WAIT", new Wait()),
	SETGRAPHICSSIZE("GRASS", "GRAPHICS SET SIZE", new GraphicsExecutor(GraphicsTask.setSize)),
	TESTDRAW("TDRAW", "", new GraphicsExecutor(GraphicsTask.draw)),
	CLEAR("CLEAR", "", new GraphicsExecutor(GraphicsTask.clear)),
	SETGRAPHICSPROPERTY("GRAPHICSPROPERTY", "", new GraphicsExecutor(GraphicsTask.setProperty)),
	ADDLISTENER("ADDLISTENER", "", new Listener()),
	CLEARLAST("CLEARLAST", "", new GraphicsExecutor(GraphicsTask.clearLast)),
	ADDPROCESS("ADDPROCESS", "", new ChangeProcess()),
	RETURNPROCESS("RETPRO", "", new ReturnProcess()),
	WHILE("WHL", "", new While()),
	ROUND("ROUND", "", new Round()),
	FLOOR("FLOOR", "", new Floor()),
	CEILING("CEIL", "", new Ceiling());
	
	
	private Instruction(final String ins, final String name, final InstructionExecutor executor){
		this.instruction = ins;
		this.name = name;
		this.executor = executor;	
	}
	
	public final InstructionExecutor executor;
	public final String instruction;
	public final String name;

}
