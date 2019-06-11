package hpms.discordchat.api;

import java.util.UUID;

import hpms.discordchat.channel.Channel;

public abstract class ChannelBackendAPI {
	
	private static ChannelBackendAPI api;
	
	public static ChannelBackendAPI getAPI() {
		if(api == null) {
			throw new RuntimeException("DiscordChat is not enabled ?");
		}else {
			return api;
		}
	}
	
	public static void setAPI(ChannelBackendAPI backend) {
		api = backend;
	}
	
	public abstract Channel createNewChannel(String name,UUID leader,boolean getFlag);
	
	public abstract Channel getChannelByName(String channelName);
	
	public abstract Channel getPlayerCurrentChannel(UUID player);
	
	public abstract String getPlayerCurrentChannelName(UUID player);
	
	public abstract boolean setChannelChatPrefix(UUID setter,String channelName,String prefix);
	
	public abstract boolean setChannelPlayerRole(UUID setter,UUID member,String name,String rank);
	
	public abstract void joinChannel(UUID player,String channelName,boolean bypass);
	
	public abstract boolean removeChannel(UUID setter,String channelName);
}
