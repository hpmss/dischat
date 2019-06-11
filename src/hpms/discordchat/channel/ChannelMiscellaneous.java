package hpms.discordchat.channel;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import hpms.discordchat.inv.InventoryLinker;
import hpms.discordchat.utils.ShortermRequest;
import hpms.discordchat.utils.Validator;
import net.md_5.bungee.api.ChatColor;

public abstract class ChannelMiscellaneous extends ChannelEconomy{

	public ChannelMiscellaneous(String name, UUID leader, boolean getFlag) {
		super(name, leader, getFlag);
	}

	public boolean requestTeleportation(UUID receiver, UUID requester) {
		if(receiver.equals(requester)) {
			Bukkit.getPlayer(receiver).sendMessage(ChatColor.YELLOW + "You cant teleport to yourself.");
			return false;
		}
		if(Validator.isPlayerOnlineAndJoined(receiver,this.name) && Validator.isPlayerOnlineAndJoined(requester, this.name)) {
			if(Validator.arePlayersSameChannel(receiver, requester)) {
				ShortermRequest request = ShortermRequest.createRequest(Bukkit.getPlayer(requester), Bukkit.getPlayer(receiver),"teleport");
				if(request.isSent()) {
					return false;
				}
				return true;
			}

		}
		return false;
	}
	
	public boolean requestInventorySharing(UUID receiver, UUID requester) {
		if(receiver.equals(requester)) {
			Bukkit.getPlayer(receiver).sendMessage(ChatColor.YELLOW + "You cant share with yourself.");
			return false;
		}
		if(Validator.isPlayerOnlineAndJoined(receiver,this.name) && Validator.isPlayerOnlineAndJoined(requester, this.name)) {
			if(Validator.arePlayersSameChannel(receiver, requester)) {
				ShortermRequest request = ShortermRequest.createRequest(Bukkit.getPlayer(requester), Bukkit.getPlayer(receiver),"inventory");
				if(request.isSent()) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public boolean acceptTeleportation(UUID receiver) {
		if(Validator.isPlayerOnlineAndJoined(receiver, this.name)) {
			Player receiv = Bukkit.getPlayer(receiver);
			ShortermRequest.acceptRequest(receiv,"teleport");
			return true;
		}
		return false;
		
	}
	
	public boolean acceptInventorySharing(UUID receiver) {
		if(Validator.isPlayerOnlineAndJoined(receiver, this.name)) {
			Player receiv = Bukkit.getPlayer(receiver);
			ShortermRequest.acceptRequest(receiv,"inventory");
			return true;
		}
		return false;
	}
	
	public boolean stopInventorySharing(UUID player) {
		if(Validator.isPlayerOnlineAndJoined(player, this.name)) {
			Player p = Bukkit.getPlayer(player);
			InventoryLinker.removePlayerSharingInventory(this.name, p);
			return true;
		}
		return false;
	}

	public boolean toggleTeleportation(UUID receiver) {
		return false;
	}

	public boolean toggleExpSharing(UUID member) {
		return false;
	}

	public boolean toggleInventorySharing(UUID member) {
		return false;
	}



}
