package hpms.discordchat.api;

import org.bukkit.inventory.Inventory;

public abstract class MenuAPI {
	private static MenuAPI api;
	
	public static MenuAPI getAPI() {
		return api;
	}
	
	public static void setAPI(MenuAPI menu) {
		menu = api;
	}
	
	public abstract Inventory createNewMenu(int size);
}
