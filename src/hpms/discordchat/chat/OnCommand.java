package hpms.discordchat.chat;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import hpms.discordchat.api.ChannelAPI;
import hpms.discordchat.channel.Channel;
import hpms.discordchat.data.ChannelData;
import hpms.discordchat.data.ChannelDataConstant;
import hpms.discordchat.data.Role;
import hpms.discordchat.inv.ChannelGUI;
import hpms.discordchat.inv.InventoryLinker;
import hpms.discordchat.utils.PendingInvitation;
import hpms.discordchat.utils.Validator;
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
				sender.sendMessage(ChatColor.YELLOW + "/discordchat leader <name> <player_name> - Make player a leader of channel ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat tp <player> - Request a teleportation with player in the same channel.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat shareinv <player> - Request a inventory sharing with player in the same channel");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat shareaccept - Accept inventory share request.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat sharestop - Stop the current inventory sharing.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat create <name> - Create a new channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat remove <name> - Remove a channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat join <name> - Join a channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat join <name> <player_name> - Make a player join a channel ( Admin ) .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat joinaccept <name> <player> - Accept a player joining request ( leader required ) .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat setrole <name> <playername> <role> - Set a channel's player role. ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat leaderprefix <name> <prefix> - Set a channel's leader prefix. (leader required) ");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat roleprefix <name> <role> <prefix> - Set a channel's role's prefix. ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat channelprefix <name> <prefix> - Set a channel's chat prefix ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat channel - Open channel menu.");
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
					Player p = (Player) sender;
					Channel channel = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId());
					ChannelGUI gui = new ChannelGUI(channel);
					gui.open(p);
				}
				else if(args[0].equalsIgnoreCase("tpaccept")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).acceptTeleportation(((Player) sender).getUniqueId());
				}
				else if(args[0].equalsIgnoreCase("shareaccept")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).acceptInventorySharing(((Player) sender).getUniqueId());
				}
				else if(args[0].equalsIgnoreCase("sharestop")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).stopInventorySharing(((Player) sender).getUniqueId());
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
				else if(args[0].equalsIgnoreCase("tp")) {
					Player p = (Player) sender;
					Player receiver = Bukkit.getPlayer(args[1]);
					if(receiver != null) {
						boolean b = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).requestTeleportation(receiver.getUniqueId(),p.getUniqueId());
						if(b) {
							p.sendMessage(ChatColor.YELLOW + "Teleport request sent.");
						}
					}else {
						p.sendMessage(ChatColor.YELLOW + "Player is either offline or doesnt exist.");
					}
				}
				else if(args[0].equalsIgnoreCase("shareinv")) {
					Player p = (Player) sender;
					Player receiver = Bukkit.getPlayer(args[1]);
					if(receiver != null) {
						if(p.hasPermission("dc.bypassrequest")) {
							if(Validator.arePlayersSameChannel(p.getUniqueId(), receiver.getUniqueId())) {
								if(p.getUniqueId().equals(receiver.getUniqueId())) {
									p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You cant share you with yourself.");
								}else {
									InventoryLinker.createInventoryLinker(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), receiver, p);
									p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You are now sharing inventory with " + ChatColor.WHITE + receiver.getName());
								}
							}else {
								p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You and the player must in the same channel.");
							}
						}else {
							boolean b = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).requestInventorySharing(receiver.getUniqueId(),p.getUniqueId());
							if(b) {
								p.sendMessage(ChatColor.YELLOW + "Inventory sharing request sent.");
							}
						}
					}else {
						p.sendMessage(ChatColor.YELLOW + "Player is either offline or doesnt exist.");
					}
					
				}
				else if(args[0].equalsIgnoreCase("join")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
							ChannelAPI.joinChannel(p.getUniqueId(), args[1]);
							p.sendMessage(ChatColor.YELLOW + "You have joined default channel: " + ChatColor.AQUA + args[1]);
						}else {
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
								ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).stopInventorySharing(p.getUniqueId());
								ChannelAPI.joinChannel(p.getUniqueId(), args[1]);
								p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You joined channel " + ChatColor.WHITE + args[1]);
							}
						}
					
						
					}else {
						sender.sendMessage(ChatColor.RED + "Joining a channel for console is currently not supported.");
					}
				}
				else if(args[0].equalsIgnoreCase("channelprefix")) {
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
				else if(args[0].equalsIgnoreCase("join")) {
					if(sender.hasPermission("dc.bypassmakejoin")) {
						ChannelAPI.joinChannel(Bukkit.getPlayer(args[2]).getUniqueId(), args[1]);
					}else {
						sender.sendMessage(ChatColor.YELLOW + "This command can only be used by admin.");
					}
				}
				else if(args[0].equalsIgnoreCase("leader")) {
					if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
						sender.sendMessage(ChatColor.YELLOW + "Default channel cannot have a leader and can only be operated by admin.");
					}else {
						if(sender.hasPermission("dc.bypassleader")) {
							OfflinePlayer p = Bukkit.getPlayer(args[2]);
							ChannelAPI.getChannelByName(args[1]).setLeader(p.getUniqueId());
							if(p.isOnline()) {
								p.getPlayer().sendMessage(ChatColor.YELLOW + "You have been made a leader of channel " + ChatColor.WHITE + args[1]);
							}
							
						}else {
							Channel channel = ChannelAPI.getChannelByName(args[1]);
							if(channel.getLeader().equals(((Player)sender).getUniqueId())) {
								OfflinePlayer p = Bukkit.getPlayer(args[2]);
								channel.setLeader(p.getUniqueId());
								if(p.isOnline()) {
									p.getPlayer().sendMessage(ChatColor.YELLOW + "You have been made a leader of channel " + ChatColor.WHITE + args[1]);
								}
							}else {
								sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
							}
						}
					}
				}
				else if(args[0].equalsIgnoreCase("channelprefix")) {
					boolean b = ChannelAPI.setChannelChatPrefix(((Player) sender).getUniqueId(),args[1], args[2]);
					if(b) {
						if(sender.hasPermission("dc.godsetchannelprefix") || sender.hasPermission("dc.default")) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You channel prefix to " + ChatColor.WHITE + args[2]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Channel prefix setted to \'" + args[2] + "\'.");
						}
					}
					else {
						sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
					}
				}
				else if(args[0].equalsIgnoreCase("leaderprefix")) {
					switch(ChannelAPI.getChannelByName(args[1]).setRolePrefix(((Player) sender).getUniqueId(), null, args[2])) {
					case INVALID_LENGTH:
						break;
					case SUCCESS:
						if(sender.hasPermission("dc.godsetroleprefix") || sender.hasPermission("dc.default")) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You set leader role prefix to " + args[2]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Leader prefix changed to " + args[2]);
						}
						break;
					default:
						break;
				
					}
				}
				else if(args[0].equalsIgnoreCase("joinaccept")) {
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					PendingInvitation inv = PendingInvitation.deserializeInvitation(channel.getLeader());
					boolean b = inv.acceptInvitation(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
					if(b) {
						ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).stopInventorySharing(((Player) sender).getUniqueId());
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
						if(sender.hasPermission("dc.godremoverole") || sender.hasPermission("dc.default") ) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You removed role " + ChatColor.WHITE +  args[2]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Role removed.");
						}
						break;
					default:
						break;
					}
				}
			}
			else if(args.length >= 4) {
				if(args[0].equalsIgnoreCase("setrole")) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
					if(ChannelAPI.setChannelPlayerRole(((Player) sender).getUniqueId(), p.getUniqueId(), args[1], args[3])) {
						if(sender.hasPermission("dc.godsetrole") || sender.hasPermission("dc.default") ) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You set player role to " + ChatColor.WHITE +  args[3]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "You set player role to " + ChatColor.WHITE +  args[3]);
						}
					}
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
						if(sender.hasPermission("dc.godaddrole") || sender.hasPermission("dc.default")) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You added role " + ChatColor.WHITE +  args[2]);
						}
						else {
							sender.sendMessage(ChatColor.YELLOW + "Successfully added new role.");
						}
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
					switch(ChannelAPI.getChannelByName(args[1]).setRolePrefix(((Player) sender).getUniqueId(), null, prefix)) {
					case INVALID_LENGTH:
						break;
					case SUCCESS:
						if(sender.hasPermission("dc.godsetroleprefix") || sender.hasPermission("dc.default") ) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You set leader role prefix to " + prefix);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Leader prefix changed to " + prefix);
						}
						break;
					default:
						break;
					
					}
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
						if(sender.hasPermission("dc.godsetroleprefix") || sender.hasPermission("dc.default")) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You set role prefix to " + prefix);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Successfully changed role's prefix.");
						}
						
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
