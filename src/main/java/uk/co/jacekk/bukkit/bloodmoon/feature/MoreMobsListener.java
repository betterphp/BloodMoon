package uk.co.jacekk.bukkit.bloodmoon.feature;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class MoreMobsListener extends BaseListener<BloodMoon> {
	
	private HashMap<String, Integer> worldTasks;
	
	public MoreMobsListener(BloodMoon plugin){
		super(plugin);
		
		this.worldTasks = new HashMap<String, Integer>();
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_MORE_MOBS_ENABLED)){
			int taskID = plugin.scheduler.scheduleSyncRepeatingTask(plugin, new MoreMobsTask(plugin, world), 0L, 100L);
			
			if (taskID != -1){
				this.worldTasks.put(worldName, taskID);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		Integer taskID = this.worldTasks.get(worldName);
		
		if (taskID != null){
			plugin.scheduler.cancelTask(taskID);
			this.worldTasks.remove(worldName);
			
			for (LivingEntity entity : world.getLivingEntities()){
				if (worldConfig.getStringList(Config.FEATURE_MORE_MOBS_SPAWN).contains(entity.getType().name())){
					entity.remove();
				}
			}
		}
	}
	
}
