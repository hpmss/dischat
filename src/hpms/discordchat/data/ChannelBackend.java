package hpms.discordchat.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import hpms.discordchat.api.ChannelBackendAPI;
import hpms.discordchat.channel.Channel;
import hpms.discordchat.utils.ErrorState;
import hpms.discordchat.utils.Validator;
import net.md_5.bungee.api.ChatColor;

public class ChannelBackend extends ChannelBackendAPI {
	
	public static void initBackend() {
		new ChannelBackend();
	}
	
	public ChannelBackend() {
		ChannelBackendAPI.setAPI(this);
	}

	public Channel createNewChannel(String name, UUID leader, boolean getFlag) {
		if(name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(leader == null) {
				Channel channel = new Channel(name,null,getFlag);
				ChannelData.cacheChannel(channel);
				return null;
			}else {
				Bukkit.getPlayer(leader).sendMessage(ChatColor.RED + "You are not allowed to create default channel.");
				return null;
			}
		}else {
			OfflinePlayer player = Bukkit.getOfflinePlayer(leader);
			if(ChannelData.isPlayerLeader(leader))  {
				if(player.isOnline()) {
					if(!player.getPlayer().hasPermission("discordchat.multiplechannel")) {
						player.getPlayer().sendMessage(ChatColor.YELLOW + "You are already a leader of channel.");
						return null;
					}
				}
			}
			if(this.getPlayerCurrentChannel(leader) != null) {
				this.getPlayerCurrentChannel(leader).removeMember(leader,false);
			}
			Channel channel = new Channel(name,leader,getFlag);
			ChannelData.cacheChannel(channel);
			ChannelData.cacheLeader(leader.toString());
			return channel;
		}
		
	}

	public Channel getChannelByName(String channelName) {
		return (Channel) ChannelData.getChannel(channelName);
	}

	public Channel getPlayerCurrentChannel(UUID player) {
		return getChannelByName(ChannelData.getPlayerCurrentChannel(player));
	}

	public String getPlayerCurrentChannelName(UUID player) {
		return ChannelData.getPlayerCurrentChannel(player);
	}

	public boolean setChannelChatPrefix(UUID setter, String channelName, String prefix) {
		Channel channel = getChannelByName(channelName);
		if(channel == null) return false;
		return channel.setChannelChatPrefix(setter,prefix);
	}

	public boolean setChannelPlayerRole(UUID setter, UUID member, String name, String role) {
		Channel channel = getChannelByName(name);
		if(channel == null) return false;
		return channel.setRole(setter, member, role);
	}

	public void joinChannel(UUID playerUID, String channel,boolean bypass) {
		ErrorState state = ChannelData.setPlayerCurrentChannel(playerUID,channel,bypass);
		if(Validator.isPlayerOnline(playerUID)) {
			Player player = Bukkit.getPlayer(playerUID);
			switch(state) {
			case MATCHED:
				player.sendMessage(ChatColor.YELLOW + "You are already in \'" + ChatColor.AQUA +channel + ChatColor.YELLOW + "\' channel .");
				break;
			case NO_EXISTENCE:
				player.sendMessage(ChatColor.YELLOW + "Channel \'" + ChatColor.AQUA + channel +ChatColor.YELLOW + "\' doesnt exist .");
				break;
			case SUCCESS:
				if(!channel.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
					player.sendMessage(ChatColor.AQUA + "\'" + channel + "\'" + ChatColor.YELLOW + " channel joined.");
				}
				break;
			case OUT_OF_BOUND:
				player.sendMessage(ChatColor.YELLOW + "Channel reached maximum size.");
				break;
			default:
				break;
			}
		}
	}

	public boolean removeChannel(UUID setter,String channelName) {
		Channel channel = getChannelByName(channelName);
		if(channel != null) {
			if(channel.getLeader().equals(setter) || Bukkit.getPlayer(setter).hasPermission("dc.godremovechannel")) {
				Role.remove(channelName);
				ChannelData.remove(channelName);
				return true;
			}
		}
		return false;
		
	}
	

}
