package uk.co.jacekk.bukkit.bloodmoon.feature.mob;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityLiving;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class TargetDistanceListener extends BaseListener<BloodMoon> {
	
	public TargetDistanceListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isFeatureEnabled(worldName, Feature.TARGET_DISTANCE)){
			for (LivingEntity entity : world.getLivingEntities()){
				if (worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entity.getType().name())){
					try{
						BloodMoonEntityLiving bloodMoonEntity = BloodMoonEntityLiving.getBloodMoonEntity(((CraftLivingEntity) entity).getHandle());
						bloodMoonEntity.setFollowRangeMultiplier(worldConfig.getDouble(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER));
					}catch (IllegalArgumentException e){
						// This means the entity is not supported *shrug*
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		
		if (plugin.isFeatureEnabled(worldName, Feature.TARGET_DISTANCE)){
			for (LivingEntity entity : world.getLivingEntities()){
				try{
					BloodMoonEntityLiving.getBloodMoonEntity(((CraftLivingEntity) entity).getHandle()).clearFollowRangeMultiplier();
				}catch (IllegalArgumentException e){
					// This means the entity is not supported *shrug*
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		LivingEntity entity = event.getEntity();
		World world = entity.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.TARGET_DISTANCE)){
			if (worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entity.getType().name())){
				try{
					BloodMoonEntityLiving bloodMoonEntity = BloodMoonEntityLiving.getBloodMoonEntity(((CraftLivingEntity) entity).getHandle());
					bloodMoonEntity.setFollowRangeMultiplier(worldConfig.getDouble(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER));
				}catch (IllegalArgumentException e){
					// This means the entity is not supported *shrug*
				}
			}
		}
	}
	
}
