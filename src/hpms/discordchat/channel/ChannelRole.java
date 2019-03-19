package hpms.discordchat.channel;

import java.util.UUID;

import hpms.discordchat.data.ChannelData;
import hpms.discordchat.data.Role;
import hpms.discordchat.utils.ErrorState;

public abstract class ChannelRole extends ChannelCore{
	
	public ChannelRole(String name, UUID leader,boolean getFlag) {
		super(name, leader,getFlag);
	}
	
	public boolean setRole(UUID setter,UUID member,String role) {
		if(!Role.isRole(this.name, role)) return false;
		if(!setter.equals(this.leader)) return false;
		if(member.equals(this.leader)) return false;
		if(this.member.containsKey(member)) {
			this.member.put(member,role);
		}
		Role.update(this.name, member, role);
		ChannelData.put(this);
		return true;
	}
	
	public ErrorState setRolePrefix(UUID setter,String role,String prefix) {
		if(setter.equals(this.leader)) {
			return Role.setRolePrefix(this.name, role, prefix);
		}
		return ErrorState.NULL;
	}
	
	public boolean setChannelChatPrefix(UUID setter,String prefix) {
		if(setter.equals(this.leader)) {
			Role.setChannelChatPrefix(this.name, prefix);
			return true;
		}
		return false;
	}
	
	public ErrorState addRole(UUID setter,String role,boolean makeDefault) {
		if(setter.equals(this.leader)) {
			return Role.addRole(this.name, role,makeDefault);
		}
		return ErrorState.NULL;
	}
	
	public ErrorState removeRole(UUID setter,String prefix) {
		if(setter.equals(this.leader)) {
			return Role.removeRole(this.name, prefix);
		}
		return ErrorState.NULL;
	}
	
	public String getRole(UUID member) {
		return Role.getRoleFromPlayer(member, this.name);
	}
	
	public String getRolePrefix(String prefix) {
		return Role.getRolePrefix(this.name, prefix);
	}
	
	public String getChannelChatPrefix() {
		return Role.getChannelChatPrefix(this.name);
	}

}
