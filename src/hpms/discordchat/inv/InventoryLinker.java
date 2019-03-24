package hpms.discordchat.inv;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
			SharingInventory.createSharingInventory(uuid.toString(),receiver,requester);
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
				requesterInv = SharingInventory.createSharingInventory(invRequester,receiver,requester);
			}
			link.set(receiver.getUniqueId().toString(), invRequester);
			link.set(requester.getUniqueId().toString(), invRequester);
			updateInventory(receiver,requester);
			save(channel,link);
		}
	}
	
	public static void deleteInventoryLinker(String channel) {
		if(!FileManager.isFileExist(channel + EXTENSION, PARENT)) return;
		FileManager.createNewFile(channel + EXTENSION, PARENT).delete();
	}
	
	public static SharingInventory getSharingInventory(String channel,Player player) {
		if(!FileManager.isFileExist(channel + EXTENSION, PARENT)) return null;
		YamlConfiguration link = FileManager.getYamlConfiguration(channel + EXTENSION,PARENT);
		String invUID = link.getString(player.getUniqueId().toString());
		return SharingInventory.getSharingInventory(invUID);
	}
	
	private static void updateInventory(Player... players) {
		for(Player p : players) {
			Inventory inv = p.getInventory();
			ItemStack item = inv.getItem(35);
			ItemStack share = ShareItem.getItem();
			if(item != null && !item.equals(share)) {
				p.getWorld().dropItemNaturally(p.getLocation(), item);
			}
			inv.setItem(35, share);
		}
	}
	
	private static void save(String channel,YamlConfiguration file) {
		try {
			file.save(FileManager.createNewFile(channel + ".invl", PARENT));
		}catch(IOException e) {e.printStackTrace();}
	}
}
