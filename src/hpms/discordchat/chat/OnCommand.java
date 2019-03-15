package hpms.discordchat.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import hpms.discordchat.channel.Channel;
import hpms.discordchat.channel.ChannelHandler;
import hpms.discordchat.data.ChannelHolder;
import hpms.discordchat.item.ConfigMenu;
import net.md_5.bungee.api.ChatColor;

public class OnCommand implements CommandExecutor{
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.YELLOW + "/discordchat list - Get a list of channels.");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat create <name> - Create a new channel .");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat remove <name> - Remove a channel .");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat join <name> - Join a channel .");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat prefix <name> <prefix> - Set a channel prefix ( leader required ) .");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat prefix <name> <playername> <prefix> - Set a channel's player prefix ( leader required ) .");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat channel");
		}
		else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list")) {
				sender.sendMessage(ChatColor.YELLOW + "Channel list: " + ChannelHolder.getChannelList());
			}
			else if(args[0].equalsIgnoreCase("debug")) {
				ChannelHolder.debug();
			}
			else if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")) {
				sender.sendMessage(ChatColor.YELLOW + "You must specify a name");
			}
			else if(args[0].equalsIgnoreCase("prefix")) {
				sender.sendMessage(ChatColor.YELLOW + "Missing two params <name> and <prefix>");
			}
			else if(args[0].equalsIgnoreCase("channel")) {
				ConfigMenu.open(((Player) sender).getUniqueId());
			}
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("create")) {
				if(sender instanceof ConsoleCommandSender) {
					sender.sendMessage(ChatColor.RED + "Create from console must specify a leader player");
				}
				else{
					Channel channel = ChannelHandler.createNewChannel(args[1], ((Player) sender).getUniqueId(),false);
					if(channel != null) {
						sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel created.");
					}
				}
				
			}
			else if(args[0].equalsIgnoreCase("remove")) {
				ChannelHandler.removeChannel(args[1]);
				sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel removed.");
			}
			else if(args[0].equalsIgnoreCase("join")) {
				if(sender instanceof Player) {
					ChannelHandler.joinChannel((Player)sender,args[1]);
				}else {
					sender.sendMessage(ChatColor.RED + "Joining a channel for console is currently not supported.");
				}
			}
			else if(args[0].equalsIgnoreCase("prefix")) {
				sender.sendMessage("Missing one param <prefix>");
			}
				
		}
		else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("create")) {
				if(sender instanceof ConsoleCommandSender) {
					Channel channel = ChannelHandler.createNewChannel(args[1], Bukkit.getPlayer(args[2]).getUniqueId(),true);
					if(channel != null) {
						sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel created.");
					}
				}
			}
			else if(args[0].equalsIgnoreCase("prefix")) {
				boolean b = ChannelHandler.setChannelChatPrefix(((Player) sender).getUniqueId(),args[1], args[2]);
				if(b) {
					sender.sendMessage(ChatColor.YELLOW + "Channel prefix setted to \'" + args[2] + "\'.");
				}
				else {
					sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
				}
			}
		}
		else if(args.length == 4) {
			if(args[0].equalsIgnoreCase("prefix")) {
				ChannelHandler.setChannelPlayerPrefix(((Player) sender).getUniqueId(), Bukkit.getOfflinePlayer(args[2]).getUniqueId(), args[1], args[3]);
			}
		}
		
		return false;
	}

}
