package hpms.discordchat.inv;

import java.util.Iterator;
import java.util.LinkedList;
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

import hpms.discordchat.api.MenuUnit;

public class SharingInventory implements MenuUnit{
	
	/*
	 * Hot-bar independent
	 */
	
	private static Map<String,SharingInventory> tracker = Maps.newHashMap();
	
	private LinkedList<String> players = new LinkedList<String>();
	private Map<String,String> currentPointer = Maps.newHashMap();
	private Map<String,ItemStack[]> storageTracker = Maps.newHashMap();
	
	public SharingInventory(Player... players) {
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
		if(!this.players.contains(player.getUniqueId().toString())) return;
		int i = players.indexOf(player.getUniqueId().toString());
		try {
			Player nextPlayer = Bukkit.getServer().getPlayer(UUID.fromString(players.get(i + 1)));
			this.currentPointer.put(player.getUniqueId().toString(), nextPlayer.getName());
			ItemStack[] storage = this.storageTracker.get(nextPlayer.getName());
			storage = this.fillin(player,storage);
			player.getInventory().setStorageContents(storage);
		}catch(IndexOutOfBoundsException  e) {}
	}

	public void close(Player player) {
		if(!this.players.contains(player.getUniqueId().toString())) return;
		int i = players.indexOf(player.getUniqueId().toString());
		try {
			Player previousPlayer = Bukkit.getServer().getPlayer(UUID.fromString(players.get(i - 1)));
			this.currentPointer.put(player.getUniqueId().toString(), previousPlayer.getName());
			ItemStack[] storage = this.storageTracker.get(previousPlayer.getName());
			storage = this.fillin(player, storage);
			player.getInventory().setStorageContents(storage);
		}catch(IndexOutOfBoundsException e) {}
	}
	
	public void add(Player player) {
		if(!this.players.contains(player.getUniqueId().toString())) this.players.add(player.getUniqueId().toString());
	}
	
	public void remove(Player player) {
		if(this.players.contains(player.getUniqueId().toString())) this.players.remove(player.getUniqueId().toString());
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
			break;
		case DROP_ALL_CURSOR:
			if(e.getSlotType() == SlotType.QUICKBAR) return;
			break;
		case DROP_ALL_SLOT:
			break;
		case DROP_ONE_CURSOR:
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
			else if(e.getCurrentItem().getType() != Material.AIR) {
				ItemStack[] playerStorage = player.getInventory().getStorageContents();
				/*
				 * TODO
				 * Hot-bar swap
				 */
				if(playerStorage[e.getHotbarButton()] != null) {
					
				}
			}
			else {
				if(storage[e.getHotbarButton()] != null) {
					ItemStack hotbarItem = storage[e.getHotbarButton()].clone();
					storage[e.getHotbarButton()] = null;
					storage[slot] = hotbarItem;
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
