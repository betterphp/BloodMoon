package uk.co.jacekk.bukkit.bloodmoon.entity;

import net.minecraft.server.v1_5_R1.EntityHuman;
import net.minecraft.server.v1_5_R1.EntityMonster;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftLivingEntity;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class BloodMoonEntityEndermen extends BloodMoonEntityMonster {
	
	public BloodMoonEntityEndermen(BloodMoon plugin, EntityMonster nmsEntity, CraftLivingEntity bukkitEntity, BloodMoonEntityType type){
		super(plugin, nmsEntity, bukkitEntity, type);
	}
	
	@Override
	public void onTick(){
		String worldName = nmsEntity.world.worldData.getName();
		String entityName = bukkitEntity.getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (nmsEntity.target instanceof EntityHuman && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_ENABLED) && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains(entityName) && nmsEntity.world.getTime() % 20 == 0 && nmsEntity.world.worldData.getName().equals(nmsEntity.target.world.worldData.getName())){
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
