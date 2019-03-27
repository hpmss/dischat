package hpms.discordchat.inv;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import hpms.discordchat.data.ChannelData;
import hpms.discordchat.item.ShareItem;
import hpms.discordchat.utils.FileManager;
import hpms.discordchat.utils.Validator;

public class InventoryLinker {
	
	private static String PARENT = "data/inv";
	private static String EXTENSION = ".invl";
	
	public static void createInventoryLinker(String channel,Player receiver,Player requester) {
		YamlConfiguration link = FileManager.getYamlConfiguration(channel + EXTENSION, PARENT);
		if(link.getValues(false).size() == 0) {
			UUID uuid = UUID.randomUUID();
			link.set(receiver.getUniqueId().toString(), uuid.toString());
			link.set(requester.getUniqueId().toString(), uuid.toString());
			SharingInventory.createSharingInventory(channel,uuid.toString(),receiver,requester);
			updateInventory(receiver,requester);
			save(channel,link);
		}else {
			String invReceiver = null;
			String invRequester = null;
			SharingInventory requesterInv = null;
			SharingInventory receiverInv = null;
			if(link.contains(receiver.getUniqueId().toString())) {
				invReceiver = link.getString(receiver.getUniqueId().toString());
			}
			
			if(link.contains(requester.getUniqueId().toString())) {
				invRequester = link.getString(requester.getUniqueId().toString());
			}
			
			if(invRequester != null && invReceiver != null) {
				if(invRequester.equalsIgnoreCase(invReceiver)) {
					return;
				}
			}
			if(invReceiver != null) {
				receiverInv = SharingInventory.getSharingInventory(invReceiver);
				Validator.isNotNull(receiverInv);
				receiverInv.remove(receiver);
			}
			if(invRequester != null) {
				requesterInv = SharingInventory.getSharingInventory(invRequester);
				Validator.isNotNull(requesterInv);
				requesterInv.add(receiver);
			}else {
				invRequester = UUID.randomUUID().toString();
				requesterInv = SharingInventory.createSharingInventory(channel,invRequester,receiver,requester);
			}
			link.set(receiver.getUniqueId().toString(), invRequester);
			link.set(requester.getUniqueId().toString(), invRequester);
			updateInventory(receiver,requester);
			save(channel,link);
			SharingInventory.debug();
		}
	}
	
	public static void deleteInventoryLinkers() {
		
		String listString = ChannelData.getChannelList();
		String[] list = listString.substring(0, listString.length() - 1).split(",");
		rollbackPlayersInventory();
		for(String channel : list) {
			if(!FileManager.isFileExist(channel + EXTENSION, PARENT)) continue;
			FileManager.createNewFile(channel + EXTENSION, PARENT).delete();
		}
	}
	
	public static SharingInventory getSharingInventory(String channel,Player player) {
		if(!FileManager.isFileExist(channel + EXTENSION, PARENT)) return null;
		YamlConfiguration link = FileManager.getYamlConfiguration(channel + EXTENSION,PARENT);
		String invUID = link.getString(player.getUniqueId().toString());
		return SharingInventory.getSharingInventory(invUID);
	}
	
	public static void removePlayerSharingInventory(String channel,Player player) {
		SharingInventory inv = getSharingInventory(channel,player);
		if(inv != null) {
			inv.remove(player);
			removePlayerFromFile(channel,player);
		}
	}
	
	public static void removePlayerFromFile(String channel ,Player player) {
		YamlConfiguration link = FileManager.getYamlConfiguration(channel + EXTENSION,PARENT);
		link.set(player.getUniqueId().toString(), null);
		save(channel,link);
	}
	
	private static void rollbackPlayersInventory() {
		Collection<SharingInventory> col = SharingInventory.getSharingInventories();
		Iterator<SharingInventory> iter = col.iterator();
		while(iter.hasNext()) {
			SharingInventory shareInv = iter.next();
			shareInv.rollback();
		}
	}
	
	private static void updateInventory(Player... players) {
		for(Player p : players) {
			Inventory inv = p.getInventory();
			ItemStack nextItem = inv.getItem(35);
			ItemStack previousItem = inv.getItem(34);
			ItemStack shareNext = ShareItem.getNextItem();
			ItemStack sharePrevious = ShareItem.getPreviousItem();
			if(nextItem != null && !nextItem.equals(shareNext)) {
				p.getWorld().dropItemNaturally(p.getLocation(), nextItem);
			}
			inv.setItem(35, shareNext);
			if(previousItem != null && !previousItem.equals(sharePrevious)) {
				p.getWorld().dropItemNaturally(p.getLocation(), previousItem);
			}
			inv.setItem(34, sharePrevious);
		}
	}
	
	private static void save(String channel,YamlConfiguration file) {
		try {
			file.save(FileManager.createNewFile(channel + ".invl", PARENT));
		}catch(IOException e) {e.printStackTrace();}
	}
}
