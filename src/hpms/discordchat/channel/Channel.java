package hpms.discordchat.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;

import hpms.discordchat.data.ChannelHolder;
import hpms.discordchat.data.Prefix;
import hpms.discordchat.utils.Validator;

public abstract class Channel {
	
	protected Player leader;
	protected String name;
	protected HashMap<UUID,String> member;
	
	
	public Channel(String name,Player leader,boolean perm) {
		this.name = name;
		this.member = new HashMap<UUID,String>();
		this.leader = leader;
		if(perm == false) {
			Validator.isTrue(!ChannelHolder.isChannelExisted(name));
		}
		Prefix.put(this);
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
		if(member.getUniqueId().equals(this.leader.getUniqueId())) return;
		this.member.put(member.getUniqueId(),Prefix.getInitialPrefix(this));
		ChannelHolder.put(this);
	}
	
	public void removeMember(Player member) {
		if(member.getUniqueId().equals(this.leader.getUniqueId())) return;
		if(this.member.containsKey(member.getUniqueId())) {
			this.member.remove(member.getUniqueId());
			ChannelHolder.put(this);
		}
	}
	
	public void setChannelName(String name) {
		ChannelHolder.remove(this.name);
		this.name = name;
		ChannelHolder.put(this);
	}
	
	public void setLeader(Player leader) {
		this.leader = leader;
		ChannelHolder.put(this);
	}
	
	public String getChannelName() {
		return name;
	}
	
	public Player getLeaderPlayer() {
		return this.leader;
	}
	
	public String getLeader() {
		return leader.getUniqueId() + "#" + Prefix.getLeaderPrefix(this);
	}
	
	public HashMap<UUID,String> getMemberList() {
		return member;
	}
	
	public abstract void setPrefix(Player member,String prefix);
	public abstract String getPrefix(Player member);

}
