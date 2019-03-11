package hpms.discordchat.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	
	static {
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
	
	public static void put(Channel channel) {
		ConfigurationSection section = prefixSection.createSection(channel.getChannelName());
		ConfigurationSection leaderConfig = section.createSection("leader");
		ConfigurationSection memberConfig = section.createSection("member");
		leaderConfig.set("prefix", DEFAULT_LEADER_PREFIX);
		leaderConfig.set("permission", new ArrayList<String>());
		
		ConfigurationSection memberConfigPrefix = memberConfig.createSection(DEFAULT_MEMBER_PREFIX);
		memberConfigPrefix.set("default", true);
		memberConfigPrefix.set("permission", new ArrayList<String>());
		FileManager.saveConfiguration("prefix");
	}
	
	public static void remove(String name) {
		prefixSection.set(name, null);
		FileManager.saveConfiguration("prefix");
	}
	
	public static String getInitialPrefix(Channel channel) {
		if(cachedInitPrefix.containsKey(channel.getChannelName())) {
			return cachedInitPrefix.get(channel.getChannelName());
		}
		return null;
	}
	
	public static String getLeaderPrefix(Channel channel) {
		ConfigurationSection section = prefixSection.getConfigurationSection(channel.getChannelName());
		Validator.isNotNull(section);
		ConfigurationSection leader = section.getConfigurationSection("leader");
		Validator.isNotNull(leader);
		String prefix = leader.getString("prefix");
		return prefix;
	}
	
	public static ConfigurationSection getPrefix(Channel channel,String prefix) {
		ConfigurationSection section = prefixSection.getConfigurationSection(channel.getChannelName());
		Validator.isNotNull(section);
		ConfigurationSection member = prefixSection.getConfigurationSection("member");
		Validator.isNotNull(member);
		ConfigurationSection prefixSection = member.getConfigurationSection(prefix);
		return prefixSection;
	}

}
