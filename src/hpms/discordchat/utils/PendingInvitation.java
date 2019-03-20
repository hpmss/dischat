package hpms.discordchat.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;

import hpms.discordchat.chat.DiscordChat;

public class PendingInvitation implements ConfigurationSerializable{
	
	{
		ConfigurationSerialization.registerClass(PendingInvitation.class);
	}
	
	private static HashMap<UUID,PendingInvitation> cachedInvitation = Maps.newHashMap();
	private static int EXPIRATION = 10;
	
	private JavaPlugin plugin;
	private LinkedHashMap<String,Integer> requester = Maps.newLinkedHashMap();
	private UUID receiver;
	private UUID previousRequester;
	private UUID previousAccepted;
	private int expiration;
	
	public PendingInvitation(JavaPlugin plugin,UUID receiver, int expiration) {
		this.plugin = plugin;
		this.receiver = receiver;
		this.expiration = expiration;
	}
	
	public PendingInvitation(Map<String,Object> map) {
		for(Entry<String,Object> data : map.entrySet()) {
			switch(data.getKey()) {
			case "plugin" :
				this.plugin = (JavaPlugin) data.getValue();
				break;
			case "receiver" :
				this.receiver = UUID.fromString(data.getValue().toString());
				break;
			case "previousRequester" : 
				this.previousRequester = data.getValue() == null ? null : UUID.fromString(data.getValue().toString());
				break;
			case "previousAccepted" :
				this.previousAccepted = data.getValue() == null ? null : UUID.fromString(data.getValue().toString());
				break;
			case "expiration" :
				this.expiration = Integer.parseInt(data.getValue().toString());
				break;
			case "requester" :
				this.requester = (LinkedHashMap<String,Integer>) data.getValue();
				break;
			}
		}
		
	}
	
	public void addRequester(UUID requester) {
		this.requester.put(requester.toString(), this.expiration);
		this.previousRequester = requester;
		this.invalidate();
		this.serializeInvitation();
	}
	
	public boolean acceptInvitation(UUID player) {
		if(!this.requester.containsKey(player.toString())) return false;
		this.requester.put(player.toString(), -1);
		this.previousAccepted = player;
		this.serializeInvitation();
		return true;
	}
	
	public ErrorState isAccepted(UUID player) {
		if(!this.requester.containsKey(player.toString())) return ErrorState.NO_EXISTENCE;
		return (requester.get(player.toString()) == -1) ? ErrorState.SUCCESS : ErrorState.FAIL;
	}
	
	public void feedbackMessage(String receive,String prevAccepted) {
		if(this.previousAccepted != null) {
			OfflinePlayer receiver = Bukkit.getServer().getOfflinePlayer(this.receiver);
			OfflinePlayer previousAccepted = Bukkit.getServer().getOfflinePlayer(this.previousAccepted);
			if(receiver.isOnline()) {
				receiver.getPlayer().sendMessage(receive);
			}
			if(previousAccepted.isOnline()) {
				previousAccepted.getPlayer().sendMessage(prevAccepted);
			}
		}
	}
	
	public void setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public UUID getReceiver() {
		return this.receiver;
	}
	
	public YamlConfiguration serializeInvitation() {
		YamlConfiguration config = FileManager.getYamlConfiguration("inv_data.dat");
		config.set(this.receiver.toString(), this);
		try {
			config.save(FileManager.createNewFile("inv_data.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}
	
	public void removeAcceptedRequest() {
		WeakHashMap<String, Integer> map = new WeakHashMap<>();
		map.putAll(this.requester);
		for(Entry<String,Integer> entry : map.entrySet()) {
			if(entry.getValue() == -1) {
				requester.remove(entry.getKey());
			}
 		}
	}
	
	private void reloadInvitationExpiration() {
		if(this.requester.size() != 0) {
			WeakHashMap<String,Integer> copy = new WeakHashMap<>();
			copy.putAll(this.requester);
			for(Entry<String,Integer> request : copy.entrySet()) {
				if(request.getValue() != -1) {
					new TaskHandler(this.plugin,20,20) {
						private String player = request.getKey();
						public void run() {
							if(requester.containsKey(player) && requester.get(player) == 0) {
								requester.remove(player);
								serializeInvitation();
								cancelTask();
							}
							if(requester.containsKey(player)) {
								requester.put(player, requester.get(player) - 1);
							}
						}
					};
				}
			}
		}
	}
	
	private void invalidate() {
		if(this.plugin != null) {
			if(requester.get(previousRequester.toString()) != -1) {
				new TaskHandler(this.plugin,20,20) {
					private String player = previousRequester.toString();
						public void run() {
							if(requester.containsKey(player) && requester.get(player) == 0) {
								requester.remove(player);
								serializeInvitation();
								cancelTask();
							}
							if(requester.containsKey(player)) {
								requester.put(player, requester.get(player) - 1);
							}
						}
				};
			}
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String,Object> map = Maps.newHashMap();
		map.put("receiver", this.receiver.toString());
		map.put("previousRequester", this.previousRequester == null ? null : this.previousRequester.toString());
		map.put("previousAccepted", this.previousAccepted == null ? null : this.previousAccepted.toString());
		map.put("requester", this.requester);
		map.put("expiration", this.expiration);
		return map;
	}
	
	public static PendingInvitation deserializeInvitation(UUID player) {
		PendingInvitation inv = cachedInvitation.get(player);
		if(inv != null) {
			return inv;
		}
		YamlConfiguration config = FileManager.getYamlConfiguration("inv_data.dat");
		if(config.getValues(false).containsKey(player.toString())) {
			inv = (PendingInvitation) config.getValues(false).get(player.toString());
			inv.setPlugin(DiscordChat.plugin);
			inv.reloadInvitationExpiration();
		}
		else {
			inv = new PendingInvitation(DiscordChat.plugin,player,EXPIRATION);
			inv.serializeInvitation();
		}
		cachedInvitation.put(player, inv);
		return inv;
	}
	
	public static void debug() {
		PendingInvitation inv = deserializeInvitation(Bukkit.getPlayer("hpms").getUniqueId());
		Log.info(inv.getReceiver());
	}

}
