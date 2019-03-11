package hpms.discordchat.utils;

public class Validator {
	
	public static boolean isNotNull(Object obj) {
		if(!obj.equals(null)) {
			return true;
		}
		else {
			throw new IllegalArgumentException("Object is null");
		}
	}
	
	public static boolean isTrue(boolean prep) {
		if(prep == true) {
			return true;
		}else {
			throw new IllegalArgumentException("Proposition is false");
		}
	}

}
