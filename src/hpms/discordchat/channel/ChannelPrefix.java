package hpms.discordchat.channel;

import java.util.UUID;

import hpms.discordchat.data.ChannelHolder;
import hpms.discordchat.data.Prefix;
import hpms.discordchat.utils.ErrorState;

public abstract class ChannelPrefix extends Channel{
	
	public ChannelPrefix(String name, UUID leader,boolean getFlag) {
		super(name, leader,getFlag);
	}
	
	public boolean setPrefix(UUID setter,UUID member,String prefix) {
		if(!Prefix.isPrefix(this.name, prefix)) return false;
		if(!setter.equals(this.leader)) return false;
		if(this.member.containsKey(member)) {
			this.member.put(member,prefix);
		}
		Prefix.update(this.name, member, prefix);
		ChannelHolder.put(this);
		return true;
	}
	
	public ErrorState setPrefixChatPrefix(UUID setter,String prefix,String chatPrefix) {
		if(setter.equals(this.leader)) {
			return Prefix.setPrefixChatPrefix(this.name, prefix, chatPrefix);
		}
		return ErrorState.NULL;
	}
	
	public boolean setChannelChatPrefix(UUID setter,String prefix) {
		if(setter.equals(this.leader)) {
			Prefix.setChannelChatPrefix(this.name, prefix);
			return true;
		}
		return false;
	}
	
	public ErrorState addPrefix(UUID setter,String prefix,boolean makeDefault) {
		if(setter.equals(this.leader)) {
			return Prefix.addPrefix(this.name, prefix,makeDefault);
		}
		return ErrorState.NULL;
	}
	
	public ErrorState removePrefix(UUID setter,String prefix) {
		if(setter.equals(this.leader)) {
			return Prefix.removePrefix(this.name, prefix);
		}
		return ErrorState.NULL;
	}
	
	public String getPrefix(UUID member) {
		return Prefix.getPrefixFromPlayer(member, this.name);
	}
	
	public String getPrefixChatPrefix(String prefix) {
		return Prefix.getPrefixChatPrefix(this.name, prefix);
	}
	
	public String getChannelChatPrefix() {
		return Prefix.getChannelChatPrefix(this.name);
	}

}
