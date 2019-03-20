package hpms.discordchat.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class TaskHandler implements Runnable{
	
	private static List<Integer> taskIdList = new ArrayList<Integer>();

	public static void cancelTask() {
		Bukkit.getScheduler().cancelTask(taskIdList.get(taskIdList.size() - 1));
	}
	/*
	 * FIFO-Order
	 */
	public TaskHandler(JavaPlugin plugin,int delay,int period) {
		taskIdList.add(0,Bukkit.getScheduler().runTaskTimerAsynchronously(plugin,this,delay,period).getTaskId());
	}

}
