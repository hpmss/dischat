package hpms.discordchat.chat;

import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.events.EventRegister;
import hpms.discordchat.inv.InventoryLinker;

public class DiscordChat extends JavaPlugin{
	
	public static JavaPlugin plugin;
	
	public void onEnable() {
		initDiscordChat();
	}
	
	public void onDisable() {
		saveData();
		saveDefaultConfig();
		cleanUpDiscordChat();
	}
	
	public void initDiscordChat() {
		plugin = this;
		ChannelAPI.initChannelAPI(this);
		EventRegister.registerAllEvents(this);
		OnCommand command = new OnCommand();
		this.getCommand("discordchat").setExecutor(command);
		this.getCommand("flush").setExecutor(command);
	}
	
	public void cleanUpDiscordChat() {
		InventoryLinker.deleteInventoryLinkers();
	}
	
	public void saveData() {
		ChannelAPI.saveData();
	}
	
}
