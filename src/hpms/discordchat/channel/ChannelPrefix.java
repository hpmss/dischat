package hpms.discordchat.channel;

import java.util.UUID;

import hpms.discordchat.data.Prefix;

public abstract class ChannelPrefix extends Channel{
	
	public ChannelPrefix(String name, UUID leader,boolean getFlag) {
		super(name, leader,getFlag);
	}
	
	public boolean setPrefix(UUID setter,UUID member,String prefix) {
		if(!Prefix.isPrefix(prefix, this)) return false;
		if(!setter.equals(this.leader)) return false;
		if(this.member.containsKey(member)) {
			this.member.put(member,prefix);
		}
		Prefix.update(this, member, prefix);
		return true;
	}
	
	public boolean setChannelChatPrefix(UUID setter,String prefix) {
		if(setter.equals(this.leader)) {
			Prefix.setChannelChatPrefix(this.getChannelName(), prefix);
			return true;
		}
		return false;
	}
	
	public void addPrefix(UUID setter,String prefix,boolean makeDefault) {
		if(setter.equals(this.leader)) {
			Prefix.addPrefix(this.getChannelName(), prefix,makeDefault);
		}
	}
	
	public void removePrefix(UUID setter,String prefix) {
		if(!Prefix.isPrefix(prefix, this)) return;
		if(setter.equals(this.leader)) {
			
		}
	}
	
	public String getPrefix(UUID member) {
		return Prefix.getPrefix(member, this);
	}
	
	public String getChannelChatPrefix() {
		return Prefix.getChannelChatPrefix(this.getChannelName());
	}

}
