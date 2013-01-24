package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v8.event.BaseListener;
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
			for (LivingEntity entity : world.getLivingEntities()){
				if (worldConfig.getStringList(Config.FEATURE_DOUBLE_HEALTH_MOBS).contains(entity.getType().name())){
					int newMaxHealth = (int) (entity.getMaxHealth() * worldConfig.getDouble(Config.FEATURE_DOUBLE_HEALTH_MULTIPLIER));
					int damage = entity.getMaxHealth() - entity.getHealth();
					
					entity.setMaxHealth(newMaxHealth);
					entity.setHealth(newMaxHealth - damage);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		LivingEntity entity = event.getEntity();
		String worldName = event.getLocation().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_DOUBLE_HEALTH_ENABLED) && worldConfig.getStringList(Config.FEATURE_DOUBLE_HEALTH_MOBS).contains(entity.getType().name())){
			int newMaxHealth = (int) (entity.getMaxHealth() * worldConfig.getDouble(Config.FEATURE_DOUBLE_HEALTH_MULTIPLIER));
			int damage = entity.getMaxHealth() - entity.getHealth();
			
			entity.setMaxHealth(newMaxHealth);
			entity.setHealth(newMaxHealth - damage);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_DOUBLE_HEALTH_ENABLED)){
			for (LivingEntity entity : world.getLivingEntities()){
				if (worldConfig.getStringList(Config.FEATURE_DOUBLE_HEALTH_MOBS).contains(entity.getType().name())){
					entity.resetMaxHealth();
				}
			}
		}
	}
	
}
