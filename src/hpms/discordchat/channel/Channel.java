package hpms.discordchat.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import hpms.discordchat.data.ChannelHolder;
import hpms.discordchat.data.Prefix;
import hpms.discordchat.utils.ErrorState;
import hpms.discordchat.utils.Validator;

public abstract class Channel {
	
	protected UUID leader;
	protected String name;
	protected HashMap<UUID,String> member;
	protected int maxSlot = ChannelHolder.DEFAULT_SLOT;
	protected boolean slotLimit = true;
	
	/* TODO
	 * Handle channel slot
	 * Handle buff-exp
	 * Handle teleportation
	 * Handle slot upgrades 
	 */
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
		this.member.put(this.leader, Prefix.getLeaderPrefix(this.name));
		ChannelHolder.put(this);
	}
	
	public void overrideMember(Map<? extends Object,? extends Object> memberList) {
		if(memberList.size() != 0) {
			for(Entry<? extends Object,? extends Object> entry : memberList.entrySet()) {
				member.put(UUID.fromString(entry.getKey().toString()), entry.getValue().toString());
			}
		}
		ChannelHolder.put(this);
	}
	
	public void addMember(UUID member) {
		String prefix = Prefix.getPrefixFromPlayer(member, this);
		if(prefix == null) {
			prefix = Prefix.getInitialPrefix(this.name);
		}
		this.member.put(member,prefix);
		ChannelHolder.put(this);
		Prefix.update(this.name,member,prefix);
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
	
	public void setLeader(UUID leader) {
		this.leader = leader;
		ChannelHolder.put(this);
	}
	
	public void setMaxSlot(int maxSlot) {
		this.maxSlot = maxSlot;
		ChannelHolder.put(this);
	}
	
	public String getChannelName() {
		return name;
	}
	
	public UUID getLeader() {
		return this.leader;
	}
	
	public int getMaxSize() {
		return maxSlot;
	}
	
	public int getCurrentSize() {
		return member.size();
	}
	
	public HashMap<UUID,String> getMemberList() {
		return member;
	}
	
	public abstract boolean setPrefix(UUID setter,UUID member,String prefix);
	public abstract boolean setChannelChatPrefix(UUID setter,String prefix);
	public abstract ErrorState addPrefix(UUID setter,String prefix,boolean makeDefault);
	public abstract ErrorState removePrefix(UUID setter,String prefix);
	public abstract String getPrefix(UUID member);
	public abstract String getChannelChatPrefix();

}
