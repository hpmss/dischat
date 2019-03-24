package hpms.discordchat.inv;

import java.util.LinkedList;
import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import hpms.discordchat.api.MenuUnit;

public class SharingInventory implements MenuUnit{
	
	private static Map<String,SharingInventory> tracker = Maps.newHashMap();
	
	private LinkedList<Player> players = new LinkedList<Player>();
	
	public SharingInventory(Player... players) {
		if(players.length != 0) {
			for(Player p : players) {
				this.players.add(p);
			}
		}
	}
	
	public void open(Player player) {
		if(!this.players.contains(player)) return;
		int i = players.indexOf(player);
	}

	public void close(Player player) {
		if(!this.players.contains(player)) return;
		int i = players.indexOf(player);
	}
	
	public void add(Player player) {
		if(!this.players.contains(player)) this.players.add(player);
	}
	
	public void remove(Player player) {
		if(this.players.contains(player)) this.players.remove(player);
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

}
