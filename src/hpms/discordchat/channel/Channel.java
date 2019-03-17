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
	protected int maxSlot = 1;//ChannelHolder.DEFAULT_SLOT;
	protected boolean slotLimit = true;
	protected boolean friendlyFire = false;
	
	/* TODO
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
		if(getFlag == false) {
			this.member.put(this.leader, Prefix.getLeaderPrefix(this.name));
		}
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
	
	public boolean addMember(UUID member) {
		String prefix = Prefix.getPrefixFromPlayer(member, this.name);
		if(prefix == null) {
			prefix = Prefix.getInitialPrefix(this.name);
		}
		if(this.isPlayerAlreadyJoined(member)) {
			this.member.put(member, prefix);
			ChannelHolder.put(this);
			return true;
		}
		if(!(this.getJoinedSize() < this.maxSlot) && slotLimit) return false;
		this.member.put(member,prefix);
		ChannelHolder.put(this);
		Prefix.update(this.name,member,prefix);
		return true;
	}
	
	public boolean removeMember(UUID uuid,boolean hard) {
		if(hard) {
			Prefix.removePlayer(this.name, uuid);
		}
		if(this.member.containsKey(uuid)) {
			this.member.remove(uuid);
			ChannelHolder.put(this);
			return true;
		}
		return false;
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
	
	public void setLeaderChatPrefix(String prefix) {
		if(this.member.containsKey(leader)) {
			this.member.put(this.leader, prefix);
		}
		ChannelHolder.put(this);
	}
	
	public void setMaxSlot(int maxSlot) {
		this.maxSlot = maxSlot;
		ChannelHolder.put(this);
	}
	
	public void setSlotLimit(boolean b) {
		this.slotLimit = b;
	}
	
	public void setFriendlyFire(boolean b) {
		this.friendlyFire = b;
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
	
	public int getJoinedSize() {
		return Prefix.getChannelSize(this.name);
	}
	
	public HashMap<UUID,String> getMemberList() {
		return member;
	}
	
	public boolean isPlayerAlreadyJoined(UUID uuid) {
		return Prefix.isPlayerAlreadyJoined(this.name,uuid);
	}
	
	public abstract boolean setPrefix(UUID setter,UUID member,String prefix);
	public abstract ErrorState setPrefixChatPrefix(UUID setter,String prefix,String chatPrefix);
	public abstract boolean setChannelChatPrefix(UUID setter,String prefix);
	public abstract ErrorState addPrefix(UUID setter,String prefix,boolean makeDefault);
	public abstract ErrorState removePrefix(UUID setter,String prefix);
	public abstract String getPrefix(UUID member);
	public abstract String getPrefixChatPrefix(String prefix);
	public abstract String getChannelChatPrefix();
	
	public abstract void upgradeSlot(int amount);
	public abstract void setUpgradeCost(int cost);
	public abstract void setNextUpgradeCost(int cost);
	public abstract void setUpgradeCostRate(float rate);
	public abstract void setMaxUpgradableSlot(int slot);
	public abstract int getMaxUpgradableSlot();
	public abstract int getCurrentUpgradeCost();
	public abstract int getNextUpgradeCost();
	

}
