package hpms.discordchat.inv;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import hpms.discordchat.data.ChannelDataConstant;
import hpms.discordchat.data.Role;
import net.md_5.bungee.api.ChatColor;

public class ChannelGUIConstant {
	
	private static Pattern r = Pattern.compile("&(?:[Aa-zZ]|[0-9])");
	
	private static ItemStack memberListItem = new ItemStack(Material.BOOK_AND_QUILL);
	private static ItemStack memberListNextItem = new ItemStack(Material.PAPER);
	private static ItemStack memberListPreviousItem = new ItemStack(Material.PAPER);
	private static ItemStack upgradeSlotItem = new ItemStack(Material.CHEST);
	private static ItemStack balanceItem = new ItemStack(Material.PAPER);
	
	
	static {
		ItemMeta memberListMeta = memberListItem.getItemMeta();
		memberListMeta.setDisplayName(ChatColor.AQUA + "List of members");
		memberListItem.setItemMeta(memberListMeta);
		ItemMeta memberListNextMeta = memberListNextItem.getItemMeta();
		memberListNextMeta.setDisplayName(ChatColor.YELLOW + "Next Page");
		memberListNextMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
		memberListNextItem.setItemMeta(memberListNextMeta);
		ItemMeta memberListPreviousMeta = memberListPreviousItem.getItemMeta(); 
		memberListPreviousMeta.setDisplayName(ChatColor.YELLOW + "Previous Page");
		memberListPreviousMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
		memberListPreviousItem.setItemMeta(memberListPreviousMeta);
		ItemMeta upgradeSlotMeta = upgradeSlotItem.getItemMeta();
		upgradeSlotMeta.setDisplayName(ChatColor.AQUA + "Upgrade slot");
		upgradeSlotItem.setItemMeta(upgradeSlotMeta);
		ItemMeta balanceMeta = balanceItem.getItemMeta();
		balanceMeta.setDisplayName(ChatColor.AQUA + "Balance");
		balanceItem.setItemMeta(balanceMeta);
	}
	
	public static ItemStack getPlayerSkull(UUID player,String channel) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
		ArrayList<String> lore = new ArrayList<String>();
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		if(channel.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			lore.add(ChatColor.GREEN + "No leader sad ;(");
			meta.setDisplayName(ChatColor.AQUA + "Information");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}else {
			OfflinePlayer p = Bukkit.getOfflinePlayer(player);
			lore.add(ChatColor.YELLOW + "Name: " + ChatColor.GREEN + p.getName());
			String role = Role.getRoleFromPlayer(player, channel);
			Matcher m = r.matcher(role);
			role = m.replaceAll("");
			lore.add(ChatColor.YELLOW + "Role: " + ChatColor.GREEN + role);
			meta.setDisplayName(ChatColor.AQUA + "Information");
			meta.setOwningPlayer(p);
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}
	
	}
	
	public static ItemStack getMemberListItem() {
		return memberListItem;
	}
	
	public static ItemStack getMemberListNextItem() {
		return memberListNextItem;
	}
	
	public static ItemStack getMemberListPreviousItem() {
		return memberListPreviousItem;
	}
	
	public static ItemStack getUpgradeSlotItem() {
		return upgradeSlotItem;
	}
	
	public static ItemStack getBalanceItem() {
		return balanceItem;
	}
	
	
}
