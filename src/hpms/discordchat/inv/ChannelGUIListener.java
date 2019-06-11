package hpms.discordchat.inv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.data.Role;
import net.md_5.bungee.api.ChatColor;

public class ChannelGUIListener implements Listener{
	
	static HashMap<UUID,ArrayList<Inventory>> tracker = new HashMap<UUID,ArrayList<Inventory>>();
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			String name = ChatColor.stripColor(e.getClickedInventory().getName());
			String channel = ChannelAPI.getPlayerCurrentChannelName(((Player)e.getWhoClicked()).getUniqueId());
			if(name.equalsIgnoreCase(channel)) {
				if(e.getCurrentItem().isSimilar(ChannelGUIConstant.getMemberListItem())) {
					ArrayList<Inventory> invs = addHeadToMemberList(ChannelGUI.createSubInventory(Role.getChannelSize(channel), channel),channel);
					tracker.put(e.getWhoClicked().getUniqueId(), invs);
					e.getWhoClicked().closeInventory();
				}
				e.setCancelled(true);
			}
			else if(name.equalsIgnoreCase(channel +" Member")) {
				
			}
		}
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		Player p = (Player)e.getPlayer();
		if(inv != null) {
			String name = ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId());
			if(ChatColor.stripColor(inv.getName()).equalsIgnoreCase(name + " Member")) {
				if(tracker.containsKey(p.getUniqueId())) {
					tracker.remove(p.getUniqueId());
				}
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
	
	public ArrayList<Inventory> addHeadToMemberList(ArrayList<Inventory> invs,String channel) {
		int i = 2;
		int j = 0;
		Inventory inv = invs.get(j);
		inv.setItem(0, ChannelGUIConstant.getMemberListPreviousItem());
		inv.setItem(1, ChannelGUIConstant.getMemberListNextItem());
		for(UUID uuid : Role.getAllMembersFromChannel(channel)) {
			if(i == 63) {
				i = 2;
				inv = invs.get(++j);
			}
			inv.setItem(i++, ChannelGUIConstant.getPlayerSkull(uuid, channel));
		}
		return invs;
	}

}
