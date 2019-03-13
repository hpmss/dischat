package hpms.discordchat.channel;

import org.bukkit.entity.Player;

public abstract class ChannelPrefix extends Channel{
	
	public ChannelPrefix(String name, Player leader,boolean perm) {
		super(name, leader,perm);
	}
	
	public void setPrefix(Player member,String prefix) {
		if(this.member.containsKey(member.getUniqueId())) {
			this.member.put(member.getUniqueId(),prefix);
		}
	}
	
	public String getPrefix(Player member) {
		if(this.member.containsKey(member.getUniqueId())) {
			return this.member.get(member.getUniqueId());
		}
		return "";
	}

}
