package hpms.discordchat.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelHandler;
import hpms.discordchat.data.ChannelHolder;

public class EventListener implements Listener{
	
	private static String PLAYER_NAME = "%1$s";
	private static String PLAYER_MESSAGE = "%2$s";
	private static String FORMAT = "[" + "<channelprefix>" + ChatColor.RESET + "][" + "<chatprefix>" + ChatColor.RESET + "]" ;
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		if(!ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId()).getChannelName().equalsIgnoreCase(ChannelHolder.DEFAULT_CHANNEL)) {
			List<Entity> players = getNearbyPlayers(e.getPlayer(),10,10,10);
			e.getRecipients().clear();
			e.getRecipients().add(e.getPlayer());
			for(Entity p : players) {
				e.getRecipients().add((Player)p);
			}
		}
		
		Channel channel = ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId());
		Log.info(e.getPlayer().getUniqueId().toString());
		Log.info(channel.getMemberList());
		String form = FORMAT.replace("<channelprefix>",ChatColor.translateAlternateColorCodes('&', channel.getChannelChatPrefix()));
		form = form.replace("<chatprefix>", ChatColor.translateAlternateColorCodes('&', channel.getPrefixChatPrefix(channel.getPrefix(e.getPlayer().getUniqueId()))));
		e.setFormat(form +  PLAYER_NAME + ": " + PLAYER_MESSAGE );
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(ChannelHandler.getPlayerCurrentChannel(e.getPlayer().getUniqueId()) == null) {
			ChannelHandler.joinChannel(e.getPlayer(), ChannelHolder.DEFAULT_CHANNEL);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(e.getClickedBlock() == null | e.getClickedBlock().getType() == Material.AIR) return;
		Block b = e.getClickedBlock();
		b.setMetadata("discordchat", new FixedMetadataValue(DiscordChat.plugin,"1"));
		b.setMetadata("discordchat2", new FixedMetadataValue(DiscordChat.plugin,"2"));
		List<MetadataValue> meta = b.getMetadata("discordchat");
		List<MetadataValue> meta2 = b.getMetadata("discordchat2");
//		for(MetadataValue m : meta) {
//			Log.info(m.value());
//		}
//		for(MetadataValue m : meta2) {
//			Log.info(m.value());
//		}
		b.getState().getData().getClass();
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
