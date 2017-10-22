package in.blazingk.blz.packagemanager;

public class PackageSettings {
	public String name;
	private int[] version;
	public int getMajorVersion(){
		return version[0];
	}
	
	public String getVersion(){
		String buildingString = "";
		for (int v : version){
			buildingString += v + ".";
		}
		
		return buildingString;
	}
}


