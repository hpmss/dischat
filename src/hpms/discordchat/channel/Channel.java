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
		this.member.put(member.getUniqueId(),Prefix.getInitialPrefix(this));
		ChannelHolder.put(this);
	}
	
	public void setChannelName(String name) {
		this.name = name;
	}
	
	public String getChannelName() {
		return name;
	}
	
	public String getLeader() {
		return leader.getUniqueId() + "#" + Prefix.getLeaderPrefix(this);
	}
	
	public HashMap<UUID,String> getMemberList() {
		return member;
	}
	
	public void reloadChannel() {
		
	}
	
	public abstract void setPrefix(Player member);
	public abstract String getPrefix(Player member);

}
