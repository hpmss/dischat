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
import org.bukkit.inventory.meta.ItemMeta;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.data.Role;
import net.md_5.bungee.api.ChatColor;

public class ChannelGUIListener implements Listener{
	
	static HashMap<UUID,Object[]> tracker = new HashMap<UUID,Object[]>();
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			Player p = (Player) e.getWhoClicked();
			String name = ChatColor.stripColor(e.getClickedInventory().getName());
			String channel = ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId());
			if(name.equalsIgnoreCase(channel)) {
				if(e.getCurrentItem().isSimilar(ChannelGUIConstant.getMemberListItem())) {
					tracker.put(p.getUniqueId(),ChannelGUI.createSubInventory(Role.getChannelSize(channel) + 4, channel));
					Inventory inv = addHeadToMemberList(p.getUniqueId(),channel,true,0);
					p.closeInventory();
					p.openInventory(inv);
				}
				e.setCancelled(true);
			}
			else if(name.equalsIgnoreCase(channel +" Member")) {
				ItemMeta currentItem = e.getCurrentItem().getItemMeta();
				Integer overFlow = (Integer) tracker.get(p.getUniqueId())[1];
				if(currentItem != null) {
					if(ChatColor.stripColor(currentItem.getDisplayName()).equalsIgnoreCase("Next Page")) {
						int code = Integer.parseInt(currentItem.getLore().get(0).replace(String.valueOf(ChatColor.COLOR_CHAR),""));
						 if(code - 1 == overFlow) {
							p.sendMessage(ChatColor.YELLOW + "Bạn đang ở cuối danh sách.");
						}else {
							p.openInventory(addHeadToMemberList(p.getUniqueId(),channel,true,code));
						}
					
					}else if(ChatColor.stripColor(currentItem.getDisplayName()).equalsIgnoreCase("Previous Page")) {
						int code = Integer.parseInt(currentItem.getLore().get(0).replace(String.valueOf(ChatColor.COLOR_CHAR),""));
						if(code == 0) {
							p.sendMessage(ChatColor.YELLOW + "Bạn đang ở đầu danh sách.");
						}else {
							p.openInventory(addHeadToMemberList(p.getUniqueId(),channel,false,code - 1));
						}
					}
				}
				e.setCancelled(true);
				
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
//					tracker.remove(p.getUniqueId());
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
		}else if(name.equalsIgnoreCase(channel +" Member")) {
			e.setCancelled(true);
		}
	}
	
	/**
	 * I fucked up
	 * @param traverse - True indicates forward , False indicates backward
	 * @currentPointer - Always start at 2 the first time
	 */
	
	public Inventory addHeadToMemberList(UUID holder,String channel,boolean traverse,int code) {
		Object[] quad = tracker.get(holder);
		Inventory inv = (Inventory) quad[0];
		inv.clear();
		Integer overFlow = (Integer) quad[1];
		Integer currentPointer = (Integer) quad[2];
		Integer previousForwardLength = (Integer) quad[3];
		Integer previousBackwardLength = (Integer) quad[4];
		int invIndex = 0;
		inv.setItem(0, ChannelGUIConstant.getMemberListPreviousItem(code));
		inv.setItem(1, ChannelGUIConstant.getMemberListNextItem(code+1));
		ArrayList<UUID> list = Role.getAllMembersFromChannel(channel);
		System.out.print("Pointer: " + currentPointer);
		if(overFlow > 0) {
			try {
				if(traverse) {
					currentPointer= currentPointer + previousBackwardLength;
					int max = 53;
					invIndex = 2;
					while(invIndex < max) {
						inv.setItem(invIndex++, ChannelGUIConstant.getPlayerSkull(list.get(currentPointer++), channel));
					}
					if(invIndex == 53) {
						inv.setItem(invIndex, ChannelGUIConstant.getPlayerSkull(list.get(currentPointer++), channel));
					}
					previousForwardLength = invIndex - 2;
					previousBackwardLength = 0;
				}else {
					currentPointer = currentPointer - (previousForwardLength + 2);
					int max = 2;
					invIndex = 53;
					while(max < invIndex) {
						inv.setItem(invIndex--, ChannelGUIConstant.getPlayerSkull(list.get(currentPointer--), channel));
					}
					if(invIndex == 2) {
						inv.setItem(invIndex, ChannelGUIConstant.getPlayerSkull(list.get(currentPointer--), channel));
					}
					previousBackwardLength=53;
					previousForwardLength = 0;
				}
			}catch(IndexOutOfBoundsException e) {
				previousForwardLength = invIndex - 3;
				}
		}else {
			for(int i = 2; i < list.size();i++) {
				inv.setItem(i++, ChannelGUIConstant.getPlayerSkull(list.get(i), channel));
			}
		}
		quad[1] = overFlow;
		quad[2] = currentPointer;
		quad[3] = previousForwardLength;
		quad[4] = previousBackwardLength;
		tracker.put(holder, quad);
		return inv;
	}

}
