package hpms.discordchat.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ShareItem{
	private static String name = ChatColor.YELLOW + "Click to next";
	private static ItemStack item = new ItemStack(Material.PAPER);;	
	static {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addEnchant(Enchantment.DURABILITY, 5, true);
		item.setItemMeta(meta);
	}
	
	public static ItemStack getItem() {
		return item.clone();
	}
	
}
