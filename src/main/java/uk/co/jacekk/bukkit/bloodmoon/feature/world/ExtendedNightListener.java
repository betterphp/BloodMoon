package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class ExtendedNightListener extends BaseListener<BloodMoon> {
	
	private HashMap<String, ExtendedNightTask> worldTasks;
	private ArrayList<EntityType> hostileTypes;
	
	public ExtendedNightListener(BloodMoon plugin){
		super(plugin);
		
		this.worldTasks = new HashMap<String, ExtendedNightTask>();
		this.hostileTypes = new ArrayList<EntityType>();
		
		this.hostileTypes.add(EntityType.SKELETON);
		this.hostileTypes.add(EntityType.SPIDER);
		this.hostileTypes.add(EntityType.CAVE_SPIDER);
		this.hostileTypes.add(EntityType.ZOMBIE);
		this.hostileTypes.add(EntityType.PIG_ZOMBIE);
		this.hostileTypes.add(EntityType.CREEPER);
		this.hostileTypes.add(EntityType.ENDERMAN);
		this.hostileTypes.add(EntityType.BLAZE);
		this.hostileTypes.add(EntityType.GHAST);
		this.hostileTypes.add(EntityType.MAGMA_CUBE);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isFeatureEnabled(worldName, Feature.EXTENDED_NIGHT)){
			ExtendedNightTask task = new ExtendedNightTask(plugin, world, worldConfig);
			int taskID = plugin.scheduler.scheduleSyncRepeatingTask(plugin, task, 0L, 100L);
			
			if (taskID != -1){
				task.taskID = taskID;
				this.worldTasks.put(worldName, task);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		
		ExtendedNightTask task = this.worldTasks.get(worldName);
		
		if (task != null){
			plugin.scheduler.cancelTask(task.taskID);
			this.worldTasks.remove(worldName);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event){
		if (this.hostileTypes.contains(event.getEntityType())){
			ExtendedNightTask task = this.worldTasks.get(event.getEntity().getWorld().getName());
			
			if (task != null){
				++task.kills;
			}
		}
	}
	
}
