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
			{"while",				Instruction.WHILE.instruction}
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
		for (int i = 0; i < originalLines.size(); i++){
			for (int y = 0; y < Translator.replace.length; y++){
				String splits[] = originalLines.get(i).split(" ");
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
				originalLines.set(i, fin);
			}
			if (!originalLines.get(i).contains("call") && originalLines.get(i).contains("if") && originalLines.get(i).contains("==")){
				String[] splits = originalLines.get(i).split("==");
				String b = splits[1].trim();
				String a = splits[0].split(" ")[1].trim();
				originalLines.set(i, Instruction.IFBLOCK.instruction+" "+a+" "+b);
			}
			if (!originalLines.get(i).contains("call") && originalLines.get(i).contains("if") && originalLines.get(i).contains("~=")){
				String[] splits = originalLines.get(i).split("~=");
				String b = splits[1].trim();
				String a = splits[0].split(" ")[1].trim();
				originalLines.set(i, Instruction.NOTIFBLOCK.instruction+" "+a+" "+b);
			}
			if (originalLines.get(i).contains("call") && originalLines.get(i).contains(" if ") && originalLines.get(i).contains("==")){
				String[] splitsa = originalLines.get(i).split(" if ");
				String[] splitsb = splitsa[1].split("==");
				String a = splitsb[1].trim();
				String c = splitsb[0].trim();
				String b = splitsa[0].split(" ")[1];
				originalLines.set(i, Instruction.EQUALSRETURNJUMP.instruction+" "+b+" "+c+" "+a );
			}
			if (originalLines.get(i).contains("call") && originalLines.get(i).contains(" if ") && originalLines.get(i).contains("~=")){
				String[] splitsa = originalLines.get(i).split(" if ");
				String[] splitsb = splitsa[1].split("~=");
				String a = splitsb[1].trim();
				String c = splitsb[0].trim();
				String b = splitsa[0].split(" ")[1];
				originalLines.set(i, Instruction.NOTEQUALSRETURNJUMP.instruction+" "+b+" "+c+" "+a );
			}
			if (originalLines.get(i).contains("call") && originalLines.get(i).contains(" if ") && originalLines.get(i).contains("<")){
				String[] splitsa = originalLines.get(i).split(" if ");
				String[] splitsb = splitsa[1].split("<");
				String a = splitsb[1].trim();
				String c = splitsb[0].trim();
				String b = splitsa[0].split(" ")[1];
				originalLines.set(i, Instruction.LESSTHANRETURNJUMP.instruction+" "+b+" "+c+" "+a );
			}
			if (originalLines.get(i).contains("call") && originalLines.get(i).contains(" if ") && originalLines.get(i).contains(">")){
				String[] splitsa = originalLines.get(i).split(" if ");
				String[] splitsb = splitsa[1].split(">");
				String a = splitsb[1].trim();
				String c = splitsb[0].trim();
				String b = splitsa[0].split(" ")[1];
				originalLines.set(i, Instruction.MORETHANRETURNJUMP.instruction+" "+b+" "+c+" "+a );
			}
			String spl[] = originalLines.get(i).split(" ");
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
			originalLines.set(i, fin);
			if (originalLines.get(i).contains("+") && originalLines.get(i).contains("=")){
				String[] splitsa = originalLines.get(i).split("=");
				String a = splitsa[0].trim();
				String[] splitsb = splitsa[1].split("\\+");
				String b = splitsb[0].trim();
				String c = splitsb[1].trim();
				originalLines.set(i, Instruction.ADDVARIABLE.instruction+" "+b+" "+c+" "+a );
			}
			if (originalLines.get(i).contains("-") && originalLines.get(i).contains("=")){
				if (!originalLines.get(i).contains("*") && !originalLines.get(i).contains("/") && !originalLines.get(i).contains("+")){
					String[] splitsa = originalLines.get(i).split("=");
					String a = splitsa[0].trim();
					String[] splitsb = splitsa[1].split("\\-");
					String b = splitsb[0].trim();
					String c = splitsb[1].trim();
					originalLines.set(i, Instruction.SUBTRACTVARIABLE.instruction+" "+b+" "+c+" "+a );
				}
			}
			if (originalLines.get(i).contains("*") && originalLines.get(i).contains("=")){
				String[] splitsa = originalLines.get(i).split("=");
				String a = splitsa[0].trim();
				String[] splitsb = splitsa[1].split("\\*");
				String b = splitsb[0].trim();
				String c = splitsb[1].trim();
				originalLines.set(i, Instruction.MULTIPLYVARIABLE.instruction+" "+b+" "+c+" "+a );
			}
			if (originalLines.get(i).contains("/") && originalLines.get(i).contains("=")){
				String[] splitsa = originalLines.get(i).split("=");
				String a = splitsa[0].trim();
				String[] splitsb = splitsa[1].split("/");
				String b = splitsb[0].trim();
				String c = splitsb[1].trim();
				originalLines.set(i, Instruction.DIVIDEVARIABLE.instruction+" "+b+" "+c+" "+a );
			}
			if (originalLines.get(i).contains("^") && originalLines.get(i).contains("=")){
				String[] splitsa = originalLines.get(i).split("=");
				String a = splitsa[0].trim();
				String[] splitsb = splitsa[1].split("\\^");
				String b = splitsb[0].trim();
				String c = splitsb[1].trim();
				originalLines.set(i, Instruction.POWVARIABLE.instruction+" "+b+" "+c+" "+a );

			}
			if (originalLines.get(i).contains("++")){
				String[] splits = originalLines.get(i).split("\\+");
				String a = splits[0].trim();
				originalLines.set(i, Instruction.INCREMENT.instruction+" "+a);
			}
			if (originalLines.get(i).contains("--")){
				String[] splits = originalLines.get(i).split("\\-");
				String a = splits[0].trim();
				originalLines.set(i, Instruction.DECREMENT.instruction+" "+a);
			}
			if (originalLines.get(i).contains("=")){
				String[] splits = originalLines.get(i).split("=");
				String a = splits[0].trim();
				String b = splits[1].trim();
				originalLines.set(i, Instruction.STORE.instruction+" "+a+" "+b);
			}
		}

		File finalFile = new File(filePath);
		if (finalFile.exists()){
			finalFile.delete();
		}
		finalFile.createNewFile();

		FileWriter fw = new FileWriter(finalFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < originalLines.size(); i++){
			bw.write(originalLines.get(i));
			bw.newLine();
		}
		bw.close();
	}

}
