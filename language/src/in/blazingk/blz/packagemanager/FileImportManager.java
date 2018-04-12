package in.blazingk.blz.packagemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import java.util.HashMap;

public class FileImportManager {
	
	public static HashMap<Path, Process> importedFiles = new HashMap<Path, Process>();
	
	public static Process importFile(Path path) {
		if (importedFiles.containsKey(path)) {
			return importedFiles.get(path);
		}
		try {
			Process process = new Process(path, false);
			importedFiles.put(path, process);
			/* import stuff for the package */
			return process;
		}catch(FileNotFoundException e) {
			Interpreter.throwError("Could not find file at path: "+path);
			return null;
		}
	}

	public static void printPackageInstructionHelp(){
		System.out.println("blz -p[ackages] COMMAND");
		System.out.println("Current commands:");
		System.out.println("list - list all installed packages");
		System.out.println("help - prints this message");
	}

	public static void handlePackageInstruction(String[] argv){
		String instruction = "";
		for (int i = 0; i < argv.length - 1; i++){
			String cArg = argv[i].toLowerCase();
			if (cArg.equals("-p") || cArg.equals("-package")){
				instruction = argv[i + 1].toLowerCase();
			}
		}
		if (instruction.equals("list")){
			System.out.println("Installed Packages:");
			ImportPackageInstruction instr = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
			try{
				instr.listPackages().forEach((Path p) ->
					System.out.println(p.getFileName())
				);
			}catch(IOException exception){
				System.err.println("Failed to list packages");
				exception.printStackTrace();
			}
		}else if(instruction.equals("help")){
			printPackageInstructionHelp();
		}else{
			printPackageInstructionHelp();
		}
	}

}
