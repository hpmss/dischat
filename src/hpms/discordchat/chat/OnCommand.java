package hpms.discordchat.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.channel.Channel;
import hpms.discordchat.data.ChannelData;
import hpms.discordchat.data.Role;
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
				sender.sendMessage(ChatColor.YELLOW + "/discordchat teleport <player> - Request a teleportation with player in the same channel.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat invshare <player> - Request a inventory sharing with player in the same channel");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat create <name> - Create a new channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat remove <name> - Remove a channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat join <name> - Join a channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat joinaccept <name> <player> - Accept a player joining request ( leader required ) .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat prefix <name> <prefix> - Set a channel's chat prefix ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat role <name> <playername> <role> - Set a channel's player role. ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat leaderprefix <name> <prefix> - Set a channel's leader prefix. (leader required) ");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat roleprefix <name> <role> <prefix> - Set a channel's role's prefix. ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat channel - Open channel config menu. (leader required and own a channel )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat addrole <name> <role> <makedefault> - Add a role and set to default. ( optional ) ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat removerole <name> <role> - Remove a role. ( leader required )");
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list")) {
					sender.sendMessage(ChatColor.YELLOW + "Channel list: " + ChannelData.getChannelList());
				}
				else if(args[0].equalsIgnoreCase("debug")) {
					ChannelData.debug();
					Role.debug();
					PendingInvitation.debug();
				}
				else if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")) {
					sender.sendMessage(ChatColor.YELLOW + "You must specify a name");
				}
				else if(args[0].equalsIgnoreCase("prefix")) {
					sender.sendMessage(ChatColor.YELLOW + "Missing two params <name> and <prefix>");
				}
				else if(args[0].equalsIgnoreCase("channel")) {
				}
				else if(args[0].equalsIgnoreCase("tpaccept")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).acceptTeleportation(((Player) sender).getUniqueId());
				}
				else if(args[0].equalsIgnoreCase("shareaccept")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).acceptInventorySharing(((Player) sender).getUniqueId());
				}
			}
			else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("create")) {
					if(sender instanceof ConsoleCommandSender) {
						sender.sendMessage(ChatColor.RED + "Create from console must specify a leader player");
					}
					else{
						Channel channel = ChannelAPI.createNewChannel(args[1], ((Player) sender).getUniqueId(),false);
						if(channel != null) {
							sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel created.");
						}
					}
					
				}
				else if(args[0].equalsIgnoreCase("remove")) {
					ChannelAPI.removeChannel(args[1]);
					sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel removed.");
				}
				else if(args[0].equalsIgnoreCase("teleport")) {
					Player p = (Player) sender;
					boolean b = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).requestTeleportation(Bukkit.getPlayer(args[1]).getUniqueId(), p.getUniqueId());
					if(b) {
						p.sendMessage(ChatColor.YELLOW + "Request sent.");
					}
				}
				else if(args[0].equalsIgnoreCase("invshare")) {
					Player p = (Player) sender;
					boolean b = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).requestInventorySharing(Bukkit.getPlayer(args[1]).getUniqueId(), p.getUniqueId());
					if(b) {
						p.sendMessage(ChatColor.YELLOW + "Request sent.");
					}
				}
				else if(args[0].equalsIgnoreCase("join")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						Channel channel = ChannelAPI.getChannelByName(args[1]);
						
						PendingInvitation inv = PendingInvitation.deserializeInvitation(channel.getLeader());
						if(!p.hasPermission("discordchat.bypassjoin")) {
							boolean b = inv.addRequester(p.getUniqueId());
							if(b) {
								p.sendMessage(ChatColor.YELLOW + "Request to join \'" + ChatColor.AQUA + channel.getChannelName() + "\'" + ChatColor.YELLOW + " sent.");
							}
							else {
								p.sendMessage(ChatColor.YELLOW + "Request already sent.");
							}
						}else {
							ChannelAPI.joinChannel(p.getUniqueId(), args[1]);
						}
						
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
						Channel channel = ChannelAPI.createNewChannel(args[1], Bukkit.getPlayer(args[2]).getUniqueId(),true);
						if(channel != null) {
							sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel created.");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("prefix")) {
					boolean b = ChannelAPI.setChannelChatPrefix(((Player) sender).getUniqueId(),args[1], args[2]);
					if(b) {
						sender.sendMessage(ChatColor.YELLOW + "Channel prefix setted to \'" + args[2] + "\'.");
					}
					else {
						sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
					}
				}
				else if(args[0].equalsIgnoreCase("leaderprefix")) {
					ChannelAPI.getChannelByName(args[1]).setRolePrefix(((Player) sender).getUniqueId(), null, args[2]);
				}
				else if(args[0].equalsIgnoreCase("joinaccept")) {
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					PendingInvitation inv = PendingInvitation.deserializeInvitation(channel.getLeader());
					boolean b = inv.acceptInvitation(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
					if(b) {
						ChannelAPI.joinChannel(Bukkit.getOfflinePlayer(args[2]).getUniqueId(), channel.getChannelName());
						inv.feedbackMessage(ChatColor.YELLOW + "Player \'" + args[2] + "\' joined your channel.", "");
					}
					else {
						inv.feedbackMessage(ChatColor.YELLOW + "Probably you already accepted the player ?", "");
					}
				}
				else if(args[0].equalsIgnoreCase("removerole")) {
					switch(ChannelAPI.getChannelByName(args[1]).removeRole(((Player) sender).getUniqueId(), args[2])) {
					case INVALID_LENGTH:
						break;
					case LEADER_PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "Role cannot be similar to leader.");
						break;
					case NO_EXISTENCE:
						sender.sendMessage(ChatColor.YELLOW + "Role does not exist.");
						break;
					case PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "\'" + ChatColor.AQUA + args[2] + ChatColor.YELLOW + "\' is a default role . You cant remove default role.");
						break;
					case SUCCESS:
						sender.sendMessage(ChatColor.YELLOW + "Role removed.");
						break;
					default:
						break;
					}
				}
			}
			else if(args.length >= 4) {
				if(args[0].equalsIgnoreCase("role")) {
					ChannelAPI.setChannelPlayerRole(((Player) sender).getUniqueId(), Bukkit.getOfflinePlayer(args[2]).getUniqueId(), args[1], args[3]);
				}
				else if(args[0].equalsIgnoreCase("addrole")) {
					switch(ChannelAPI.getChannelByName(args[1]).addRole(((Player) sender).getUniqueId(), args[2], Boolean.parseBoolean(args[3]))) {
					case INVALID_LENGTH:
						break;
					case LEADER_PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "Role cannot be similar to leader.");
						break;
					case PREFIX:
						sender.sendMessage(ChatColor.YELLOW + "Role has already existed.");
						break;
					case SUCCESS:
						sender.sendMessage(ChatColor.YELLOW + "Successfully added new role.");
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
					ChannelAPI.getChannelByName(args[1]).setRolePrefix(((Player) sender).getUniqueId(), null, prefix);
				}
				else if(args[0].equalsIgnoreCase("roleprefix")) {
					StringBuilder builder = new StringBuilder();
					for(int i = 3; i < args.length;i++) {
						builder.append(args[i]).append(" ");
					}
					String prefix = builder.toString().trim();
					switch(ChannelAPI.getChannelByName(args[1]).setRolePrefix(((Player) sender).getUniqueId(), args[2], prefix)) {
					case INVALID_LENGTH:
						break;
					case NO_EXISTENCE:
						sender.sendMessage(ChatColor.YELLOW + "Role does not exist.");
						break;
					case SUCCESS:
						sender.sendMessage(ChatColor.YELLOW + "Successfully changed role's prefix.");
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
