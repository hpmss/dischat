package hpms.discordchat.events;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.inv.InventoryLinker;
import hpms.discordchat.inv.SharingInventory;
import hpms.discordchat.item.ShareItem;

public class InventoryEvent implements Listener {

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getCurrentItem() == null) return;
		SharingInventory shareInv = InventoryLinker.getSharingInventory(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), p);
		if(e.getClickedInventory().getType() == InventoryType.PLAYER) {
			if(shareInv != null) {
				if(e.getCurrentItem().isSimilar(ShareItem.getNextItem())) {
					Log.info(e.getCursor());
					if(e.getCursor().getType() == Material.AIR) {
						shareInv.open(p);
						e.setCancelled(true);
					}else {
						InventoryView view = p.getOpenInventory();
						
					}
				}else if(e.getCurrentItem().isSimilar(ShareItem.getPreviousItem())) {
					if(e.getCursor().getType() == Material.AIR) {
						shareInv.close(p);
						e.setCancelled(true);
					}else {
						
					}
				}
				if(!e.getCurrentItem().isSimilar(ShareItem.getNextItem()) || !e.getCurrentItem().isSimilar(ShareItem.getPreviousItem())) {
					shareInv.updateClick(p, e);
				}
					
			}
		}
		
		
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		SharingInventory shareInv = InventoryLinker.getSharingInventory(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), p);
		if(shareInv != null) {
			Map<Integer,ItemStack> item = Maps.newHashMap();
			Iterator<Entry<Integer,ItemStack>> iter = e.getNewItems().entrySet().iterator();
			int filter = 9;
			if(e.getView().getTopInventory().getSize() == 54) {
				filter = 54;
			}
			else if(e.getView().getType() == InventoryType.CHEST || e.getView().getType() == InventoryType.ENDER_CHEST || e.getView().getType() == InventoryType.SHULKER_BOX) {
				filter = 27;
			}else if(e.getView().getType() == InventoryType.FURNACE || e.getView().getType() == InventoryType.BEACON) {
				filter = 1;
			}
			else if(e.getView().getType() == InventoryType.WORKBENCH) {
				filter = 10;
			}
			else if(e.getView().getType() == InventoryType.DISPENSER || e.getView().getType() == InventoryType.DROPPER) {
				filter = 9;
			}
			else if(e.getView().getType() == InventoryType.HOPPER) {
				filter = 5;
			}
			else if(e.getView().getType() == InventoryType.ANVIL || (p.getVehicle() != null && p.getVehicle().getType() == EntityType.HORSE)) {
				filter = 2;
			}
			else if(e.getView().getType() == InventoryType.BREWING) {
				filter = 4;
			}else if(e.getView().getType() == InventoryType.MERCHANT) {
				filter = 3;
			}
			while(iter.hasNext()) {
				Entry<Integer,ItemStack> next = iter.next();
				if(next.getKey() >= filter) {
					item.put(e.getView().convertSlot(next.getKey()), next.getValue());
				}
			}		
			shareInv.updateDrag(p,item);
		}
	}
	
}
