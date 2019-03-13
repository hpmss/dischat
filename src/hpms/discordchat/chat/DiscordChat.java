package hpms.discordchat.chat;

import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.channel.ChannelHandler;

public class DiscordChat extends JavaPlugin{
	
	public void onEnable() {
		
		initDiscordChat();
	}
	
	public void onDisable() {
		saveDefaultConfig();
	}
	
	public void initDiscordChat() {
		ChannelHandler.initChannelHandler(this);
		this.getServer().getPluginManager().registerEvents(new EventListener(),this);
		this.getCommand("discordchat").setExecutor(new OnCommand());
	}
	
}
