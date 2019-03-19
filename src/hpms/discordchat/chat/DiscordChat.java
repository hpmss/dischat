package hpms.discordchat.chat;

import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.api.ChannelAPI;

public class DiscordChat extends JavaPlugin{
	
	public static JavaPlugin plugin;
	
	public void onEnable() {
		initDiscordChat();
	}
	
	public void onDisable() {
		saveDefaultConfig();
	}
	
	public void initDiscordChat() {
		plugin = this;
		ChannelAPI.initChannelAPI(this);
		OnCommand command = new OnCommand();
		this.getServer().getPluginManager().registerEvents(new EventListener(),this);
		this.getCommand("discordchat").setExecutor(command);
		this.getCommand("flush").setExecutor(command);
	}
	
}
