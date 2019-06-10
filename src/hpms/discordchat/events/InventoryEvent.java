package hpms.discordchat.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.inv.InventoryLinker;
import hpms.discordchat.inv.SharingInventory;
import hpms.discordchat.item.ShareItem;
import hpms.discordchat.utils.PackageHandler;

public class InventoryEvent implements Listener {
	
	/**
	 * @author hpms
	 * ------------
	 * This class handles only inventory sharing events.
	 */

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getCurrentItem() == null) return;
		SharingInventory shareInv = InventoryLinker.getSharingInventory(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), p);
		if(shareInv != null) {
			if(e.getCurrentItem().isSimilar(ShareItem.getNextItem())) {
				if(e.getCursor().getType() == Material.AIR) {
					shareInv.open(p);
				}
				e.setCancelled(true);
				for(Player mem : shareInv.getPlayers()) {
					InventoryLinker.updateInventory(mem);
				}
			}else if(e.getCurrentItem().isSimilar(ShareItem.getPreviousItem())) {
				if(e.getCursor().getType() == Material.AIR) {
					shareInv.close(p);
				}
				e.setCancelled(true);
				for(Player mem : shareInv.getPlayers()) {
					InventoryLinker.updateInventory(mem);
				}
			}
			if(!e.getCurrentItem().isSimilar(ShareItem.getNextItem()) || !e.getCurrentItem().isSimilar(ShareItem.getPreviousItem())) {
				shareInv.updateClick(p, e);
			}
					
		}
		
	
		
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		SharingInventory shareInv = InventoryLinker.getSharingInventory(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), p);
		if(shareInv != null) {
			for(Integer i : e.getRawSlots()) {
				Method m = PackageHandler.getOGCMethod("inventory.CraftInventoryView", "getSlotType", InventoryView.class,int.class);
				try {
					SlotType type = (SlotType) m.invoke(null, e.getView(),i);
					if(type == SlotType.QUICKBAR) {
						e.setCancelled(true);
						return;
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
					ex.printStackTrace();
				}
			}
			
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
