package hpms.discordchat.chat;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelHandler;

public class EventListener implements Listener{
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		Set<Player> recipient = e.getRecipients();
		Channel channel = ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId());
		e.setFormat("[" + ChatColor.translateAlternateColorCodes('&',channel.getChannelChatPrefix()) + "]" + "[%1$s] %2$s");
		Log.info(channel.getLeader());
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId()) == null) {
			Log.info("No current channel");
			ChannelHandler.joinChannel(e.getPlayer(), "Global");
		}
	}

}
