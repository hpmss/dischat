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
import hpms.discordchat.data.Prefix;
import hpms.discordchat.item.ConfigMenu;
import hpms.discordchat.utils.ErrorState;
import hpms.discordchat.utils.PendingInvitation;
import net.md_5.bungee.api.ChatColor;

public class OnCommand implements CommandExecutor{
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
		if(cmd.getName().equalsIgnoreCase("flush") | cmd.getName().equalsIgnoreCase("f")) {
			for(int i = 0; i < 20;i ++) {
				Bukkit.getServer().broadcastMessage(" ");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("discordchat") | cmd.getName().equalsIgnoreCase("dc")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.YELLOW + "/discordchat list - Get a list of channels.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat create <name> - Create a new channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat remove <name> - Remove a channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat join <name> - Join a channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat prefix <name> <prefix> - Set a channel prefix ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat prefix <name> <playername> <prefix> - Set a channel's player prefix. ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat leaderprefix <name> <prefix> - Set a channel's leader prefix. (leader required) ");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat chatprefix <name> <prefix> <chatprefix> - Set a channel's prefix's chat prefix. ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat channel - Open channel config menu. (leader required and own a channel )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat addprefix <name> <prefix> <makedefault> - Add a prefix and set to default. ( optional ) ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat removeprefix <name> <prefix> - Remove a prefix. ( leader required )");
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list")) {
					sender.sendMessage(ChatColor.YELLOW + "Channel list: " + ChannelHolder.getChannelList());
				}
				else if(args[0].equalsIgnoreCase("debug")) {
					ChannelHolder.debug();
					Prefix.debug();
					PendingInvitation.debug();
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
						Player p = (Player) sender;
						Channel channel = ChannelHandler.getChannelByName(args[1]);
						
						PendingInvitation inv = PendingInvitation.deserializeInvitation(channel.getLeader());
					//	if(!p.hasPermission("discordchat.bypassjoin")) {
							inv.addRequester(p.getUniqueId());
							if(inv.isAccepted(p.getUniqueId()) == ErrorState.SUCCESS) {
								ChannelHandler.joinChannel(p,args[1]);
							}
							else {
								p.sendMessage(ChatColor.YELLOW + "You are currently not accepted by leader to join the channel.");
							}
						//}
						
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
				else if(args[0].equalsIgnoreCase("leaderprefix")) {
					ChannelHandler.getChannelByName(args[1]).setPrefixChatPrefix(((Player) sender).getUniqueId(), null, args[2]);
				}
				else if(args[0].equalsIgnoreCase("removeprefix")) {
					switch(ChannelHandler.getChannelByName(args[1]).removePrefix(((Player) sender).getUniqueId(), args[2])) {
					case INVALID_LENGTH:
						break;
					case LEADER_PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "Prefix cannot be similar to leader prefix.");
						break;
					case NO_EXISTENCE:
						sender.sendMessage(ChatColor.YELLOW + "Prefix does not exist.");
						break;
					case PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "\'" + ChatColor.AQUA + args[2] + ChatColor.YELLOW + "\' is a default prefix . You cant remove default prefix.");
						break;
					case SUCCESS:
						sender.sendMessage(ChatColor.YELLOW + "Prefix removed.");
						break;
					default:
						break;
					}
				}
			}
			else if(args.length >= 4) {
				if(args[0].equalsIgnoreCase("prefix")) {
					ChannelHandler.setChannelPlayerPrefix(((Player) sender).getUniqueId(), Bukkit.getOfflinePlayer(args[2]).getUniqueId(), args[1], args[3]);
				}
				else if(args[0].equalsIgnoreCase("addprefix")) {
					switch(ChannelHandler.getChannelByName(args[1]).addPrefix(((Player) sender).getUniqueId(), args[2], Boolean.parseBoolean(args[3]))) {
					case INVALID_LENGTH:
						break;
					case LEADER_PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "Prefix cannot be similar to leader prefix.");
						break;
					case PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "Prefix has already existed.");
						break;
					case SUCCESS:
						sender.sendMessage(ChatColor.YELLOW + "Successfully added new prefix.");
						break;
					default:
						break;
					};
				}
				else if(args[0].equalsIgnoreCase("leaderprefix")) {
					StringBuilder builder = new StringBuilder();
					for(int i = 2; i < args.length; i++) {
						builder.append(args[i]).append(" ");
					}
					String prefix = builder.toString().trim();
					ChannelHandler.getChannelByName(args[1]).setPrefixChatPrefix(((Player) sender).getUniqueId(), null, prefix);
				}
				else if(args[0].equalsIgnoreCase("chatprefix")) {
					StringBuilder builder = new StringBuilder();
					for(int i = 3; i < args.length;i++) {
						builder.append(args[i]).append(" ");
					}
					String prefix = builder.toString().trim();
					switch(ChannelHandler.getChannelByName(args[1]).setPrefixChatPrefix(((Player) sender).getUniqueId(), args[2], prefix)) {
					case INVALID_LENGTH:
						break;
					case NO_EXISTENCE:
						sender.sendMessage(ChatColor.YELLOW + "Prefix does not exist.");
						break;
					case SUCCESS:
						sender.sendMessage(ChatColor.YELLOW + "Successfully changed chat prefix.");
						break;
					default:
						break;
					};
				}
			}
		
		}
		
		return true;
	}

}
