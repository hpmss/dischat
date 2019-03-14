package hpms.discordchat.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import hpms.discordchat.channel.Channel;
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
			
			ConfigurationSection memberConfigPrefix = memberConfig.createSection(DEFAULT_MEMBER_PREFIX);
			memberConfigPrefix.set("default", true);
			memberConfigPrefix.set("permission", new ArrayList<String>());
		}
		save();
	}
	
	public static void remove(String name) {
		prefixSection.set(name, null);
		save();
	}
	
	public static void update(Channel channel,UUID member,String prefix ) {
		ConfigurationSection section = prefixSection.getConfigurationSection(channel.getChannelName());
		ConfigurationSection rank = section.getConfigurationSection("rank");
		rank.set(member.toString(), prefix);
		save();
	}
	
	public static String getInitialPrefix(Channel channel) {
		if(cachedInitPrefix.containsKey(channel.getChannelName())) {
			return cachedInitPrefix.get(channel.getChannelName());
		}
		return null;
	}
	
	public static String getLeaderPrefix(Channel channel) {
		ConfigurationSection leader = prefixSection.getConfigurationSection(channel.getChannelName()).getConfigurationSection("leader");
		Validator.isNotNull(leader);
		String prefix = leader.getString("prefix");
		return prefix;
	}
	
	public static boolean isPrefix(String prefix,Channel channel) {
		return prefixSection.getConfigurationSection(channel.getChannelName()).isConfigurationSection(prefix);
	}
	
	public static String getPrefix(UUID player,Channel channel) {
		if(!ChannelHolder.isChannelExisted(channel.getChannelName())) return null;
		ConfigurationSection rank = prefixSection.getConfigurationSection(channel.getChannelName()).getConfigurationSection("rank");
		Validator.isNotNull(rank);
		Map<String,Object> prefixRank = rank.getValues(false);
		if(prefixRank.containsKey(player.toString())) {
			return prefixRank.get(player.toString()).toString();
		}
		return null;
	}
	
	public static void addPrefix(String name,String prefix,boolean makeDefault) {
		if(prefix.length() != 0) {
			ConfigurationSection memberConfig = prefixSection.getConfigurationSection(name).getConfigurationSection("member");
			memberConfig.createSection(prefix);
			memberConfig.getConfigurationSection(prefix).set("permission", new ArrayList<String>());
			//Move current default prefix player to new prefix [To-Do] 
			if(makeDefault == true) {
				String currentDefault = cachedInitPrefix.get(name);
				memberConfig.getConfigurationSection(currentDefault).set("default",null);
				memberConfig.getConfigurationSection(prefix).set("default", true);
				cachedInitPrefix.put(name,prefix);
			}
		}
		save();
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
	
	private static void save() {
		FileManager.saveConfiguration("prefix");
	}

}
