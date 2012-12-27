package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class DoubleHealthListener extends BaseListener<BloodMoon> {
	
	public DoubleHealthListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_DOUBLE_HEALTH_ENABLED)){
			for (LivingEntity livingEntity : world.getLivingEntities()){
				CraftLivingEntity entity = (CraftLivingEntity) livingEntity;
				
				if (worldConfig.getStringList(Config.FEATURE_DOUBLE_HEALTH_MOBS).contains(entity.getType().name())){
					entity.setMaxHealth(entity.getMaxHealth() * 2);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		CraftLivingEntity entity = (CraftLivingEntity) event.getEntity();
		String worldName = event.getLocation().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_DOUBLE_HEALTH_ENABLED) && worldConfig.getStringList(Config.FEATURE_DOUBLE_HEALTH_MOBS).contains(entity.getType().name())){
			entity.setMaxHealth(entity.getMaxHealth() * 2);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_DOUBLE_HEALTH_ENABLED)){
			for (LivingEntity livingEntity : world.getLivingEntities()){
				CraftLivingEntity entity = (CraftLivingEntity) livingEntity;
				
				if (worldConfig.getStringList(Config.FEATURE_DOUBLE_HEALTH_MOBS).contains(entity.getType().name())){
					entity.resetMaxHealth();
				}
			}
		}
	}
	
}
