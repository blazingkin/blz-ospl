package in.blazingk.blz.packagemanager;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import com.blazingkin.interpreter.Interpreter;
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
			process.handleImports();
			return process;
		}catch(FileNotFoundException e) {
			Interpreter.throwError("Could not find file at path: "+path);
			return null;
		}
	}

}
