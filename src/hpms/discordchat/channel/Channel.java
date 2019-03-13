package hpms.discordchat.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import hpms.discordchat.data.ChannelHolder;
import hpms.discordchat.data.Prefix;
import hpms.discordchat.utils.Validator;

public abstract class Channel {
	
	protected UUID leader;
	protected String name;
	protected HashMap<UUID,String> member;
	
	
	public Channel(String name,UUID leader,boolean getFlag) {
		this.name = name;
		this.member = new HashMap<UUID,String>();
		this.leader = leader;
		OfflinePlayer player = Bukkit.getOfflinePlayer(leader);
		if(player.isOnline()) {
			if(!player.getPlayer().hasPermission("discordchat.overridechannel")) {
				Validator.isTrue(!ChannelHolder.isChannelExisted(name));
			}
		}
		Prefix.put(this,getFlag);
		this.member.put(this.leader, Prefix.getLeaderPrefix(this));
		ChannelHolder.put(this);
	}
	
	public void overrideMember(Map<String,Object> memberList) {
		if(memberList.size() != 0) {
			for(Entry<String,Object> entry : memberList.entrySet()) {
				member.put(UUID.fromString(entry.getKey()), entry.getValue().toString());
			}
		}
	}
	
	public void addMember(Player member) {
		String prefix = Prefix.getPrefix(member, this);
		if(prefix == null) {
			prefix = Prefix.getInitialPrefix(this);
		}
		this.member.put(member.getUniqueId(),prefix);
		ChannelHolder.put(this);
		Prefix.update(this,member,prefix);
	}
	
	public void removeMember(UUID uuid) {
		if(this.member.containsKey(uuid)) {
			this.member.remove(uuid);
			ChannelHolder.put(this);
		}
	}
	
	public void setChannelName(String name) {
		ChannelHolder.remove(this.name);
		this.name = name;
		ChannelHolder.put(this);
	}
	
	public void setLeader(Player leader) {
		this.leader = leader.getUniqueId();
		ChannelHolder.put(this);
	}
	
	public String getChannelName() {
		return name;
	}
	
	public UUID getLeader() {
		return this.leader;
	}
	
	public HashMap<UUID,String> getMemberList() {
		return member;
	}
	
	public abstract void setPrefix(Player member,String prefix);
	public abstract void setChannelChatPrefix(String prefix);
	public abstract String getPrefix(Player member);
	public abstract String getChannelChatPrefix();

}
