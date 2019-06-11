package hpms.discordchat.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.api.ChannelUnit;
import hpms.discordchat.data.ChannelData;
import hpms.discordchat.data.ChannelDataConstant;
import hpms.discordchat.data.Role;
import hpms.discordchat.utils.Validator;

public abstract class ChannelCore implements ChannelUnit{
	
	protected UUID leader;
	protected String name;
	protected HashMap<UUID,String> member;
	protected int maxSlot = ChannelData.DEFAULT_SLOT;
	protected boolean slotLimit = true;
	
	/**
	 * 
	 * @param getFlag - In case the channel was already created getFlag == false indicates that the channel is being freshly created.
	 * 					While getFlag == true indicates that the channel should only be retrieved back to be cached not to be newly create.
	 */
	
	public ChannelCore(String name,UUID leader,boolean getFlag) {
		this.name = name;
		this.member = new HashMap<UUID,String>();
		if(!name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			this.leader = leader;
			OfflinePlayer player = Bukkit.getOfflinePlayer(leader);
			if(player.isOnline()) {
				if(!player.getPlayer().hasPermission("discordchat.overridechannel") && getFlag == false) {
					Validator.isTrue(!ChannelData.isChannelExisted(name),"Channel " + name + " already existed.");
				}
			}
		}else {
			this.leader = null;
			this.slotLimit = false;
			this.maxSlot = 999;
		}
		Role.put(this,getFlag);
		if(getFlag == false && leader != null) {
			this.member.put(this.leader, Role.getLeaderPrefix(this.name));
		}
		ChannelData.put(this);
		
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
		return Role.getChannelSize(this.name);
	}
	
	public Map<UUID,String> getMemberList() {
		return member;
	}
	
	public boolean addMember(UUID member) {
		String prefix = Role.getRoleFromPlayer(member, this.name);
		if(prefix == null) {
			prefix = Role.getInitialRole(this.name);
		}
		if(this.isPlayerAlreadyJoined(member)) {
			this.member.put(member, prefix);
			ChannelData.put(this);
			return true;
		}
		if(!(this.getJoinedSize() < this.maxSlot) && slotLimit) return false;
		this.member.put(member,prefix);
		ChannelData.put(this);
		Role.update(this.name,member,prefix);
		return true;
	}
	
	public void overrideMember(Map<? extends Object,? extends Object> memberList) {
		if(memberList.size() != 0) {
			for(Entry<? extends Object,? extends Object> entry : memberList.entrySet()) {
				member.put(UUID.fromString(entry.getKey().toString()), entry.getValue().toString());
			}
		}
		ChannelData.put(this);
	}
	
	public boolean removeMember(UUID uuid,boolean hard) {
		if(hard) {
			Role.removePlayer(this.name, uuid);
			ChannelAPI.joinChannel(uuid, ChannelDataConstant.DEFAULT_CHANNEL, true);
		}
		if(this.member.containsKey(uuid)) {
			this.member.remove(uuid);
			ChannelData.put(this);
			return true;
		}
		return false;
	}
	
	public void setChannelName(String name) {
		ChannelData.remove(this.name);
		this.name = name;
		ChannelData.put(this);
	}
	
	public void setLeader(UUID newLeader) {
		if(!this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			String leaderPrefix = Role.getLeaderPrefix(this.name);
			String initRole = Role.getInitialRole(this.name);
			Role.update(this.name, this.leader, initRole);
			this.member.put(this.leader, initRole);
			ChannelData.removeLeader(this.leader.toString());
			this.leader = newLeader;
			ChannelData.cacheLeader(this.leader.toString());
			if(this.member.containsKey(this.leader)) {
				this.member.put(this.leader, leaderPrefix);
			}
			ChannelData.put(this);
			Role.update(this.name, this.leader, leaderPrefix);
	
		}
	}
	
	public void setLeaderChatPrefix(String prefix) {
		if(this.member.containsKey(leader)) {
			this.member.put(this.leader, prefix);
		}
		ChannelData.put(this);
	}
	
	public void setMaxSlot(int maxSlot) {
		this.maxSlot = maxSlot;
		ChannelData.put(this);
	}
	
	public void setSlotLimit(boolean b) {
		this.slotLimit = b;
	}
	
	public boolean isPlayerAlreadyJoined(UUID uuid) {
		return Role.isPlayerAlreadyJoined(this.name,uuid);
	}

}
