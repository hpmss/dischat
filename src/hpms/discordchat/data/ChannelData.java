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

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelCore;
import hpms.discordchat.utils.ErrorState;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.Validator;

public class ChannelData extends ChannelDataConstant{
	
	private static HashMap<String,ChannelCore> cachedChannel = new HashMap<String,ChannelCore>();
	private static HashMap<UUID,String> cachedPlayerCurrentChannel = new HashMap<UUID,String>();
	private static List<String> cachedLeader = new ArrayList<String>();
	
	private static ConfigurationSection storageSection = FileManager.getConfigurationSection(STORAGE, STORAGE);

	public static void initChannelHolder() {
		Set<String> channelSet = storageSection.getKeys(false);
		if(channelSet.size() == 0) {
			ChannelAPI.createNewChannel(ChannelDataConstant.DEFAULT_CHANNEL, null, false);
		}
		for(String channel : channelSet) {
			String uuid = storageSection.getConfigurationSection(channel).getString(LEADER);
			Map<String,Object> memberList = storageSection.getConfigurationSection(channel).getConfigurationSection(LIST).getValues(false);
			if(memberList.size() != 0) {
				for(Entry<String,Object> entry : memberList.entrySet()) {
					cachedPlayerCurrentChannel.put(UUID.fromString(entry.getKey()),channel);
				}
			}
			if(!channel.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
				cachedLeader.add(uuid);
			}
		}
	}
	
	public static void put(ChannelCore channel) {
		ConfigurationSection channelSection = null;
		if(isChannelExisted(channel.getChannelName())) {

			channelSection = storageSection.getConfigurationSection(channel.getChannelName());
		}
		else {
			channelSection = storageSection.createSection(channel.getChannelName());
		}
		channelSection.set(LEADER, (channel.getLeader() == null) ? "NO_LEADER" : channel.getLeader().toString());
		channelSection.set(SLOT,channel.getMaxSize());
		ConfigurationSection list = channelSection.createSection(LIST);
		for(Entry<UUID,String> member : channel.getMemberList().entrySet()) {
			list.set(member.getKey().toString(), member.getValue());
			cachedPlayerCurrentChannel.put(member.getKey(), channel.getChannelName());
		}
		save();
	}
	
	public static void cacheChannel(ChannelCore channel) {
		cachedChannel.put(channel.getChannelName(), channel);
	}
	
	public static void cacheLeader(String leader) {
		cachedLeader.add(leader);
	}
	
	public static void removeLeader(String leader) {
		cachedLeader.remove(leader);
	}
	
	public static void remove(String name) {
		ChannelCore channel = getChannel(name);
		ChannelCore global = getChannel(DEFAULT_CHANNEL);
		for(UUID member : channel.getMemberList().keySet()) {
			global.addMember(member);
			cachedPlayerCurrentChannel.put(member, DEFAULT_CHANNEL);
		}
		cachedLeader.remove(channel.getLeader().toString());
		cachedChannel.remove(name);
		storageSection.set(name,null);
		save();
	}
	
	public static ChannelCore getChannel(String name) {
		if(name.length() == 0) {
			return null;
		}
		boolean val = Validator.isTrue(isChannelExisted(name),"Channel " + name +  " doesnt exist.");
		if(val) {
			ChannelCore channel = cachedChannel.get(name);
			if(channel != null) {
				return channel;
			}
			ConfigurationSection channelSection = storageSection.getConfigurationSection(name);
			UUID leader = null;
			if(!name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
				leader = UUID.fromString(channelSection.getString(LEADER));
			}
			Map<String,Object> memberList = (Map<String, Object>) channelSection.getConfigurationSection(LIST).getValues(false);
			int slot = channelSection.getInt(SLOT);
			channel = new Channel(name, leader,true);
			channel.setMaxSlot(slot);
			channel.overrideMember(memberList);
			if(name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
				channel.setSlotLimit(false);
			}
			cacheChannel(channel);
			return channel;
		}else {
			return null;
		}
		
	}
	
	public static String getPlayerCurrentChannel(UUID uuid) {
		if(cachedPlayerCurrentChannel.containsKey(uuid)) {
			return cachedPlayerCurrentChannel.get(uuid);
		}
		return "";
	}
	
	public static ErrorState setPlayerCurrentChannel(UUID player,String channel,boolean bypass) {
		if(!isChannelExisted(channel)) return ErrorState.NO_EXISTENCE;
		String current = getPlayerCurrentChannel(player);
		if(current.equalsIgnoreCase(channel)) return ErrorState.MATCHED;
		ChannelCore newChannel = getChannel(channel);
		boolean b = newChannel.addMember(player);
		if(!b) {
			if(!bypass) {
				return ErrorState.OUT_OF_BOUND;	
			}else {
				newChannel.setMaxSlot(newChannel.getMaxSize() + 1);
				newChannel.addMember(player);
			}
		}
		if(current.length() != 0) {
			ChannelCore currentChannel = getChannel(current);
			currentChannel.removeMember(player,false);
		}
		cachedPlayerCurrentChannel.put(player, channel);
		return ErrorState.SUCCESS;
	}
	
	public static boolean isChannelExisted(String name) {
		return storageSection.isConfigurationSection(name);
	}
	
	public static boolean isPlayerLeader(UUID player) {
		return cachedLeader.contains(player.toString());
	}
	
	public static String getChannelList() {
		StringBuilder builder = new StringBuilder();
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
		return builder.toString();
	}
	
	private static void save() {
		FileManager.saveConfiguration(STORAGE);
	}
	
	public static void debug() {
		Log.info("---ChannelHolder---");
		Log.info("Cached channel: " + cachedChannel);
		Log.info("Cached CPC: " + cachedPlayerCurrentChannel);
		Log.info("Cached leader: " + cachedLeader);
	}
	
}
