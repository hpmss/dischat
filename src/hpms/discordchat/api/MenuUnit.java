package hpms.discordchat.api;

import org.bukkit.entity.Player;

public interface MenuUnit {
	
	void open(Player player);
	
	void close(Player player);
	
	int getSize();
	
	String getName();
	
	boolean isEditable();
}
