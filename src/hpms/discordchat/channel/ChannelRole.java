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
		if(!Role.isRole(this.name, role)) {
			Bukkit.getPlayer(setter).sendMessage(ChatColor.YELLOW + "Vài trò này không tồn tại.");
			return false;
		}
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				if(this.member.containsKey(member)) {
					this.member.put(member,role);
				}
				Role.update(this.name, member, role);
				ChannelData.put(this);
				return true;
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Kênh mặc định chỉ có thể được vận hành bởi Admin.");
			}
		}
		else {
			if(member.equals(this.leader)) {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.YELLOW + "Bạn là chủ kênh nên bạn không thể đặt cho bản thân một vai trò.");
				return false;
			}
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godsetrole"))  {
				if(this.member.containsKey(member)) {
					this.member.put(member,role);
				}
				Role.update(this.name, member, role);
				ChannelData.put(this);
				return true;
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.YELLOW + "Bạn không phải là chủ kênh này.");
			}
		}
		return false;
		
	}
	
	public ErrorState setRolePrefix(UUID setter,String role,String prefix) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				return Role.setRolePrefix(this.name, role, prefix);
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Kênh mặc định chỉ có thể được vận hành bởi Admin.");
			}
		}else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godsetroleprefix")) {
				return Role.setRolePrefix(this.name, role, prefix);
			}else return ErrorState.FAIL;
		}
		return ErrorState.NULL;
	}
	
	public boolean setChannelChatPrefix(UUID setter,String prefix) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				Role.setChannelChatPrefix(this.name, prefix);
				return true;
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Kênh mặc định chỉ có thể được vận hành bởi Admin.");
			}
		}
		else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godsetchannelprefix")) {
				Role.setChannelChatPrefix(this.name, prefix);
				return true;
			}else return false;
		}
		return false;
	}
	
	public ErrorState addRole(UUID setter,String role,boolean makeDefault) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				return Role.addRole(this.name, role,makeDefault);
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Kênh mặc định chỉ có thể được vận hành bởi Admin.");
			}
		}
		else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godaddrole")) {
				return Role.addRole(this.name, role,makeDefault);
			}else return ErrorState.FAIL;
		}
		return ErrorState.NULL;
	
	}
	
	public ErrorState removeRole(UUID setter,String prefix) {
		if(this.name.equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
			if(Bukkit.getPlayer(setter).hasPermission("dc.default")) {
				return Role.removeRole(this.name, prefix);
			}else {
				Bukkit.getPlayer(setter).sendMessage(ChatColor.RED + "Kênh mặc định chỉ có thể được vận hành bởi Admin.");
			}
		}
		else {
			if(setter.equals(this.leader) || Bukkit.getPlayer(setter).hasPermission("dc.godremoverole")) {
				return Role.removeRole(this.name, prefix);
			}else return ErrorState.FAIL;
		}
		
		return ErrorState.NULL;
	}

	public String getChannelChatPrefix() {
		return Role.getChannelChatPrefix(this.name);
	}

}
