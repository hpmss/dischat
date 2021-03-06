package hpms.discordchat.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ExpItem extends ItemStack {
	
	private String name = ChatColor.YELLOW + "Exp Item";
	
	public ExpItem() {
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
