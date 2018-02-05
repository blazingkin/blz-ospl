package in.blazingk.blz.packagemanager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
	private Path packageDirectory;
	private HashMap<String, Process> processes; // Key is process name
	public PackageSettings settings;
	
	public Package(Path directory) throws IOException{
		packageDirectory = directory;
		processes = new HashMap<String, Process>();
		// loadSettings
		for (Path f : listFileTree(packageDirectory)){
			String fileName = f.getFileName().toString();
			String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
			if (extension.toLowerCase().equals("blz")){
				Process fileProcess = new Process(f.toFile());
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
	
	
	public Collection<Path> listFileTree(Path dir) {
	    Set<Path> fileTree = new HashSet<Path>();
	    try {
	    	DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
	    	for (Path entry : stream) {
	    		fileTree.addAll(listFileTree(entry));
	    	}
	    }catch(IOException io) {
	    	/* Not directory */
	    }
	    fileTree.add(dir);
	    return fileTree;
	}
	
	public static void importCore() throws Exception {
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		Path coreFolder = importer.findPackage("Core");
		Package corePackage = new Package(coreFolder);
		for (Method m : corePackage.getAllMethodsInPackage()){
			try {
				String fileName = m.parent.readingFrom.getName();
				if (fileName.equals("ArrayUtil.blz")) {
					VariableTypes.primitiveContexts.get(VariableTypes.Array).setValue(m.functionName, Value.method(m));
				}else if (fileName.equals("StringUtil.blz")) {
					VariableTypes.primitiveContexts.get(VariableTypes.String).setValue(m.functionName, Value.method(m));
				}else if (fileName.equals("NilUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Nil).setValue(m.functionName, Value.method(m));
				}else if (fileName.equals("NumberUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Integer).setValue(m.functionName, Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Rational).setValue(m.functionName, Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Double).setValue(m.functionName, Value.method(m));
				}else if (fileName.equals("MethodUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Method).setValue(m.functionName, Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Constructor).setValue(m.functionName, Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Closure).setValue(m.functionName, Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.PrimitiveMethod).setValue(m.functionName, Value.method(m));
				}else if (fileName.equals("BooleanUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Boolean).setValue(m.functionName, Value.method(m));
				}else if (fileName.equals("HashUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Hash).setValue(m.functionName, Value.method(m));
				}
				else{
					Variable.getGlobalContext().setValue(m.functionName, new Value(VariableTypes.Method, m));
				}
			}catch(NullPointerException e) {
				Variable.getGlobalContext().setValue(m.functionName, new Value(VariableTypes.Method, m));
			}
		}
	}
}

