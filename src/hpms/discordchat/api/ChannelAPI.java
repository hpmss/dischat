package hpms.discordchat.api;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.data.ChannelBackend;
import hpms.discordchat.data.ChannelData;
import hpms.discordchat.data.Role;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.PendingInvitation;

public class ChannelAPI {
	
	public static void initChannelAPI(JavaPlugin plugin) {
		FileManager.initFileManager(plugin);
		ChannelBackend.initBackend();
		ChannelData.initChannelHolder();
		Role.initRole();
		PendingInvitation.reloadAllInvitation();
	}
	
	public static void saveData() {
		PendingInvitation.saveSerialization();
	}
	  
	public static Channel createNewChannel(String channelName,UUID leader,boolean getFlag) {
		return ChannelBackendAPI.getAPI().createNewChannel(channelName, leader, getFlag);
	}
	
	public static boolean setChannelChatPrefix(UUID setter,String channelName,String prefix) {
		return ChannelBackendAPI.getAPI().setChannelChatPrefix(setter, channelName, prefix);
	}
	
	public static boolean setChannelPlayerRole(UUID setter,UUID member,String channelName,String rank) {
		return ChannelBackendAPI.getAPI().setChannelPlayerRole(setter, member, channelName, rank);
	}
	
	public static Channel getChannelByName(String channelName) {
		return ChannelBackendAPI.getAPI().getChannelByName(channelName);
	}
	
	public static Channel getPlayerCurrentChannel(UUID player) {
		return ChannelBackendAPI.getAPI().getPlayerCurrentChannel(player);
	}
	
	public static String getPlayerCurrentChannelName(UUID player) {
		return ChannelBackendAPI.getAPI().getPlayerCurrentChannelName(player);
	}
	
	public static void joinChannel(UUID player,String channelName) {
		ChannelBackendAPI.getAPI().joinChannel(player, channelName);
	}
	
	public static void removeChannel(String channelName) {
		ChannelBackendAPI.getAPI().removeChannel(channelName);
	}
	

}
