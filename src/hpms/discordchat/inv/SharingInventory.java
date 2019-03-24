package hpms.discordchat.inv;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import hpms.discordchat.api.MenuUnit;

public class SharingInventory implements MenuUnit{
	
	private static Map<String,SharingInventory> tracker = Maps.newHashMap();
	
	private LinkedList<String> players = new LinkedList<String>();
	private Map<String,String> currentPointer = Maps.newHashMap();
	private Map<String,ItemStack[]> storageTracker = Maps.newHashMap();
	
	public SharingInventory(Player... players) {
		if(players.length != 0) {
			for(Player p : players) {
				this.currentPointer.put(p.getUniqueId().toString(), p.getName());
				this.storageTracker.put(p.getName(), p.getInventory().getStorageContents());
				this.players.add(p.getUniqueId().toString());
			}
		}
	}
	
	public int getSize() {
		return 0;
	}

	public String getName() {
		return null;
	}

	public boolean isEditable() {
		return true;
	}
	
	public void open(Player player) {
		if(!this.players.contains(player.getUniqueId().toString())) return;
		int i = players.indexOf(player.getUniqueId().toString());
		try {
			Player nextPlayer = Bukkit.getServer().getPlayer(UUID.fromString(players.get(i + 1)));
			this.currentPointer.put(player.getUniqueId().toString(), nextPlayer.getName());
			ItemStack[] storage = this.storageTracker.get(nextPlayer.getName());
			player.getInventory().setStorageContents(storage);
		}catch(IndexOutOfBoundsException  e) {}
	}

	public void close(Player player) {
		if(!this.players.contains(player.getUniqueId().toString())) return;
		int i = players.indexOf(player.getUniqueId().toString());
	}
	
	public void add(Player player) {
		if(!this.players.contains(player.getUniqueId().toString())) this.players.add(player.getUniqueId().toString());
	}
	
	public void remove(Player player) {
		if(this.players.contains(player.getUniqueId().toString())) this.players.remove(player.getUniqueId().toString());
	}
	
	public void update(Player player) {
		String currentPointer = this.currentPointer.get(player.getUniqueId().toString());
		ItemStack[] item = this.storageTracker.get(currentPointer);
		Log.info(validateStorage(player,item));
		this.storageTracker.put(currentPointer, player.getInventory().getStorageContents());
		for(Entry<String,String> entry : this.currentPointer.entrySet()) {
			if(!entry.getKey().equalsIgnoreCase(player.getUniqueId().toString()) && entry.getValue().equalsIgnoreCase(currentPointer)) {
				Player user = Bukkit.getPlayer(UUID.fromString(entry.getKey()));
				user.getInventory().setStorageContents(this.storageTracker.get(currentPointer));
			}
		}
		this.debug();
	}
	
	private boolean validateStorage(Player player,ItemStack[] storage) {
		for(ItemStack item: player.getInventory().getStorageContents()) {
			if(item != null) {
				Log.info(item);
			}
			
		}
//		ItemStack[] validate = player.getInventory().getStorageContents();
//		for(int i = 0; i < storage.length;i++) {
//			if(validate[i] != null && storage[i] != null) {
//				Log.info(validate[i]);
//				Log.info(storage[i]);
//			}
//		}
		return true;
	}

	
	public static SharingInventory createSharingInventory(String invUID,Player... players) {
		if(tracker.containsKey(invUID)) return null;
		SharingInventory inv = new SharingInventory(players);
		tracker.put(invUID, inv);
		return inv;
	}
	
	public static SharingInventory getSharingInventory(String invUID) {
		if(invUID == null) return null;
		return tracker.get(invUID);
	}
	
	public void debug() {
		Log.info("---SharingInventory---");
		Log.info(this.currentPointer);
		Log.info(this.storageTracker);
	}

}
