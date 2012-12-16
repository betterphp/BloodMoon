package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;

public class NetherMobsListener extends BaseListener<BloodMoon> {
	
	private ArrayList<EntityType> netherEntities;
	private HashMap<String, Integer> worldTasks;
	
	public NetherMobsListener(BloodMoon plugin){
		super(plugin);
		
		this.netherEntities = new ArrayList<EntityType>();
		this.worldTasks = new HashMap<String, Integer>();
		
		this.netherEntities.add(EntityType.GHAST);
		this.netherEntities.add(EntityType.PIG_ZOMBIE);
		this.netherEntities.add(EntityType.BLAZE);
		this.netherEntities.add(EntityType.MAGMA_CUBE);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_NETHER_MOBS_ENABLED)){
			int taskID = plugin.scheduler.scheduleSyncRepeatingTask(plugin, new NetherMobsTask(plugin, world, this.netherEntities), 0L, 100L);
			
			if (taskID != -1){
				this.worldTasks.put(worldName, taskID);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		
		Integer taskID = this.worldTasks.get(worldName);
		
		if (taskID != null){
			plugin.scheduler.cancelTask(taskID);
			this.worldTasks.remove(worldName);
			
			for (LivingEntity entity : world.getLivingEntities()){
				if (this.netherEntities.contains(entity.getType())){
					entity.remove();
				}
			}
		}
	}
	
}
