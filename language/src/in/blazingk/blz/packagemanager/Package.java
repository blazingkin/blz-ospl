package in.blazingk.blz.packagemanager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
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
				Process fileProcess = FileImportManager.importFile(f);
				processes.put(fileName.replace(".blz", ""), fileProcess);
			}
		}
	}
	
	public Collection<MethodNode> getAllMethodsInPackage(){
		Set<MethodNode> methods = new HashSet<MethodNode>();
		for (Process p : processes.values()){
			methods.addAll(p.methods);
		}
		return methods;
	}
	
	public Collection<Constructor> getAllConstructorsInPackage(){
		Set<Constructor> constructors = new HashSet<Constructor>();
		for (Process p : processes.values()){
			constructors.addAll(p.constructors);
		}
		return constructors;
	}
	
	public Collection<MethodNode> getAllMethodsInProcess(String processName) throws IllegalArgumentException{
		if (processes.containsKey(processName)){
			Set<MethodNode> methods = new HashSet<MethodNode>();
			for (MethodNode m : processes.get(processName).methods){
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
		for (MethodNode m : corePackage.getAllMethodsInPackage()){
			try {
				String fileName = m.parent.readingFrom.getName();
				if (fileName.equals("ArrayUtil.blz")) {
					VariableTypes.primitiveContexts.get(VariableTypes.Array).setValue(m.getStoreName(), Value.method(m));
				}else if (fileName.equals("StringUtil.blz")) {
					VariableTypes.primitiveContexts.get(VariableTypes.String).setValue(m.getStoreName(), Value.method(m));
				}else if (fileName.equals("NilUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Nil).setValue(m.getStoreName(), Value.method(m));
				}else if (fileName.equals("NumberUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Integer).setValue(m.getStoreName(), Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Rational).setValue(m.getStoreName(), Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Double).setValue(m.getStoreName(), Value.method(m));
				}else if (fileName.equals("MethodUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Method).setValue(m.getStoreName(), Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Constructor).setValue(m.getStoreName(), Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.Closure).setValue(m.getStoreName(), Value.method(m));
					VariableTypes.primitiveContexts.get(VariableTypes.PrimitiveMethod).setValue(m.getStoreName(), Value.method(m));
				}else if (fileName.equals("BooleanUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Boolean).setValue(m.getStoreName(), Value.method(m));
				}else if (fileName.equals("HashUtil.blz")){
					VariableTypes.primitiveContexts.get(VariableTypes.Hash).setValue(m.getStoreName(), Value.method(m));
				}
				else{
					Variable.getGlobalContext().setValue(m.getStoreName(), Value.method(m));
				}
			}catch(NullPointerException e) {
				Variable.getGlobalContext().setValue(m.getStoreName(), Value.method(m));
			}
		}
	}
}

