package hpms.discordchat.channel;

import java.util.UUID;

import org.bukkit.Bukkit;

import hpms.discordchat.data.ChannelData;
import hpms.discordchat.data.ChannelDataConstant;
import hpms.discordchat.data.Role;
import hpms.discordchat.utils.ErrorState;
import net.md_5.bungee.api.ChatColor;

public abstract class ChannelRole extends ChannelCore{
	
	public ChannelRole(String name, UUID leader,boolean getFlag) {
		super(name, leader,getFlag);
	}
	
	public boolean setRole(UUID setter,UUID member,String role) {
		if(!Role.isRole(this.name, role)) return false;
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				if(this.member.containsKey(member)) {
					this.member.put(member,role);
				}
				Role.update(this.name, member, role);
				ChannelData.put(this);
				return true;
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Default channel can only be operated by admin.");
			}
		}
		else {
			if(member.equals(this.leader)) return false;
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godsetrole"))  {
				if(this.member.containsKey(member)) {
					this.member.put(member,role);
				}
				Role.update(this.name, member, role);
				ChannelData.put(this);
				return true;
			}
		}
		return false;
		
	}
	
	public ErrorState setRolePrefix(UUID setter,String role,String prefix) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				return Role.setRolePrefix(this.name, role, prefix);
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Default channel can only be operated by admin.");
			}
		}else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godsetroleprefix")) {
				return Role.setRolePrefix(this.name, role, prefix);
			}
		}
		return ErrorState.NULL;
	}
	
	public boolean setChannelChatPrefix(UUID setter,String prefix) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				Role.setChannelChatPrefix(this.name, prefix);
				return true;
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Default channel can only be operated by admin.");
			}
		}
		else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godsetchannelprefix")) {
				Role.setChannelChatPrefix(this.name, prefix);
				return true;
			}	
		}
		return false;
	}
	
	public ErrorState addRole(UUID setter,String role,boolean makeDefault) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				return Role.addRole(this.name, role,makeDefault);
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Default channel can only be operated by admin.");
			}
		}
		else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godaddrole")) {
				return Role.addRole(this.name, role,makeDefault);
			}
		}
		return ErrorState.NULL;
	
	}
	
	public ErrorState removeRole(UUID setter,String prefix) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				return Role.removeRole(this.name, prefix);
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Default channel can only be operated by admin.");
			}
		}
		else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godremoverole")) {
				return Role.removeRole(this.name, prefix);
			}
		}
		
		return ErrorState.NULL;
	}
	
	public String getRole(UUID member) {
		return Role.getRoleFromPlayer(member, this.name);
	}
	
	public String getRolePrefix(String prefix) {
		return Role.getRolePrefix(this.name, prefix);
	}
	
	public String getChannelChatPrefix() {
		return Role.getChannelChatPrefix(this.name);
	}

}
