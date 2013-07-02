package uk.co.jacekk.bukkit.bloodmoon.entity;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.EntityHuman;
import net.minecraft.server.v1_6_R1.EntityMonster;

import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class BloodMoonEntitySpider extends BloodMoonEntityMonster {
	
	public BloodMoonEntitySpider(BloodMoon plugin, EntityMonster nmsEntity, CraftLivingEntity bukkitEntity, BloodMoonEntityType type){
		super(plugin, nmsEntity, bukkitEntity, type);
	}
	
	@Override
	public Entity findTarget(){
		float f = this.nmsEntity.d(1.0F);
		
		if (f < 0.5F){
			String worldName = this.nmsEntity.world.worldData.getName();
			String entityName = this.bukkitEntity.getType().name().toUpperCase();
			PluginConfig worldConfig = plugin.getConfig(worldName);
			
			double distance = 16.0d;
			
			if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
				distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
			}
			
			return this.nmsEntity.world.findNearbyVulnerablePlayer(this.nmsEntity, distance);
		}
		
		return null;
	}
	
	@Override
	public void onTick(){
		String worldName = nmsEntity.world.worldData.getName();
		String entityName = bukkitEntity.getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (nmsEntity.target instanceof EntityHuman && plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.BREAK_BLOCKS) && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains(entityName) && nmsEntity.world.getTime() % 20 == 0 && nmsEntity.world.worldData.getName().equals(nmsEntity.target.world.worldData.getName())){
			this.attemptBreakBlock(worldConfig, this.getBreakableTargetBlock());
		}
	}
	
}
