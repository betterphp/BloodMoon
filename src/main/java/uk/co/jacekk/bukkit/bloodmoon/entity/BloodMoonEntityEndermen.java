package uk.co.jacekk.bukkit.bloodmoon.entity;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.EntityHuman;
import net.minecraft.server.v1_6_R1.EntityMonster;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.nms.EntityEnderman;

public class BloodMoonEntityEndermen extends BloodMoonEntityMonster {
	
	public BloodMoonEntityEndermen(BloodMoon plugin, EntityMonster nmsEntity, CraftLivingEntity bukkitEntity, BloodMoonEntityType type){
		super(plugin, nmsEntity, bukkitEntity, type);
	}
	
	@Override
	public Entity findTarget(){
		String worldName = this.nmsEntity.world.worldData.getName();
		String entityName = this.bukkitEntity.getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		float distance = 64.0f;
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
			distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
		}
		
		EntityHuman entityhuman = this.nmsEntity.world.findNearbyVulnerablePlayer(this.nmsEntity, distance);
		EntityEnderman entityEnderman = (EntityEnderman) this.nmsEntity;
		
		if (entityhuman != null){
			if (entityEnderman.f(entityhuman)){
				entityEnderman.bv = true;
				
				if (entityEnderman.bt == 0){
					this.nmsEntity.world.makeSound(entityhuman, "mob.endermen.stare", 1.0F, 1.0F);
				}
				
				if (entityEnderman.bt++ == 5){
					entityEnderman.bt = 0;
					
					entityEnderman.a(true);
					
					return entityhuman;
				}
			}else{
				entityEnderman.bt = 0;
			}
		}
		
		return null;
	}
	
	@Override
	public void onTick(){
		String worldName = nmsEntity.world.worldData.getName();
		String entityName = bukkitEntity.getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (nmsEntity.target instanceof EntityHuman && plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.BREAK_BLOCKS) && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains(entityName) && nmsEntity.world.getTime() % 20 == 0 && nmsEntity.world.worldData.getName().equals(nmsEntity.target.world.worldData.getName())){
			Block[] blocks = new Block[3];
			
			blocks[0] = this.getBreakableTargetBlock();
			blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			blocks[2] = blocks[1].getRelative(BlockFace.DOWN);
			
			for (Block block : blocks){
				this.attemptBreakBlock(worldConfig, block);
			}
		}
	}
	
}
