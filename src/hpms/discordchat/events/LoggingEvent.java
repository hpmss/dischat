package hpms.discordchat.events;

import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.data.ChannelData;
import hpms.discordchat.inv.InventoryLinker;

public class LoggingEvent implements Listener {
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Log.info(e.getPlayer().getName());
		if(ChannelAPI.getPlayerCurrentChannel(e.getPlayer().getUniqueId()) == null) {
			ChannelAPI.joinChannel(e.getPlayer().getUniqueId(), ChannelData.DEFAULT_CHANNEL);
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		InventoryLinker.removePlayerSharingInventory(ChannelAPI.getPlayerCurrentChannelName(e.getPlayer().getUniqueId()), e.getPlayer());
	}
}
