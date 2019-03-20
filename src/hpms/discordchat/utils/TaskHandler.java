package hpms.discordchat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;

public abstract class TaskHandler implements Runnable{
	
	private static HashMap<UUID,ArrayList<Integer>> taskIdMap = Maps.newHashMap();
	
	private UUID owner;

	public void cancelTask() {
		int id = taskIdMap.get(owner).get(taskIdMap.get(owner).size() - 1);
		Bukkit.getScheduler().cancelTask(id);
		if(!Bukkit.getScheduler().isCurrentlyRunning(id)) {
			taskIdMap.get(owner).remove(taskIdMap.get(owner).size() - 1);
		}
	}
	
	/*
	 * FIFO-Order
	 */
	public TaskHandler(UUID owner,JavaPlugin plugin,int delay,int period) {
		if(!taskIdMap.containsKey(owner)) taskIdMap.put(owner, new ArrayList<Integer>());
		this.owner = owner;
		taskIdMap.get(owner).add(0,Bukkit.getScheduler().runTaskTimerAsynchronously(plugin,this,delay,period).getTaskId());
	}

}
