package hpms.discordchat.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.chat.DiscordChat;
import net.md_5.bungee.api.ChatColor;

public class ShortermRequest {
	
	private int id;
	
	private Player receiver;
	
	private boolean isSent = false;
	
	public ShortermRequest(JavaPlugin plugin,Player requester,Player receiver) {
		this.receiver = receiver;
		if(this.receiver.hasMetadata("request")) {
			String uuid = this.receiver.getMetadata("request").get(0).value().toString();
			if(uuid.equalsIgnoreCase(requester.getUniqueId().toString())) {
				requester.sendMessage(ChatColor.YELLOW + "Request was already issued.");
				this.isSent = true;
				return;
			}
		}
		this.receiver.setMetadata("request",new FixedMetadataValue(plugin,requester.getUniqueId().toString()));
		this.request(plugin);
		this.receiver.sendMessage(ChatColor.YELLOW + "A request has been sent to you by " + requester.getDisplayName());
	}
	
	public boolean isSent() {
		return this.isSent;
	}
	
	public static ShortermRequest createRequest(Player requester,Player receiver) {
		return new ShortermRequest(DiscordChat.plugin,requester,receiver);
	}
	
	public static void acceptRequest(Player receiver,String form) {
	
		if(receiver.hasMetadata("request")) {
			Player requester = Bukkit.getPlayer(UUID.fromString(receiver.getMetadata("request").get(0).value().toString()));
			if(Validator.isPlayerOnline(requester.getUniqueId())) {
				if(form.equalsIgnoreCase("teleport")) {
					requester.teleport(receiver.getLocation());
				}
				else if(form.equalsIgnoreCase("inventory")) {
					
				}
				receiver.removeMetadata("request", DiscordChat.plugin);
			}					
		}
	}
	
	private void request(JavaPlugin plugin) {
		this.id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			private int second = 0;
			public void run() {
				second++;
				if(!receiver.hasMetadata("request")) {
					Bukkit.getServer().getScheduler().cancelTask(id);
				}
				if(second == 10) {
					receiver.removeMetadata("request", plugin);
					Bukkit.getServer().getScheduler().cancelTask(id);
				}
			}
		}, 20, 20);
	}
	
}
