package hpms.discordchat.inv;

import java.util.ArrayList;

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
	
	public static int logarithm(double num,double base) {
		return (int)StrictMath.ceil(StrictMath.log(num)/StrictMath.log(base));
	}
	
	public static ArrayList<Inventory> createSubInventory(int minSize,String channelName) {
		int numInv = 0;
		ArrayList<Inventory> inv = new ArrayList<Inventory>();
		int expo = logarithm(minSize,3);
		if(expo == 1) {
			expo += 1;
		}
		double finalSize = StrictMath.pow(3, expo);
		while(finalSize > 63) {
			finalSize -= 63;
			numInv += 1;
		}
		for(int i = 0; i < numInv;i++) {
			inv.add(Bukkit.createInventory(null,63,ChatColor.RED +  channelName + " Member"));
		}
		
		inv.add(Bukkit.createInventory(null, (int)finalSize,ChatColor.RED + channelName + " Member"));
		
		return inv;
	}


}
