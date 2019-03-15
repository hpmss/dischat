package hpms.discordchat.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelHandler;
import hpms.discordchat.utils.ErrorState;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.Validator;

public class ChannelHolder {
	
	private static HashMap<String,Channel> cachedChannel = new HashMap<String,Channel>();
	private static HashMap<UUID,String> cachedPlayerCurrentChannel = new HashMap<UUID,String>();
	private static List<String> cachedLeader = new ArrayList<String>();
	
	private static ConfigurationSection storageSection = FileManager.getConfigurationSection("storage", "storage");
	private static StringBuilder builder = new StringBuilder();
	
	public static String DEFAULT_CHANNEL = FileManager.getConfig().getString("default-join-server-channel");
	public static int DEFAULT_SLOT = FileManager.getConfig().getInt("default-channel-slot");
	
	public static void initChannelHolder() {
		Set<String> channelSet = storageSection.getKeys(false);
		for(String channel : channelSet) {
			String uuid = storageSection.getConfigurationSection(channel).getString("leader");
			Map<String,Object> memberList = storageSection.getConfigurationSection(channel).getConfigurationSection("list").getValues(false);
			if(memberList.size() != 0) {
				for(Entry<String,Object> entry : memberList.entrySet()) {
					cachedPlayerCurrentChannel.put(UUID.fromString(entry.getKey()),channel);
				}
			}
			cachedLeader.add(uuid);
		}
	}
	
	public static void put(Channel channel) {
		ConfigurationSection channelSection = storageSection.createSection(channel.getChannelName());
		channelSection.set("leader", channel.getLeader().toString());
		channelSection.set("slot",channel.getMaxSize());
		ConfigurationSection list = channelSection.createSection("list");
		for(Entry<UUID,String> member : channel.getMemberList().entrySet()) {
			list.set(member.getKey().toString(), member.getValue());
			cachedPlayerCurrentChannel.put(member.getKey(), channel.getChannelName());
		}
		save();
	}
	
	public static void cacheChannel(Channel channel) {
		cachedChannel.put(channel.getChannelName(), channel);
	}
	
	public static void cacheLeader(String leader) {
		cachedLeader.add(leader);
	}
	
	public static void remove(String name) {
		Channel channel = getChannel(name);
		Channel global = getChannel(DEFAULT_CHANNEL);
		for(UUID member : channel.getMemberList().keySet()) {
			global.addMember(member);
			cachedPlayerCurrentChannel.put(member, DEFAULT_CHANNEL);
		}
		cachedLeader.remove(channel.getLeader().toString());
		cachedChannel.remove(name);
		storageSection.set(name,null);
		save();
	}
	
	public static Channel getChannel(String name) {
		if(name.length() == 0) {
			return null;
		}
		Validator.isTrue(isChannelExisted(name));
		Channel channel = cachedChannel.get(name);
		if(channel != null) {
			return channel;
		}
		ConfigurationSection channelSection = storageSection.getConfigurationSection(name);
		Validator.isNotNull(channelSection,"channel \'" + name + "\' doesnt exist.");
		UUID leader = UUID.fromString(channelSection.getString("leader"));
		Map<String,Object> memberList = (Map<String, Object>) channelSection.getConfigurationSection("list").getValues(false);
		channel = new ChannelHandler(name, leader,true);
		channel.overrideMember(memberList);
		cacheChannel(channel);
		return channel;
	}
	
	public static String getPlayerCurrentChannel(UUID uuid) {
		if(cachedPlayerCurrentChannel.containsKey(uuid)) {
			return cachedPlayerCurrentChannel.get(uuid);
		}
		return "";
	}
	
	public static ErrorState setPlayerCurrentChannel(Player player,String channel) {
		if(!isChannelExisted(channel)) return ErrorState.NO_EXISTENCE;
		String current = getPlayerCurrentChannel(player.getUniqueId());
		if(current.equalsIgnoreCase(channel)) return ErrorState.MATCHED;
		if(current.length() != 0) {
			Channel currentChannel = getChannel(current);
			currentChannel.removeMember(player.getUniqueId());
		}
		Channel newChannel = getChannel(channel);
		newChannel.addMember(player.getUniqueId());
		cachedPlayerCurrentChannel.put(player.getUniqueId(), channel);
		return ErrorState.SUCCESS;
	}
	
	public static boolean isChannelExisted(String name) {
		return storageSection.isConfigurationSection(name);
	}
	
	public static boolean isPlayerLeader(UUID player) {
		return cachedLeader.contains(player.toString());
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
	
	private static void save() {
		FileManager.saveConfiguration("storage");
	}
	
	public static void debug() {
		Log.info(cachedChannel);
		Log.info(cachedPlayerCurrentChannel);
		Log.info(cachedLeader);
	}
	
}
