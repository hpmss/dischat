package hpms.discordchat.channel;

import org.bukkit.entity.Player;

import hpms.discordchat.data.ChannelHolder;
import hpms.discordchat.data.Prefix;
import hpms.discordchat.utils.Validator;
import net.md_5.bungee.api.ChatColor;

public class ChannelHandler extends ChannelPrefix {

	public ChannelHandler(String name, Player leader,boolean perm) {
		super(name, leader,perm);
	}
	
	public static Channel createNewChannel(String name,Player leader,boolean getFlag) {
		if(ChannelHolder.isPlayerLeader(leader) && getFlag == false)  {
			leader.sendMessage(ChatColor.YELLOW + "You are already a leader of channel.");
			return null;
		}
		Channel channel = new ChannelHandler(name,leader,leader.hasPermission("discordchat.overridechannel"));
		ChannelHolder.cacheChannel(channel);
		return channel;
	}
	
	public static void removeChannel(String name) {
		Validator.isTrue(ChannelHolder.isChannelExisted(name));
		Prefix.remove(name);
		ChannelHolder.remove(name);
	}
	
	public static Channel getChannel(String name) {
		return ChannelHolder.getChannel(name);
	}
	
	public static String getPlayerCurrentChannel(Player player) {
		return ChannelHolder.getPlayerCurrentChannel(player);
	}
	
	public static void joinChannel(Player player) {
		ChannelHolder.setPlayerCurrentChannel(player,ChannelHolder.getPlayerCurrentChannel(player));
	}

}
