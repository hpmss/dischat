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
	
	public static int logarithm(double num,double base) {
		return (int)StrictMath.ceil(StrictMath.log(num)/StrictMath.log(base));
	}
	
	public static Object[] createSubInventory(int minSize,String channelName) {
		Object[] triplet = new Object[5];
		int overFlow = 0;
		Inventory inv;
		int expo = logarithm(minSize,3);
		if(expo == 1 || expo == 0) {
			expo = 2;
		}
		double finalSize = StrictMath.pow(3, expo);
		while(finalSize > minSize) {
			finalSize -= 54;
			if(finalSize < minSize) {
				finalSize += 54;
				break;
			}
		}
		while(finalSize > 54) {
			finalSize -= 54;
			overFlow++;
		}
		if(overFlow > 0) {
			inv = Bukkit.createInventory(null, (int)54,ChatColor.RED + channelName + " Member");
		}else {
			inv = Bukkit.createInventory(null, (int)finalSize,ChatColor.RED + channelName + " Member");
		}
		triplet[0] = inv;
		triplet[1] = overFlow;
		triplet[2] = 0;
		triplet[3] = 0;
		triplet[4] = 0;
		return triplet;
	}


}
