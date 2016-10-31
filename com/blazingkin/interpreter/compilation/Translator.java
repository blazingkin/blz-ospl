package com.blazingkin.interpreter.compilation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Instruction;

public class Translator {
		
		//This goes through and replaces all lines with ones that are readable by the executor
	final static String[][] replace = 
		{
			{"function",			":"},
			{"goto", 				Instruction.JUMP.instruction},
			{"print", 				Instruction.ECHO.instruction},
			{"inputnum", 			Instruction.NUMINPUT.instruction},
			{"exit",				Instruction.EXIT.instruction},
			{"copy",				Instruction.MOVE.instruction},
			{"done", 				Instruction.END.instruction},
			{"rand",				Instruction.RANDOM.instruction},
			{"setstr",				Instruction.STRINGSET.instruction},
			{"strin",				Instruction.STRINGINPUT.instruction},
			{"absvalue",			Instruction.UNSIGN.instruction},
			{"remainder",			Instruction.MODULUS.instruction},
			{"push",				Instruction.PUSH.instruction},
			{"pop",					Instruction.POP.instruction},
			{"peek",				Instruction.PEEK.instruction},
			{"concatenate",			Instruction.CONCATENATE.instruction},
			{"drawpoly",			Instruction.TESTDRAW.instruction},
			{"clear",				Instruction.CLEAR.instruction},
			{"initgraphics",		Instruction.GRAPHICSTEST.instruction},
			{"wait",				Instruction.WAIT.instruction},
			{"setgraphicssize",		Instruction.SETGRAPHICSSIZE.instruction},
			{"setgraphicsproperty",	Instruction.SETGRAPHICSPROPERTY.instruction},
			{"addlistener",			Instruction.ADDLISTENER.instruction},
			{"clearlast",			Instruction.CLEARLAST.instruction},
			{"return",				Instruction.RETURNPROCESS.instruction},
			{"while",				Instruction.WHILE.instruction},
			{"endloop",				Instruction.ENDLOOP.instruction}
		};
	
	public static void run(File f, List<String> args) throws Exception{
		List<String> originalLines = new ArrayList<String>();
		if (args.get(0) == null){
			Interpreter.throwError("Could not find compile location, please use the format -c SOURCE DESTINATION");
		}
		String filePath = args.get(0);
		Scanner s = new Scanner(f);
		while (s.hasNextLine()){
			originalLines.add(s.nextLine().replace("\t", "").split(";")[0]);
		}
		s.close();
		ArrayList<String> newLine = new ArrayList<String>();
		for (int i = 0; i < originalLines.size(); i++){
			ArrayList<String> replacedLines = parseLine(originalLines.get(i));
			for (Object str: replacedLines.toArray()){
				newLine.add((String)str);
			}
		}
		File finalFile = new File(filePath);
		if (finalFile.exists()){
			finalFile.delete();
		}
		finalFile.createNewFile();

		FileWriter fw = new FileWriter(finalFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < newLine.size(); i++){
			bw.write(newLine.get(i));
			bw.newLine();
		}
		bw.close();
	}
	
	
	public static ArrayList<String> parseLine(String line){
		ArrayList<String> newLine = new ArrayList<String>();
		
		for (int y = 0; y < Translator.replace.length; y++){ // This fixes everything in the array at the top
			String splits[] = line.split(" ");
			if (splits[0].equals(Translator.replace[y][0])){
				splits[0] = Translator.replace[y][1];
			}
			String fin = "";
			for (int z = 0; z < splits.length; z++){
				fin = fin + splits[z];
				if (z+1 != splits.length && !splits[z].equals(":")){
					fin = fin + " ";
				}
			}
			line = fin;
		}
		//If it uses print, we dont want to parse it anymore
		if (line.contains(Instruction.ECHO.instruction) || line.contains(Instruction.SAMELINEECHO.instruction)){
			newLine.add(line);
			return newLine;
		}
		//Things of the form: for init, term, loop
		//Example: for a=0, |a|<20, a++
		//This reinterprets all of the arguments of the for function
		if (line.contains("for")){
			line = line.replace("(", "");
			line = line.replace(")", "");
			String[] splits = line.split(",");
			String tmpString = "";
			for (int i = 1; i < splits[0].split(" ").length; i++){
				tmpString = tmpString + splits[0].split(" ")[i];
			}
			splits[0] = tmpString.trim();
			splits[0] = parseLine(splits[0]).get(0);
			splits[2] = parseLine(splits[2]).get(0);
			newLine.add(Instruction.FORLOOP.instruction+" "+splits[0]+","+splits[1]+","+splits[2]);
			return newLine;
		}
		//Things of the form sigma varname, bot, top, instr
		//Example sigma a, i=1, 30, 5 
		if (line.contains("sigma")){
			String[] spacesplit = line.split(" ");
			String noarg = "";
			for (int i = 1; i < spacesplit.length; i++){
				noarg = noarg + spacesplit[i];
			}
			String[] csv = noarg.split(",");
			String varName = csv[0].trim();
			String initialiser = csv[1].trim();
			String fin = csv[2].trim();
			String instr = csv[3].trim();
			newLine.add(Instruction.STORE.instruction+" "+varName+" 0");
			String jnkVarName = initialiser.split("=")[0];
			initialiser = parseLine(initialiser).get(0);
			newLine.add(Instruction.FORLOOP.instruction+" "+initialiser+",|"+jnkVarName+"| <= "+fin+","+
			Instruction.INCREMENT.instruction+" "+jnkVarName);
			newLine.add(Instruction.ADDVARIABLE.instruction+" "+instr+" |"+varName+"| "+varName);
			newLine.add(Instruction.ENDLOOP.instruction);
			return newLine;
		}
		//Things of the form: if a == b
		if (!line.contains("call") && line.contains("if") && line.contains("==")){
			String[] splits = line.split("==");
			String b = splits[1].trim();
			String a = splits[0].split(" ")[1].trim();
			newLine.add(Instruction.IFBLOCK.instruction+" "+a+" "+b);
			return newLine;
		}
		//Things of the form: if a != b
		if (!line.contains("call") && line.contains("if") && line.contains("!=")){
			String[] splits = line.split("!=");
			String b = splits[1].trim();
			String a = splits[0].split(" ")[1].trim();
			newLine.add(Instruction.NOTIFBLOCK.instruction+" "+a+" "+b);
			return newLine;
		}
		//Things of the form: call f if a == b
		if (line.contains("call") && line.contains(" if ") && line.contains("==")){
			String[] splitsa = line.split(" if ");
			String[] splitsb = splitsa[1].split("==");
			String a = splitsb[1].trim();
			String c = splitsb[0].trim();
			String b = splitsa[0].split(" ")[1];
			newLine.add(Instruction.EQUALSRETURNJUMP.instruction+" "+b+" "+c+" "+a);
			return newLine;
		}
		//Things of the form: call f if a != b
		if (line.contains("call") && line.contains(" if ") && line.contains("!=")){
			String[] splitsa = line.split(" if ");
			String[] splitsb = splitsa[1].split("!=");
			String a = splitsb[1].trim();
			String c = splitsb[0].trim();
			String b = splitsa[0].split(" ")[1];
			newLine.add(Instruction.NOTEQUALSRETURNJUMP.instruction+" "+b+" "+c+" "+a);
			return newLine;
		}
		//Things of the form: call f if a < b
		if (line.contains("call") && line.contains(" if ") && line.contains("<")){
			String[] splitsa = line.split(" if ");
			String[] splitsb = splitsa[1].split("<");
			String a = splitsb[1].trim();
			String c = splitsb[0].trim();
			String b = splitsa[0].split(" ")[1];
			newLine.add(Instruction.LESSTHANRETURNJUMP.instruction+" "+b+" "+c+" "+a);
			return newLine;
		}
		//Things of the form: call f is a > b
		if (line.contains("call") && line.contains(" if ") && line.contains(">")){
			String[] splitsa = line.split(" if ");
			String[] splitsb = splitsa[1].split(">");
			String a = splitsb[1].trim();
			String c = splitsb[0].trim();
			String b = splitsa[0].split(" ")[1];
			newLine.add(Instruction.MORETHANRETURNJUMP.instruction+" "+b+" "+c+" "+a);
			return newLine;
		}
		//Things of the form: call f
		//Also works for things of the form: call f (a,b,c)
		if (line.contains("call")){
			String spl[] = line.split(" ");
			if (spl[0].equals("call")){
				spl[0] = Instruction.RETURNJUMP.instruction;
			}
			String fin = "";
			for (int z = 0; z < spl.length; z++){
				fin = fin + spl[z];
				if (z+1 != spl.length){
					fin = fin + " ";	
				}
			}
			newLine.add(fin);
			return newLine;
		}
		//Things of the form: a = b + c
		if (line.contains("+") && line.contains("=")){
			String[] splitsa = line.split("=");
			String a = splitsa[0].trim();
			String[] splitsb = splitsa[1].split("\\+");
			String b = splitsb[0].trim();
			String c = splitsb[1].trim();
			newLine.add(Instruction.ADDVARIABLE.instruction+" "+b+" "+c+" "+a );
			return newLine;
		}
		//Things of the form: a = b - c
		if (line.contains("-") && line.contains("=")){
			if (!line.contains("*") && !line.contains("/") && !line.contains("+")){
				String[] splitsa = line.split("=");
				String a = splitsa[0].trim();
				String[] splitsb = splitsa[1].split("\\-");
				String b = splitsb[0].trim();
				String c = splitsb[1].trim();
				newLine.add(Instruction.SUBTRACTVARIABLE.instruction+" "+b+" "+c+" "+a );
				return newLine;
			}
		}
		//Things of the form: a = b * c
		if (line.contains("*") && line.contains("=") && !line.contains(",") 
				&& !(line.charAt(0) == '*' && line.split("\\*").length == 2)){
			String[] splitsa = line.split("=");
			String a = splitsa[0].trim();
			String[] splitsb = splitsa[1].split(" \\* ");
			String b = splitsb[0].trim();
			String c = splitsb[1].trim();
			newLine.add(Instruction.MULTIPLYVARIABLE.instruction+" "+b+" "+c+" "+a );
			return newLine;
		}
		//Things of the form: a = b / c
		if (line.contains("/") && line.contains("=")){
			String[] splitsa = line.split("=");
			String a = splitsa[0].trim();
			String[] splitsb = splitsa[1].split("/");
			String b = splitsb[0].trim();
			String c = splitsb[1].trim();
			newLine.add(Instruction.DIVIDEVARIABLE.instruction+" "+b+" "+c+" "+a );
			return newLine;
		}
		//Things of the form: a = b ^ c
		if (line.contains("^") && line.contains("=")){
			String[] splitsa = line.split("=");
			String a = splitsa[0].trim();
			String[] splitsb = splitsa[1].split("\\^");
			String b = splitsb[0].trim();
			String c = splitsb[1].trim();
			newLine.add(Instruction.POWVARIABLE.instruction+" "+b+" "+c+" "+a );
			return newLine;
		}
		//Things of the form: a++
		if (line.contains("++")){
			String[] splits = line.split("\\+");
			String a = splits[0].trim();
			newLine.add(Instruction.INCREMENT.instruction+" "+a);
			return newLine;
		}
		//Things of the form: a--
		if (line.contains("--")){
			String[] splits = line.split("\\-");
			String a = splits[0].trim();
			newLine.add(Instruction.DECREMENT.instruction+" "+a);
			return newLine;
		}
		//Things of the form a = b
		if (line.contains("=")){
			String[] splits = line.split("=");
			String a = splits[0].trim();
			String b = splits[1].trim();
			newLine.add(Instruction.STORE.instruction+" "+a+" "+b);
			return newLine;
		}
		
		newLine.add(line);
		return newLine;
	}

}
