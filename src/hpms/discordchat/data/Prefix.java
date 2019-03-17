package hpms.discordchat.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.utils.ErrorState;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.Validator;
import net.md_5.bungee.api.ChatColor;

public class Prefix {
	
	private static HashMap<String,String> cachedInitPrefix = new HashMap<String,String>();
	
	private static ConfigurationSection prefixSection = FileManager.getConfigurationSection("prefix", "prefix");
	private final static String DEFAULT_MEMBER_PREFIX = ChatColor.translateAlternateColorCodes('&',FileManager.getConfig().getString("default-prefix"));
	private final static String DEFAULT_LEADER_PREFIX = ChatColor.translateAlternateColorCodes('&',FileManager.getConfig().getString("default-leader-prefix"));
	
	public static void initPrefix() {
		Set<String> channelSet = prefixSection.getKeys(false);
		for(String name : channelSet) {
			ConfigurationSection prefixConfig = prefixSection.getConfigurationSection(name).getConfigurationSection("member");
			Map<String,Object> config = prefixConfig.getValues(true);
			for(Entry<String,Object> entry : config.entrySet()) {
				if(entry.getKey().endsWith(".default")) {
					if(Boolean.parseBoolean(entry.getValue().toString())) {
						cachedInitPrefix.put(name, entry.getKey().split("[.]")[0]);
						break;
					}
				}
			}
		}
	}
	
	public static void put(Channel channel,boolean getFlag) {
		if(getFlag == false) {
			ConfigurationSection section = prefixSection.createSection(channel.getChannelName());
			section.set("channel-prefix",channel.getChannelName());
			ConfigurationSection leaderConfig = section.createSection("leader");
			ConfigurationSection memberConfig = section.createSection("member");
			ConfigurationSection rankConfig = section.createSection("rank");
			leaderConfig.set("prefix", DEFAULT_LEADER_PREFIX);
			rankConfig.set(channel.getLeader().toString(), DEFAULT_LEADER_PREFIX);
			
			ConfigurationSection memberConfigPrefix = memberConfig.createSection(ChatColor.stripColor(DEFAULT_MEMBER_PREFIX));
			memberConfigPrefix.set("default", true);
			memberConfigPrefix.set("prefix", DEFAULT_MEMBER_PREFIX);
			memberConfigPrefix.set("permission", new ArrayList<String>());
			cachedInitPrefix.put(channel.getChannelName(), ChatColor.stripColor(DEFAULT_MEMBER_PREFIX));
		}
		save();
	}
	
	public static void remove(String name) {
		prefixSection.set(name, null);
		save();
	}
	
	public static void removePlayer(String name,UUID player) {
		prefixSection.getConfigurationSection(name).getConfigurationSection("rank").set(player.toString(), null);
		save();
	}
	
	public static void update(String channel,UUID member,String prefix ) {
		ConfigurationSection section = prefixSection.getConfigurationSection(channel);
		ConfigurationSection rank = section.getConfigurationSection("rank");
		rank.set(member.toString(), prefix);
		save();
	}
	
	public static String getInitialPrefix(String channel) {
		return cachedInitPrefix.get(channel);
	}
	
	public static String getLeaderPrefix(String channel) {
		ConfigurationSection leader = prefixSection.getConfigurationSection(channel).getConfigurationSection("leader");
		Validator.isNotNull(leader);
		String prefix = leader.getString("prefix");
		return prefix;
	}
	
	public static String getPrefixFromPlayer(UUID player,String channel) {
		if(!ChannelHolder.isChannelExisted(channel)) return null;
		ConfigurationSection rank = prefixSection.getConfigurationSection(channel).getConfigurationSection("rank");
		Validator.isNotNull(rank);
		Map<String,Object> prefixRank = rank.getValues(false);
		if(prefixRank.containsKey(player.toString())) {
			return prefixRank.get(player.toString()).toString();
		}
		return null;
	}
	
	public static String getPrefixChatPrefix(String channel,String prefix) {
		if(prefix.equalsIgnoreCase(getLeaderPrefix(channel))) return prefix; 
		return prefixSection.getConfigurationSection(channel).getConfigurationSection("member").getConfigurationSection(prefix).getString("prefix");
	}
	
	public static ErrorState setPrefixChatPrefix(String channel,String prefix,String chatPrefix) {
		if(prefix == null) {
			prefixSection.getConfigurationSection(channel).getConfigurationSection("leader").set("prefix", chatPrefix);
			Channel c = ChannelHolder.getChannel(channel);
			prefixSection.getConfigurationSection(channel).getConfigurationSection("rank").set(c.getLeader().toString(), chatPrefix);
			c.setLeaderChatPrefix(chatPrefix);
			save();
			return ErrorState.SUCCESS;
		}
		if(!isPrefix(channel,prefix)) return ErrorState.NO_EXISTENCE;
		if(chatPrefix.length() == 0) return ErrorState.INVALID_LENGTH;
		prefixSection.getConfigurationSection(channel).getConfigurationSection("member").getConfigurationSection(prefix).set("prefix", chatPrefix);
		save();
		return ErrorState.SUCCESS;
	}
	
	public static int getChannelSize(String channel) {
		return prefixSection.getConfigurationSection(channel).getConfigurationSection("rank").getValues(false).size();
	}
	
	public static ErrorState addPrefix(String channel,String prefix,boolean makeDefault)  {
		if(prefix.length() != 0) {
			if(prefix.equalsIgnoreCase(getLeaderPrefix(channel))) return ErrorState.LEADER_PREFIX;
			if(isPrefix(channel,prefix)) return ErrorState.PREFIX;
			ConfigurationSection memberConfig = prefixSection.getConfigurationSection(channel).getConfigurationSection("member");
			ConfigurationSection prefixSection = memberConfig.createSection(prefix);
			prefixSection.set("prefix", prefix);
			prefixSection.set("permission", new ArrayList<String>()); 
			if(makeDefault) {
				makePrefixDefault(channel,prefix);
			}
			save();
			return ErrorState.SUCCESS;
		}	
		return ErrorState.INVALID_LENGTH;
	}
	
	public static ErrorState removePrefix(String channel,String prefix) {
		if(prefix.length() != 0) {
			if(isPrefix(channel,prefix)) {
				if(prefix.equalsIgnoreCase(getLeaderPrefix(channel))) return ErrorState.LEADER_PREFIX;
				if(prefix.equalsIgnoreCase(getInitialPrefix(channel))) return ErrorState.PREFIX;
				ConfigurationSection memberConfig = prefixSection.getConfigurationSection(channel).getConfigurationSection("member");
				memberConfig.set(prefix, null);
				overrideChannelPrefix(channel,prefix,getInitialPrefix(channel));
				save();
				return ErrorState.SUCCESS;
			}
			return ErrorState.NO_EXISTENCE;
		}
		return ErrorState.INVALID_LENGTH;
	}
	
	public static ErrorState makePrefixDefault(String channel,String prefix) {
		if(prefix.length() != 0) {
			if(isPrefix(channel,prefix)) {
				if(prefix.equalsIgnoreCase(getLeaderPrefix(channel))) return ErrorState.LEADER_PREFIX;
				ConfigurationSection memberConfig = prefixSection.getConfigurationSection(channel).getConfigurationSection("member");
				String currentDefault = cachedInitPrefix.get(channel);
				memberConfig.getConfigurationSection(currentDefault).set("default",null);
				memberConfig.getConfigurationSection(prefix).set("default", true);
				cachedInitPrefix.put(channel,prefix);
				overrideChannelPrefix(channel,currentDefault,prefix);
				save();
				return ErrorState.SUCCESS;
			}
			return ErrorState.NO_EXISTENCE;
		}
		return ErrorState.INVALID_LENGTH;
	}
	
	public static void setChannelChatPrefix(String channel,String prefix) {
		if(ChannelHolder.isChannelExisted(channel)) {
			prefixSection.getConfigurationSection(channel).set("channel-prefix", prefix);
			save();
		}
	}
	
	public static String getChannelChatPrefix(String channel) {
		if(ChannelHolder.isChannelExisted(channel)) {
			return prefixSection.getConfigurationSection(channel).getString("channel-prefix");
		}
		return "";
	}
	
	public static boolean isPrefix(String channel,String prefix) {
		return prefixSection.getConfigurationSection(channel).getConfigurationSection("member").isConfigurationSection(prefix);
	}
	
	public static boolean isPlayerAlreadyJoined(String channel,UUID uuid) {
		return prefixSection.getConfigurationSection(channel).getConfigurationSection("rank").getValues(false).containsKey(uuid.toString());
	}
	
	private static void save() {
		FileManager.saveConfiguration("prefix");
	}
	
	private static void overrideChannelPrefix(String name,String oldPrefix,String newPrefix) {
		ConfigurationSection rank = prefixSection.getConfigurationSection(name).getConfigurationSection("rank");
		for(Entry<String,Object> entry : rank.getValues(false).entrySet()) {
			if(entry.getValue().toString().equalsIgnoreCase(oldPrefix)) {
				rank.set(entry.getKey(), newPrefix);
			}
		}
		Channel channel = ChannelHolder.getChannel(name);
		if(channel.getMemberList().size() != 0) {
			HashMap<UUID,String> memberList = new HashMap<UUID,String>();
			memberList.putAll(channel.getMemberList());
			for(Entry<UUID,String> entry : memberList.entrySet()) {
				if(entry.getValue().equalsIgnoreCase(oldPrefix)) {
					entry.setValue(newPrefix);
				}
			}
			channel.overrideMember(memberList);
		}
	}
	
	public static void debug() {
		Log.info("---Prefix---");
		Log.info("Cached default prefix: " + cachedInitPrefix);
	}

}
