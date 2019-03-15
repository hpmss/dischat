package hpms.discordchat.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelHandler;
import hpms.discordchat.data.ChannelHolder;

public class EventListener implements Listener{
	
	private static String PLAYER_NAME = "%1$s";
	private static String PLAYER_MESSAGE = "%2$s";
	private static String FORMAT = "[" + "%s" + ChatColor.RESET + "]";
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		if(ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId()).getChannelName() != ChannelHolder.DEFAULT_CHANNEL) {
			List<Entity> players = getNearbyPlayers(e.getPlayer(),10,10,10);
			e.getRecipients().clear();
			e.getRecipients().add(e.getPlayer());
			for(Entity p : players) {
				e.getRecipients().add((Player)p);
			}
		}
		
		Channel channel = ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId());
		FORMAT = String.format(FORMAT, ChatColor.translateAlternateColorCodes('&', channel.getChannelChatPrefix()));
		e.setFormat( PLAYER_NAME + ": " + PLAYER_MESSAGE );
		e.setMessage(FORMAT + e.getMessage());
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId()) == null) {
			ChannelHandler.joinChannel(e.getPlayer(), ChannelHolder.DEFAULT_CHANNEL);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		
	}
	
	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent e) {
		
	}
	
	
	public List<Entity> getNearbyPlayers(Player p,double x,double y,double z) {
		List<Entity> entities = p.getNearbyEntities(x, y, z);
		List<Entity> players = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(entity instanceof Player) {
				players.add( entity);
			}
		}
		return players;
	}

}
