package hpms.discordchat.inv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import hpms.discordchat.api.MenuUnit;
import hpms.discordchat.channel.Channel;
import net.md_5.bungee.api.ChatColor;

public class ChannelGUI implements MenuUnit{
	
	private int size;
	private Inventory inv;
	private Channel channel;
	
	public ChannelGUI(Channel channel) {
		this.channel = channel;
		this.size = 54;
		this.inv = Bukkit.createInventory(null,size,ChatColor.RED + channel.getChannelName());
		this.inv.setItem(11,ChannelGUIConstant.getPlayerSkull(channel.getLeader(), channel.getChannelName()));
		this.inv.setItem(21, ChannelGUIConstant.getMemberListItem());
		this.inv.setItem(22, ChannelGUIConstant.getUpgradeSlotItem());
		this.inv.setItem(23, ChannelGUIConstant.getBalanceItem());
	}
	
	public int getSize() {
		return this.size;
	}

	public String getName() {
		return this.channel.getChannelName();
	}

	public void open(Player player) {
		player.openInventory(inv);
	}

	public void close(Player player) {
		player.closeInventory();
	}

	public boolean isEditable() {
		return false;
	}
	
	//Make minSize automatically a multiple of 9
	//Create multiple inventories if size is over 63
	public static Inventory[] createSubInventorySize(int minSize) {
		int numInv = 0;
		Inventory[] inv = new Inventory[numInv];
		return inv;
	}


}
