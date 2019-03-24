package hpms.discordchat.inv;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;

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
		Log.info(invUID);
		return SharingInventory.getSharingInventory(invUID);
	}
	
	private static void save(String channel,YamlConfiguration file) {
		try {
			file.save(FileManager.createNewFile(channel + ".invl", PARENT));
		}catch(IOException e) {e.printStackTrace();}
	}
}
