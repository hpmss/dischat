package hpms.discordchat.channel;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.data.ChannelHolder;
import hpms.discordchat.data.Prefix;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.Validator;
import net.md_5.bungee.api.ChatColor;

public class ChannelHandler extends ChannelPrefix {

	public ChannelHandler(String name, UUID leader,boolean getFlag) {
		super(name, leader,getFlag);
	}
	
	public static void initChannelHandler(JavaPlugin plugin) {
		FileManager.initFileManager(plugin);
		ChannelHolder.initChannelHolder();
		Prefix.initPrefix();
	}
	
	public static Channel createNewChannel(String name,UUID leader,boolean getFlag) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(leader);
		if(ChannelHolder.isPlayerLeader(leader))  {
			if(player.isOnline()) {
				if(!player.getPlayer().hasPermission("discordchat.multiplechannel")) {
					player.getPlayer().sendMessage(ChatColor.YELLOW + "You are already a leader of channel.");
					return null;
				}
			}
		}
		if(getPlayerCurrentChannel(leader) != null) {
			getPlayerCurrentChannel(leader).removeMember(leader);
		}
		Channel channel = new ChannelHandler(name,leader,getFlag);
		ChannelHolder.cacheChannel(channel);
		ChannelHolder.cacheLeader(leader.toString());
		return channel;
	}
	
	public static boolean setChannelChatPrefix(UUID setter,String name,String prefix) {
		Channel channel = getChannelByName(name);
		return channel.setChannelChatPrefix(setter,prefix);
	}
	
	public static boolean setChannelPlayerPrefix(UUID setter,UUID member,String name,String prefix) {
		Channel channel = getChannelByName(name);
		return channel.setPrefix(setter, member, prefix);
	}
	
	public static void removeChannel(String name) {
		Validator.isTrue(ChannelHolder.isChannelExisted(name));
		Prefix.remove(name);
		ChannelHolder.remove(name);
	}
	
	public static Channel getChannelByName(String name) {
		return ChannelHolder.getChannel(name);
	}
	
	public static Channel getPlayerCurrentChannel(UUID player) {
		return getChannelByName(ChannelHolder.getPlayerCurrentChannel(player));
	}
	
	public static void joinChannel(Player player,String channel) {
		switch( ChannelHolder.setPlayerCurrentChannel(player,channel)) {
		case INVALID_LENGTH:
			break;
		case LEADER_PREFIX:
			break;
		case MATCHED:
			player.sendMessage(ChatColor.YELLOW + "You are already in \'" + ChatColor.AQUA +channel + ChatColor.YELLOW + "\' channel .");
			break;
		case NO_EXISTENCE:
			player.sendMessage(ChatColor.YELLOW + "Channel \'" + ChatColor.AQUA + channel +ChatColor.YELLOW + "\' doesnt exist .");
			break;
		case SUCCESS:
			player.sendMessage(ChatColor.AQUA + "\'" + channel + "\'" + ChatColor.YELLOW + " channel joined.");
			break;
		case PREFIX:
			break;
		case NULL:
			break;
		}
	}

}
