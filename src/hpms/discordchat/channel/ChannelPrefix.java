package hpms.discordchat.channel;

import java.util.UUID;

import org.bukkit.entity.Player;

import hpms.discordchat.data.Prefix;

public abstract class ChannelPrefix extends Channel{
	
	public ChannelPrefix(String name, UUID leader,boolean getFlag) {
		super(name, leader,getFlag);
	}
	
	public void setPrefix(Player member,String prefix) {
		if(!Prefix.isPrefix(prefix, this)) return;
		if(this.member.containsKey(member.getUniqueId())) {
			this.member.put(member.getUniqueId(),prefix);
			Prefix.update(this, member, prefix);
		}
	}
	
	public void setChannelChatPrefix(String prefix) {
		Prefix.setChannelChatPrefix(this.getChannelName(), prefix);
	}
	
	public String getPrefix(Player member) {
		return Prefix.getPrefix(member, this);
	}
	
	public String getChannelChatPrefix() {
		return Prefix.getChannelChatPrefix(this.getChannelName());
	}

}
