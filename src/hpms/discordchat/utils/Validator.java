package hpms.discordchat.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Validator {
	
	public static boolean isNotNull(Object obj) {
		if(obj != null) {
			return true;
		}
		else {
			throw new IllegalArgumentException("Object is null");
		}
	}
	
	public static boolean isNotNull(Object obj,String message) {
		if(obj != null) {
			return true;
		}else {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static boolean isPlayerOnline(UUID uuid) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		if(player.isOnline()) {
				return true;
		}
		return false;
	}
	
	public static boolean isPlayerExistInServer(UUID uuid) {
		return false;
	}
	
	public static boolean isTrue(boolean prep) {
		if(prep == true) {
			return true;
		}else {
			throw new IllegalArgumentException("Proposition is false");
		}
	}

}
