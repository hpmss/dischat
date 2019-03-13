package hpms.discordchat.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelHandler;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.Validator;

public class ChannelHolder {
	
	private static HashMap<String,Channel> cachedChannel = new HashMap<String,Channel>();
	private static HashMap<UUID,String> cachedPlayerCurrentChannel = new HashMap<UUID,String>();
	private static List<String> cachedLeader = new ArrayList<String>();
	
	private static ConfigurationSection storageSection = FileManager.getConfigurationSection("storage", "storage");
	private static StringBuilder builder = new StringBuilder();
	
	static {
		Set<String> channelSet = storageSection.getKeys(false);
		for(String channel : channelSet) {
			String uuid = storageSection.getConfigurationSection(channel).getString("leader").split("#")[0];
			Map<String,Object> memberList = storageSection.getConfigurationSection(channel).getConfigurationSection("list").getValues(false);
			if(memberList.size() != 0) {
				for(Entry<String,Object> entry : memberList.entrySet()) {
					cachedPlayerCurrentChannel.put(UUID.fromString(entry.getKey()),channel);
				}
			}
			cachedPlayerCurrentChannel.put(UUID.fromString(uuid),channel);
			cachedLeader.add(uuid);
		}
	}
	
	public static void put(Channel channel) {
		ConfigurationSection channelSection = storageSection.createSection(channel.getChannelName());
		channelSection.set("leader", channel.getLeader());
		channelSection.set("list", channel.getMemberList());
		FileManager.saveConfiguration("storage");
	}
	
	public static void cacheChannel(Channel channel) {
		cachedChannel.put(channel.getChannelName(), channel);
	}
	
	public static void remove(String name) {
		storageSection.set(name,null);
		FileManager.saveConfiguration("storage");
	}
	
	
	public static Channel getChannel(String name) {
		Validator.isTrue(isChannelExisted(name));
		Channel channel = cachedChannel.get(name);
		if(channel != null) {
			return channel;
		}
		ConfigurationSection channelSection = storageSection.getConfigurationSection(name);
		Player leader = Bukkit.getPlayer(UUID.fromString(channelSection.getString("leader").split("#")[0]));
		Map<String,Object> memberList = (Map<String, Object>) channelSection.getConfigurationSection("list").getValues(false);
		channel = ChannelHandler.createNewChannel(name, leader,true);
		channel.overrideMember(memberList);
		return channel;
	}
	
	public static String getPlayerCurrentChannel(Player player) {
		if(cachedPlayerCurrentChannel.containsKey(player.getUniqueId())) {
			return cachedPlayerCurrentChannel.get(player.getUniqueId());
		}
		return "";
	}
	
	public static boolean setPlayerCurrentChannel(Player player,String channel) {
		if(!isChannelExisted(channel)) return false;
		String old = getPlayerCurrentChannel(player);
		if(old.equalsIgnoreCase(channel)) return false;
		
		cachedPlayerCurrentChannel.put(player.getUniqueId(), channel);
		Channel oldChannel = getChannel(old);
		Channel newChannel = getChannel(channel);
		oldChannel.removeMember(player);
		newChannel.addMember(player);
		return true;
	}
	
	public static boolean isChannelExisted(String name) {
		return storageSection.isConfigurationSection(name);
	}
	
	public static boolean isPlayerLeader(Player player) {
		return cachedLeader.contains(player.getUniqueId().toString());
	}
	
	public static String getChannelList() {
		for(String s : storageSection.getValues(false).keySet()) {
			builder.append(s);
			builder.append(',');
		}
		if(builder.charAt(builder.length() - 1) == ',') {
			builder.setCharAt(builder.length() - 1,'.');
		}
		else {
			builder.insert(builder.length(), '.');
		}
		String str = builder.toString();
		builder = new StringBuilder();
		return str;
	}
	
}
