package in.blazingk.blz.packagemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;

public class ImportPackageInstruction implements InstructionExecutor{
	private static File packageDirectory;
	
	@Override
	public Value run(String line) {

		return null;
	}
	
	public File getRunningDirectory(){
		return new File(ClassLoader.getSystemClassLoader().getResource(".").getPath()).getParentFile();
	}
	
	public File findPackageDirectory() throws FileNotFoundException {
		try {
			return getEnvironmentPackageDirectory();
		}catch(Exception e) {
		}
		if (ImportPackageInstruction.packageDirectory == null){
			File dir = getRunningDirectory();
			for (File f : listFileTree(dir,0)){
				if (f.getName().toLowerCase().equals("packages")){
					ImportPackageInstruction.packageDirectory = f;
					return f;
				}
			}
			throw new FileNotFoundException("Could not find Packages directory in "+dir);
		}else{
			return ImportPackageInstruction.packageDirectory;
		}
	}
	
	private File getEnvironmentPackageDirectory() {
		String PackageDirectory = System.getenv("BLZPACKAGES");
		return new File(PackageDirectory);
	}

	public File findPackage(String packageName) throws FileNotFoundException{
		File dir = findPackageDirectory();
		for (File f : dir.listFiles()){
			if (f.getName().equals(packageName)){
				return f;
			}
		}
		throw new FileNotFoundException("Could not find the package "+packageName+" in "+dir);
	}
	
	public Collection<File> listFileTree(File dir, int depth) {
		if (depth > 4){
			return new HashSet<File>();
		}
	    Set<File> fileTree = new HashSet<File>();
	    if(dir==null||dir.listFiles()==null){
	        return fileTree;
	    }
	    for (File entry : dir.listFiles()) {
	        fileTree.add(entry);
	    	if (!entry.isFile()){ 
	    		fileTree.addAll(listFileTree(entry, depth + 1));
	    	}
	    }
	    return fileTree;
	}

}
