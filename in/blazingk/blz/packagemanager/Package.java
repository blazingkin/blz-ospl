package in.blazingk.blz.packagemanager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.sourcestructures.Method;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Package {
	private File packageDirectory;
	private HashMap<String, Process> processes; // Key is process name
	public PackageSettings settings;
	
	public Package(File directory) throws IOException{
		packageDirectory = directory;
		processes = new HashMap<String, Process>();
		// loadSettings
		for (File f : listFileTree(packageDirectory)){
			String fileName = f.getName();
			String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
			if (extension.toLowerCase().equals("blz")){
				Process fileProcess = new Process(f, !packageDirectory.getName().toLowerCase().equals("core"));
				processes.put(fileName.replace(".blz", ""), fileProcess);
			}
		}
	}
	
	public Collection<Method> getAllMethodsInPackage(){
		Set<Method> methods = new HashSet<Method>();
		for (Process p : processes.values()){
			for (Method m : p.methods){
				methods.add(m);
			}
		}
		return methods;
	}
	
	public Collection<Method> getAllMethodsInProcess(String processName) throws IllegalArgumentException{
		if (processes.containsKey(processName)){
			Set<Method> methods = new HashSet<Method>();
			for (Method m : processes.get(processName).methods){
				methods.add(m);
			}
			return methods;
		}else{
			throw new IllegalArgumentException("Could not find a package element with the name "+processName+" in "+packageDirectory);
		}
	}
	
	
	// Shamelessly used from StackOverflow
	public Collection<File> listFileTree(File dir) {
	    Set<File> fileTree = new HashSet<File>();
	    if(dir==null||dir.listFiles()==null){
	        return fileTree;
	    }
	    for (File entry : dir.listFiles()) {
	        if (entry.isFile()) fileTree.add(entry);
	        else fileTree.addAll(listFileTree(entry));
	    }
	    return fileTree;
	}
	
	public static void importCore() throws Exception {
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		File coreFolder = importer.findPackage("Core");
		Package corePackage = new Package(coreFolder);
		for (Method m : corePackage.getAllMethodsInPackage()){
			Variable.getGlobalContext().setValue(m.functionName, new Value(VariableTypes.Method, m));
		}
	}
}

