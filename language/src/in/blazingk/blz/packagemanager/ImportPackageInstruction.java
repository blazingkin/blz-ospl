package in.blazingk.blz.packagemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;

public class ImportPackageInstruction implements InstructionExecutor{
	private static Path packageDirectory;
	
	@Override
	public Value run(String line) {

		return null;
	}
	
	public Path getRunningDirectory(){
		try {
			URL cwd = ImportPackageInstruction.class.getProtectionDomain().getCodeSource().getLocation();
			return Paths.get(cwd.toURI()).getParent().getParent();
		} catch (URISyntaxException e) {
			// If you can tell me a system that doesn't define . I'll buy you a drink
			e.printStackTrace();
		}
		return null;
	}
	
	public Path findPackageDirectory() throws IOException {
		try {
			return getEnvironmentPackageDirectory();
		}catch(Exception e) {
		}
		if (ImportPackageInstruction.packageDirectory == null){
			Path dir = getRunningDirectory();
			for (Path f : Files.newDirectoryStream(dir)){
				if (f.getFileName().toString().toLowerCase().equals("packages")){
					ImportPackageInstruction.packageDirectory = f;
					return f;
				}
			}
			throw new FileNotFoundException("Could not find Packages directory in "+dir);
		}else{
			return ImportPackageInstruction.packageDirectory;
		}
	}
	
	private Path getEnvironmentPackageDirectory() {
		String PackageDirectory = System.getenv("BLZPACKAGES");
		return Paths.get(PackageDirectory);
	}

	public Path findPackage(String packageName) throws IOException {
		Path dir = findPackageDirectory();
		for (Path f : Files.newDirectoryStream(dir)){
			if (f.getFileName().toString().equals(packageName)){
				return f;
			}
		}
		throw new FileNotFoundException("Could not find the package "+packageName+" in "+dir);
	}
	
	public DirectoryStream<Path> listPackages() throws IOException {
		return Files.newDirectoryStream(findPackageDirectory());
	}

	public Collection<Path> listFileTree(Path dir) {
	    Set<Path> fileTree = new HashSet<Path>();
	    try {
	    	DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
	    	for (Path entry : stream) {
	    		fileTree.addAll(listFileTree(entry));
	    	}
	    }catch(IOException io) {
	    	Interpreter.throwError("IO Exception: "+io.getMessage());
	    }
	    fileTree.add(dir);
	    return fileTree;
	}

}
