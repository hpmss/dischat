package hpms.discordchat.channel;

import org.bukkit.entity.Player;

public abstract class ChannelPrefix extends Channel{
	
	public ChannelPrefix(String name, Player leader,boolean perm) {
		super(name, leader,perm);
	}
	
	public void setPrefix(Player member) {
		
	}
	
	public String getPrefix(Player member) {
		return "";
	}

}
