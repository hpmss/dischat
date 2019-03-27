package hpms.discordchat.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ShareItem{
	private static String nextName = ChatColor.YELLOW + "Click to next";
	private static String previousName = ChatColor.YELLOW + "Click to previous";
	private static ItemStack nextItem = new ItemStack(Material.PAPER);;	
	private static ItemStack previousItem = new ItemStack(Material.PAPER);
	static {
		ItemMeta nextMeta = nextItem.getItemMeta();
		nextMeta.setDisplayName(nextName);
		nextMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		nextItem.setItemMeta(nextMeta);
		
		ItemMeta previousMeta = previousItem.getItemMeta();
		previousMeta.setDisplayName(previousName);
		previousMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		previousItem.setItemMeta(previousMeta);
	}
	
	public static ItemStack getNextItem() {
		return nextItem.clone();
	}
	
	public static ItemStack getPreviousItem() {
		return previousItem.clone();
	}
	
}
