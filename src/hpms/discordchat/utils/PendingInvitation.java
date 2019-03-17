package hpms.discordchat.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PendingInvitation{
	
	/* TODO
	 * Multiple invitations
	 */
	
	private JavaPlugin plugin;
	private HashMap<UUID,Boolean> requester = new HashMap<UUID,Boolean>();
	private Player receiver;
	private Player previousRequester;
	private int expiration;
	
	public PendingInvitation(JavaPlugin plugin,Player receiver, int expiration) {
		this.plugin = plugin;
		this.receiver = receiver;
		this.expiration = expiration;
	}
	
	public void addRequester(Player requester) {
		this.requester.put(requester.getUniqueId(), false);
		this.previousRequester = requester;
		invalidate();
	}
	
	public boolean acceptInvitation(Player player) {
		if(!this.requester.containsKey(player.getUniqueId())) return false;
		this.requester.put(player.getUniqueId(), true);
		return true;
	}
	
	public ErrorState isAccepted(Player player) {
		if(!this.requester.containsKey(player.getUniqueId())) return ErrorState.NO_EXISTENCE;
		return (requester.get(player.getUniqueId()) == true) ? ErrorState.SUCCESS : ErrorState.FAIL;
	}
	
	public Player getReceiver() {
		return this.receiver;
	}
	
	private void invalidate() {
		new TaskHandler(this.plugin,expiration,0) {
			private Player player = previousRequester;
				public void run() {
					requester.remove(player.getUniqueId());
				}
			};
	}
	
	

}
