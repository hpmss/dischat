package hpms.discordchat.api;

import java.util.Map;
import java.util.UUID;

import hpms.discordchat.utils.ErrorState;

public interface ChannelUnit {
	
	boolean addMember(UUID member);
	
	boolean removeMember(UUID member,boolean hard);
	
	void overrideMember(Map<? extends Object,? extends Object> memberList);
	
	String getChannelName();
	
	UUID getLeader();
	
	int getMaxSize();
	
	int getCurrentSize();
	
	int getJoinedSize();
	
	Map<UUID,String> getMemberList();
	
	void setChannelName(String name);
	
	void setLeader(UUID leader);
	
	void setLeaderChatPrefix(String prefix);
	
	void setMaxSlot(int maxSlot);
	
	void setSlotLimit(boolean b);
	
	void setFriendlyFire(boolean b);
	
	boolean isPlayerAlreadyJoined(UUID uuid);
	
	
	String getRole(UUID member);
	
	String getRolePrefix(String prefix);
	
	String getChannelChatPrefix();
	
        boolean setRole(UUID setter,UUID member,String prefix);
    
        boolean setChannelChatPrefix(UUID setter,String prefix);
    
	ErrorState setRolePrefix(UUID setter,String prefix,String chatPrefix);
	
	ErrorState addRole(UUID setter,String prefix,boolean makeDefault);
	
	ErrorState removeRole(UUID setter,String prefix);
	
	
	int getMaxUpgradableSlot();
	
	int getCurrentUpgradeCost();
	
	int getNextUpgradeCost();
	
	void upgradeSlot(int amount);
	
	void setUpgradeCost(int cost);
	
	void setNextUpgradeCost(int cost);
	
	void setUpgradeCostRate(float rate);
	
	void setMaxUpgradableSlot(int slot);
	
	
	void requestTeleportation(UUID receiver,UUID requester);
	
	void requestInventorySharing(UUID receiver,UUID requester);
	
	void acceptTeleportation(UUID receiver,UUID requester);
	
	void acceptInventorySharing(UUID receiver,UUID requester);
	
	void toggleTeleportation(UUID receiver);
	
	void toggleExpSharing(UUID member);

	void toggleInventorySharing(UUID member);
	
}
