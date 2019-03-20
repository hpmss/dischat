package hpms.discordchat.channel;

import java.util.UUID;

import hpms.discordchat.utils.Validator;

public class ChannelMiscellaneous extends ChannelEconomy{

	public ChannelMiscellaneous(String name, UUID leader, boolean getFlag) {
		super(name, leader, getFlag);
	}

	public boolean requestTeleportation(UUID receiver, UUID requester) {
		if(Validator.isPlayerOnline(receiver) && Validator.isPlayerOnline(requester)) {
			if(this.isPlayerAlreadyJoined(receiver) && this.isPlayerAlreadyJoined(requester)) {
				
				
				
				return true;
			}
		}
		return false;
	}
	
	public boolean requestInventorySharing(UUID receiver, UUID requester) {
		return false;
	}

	public boolean acceptTeleportation(UUID receiver, UUID requester) {
		return false;
		
	}
	
	public boolean acceptInventorySharing(UUID receiver, UUID requester) {
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
