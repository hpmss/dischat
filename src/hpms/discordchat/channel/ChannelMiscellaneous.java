package hpms.discordchat.channel;

import java.util.UUID;

import hpms.discordchat.utils.Validator;

public class ChannelMiscellaneous extends ChannelEconomy{

	public ChannelMiscellaneous(String name, UUID leader, boolean getFlag) {
		super(name, leader, getFlag);
	}

	public void requestTeleportation(UUID receiver, UUID requester) {
		if(Validator.isPlayerOnline(receiver) && Validator.isPlayerOnline(requester)) {
			
		}else {
			
		}
	}

	public void requestTrading(UUID receiver, UUID requester) {
		
	}
	
	public void requestInventorySharing(UUID receiver, UUID requester) {
	}

	public void acceptTeleportation(UUID receiver, UUID requester) {
		
	}

	public void acceptTrading(UUID receiver, UUID requester) {
		
	}
	
	public void acceptInventorySharing(UUID receiver, UUID requester) {
	}

	public void toggleTeleportation(UUID receiver) {
		
	}

	public void toggleExpSharing(UUID member) {
		
	}

	public void toggleInventorySharing(UUID member) {
		
	}



}
