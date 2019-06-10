package hpms.discordchat.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import hpms.discordchat.channel.ChannelCore;
import hpms.discordchat.utils.ErrorState;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.Validator;
import net.md_5.bungee.api.ChatColor;

public class Role extends RoleConstant{
	
	private static HashMap<String,String> cachedInitRole = new HashMap<String,String>();
	private static ConfigurationSection roleSection = FileManager.getConfigurationSection(ROLE, ROLE);
	
	public static void initRole() {
		Set<String> channelSet = roleSection.getKeys(false);
		for(String name : channelSet) {								
			ConfigurationSection roleConfig = roleSection.getConfigurationSection(name).getConfigurationSection(MEMBER);
			Map<String,Object> config = roleConfig.getValues(true);
			for(Entry<String,Object> entry : config.entrySet()) {
				if(entry.getKey().endsWith(".default")) {
					if(Boolean.parseBoolean(entry.getValue().toString())) {
						cachedInitRole.put(name, entry.getKey().split("[.]")[0]);
						break;
					}
				}
			}
		}
	}
	
	public static void put(ChannelCore channel,boolean getFlag) {
		if(getFlag == false) {
			ConfigurationSection section = roleSection.createSection(channel.getChannelName());
			section.set(CHANNEL_PREFIX,channel.getChannelName());
			ConfigurationSection leaderConfig = section.createSection(LEADER);
			ConfigurationSection memberConfig = section.createSection(MEMBER);
			ConfigurationSection roleConfig = section.createSection(ROLE);
			leaderConfig.set(PREFIX, DEFAULT_LEADER_PREFIX);
			if(channel.getLeader() != null) {
				roleConfig.set(channel.getLeader().toString(), DEFAULT_LEADER_PREFIX);
			}
			ConfigurationSection memberConfigPrefix = memberConfig.createSection(ChatColor.stripColor(DEFAULT_MEMBER_PREFIX));
			memberConfigPrefix.set(DEFAULT, true);
			memberConfigPrefix.set(PREFIX, DEFAULT_MEMBER_PREFIX);
			memberConfigPrefix.set(PERMISSION, new ArrayList<String>());
			cachedInitRole.put(channel.getChannelName(), ChatColor.stripColor(DEFAULT_MEMBER_PREFIX));
		}
		save();
	}
	
	public static void remove(String name) {
		roleSection.set(name, null);
		save();
	}
	
	public static void removePlayer(String name,UUID player) {
		roleSection.getConfigurationSection(name).getConfigurationSection(ROLE).set(player.toString(), null);
		save();
	}
	
	public static void update(String channel,UUID member,String role ) {
		ConfigurationSection section = roleSection.getConfigurationSection(channel);
		ConfigurationSection roleConfig = section.getConfigurationSection(ROLE);
		roleConfig.set(member.toString(), role);
		save();
	}
	
	private static void save() {
		FileManager.saveConfiguration(ROLE);
	}
	
	public static String getInitialRole(String channel) {
		return cachedInitRole.get(channel);
	}
	
	public static String getLeaderPrefix(String channel) {
		ConfigurationSection leader = roleSection.getConfigurationSection(channel).getConfigurationSection(LEADER);
		Validator.isNotNull(leader);
		String prefix = leader.getString(PREFIX);
		return prefix;
	}
	
	public static String getRoleFromPlayer(UUID player,String channel) {
		if(!ChannelData.isChannelExisted(channel)) return null;
		ConfigurationSection role = roleSection.getConfigurationSection(channel).getConfigurationSection(ROLE);
		Validator.isNotNull(role);
		Map<String,Object> roles = role.getValues(false);
		if(roles.containsKey(player.toString())) {
			return roles.get(player.toString()).toString();
		}
		return null;
	}
	
	public static String getRolePrefix(String channel,String role) {
		if(role.equalsIgnoreCase(getLeaderPrefix(channel))) return role; 
		return roleSection.getConfigurationSection(channel).getConfigurationSection(MEMBER).getConfigurationSection(role).getString(PREFIX);
	}
	
	public static String getChannelChatPrefix(String channel) {
		if(ChannelData.isChannelExisted(channel)) {
			return roleSection.getConfigurationSection(channel).getString(CHANNEL_PREFIX);
		}
		return "";
	}
	
	public static ErrorState setRolePrefix(String channel,String role,String chatPrefix) {
		if(role == null) {
			roleSection.getConfigurationSection(channel).getConfigurationSection(LEADER).set(PREFIX, chatPrefix);
			ChannelCore c = ChannelData.getChannel(channel);
			roleSection.getConfigurationSection(channel).getConfigurationSection(ROLE).set(c.getLeader().toString(), chatPrefix);
			c.setLeaderChatPrefix(chatPrefix);
			save();
			return ErrorState.SUCCESS;
		}
		if(!isRole(channel,role)) return ErrorState.NO_EXISTENCE;
		if(chatPrefix.length() == 0) return ErrorState.INVALID_LENGTH;
		roleSection.getConfigurationSection(channel).getConfigurationSection(MEMBER).getConfigurationSection(role).set(PREFIX, chatPrefix);
		save();
		return ErrorState.SUCCESS;
	}
	
	public static int getChannelSize(String channel) {
		return roleSection.getConfigurationSection(channel).getConfigurationSection(ROLE).getValues(false).size();
	}
	
	public static ErrorState addRole(String channel,String role,boolean makeDefault)  {
		if(role.length() != 0) {
			if(role.equalsIgnoreCase(getLeaderPrefix(channel))) return ErrorState.LEADER_PREFIX;
			if(isRole(channel,role)) return ErrorState.PREFIX;
			ConfigurationSection memberConfig = roleSection.getConfigurationSection(channel).getConfigurationSection(MEMBER);
			ConfigurationSection roleSection = memberConfig.createSection(role);
			roleSection.set(PREFIX, role);
			roleSection.set(PERMISSION, new ArrayList<String>()); 
			if(makeDefault) {
				makeRoleDefault(channel,role);
			}
			save();
			return ErrorState.SUCCESS;
		}	
		return ErrorState.INVALID_LENGTH;
	}
	
	public static ErrorState removeRole(String channel,String role) {
		if(role.length() != 0) {
			if(isRole(channel,role)) {
				if(role.equalsIgnoreCase(getLeaderPrefix(channel))) return ErrorState.LEADER_PREFIX;
				if(role.equalsIgnoreCase(getInitialRole(channel))) return ErrorState.PREFIX;
				ConfigurationSection memberConfig = roleSection.getConfigurationSection(channel).getConfigurationSection(MEMBER);
				memberConfig.set(role, null);
				overrideChannelRole(channel,role,getInitialRole(channel));
				save();
				return ErrorState.SUCCESS;
			}
			return ErrorState.NO_EXISTENCE;
		}
		return ErrorState.INVALID_LENGTH;
	}
	
	public static ErrorState makeRoleDefault(String channel,String role) {
		if(role.length() != 0) {
			if(isRole(channel,role)) {
				if(role.equalsIgnoreCase(getLeaderPrefix(channel))) return ErrorState.LEADER_PREFIX;
				ConfigurationSection memberConfig = roleSection.getConfigurationSection(channel).getConfigurationSection(MEMBER);
				String currentDefault = cachedInitRole.get(channel);
				memberConfig.getConfigurationSection(currentDefault).set(DEFAULT,null);
				memberConfig.getConfigurationSection(role).set(DEFAULT, true);
				cachedInitRole.put(channel,role);
				overrideChannelRole(channel,currentDefault,role);
				save();
				return ErrorState.SUCCESS;
			}
			return ErrorState.NO_EXISTENCE;
		}
		return ErrorState.INVALID_LENGTH;
	}
	
	public static void setChannelChatPrefix(String channel,String prefix) {
		if(ChannelData.isChannelExisted(channel)) {
			roleSection.getConfigurationSection(channel).set(CHANNEL_PREFIX, prefix);
			save();
		}
	}
	
	public static boolean isRole(String channel,String role) {
		return roleSection.getConfigurationSection(channel).getConfigurationSection(MEMBER).isConfigurationSection(role);
	}
	
	public static boolean isPlayerAlreadyJoined(String channel,UUID uuid) {
		return roleSection.getConfigurationSection(channel).getConfigurationSection(ROLE).getValues(false).containsKey(uuid.toString());
	}
	
	private static void overrideChannelRole(String name,String oldRole,String newRole) {
		ConfigurationSection roleConfig = roleSection.getConfigurationSection(name).getConfigurationSection(ROLE);
		for(Entry<String,Object> entry : roleConfig.getValues(false).entrySet()) {
			if(entry.getValue().toString().equalsIgnoreCase(oldRole)) {
				roleConfig.set(entry.getKey(), newRole);
			}
		}
		ChannelCore channel = ChannelData.getChannel(name);
		if(channel.getMemberList().size() != 0) {
			WeakHashMap<UUID,String> memberList = new WeakHashMap<UUID,String>();
			memberList.putAll(channel.getMemberList());
			for(Entry<UUID,String> entry : memberList.entrySet()) {
				if(entry.getValue().equalsIgnoreCase(oldRole)) {
					entry.setValue(newRole);
				}
			}
			channel.overrideMember(memberList);
		}
	}
	
	public static void debug() {
		Log.info("---Prefix---");
		Log.info("Cached default role: " + cachedInitRole);
	}

}
