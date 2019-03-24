package hpms.discordchat.data;

import hpms.discordchat.utils.FileManager;

public class ChannelDataConstant {
	protected static String STORAGE = "storage";
	protected static String LEADER = "leader";
	protected static String SLOT = "slot";
	protected static String LIST = "list";
	public static String DEFAULT_CHANNEL = FileManager.getConfig().getString("default-join-server-channel");
	public static int DEFAULT_SLOT = FileManager.getConfig().getInt("default-channel-slot");
}
