package hpms.discordchat.inv;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import hpms.discordchat.api.ChannelAPI;
import net.md_5.bungee.api.ChatColor;

public class ChannelGUIListener implements Listener{
	
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			String name = ChatColor.stripColor(e.getClickedInventory().getName());
			String channel = ChannelAPI.getPlayerCurrentChannelName(((Player)e.getWhoClicked()).getUniqueId());
			if(name.equalsIgnoreCase(channel)) {
				//Create inventory showing all member
				if(e.getCurrentItem().isSimilar(ChannelGUIConstant.getMemberListItem())) {
					
				}
				
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		
		String name = ChatColor.stripColor(e.getView().getTopInventory().getName());
		String channel = ChannelAPI.getPlayerCurrentChannelName(((Player)e.getWhoClicked()).getUniqueId());
		if(name.equalsIgnoreCase(channel)) {
			e.setCancelled(true);
		}
		
	}
	
	public void decorateMemberInventory(Inventory inv) {
		
	}

}
