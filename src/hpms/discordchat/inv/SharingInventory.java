package hpms.discordchat.inv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import hpms.discordchat.api.MenuUnit;
import net.md_5.bungee.api.ChatColor;

public class SharingInventory implements MenuUnit{
	
	private static Map<String,SharingInventory> tracker = Maps.newHashMap();
	
	private String uid;
	private String channel;
	
	private ArrayList<String> players = new ArrayList<String>();
	private Map<String,String> currentPointer = Maps.newHashMap();
	private Map<String,ItemStack[]> storageTracker = Maps.newHashMap();
	
	public SharingInventory(String channel,String uid,Player... players) {
		this.uid = uid;
		this.channel = channel;
		if(players.length != 0) {
			for(Player p : players) {
				this.currentPointer.put(p.getUniqueId().toString(), p.getName());
				this.storageTracker.put(p.getName(), this.fillout(p.getInventory().getStorageContents()));
				this.players.add(p.getUniqueId().toString());
			}
		}
	}
	
	public int getSize() {
		return players.size();
	}

	public String getName() {
		return null;
	}

	public boolean isEditable() {
		return true;
	}
	
	public void open(Player player) {
		int i = this.players.indexOf(player.getUniqueId().toString());
		String currentPointer = this.currentPointer.get(player.getUniqueId().toString());
		try {
			if(currentPointer.equalsIgnoreCase(player.getName())) {
				Player nextPlayer = Bukkit.getServer().getPlayer(UUID.fromString(players.get(i + 1)));
				this.currentPointer.put(player.getUniqueId().toString(), nextPlayer.getName());
				ItemStack[] storage = this.storageTracker.get(nextPlayer.getName());
				storage = this.fillin(player,storage);
				player.getInventory().setStorageContents(storage);
			}else {
				i = this.players.indexOf(Bukkit.getPlayer(currentPointer).getUniqueId().toString()) + 1;
				Player nextPlayer = Bukkit.getServer().getPlayer(UUID.fromString(players.get(i)));
				this.currentPointer.put(player.getUniqueId().toString(),nextPlayer.getName());
				ItemStack[] storage = this.storageTracker.get(nextPlayer.getName());
				storage = this.fillin(player,storage);
				player.getInventory().setStorageContents(storage);
			}
		}catch(IndexOutOfBoundsException  e) {player.sendMessage(ChatColor.YELLOW + "You reached the top.");}
	}

	public void close(Player player) {
		int i = players.indexOf(player.getUniqueId().toString());
		String currentPointer = this.currentPointer.get(player.getUniqueId().toString());
		try {
			if(currentPointer.equalsIgnoreCase(player.getName())) {
				Player previousPlayer = Bukkit.getServer().getPlayer(UUID.fromString(players.get(i - 1)));
				this.currentPointer.put(player.getUniqueId().toString(), previousPlayer.getName());
				ItemStack[] storage = this.storageTracker.get(previousPlayer.getName());
				storage = this.fillin(player, storage);
				player.getInventory().setStorageContents(storage);
			}else {
				i = this.players.indexOf(Bukkit.getPlayer(currentPointer).getUniqueId().toString()) - 1;
				Player previousPlayer = Bukkit.getServer().getPlayer(UUID.fromString(players.get(i)));
				this.currentPointer.put(player.getUniqueId().toString(),previousPlayer.getName());
				ItemStack[] storage = this.storageTracker.get(previousPlayer.getName());
				storage = this.fillin(player,storage);
				player.getInventory().setStorageContents(storage);
			}
		}catch(IndexOutOfBoundsException e) {player.sendMessage(ChatColor.YELLOW + "You reached the bottom.");}
	}
	
	public void add(Player player) {
		if(!this.players.contains(player.getUniqueId().toString())) {
			this.players.add(player.getUniqueId().toString());
			this.currentPointer.put(player.getUniqueId().toString(), player.getName());
			this.storageTracker.put(player.getName(), this.fillout(player.getInventory().getStorageContents()));
		}
	}
	
	public void remove(Player player) {
		this.rollback(player);
		this.players.remove(player.getUniqueId().toString());
		this.currentPointer.remove(player.getUniqueId().toString());
		this.storageTracker.remove(player.getName());
		this.cleanup(this.uid);
	}
	
	public void updateClick(Player player,InventoryClickEvent e) {
		String currentPointer = this.currentPointer.get(player.getUniqueId().toString());
		ItemStack[] storage = player.getInventory().getStorageContents();
		storage = this.fillout(storage);
		int slot = e.getSlot();
		ItemStack slotItem = storage[slot];
		switch(e.getAction()) {
		case CLONE_STACK:
			break;
		case COLLECT_TO_CURSOR:
			/*
			 * Currently not supported
			 */
			player.getInventory().setItem(10, new ItemStack(Material.APPLE));
			player.getInventory().setItem(10, null);
			Log.info(player.getOpenInventory().getCursor());
			for(int i : storageDifferences(storage,this.storageTracker.get(currentPointer))) {
				Log.info(i);
			}
//			e.setCancelled(true);
			return;
		case DROP_ALL_CURSOR:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			break;
		case DROP_ALL_SLOT:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			break;
		case DROP_ONE_CURSOR:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			break;
		case DROP_ONE_SLOT:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			if(true) {
				ItemStack item = e.getCurrentItem().clone();
				item.setAmount(item.getAmount() - 1);
				slotItem = item;
			}
			storage[slot] = slotItem;
			break;
		case HOTBAR_MOVE_AND_READD:
			break;
		case HOTBAR_SWAP:
			if(slot == e.getHotbarButton()) return;
			else {
				ItemStack[] playerStorage = player.getInventory().getStorageContents();
				if(e.getCurrentItem().getType() != Material.AIR) {
					if(playerStorage[e.getHotbarButton()] != null) {
						storage[slot] = playerStorage[e.getHotbarButton()];
					}
					else {
						storage[slot] = null;
					}
				}
				else {
					if(playerStorage[e.getHotbarButton()] != null) {
						storage[slot] = playerStorage[e.getHotbarButton()];
					}
				}
			}
			break;
		case MOVE_TO_OTHER_INVENTORY:
			if(e.getSlotType() == SlotType.QUICKBAR) e.setCancelled(true);
			break;
		case PICKUP_ALL:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			storage[slot] = null;
			break;
		case PICKUP_HALF:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			slotItem.setAmount(slotItem.getAmount() - e.getCursor().getAmount());
			storage[slot] = slotItem;
			break;
		case PICKUP_ONE:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			slotItem.setAmount(slotItem.getAmount() - 1);
			storage[slot] = slotItem;
			break;
		case PICKUP_SOME:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			slotItem.setAmount(slotItem.getAmount() - e.getCursor().getAmount());
			storage[slot] = slotItem;
			break;
		case PLACE_ALL:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			if(e.getCurrentItem().getType() != Material.AIR) {
				ItemStack item = e.getCurrentItem().clone();
				item.setAmount(item.getAmount() + e.getCursor().getAmount());
				slotItem = item;
			}else {
				slotItem = e.getCursor();
			}
			storage[slot] = slotItem;
			break;
		case PLACE_ONE:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			if(e.getCurrentItem().getType() != Material.AIR) {
				ItemStack item = e.getCurrentItem().clone();
				item.setAmount(item.getAmount() + 1);
				slotItem = item;
			}else {
				ItemStack item = e.getCursor().clone();
				item.setAmount(1);
				slotItem = item;
			}
			storage[slot] = slotItem;
			break;
		case PLACE_SOME:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			if(true) {
				ItemStack item = e.getCurrentItem().clone();
				item.setAmount(64);
				slotItem = item;
			}
			storage[slot] = slotItem;
			break;
		case SWAP_WITH_CURSOR:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			if(true) {
				slotItem = e.getCursor();
			}
			storage[slot] = slotItem;
			break;
		default:
			return;
		}
		this.storageTracker.put(currentPointer,storage);
		this.syncClick(player.getUniqueId().toString(),currentPointer,slot);
	}
	
	public void updateDrag(Player player,Map<Integer,ItemStack> map) {
		if(map.size() != 0) {
			try {
				String currentPointer = this.currentPointer.get(player.getUniqueId().toString());
				ItemStack[] storage = player.getInventory().getStorageContents();
				storage = this.fillout(storage);
				Iterator<Entry<Integer,ItemStack>> iter = map.entrySet().iterator();
				while(iter.hasNext()) {
					Entry<Integer,ItemStack> entry = iter.next();
					storage[entry.getKey()] = entry.getValue();
				}
				this.storageTracker.put(currentPointer,storage);
				this.syncDrag(player.getUniqueId().toString(), currentPointer,map.keySet());
			}catch(IndexOutOfBoundsException e) {}
		}
	}
	
	public void rollback() {
		for(String uuid : this.players) {
			Player p = Bukkit.getPlayer(UUID.fromString(uuid));
			ItemStack[] storage = this.storageTracker.get(p.getName());
			storage = this.fillin(p, storage);
			p.getInventory().setStorageContents(storage);
		}
	}

	private void syncClick(String whitelist,String currentPointer,int slot) {
		Iterator<Entry<String,String>> iter = this.currentPointer.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String,String> entry = iter.next();
			if(!entry.getKey().equalsIgnoreCase(whitelist) && entry.getValue().equalsIgnoreCase(currentPointer)) {
				Player user = Bukkit.getPlayer(UUID.fromString(entry.getKey()));
				user.getInventory().setItem(slot, this.storageTracker.get(currentPointer)[slot]);
			}
		}
	}
	
	private void syncDrag(String whitelist,String currentPointer,Set<Integer> slots) {
		Iterator<Entry<String,String>> iter = this.currentPointer.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String,String> entry = iter.next();
			if(!entry.getKey().equalsIgnoreCase(whitelist) && entry.getValue().equalsIgnoreCase(currentPointer)) {
				Player user = Bukkit.getPlayer(UUID.fromString(entry.getKey()));
				for(Integer i : slots) {
					user.getInventory().setItem(i, this.storageTracker.get(currentPointer)[i]);
				}
			}
		}
	}
	
	private ItemStack[] fillout(ItemStack[] storage) {
		storage = storage.clone();
		storage[0] = null;
		storage[1] = null;
		storage[2] = null;
		storage[3] = null;
		storage[4] = null;
		storage[5] = null;
		storage[6] = null;
		storage[7] = null;
		storage[8] = null;
		return storage;
	}
	
	private ItemStack[] fillin(Player player,ItemStack[] storage) {
		storage = storage.clone();
		ItemStack[] playerStorage = player.getInventory().getStorageContents();
		storage[0] = playerStorage[0];
		storage[1] = playerStorage[1];
		storage[2] = playerStorage[2];
		storage[3] = playerStorage[3];
		storage[4] = playerStorage[4];
		storage[5] = playerStorage[5];
		storage[6] = playerStorage[6];
		storage[7] = playerStorage[7];
		storage[8] = playerStorage[8];
		return storage;
	}
	
	private void rollback(Player player) {
		ItemStack[] storage = this.storageTracker.get(player.getName());
		storage = this.fillin(player, storage);
		player.getInventory().setStorageContents(storage);
		for(Entry<String,String> entry : this.currentPointer.entrySet()) {
			if(!entry.getKey().equalsIgnoreCase(player.getUniqueId().toString()) && entry.getValue().equalsIgnoreCase(player.getName())) {
				Player user = Bukkit.getPlayer(UUID.fromString(entry.getKey()));
				storage = this.storageTracker.get(user.getName());
				storage = this.fillin(user, storage);
				user.getInventory().setStorageContents(storage);
				entry.setValue(user.getName());
			}
		}
	}
	
	private void cleanup(String invUID) {
		if(players.size() == 1) {
			tracker.remove(invUID);
			InventoryLinker.removePlayerFromFile(this.channel,Bukkit.getPlayer(UUID.fromString(this.players.get(0))));
			this.currentPointer.clear();
			this.players.clear();
			this.storageTracker.clear();
		}
	}
	
	private Set<Integer> storageDifferences(ItemStack[] storage1 , ItemStack[] storage2) {
		Set<Integer> set = Sets.newHashSet();
		for(int i = 9; i < storage1.length;i++) {
			if(storage1[i] == null && storage2[i] == null) continue;
			try {
				storage1[i].isSimilar(storage2[i]);
			}catch(NullPointerException e) {set.add(i);}
		}
		return set;
	}
	
	
	public static SharingInventory createSharingInventory(String channel,String invUID,Player... players) {
		if(tracker.containsKey(invUID)) return null;
		SharingInventory inv = new SharingInventory(channel,invUID,players);
		tracker.put(invUID, inv);
		return inv;
	}
	
	public static SharingInventory getSharingInventory(String invUID) {
		if(invUID == null) return null;
		return tracker.get(invUID);
	}
	
	public static Collection<SharingInventory> getSharingInventories() {
		return tracker.values();
	}
	
	public static void debug() {
		Log.info(tracker);
	}

}
