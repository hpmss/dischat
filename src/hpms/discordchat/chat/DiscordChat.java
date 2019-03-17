package hpms.discordchat.chat;

import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.channel.ChannelHandler;

public class DiscordChat extends JavaPlugin{
	
	public static JavaPlugin plugin;
	
	public void onEnable() {
		initDiscordChat();
	}
	
	public void onDisable() {
		saveDefaultConfig();
	}
	
	public void initDiscordChat() {
		ChannelHandler.initChannelHandler(this);
		OnCommand command = new OnCommand();
		plugin = this;
		this.getServer().getPluginManager().registerEvents(new EventListener(),this);
		this.getCommand("discordchat").setExecutor(command);
		this.getCommand("flush").setExecutor(command);
	}
	
}
