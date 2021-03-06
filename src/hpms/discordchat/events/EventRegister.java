package hpms.discordchat.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.inv.ChannelGUIListener;

public class EventRegister {
	
	public static void registerAllEvents(JavaPlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(new AsyncChatEvent(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new InteractEvent(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new LoggingEvent(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryEvent(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new ChannelGUIListener(), plugin);
	}

}
