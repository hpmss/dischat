package hpms.discordchat.channel;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import hpms.discordchat.utils.ShortermRequest;
import hpms.discordchat.utils.Validator;

public class ChannelMiscellaneous extends ChannelEconomy{

	public ChannelMiscellaneous(String name, UUID leader, boolean getFlag) {
		super(name, leader, getFlag);
	}

	public boolean requestTeleportation(UUID receiver, UUID requester) {
		if(receiver.equals(requester)) return false;
		if(Validator.isPlayerOnlineAndJoined(receiver,this.name) && Validator.isPlayerOnlineAndJoined(requester, this.name)) {
			if(Validator.arePlayersSameChannel(receiver, requester)) {
				ShortermRequest request = ShortermRequest.createRequest(Bukkit.getPlayer(requester), Bukkit.getPlayer(receiver));
				if(request.isSent()) {
					return false;
				}
				return true;
			}

		}
		return false;
	}
	
	public boolean requestInventorySharing(UUID receiver, UUID requester) {
		if(receiver.equals(requester)) return false;
		if(Validator.isPlayerOnlineAndJoined(receiver,this.name) && Validator.isPlayerOnlineAndJoined(requester, this.name)) {
			if(Validator.arePlayersSameChannel(receiver, requester)) {
				ShortermRequest request = ShortermRequest.createRequest(Bukkit.getPlayer(requester), Bukkit.getPlayer(receiver));
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
			if(Validator.arePlayersSameChannel(receiver, receiv.getUniqueId())) {
				ShortermRequest.acceptRequest(receiv,"teleport");
				return true;
			}
		}
		return false;
		
	}
	
	public boolean acceptInventorySharing(UUID receiver) {
		if(Validator.isPlayerOnlineAndJoined(receiver, this.name)) {
			Player receiv = Bukkit.getPlayer(receiver);
			if(Validator.arePlayersSameChannel(receiver, receiv.getUniqueId())) {
				ShortermRequest.acceptRequest(receiv,"inventory");
				return true;
			}
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
