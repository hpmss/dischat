package hpms.discordchat.events;

import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.inv.InventoryLinker;
import hpms.discordchat.inv.SharingInventory;
import hpms.discordchat.item.ShareItem;

public class InventoryEvent implements Listener {

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Log.info(e.getAction());
		Player p = (Player) e.getWhoClicked();
		if(e.getCurrentItem() == null) return;
		SharingInventory shareInv = InventoryLinker.getSharingInventory(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), p);
		if(shareInv != null) {
			if(e.getCurrentItem().isSimilar(ShareItem.getItem())) {
				shareInv.open(p);
				e.setCancelled(true);
			}
			if(!e.getCurrentItem().isSimilar(ShareItem.getItem())) {
				shareInv.updateClick(p, e);
//				if(e.getAction() == InventoryAction.PLACE_ONE || e.getAction() == InventoryAction.PLACE_SOME||
//						e.getAction() == InventoryAction.PLACE_ALL) {
//					shareInv.updateClick(p, e.getSlot(), e.getCursor(),false);
//				}
//				else if(e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_HALF ||
//					e.getAction() == InventoryAction.PICKUP_SOME ||e.getAction() == InventoryAction.PICKUP_ONE ) {
//					shareInv.updateClick(p, e.getSlot(), e.getCurrentItem(),true);
//				}
//				else if(e.getAction() == InventoryAction.DROP_ALL_SLOT || e.getAction() == InventoryAction.DROP_ONE_SLOT) {
//					
//				}
//				else if(e.getAction() == InventoryAction.DROP_ALL_CURSOR || e.getAction() == InventoryAction.DROP_ONE_CURSOR) {
//					
//				}
//				else if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
//					
//				}
//				else if(e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
//					
//				}
			}
			
		}
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		Log.info(e.getType());
		Player p = (Player) e.getWhoClicked();
		Log.info(p.getName());
		SharingInventory shareInv = InventoryLinker.getSharingInventory(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), p);
		if(shareInv != null) {
			shareInv.updateDrag(p, e.getNewItems());
		}
	}
	
}
