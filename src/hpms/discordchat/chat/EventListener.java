package hpms.discordchat.chat;

import java.util.Set;

import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelHandler;

public class EventListener implements Listener{
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		Set<Player> recipient = e.getRecipients();
		String channelName = ChannelHandler.getPlayerCurrentChannel(e.getPlayer());
		Channel channel = ChannelHandler.getChannel(channelName);
		e.setFormat("[" + channelName + "]" + "[%1$s] %2$s");
		Log.info(channel.getLeader());
	}

}
