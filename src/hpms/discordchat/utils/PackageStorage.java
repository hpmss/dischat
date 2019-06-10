package hpms.discordchat.utils;

import java.util.HashMap;

public final class PackageStorage {

	private String packageName;
	
	private static HashMap<String,Class<?>> storage = new HashMap<String,Class<?>>();
	
	public PackageStorage(String packageName) {
		this.packageName = packageName;
	}
	
	public Class<?> getClass(String className) {
		Class<?> clazz = storage.get(className);
		if(clazz == null) {
			try {
				clazz = Class.forName(packageName + "." + className);
			}catch(ClassNotFoundException e) {
				throw new NullPointerException("Class with package: " + packageName + "." + className + " doesnt exist");
			}
			storage.put(className, clazz);
		}
		return clazz;
	}
	
	public String toString() {
		return packageName;
	}
	
}
