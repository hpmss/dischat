package hpms.discordchat.chat;

import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.events.InventoryEvent;
import hpms.discordchat.inv.InventoryLinker;

public class DiscordChat extends JavaPlugin{
	
	public static JavaPlugin plugin;
	
	public void onEnable() {
		initDiscordChat();
		InventoryLinker.deleteInventoryLinker("Market");
	}
	
	public void onDisable() {
		saveData();
		saveDefaultConfig();
	}
	
	public void initDiscordChat() {
		plugin = this;
		ChannelAPI.initChannelAPI(this);
		OnCommand command = new OnCommand();
		this.getServer().getPluginManager().registerEvents(new EventListener(),this);
		this.getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
		this.getCommand("discordchat").setExecutor(command);
		this.getCommand("flush").setExecutor(command);
	}
	
	public void saveData() {
		ChannelAPI.saveData();
	}
	
}
