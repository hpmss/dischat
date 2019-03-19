package hpms.discordchat.inv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import hpms.discordchat.api.MenuUnit;

public class ConfigMenu implements MenuUnit{
	
	private String name;
	private int size;
	private Inventory inv;
	
	public ConfigMenu(String name,int size) {
		this.name = name;
		this.size = size;
		this.inv = Bukkit.createInventory(null,size,name);
	}
	
	public int getSize() {
		return this.size;
	}

	public String getName() {
		return this.name;
	}

	public void open(Player player) {
		player.openInventory(inv);
	}

	public void close(Player player) {
		player.closeInventory();
	}

	public boolean isEditable() {
		return false;
	}

}
