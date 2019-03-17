package hpms.discordchat.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class TaskHandler implements Runnable{
	
	private int taskId;
	protected JavaPlugin plugin;

	public void cancelTask() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	public TaskHandler(JavaPlugin plugin,int delay,int period) {
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, period);
		this.plugin = plugin;
	}
	
	

}
