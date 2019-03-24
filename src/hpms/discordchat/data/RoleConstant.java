package hpms.discordchat.data;

import hpms.discordchat.utils.FileManager;
import net.md_5.bungee.api.ChatColor;

public class RoleConstant {
	protected static String LEADER = "leader";
	protected static String MEMBER = "member";
	protected static String ROLE = "role";
	protected static String PREFIX = "prefix";
	protected static String DEFAULT = "default";
	protected static String PERMISSION = "permission";
	protected static String CHANNEL_PREFIX = "channel-prefix";
	protected static String DEFAULT_MEMBER_PREFIX = ChatColor.translateAlternateColorCodes('&',FileManager.getConfig().getString("default-role"));
	protected static String DEFAULT_LEADER_PREFIX = ChatColor.translateAlternateColorCodes('&',FileManager.getConfig().getString("default-leader-prefix"));
}
