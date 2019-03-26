package hpms.discordchat.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.channel.Channel;
import hpms.discordchat.data.ChannelData;

public class EventListener implements Listener{
	
	private String PLAYER_NAME = "%1$s";
	private String PLAYER_MESSAGE = "%2$s";
	private String FORMAT = "[" + "<channelprefix>" + ChatColor.RESET + "][" + "<chatprefix>" + ChatColor.RESET + "]" ;
	
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		if(!ChannelAPI.getPlayerCurrentChannel(e.getPlayer().getUniqueId()).getChannelName().equalsIgnoreCase(ChannelData.DEFAULT_CHANNEL)) {
			List<Entity> players = getNearbyPlayers(e.getPlayer(),10,10,10);
			e.getRecipients().clear();
			e.getRecipients().add(e.getPlayer());
			for(Entity p : players) {
				e.getRecipients().add((Player)p);
			}
		}
		
		Channel channel = ChannelAPI.getPlayerCurrentChannel(e.getPlayer().getUniqueId());
		String form = FORMAT.replace("<channelprefix>",ChatColor.translateAlternateColorCodes('&', channel.getChannelChatPrefix()));
		form = form.replace("<chatprefix>", ChatColor.translateAlternateColorCodes('&', channel.getRolePrefix(channel.getRole(e.getPlayer().getUniqueId()))));
		e.setFormat(form +  PLAYER_NAME + ": " + PLAYER_MESSAGE );
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(ChannelAPI.getPlayerCurrentChannel(e.getPlayer().getUniqueId()) == null) {
			ChannelAPI.joinChannel(e.getPlayer().getUniqueId(), ChannelData.DEFAULT_CHANNEL);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(e.getClickedBlock() == null | e.getClickedBlock().getType() == Material.AIR) return;
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
