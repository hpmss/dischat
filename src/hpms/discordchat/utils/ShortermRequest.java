package hpms.discordchat.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class ShortermRequest {
	
	private int id;
	
	private Player requester;
	
	private Player receiver;
	
	private boolean accepted = false;
	
	public ShortermRequest(JavaPlugin plugin,Player requester,Player receiver) {
		this.requester = requester;
		this.receiver = receiver;
		this.precondition();
		this.receiver.setMetadata(requester.getDisplayName(), new FixedMetadataValue(plugin,"request"));
		this.requester.setMetadata(receiver.getDisplayName(), new FixedMetadataValue(plugin,"request"));
		this.request(plugin);
	}
	
	private void precondition() {
		if(requester.hasMetadata(receiver.getDisplayName()) && receiver.hasMetadata(requester.getDisplayName())) {
			this.accepted = true;
		}
	}
	
	private void request(JavaPlugin plugin) {
		this.id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			private int second = 0;
			public void run() {
				second++;
				if(second == 10) {
					receiver.removeMetadata("request", plugin);
					requester.removeMetadata("request", plugin);
					Bukkit.getServer().getScheduler().cancelTask(id);
				}
			}
		}, 20, 20);
	}
	
}
