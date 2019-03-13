package hpms.discordchat.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import hpms.discordchat.channel.ChannelHandler;
import hpms.discordchat.data.ChannelHolder;
import net.md_5.bungee.api.ChatColor;

public class OnCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.YELLOW + "/discordchat list - Get a list of channels.");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat create <name> - Create a new channel .");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat remove <name> - Remove a channel .");
			sender.sendMessage(ChatColor.YELLOW + "/discordchat join <name> - Join a channel .");
		}
		else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list")) {
				sender.sendMessage(ChatColor.YELLOW + "Channel list: " + ChannelHolder.getChannelList());
			}
			else if(args[0].equalsIgnoreCase("debug")) {
			}
			else if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")) {
				sender.sendMessage(ChatColor.YELLOW + "You must specify a name");
			}
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("create")) {
				if(sender instanceof ConsoleCommandSender) {
					sender.sendMessage(ChatColor.RED + "Create from console must specify a leader player");
				}
				else{
					ChannelHandler.createNewChannel(args[1], (Player) sender,true);
					sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel created.");
				}
				
			}
			else if(args[0].equalsIgnoreCase("remove")) {
				ChannelHandler.removeChannel(args[1]);
				sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel removed.");
			}
			else if(args[0].equalsIgnoreCase("join")) {
				if(sender instanceof Player) {
					ChannelHandler.joinChannel((Player)sender);
					sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel joined.");
				}else {
					sender.sendMessage(ChatColor.RED + "Joining a channel for console is currently not supported.");
				}
			}
				
		}
		else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("create")) {
				if(sender instanceof ConsoleCommandSender) {
					ChannelHandler.createNewChannel(args[1], Bukkit.getPlayer(args[2]),true);
					sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel created.");
				}
			}
		}
		
		return false;
	}

}
