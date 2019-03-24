package hpms.discordchat.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ShareItem extends ItemStack{
	private String name = ChatColor.YELLOW + "Click to next";
	
	public ShareItem() {
		super(Material.PAPER);
		this.updateItemMeta();
	}
	
	private void updateItemMeta() {
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(name);
		meta.addEnchant(Enchantment.DURABILITY, 5, true);
		this.setItemMeta(meta);
	}
}