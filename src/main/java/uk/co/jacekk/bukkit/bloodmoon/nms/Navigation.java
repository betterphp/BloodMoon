package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.Random;

import net.minecraft.server.v1_5_R1.EntityLiving;
import net.minecraft.server.v1_5_R1.PathEntity;
import net.minecraft.server.v1_5_R1.World;
import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class Navigation extends net.minecraft.server.v1_5_R1.Navigation {
	
	private BloodMoon plugin;
	private EntityLiving entity;
	private Random random;
	
	private PluginConfig worldConfig;
	private float multiplier;
	
	public Navigation(BloodMoon plugin, EntityLiving entity, World world, int i){
		super(entity, world, i);
		
		this.plugin = plugin;
		this.entity = entity;
		this.random = new Random();
		
		this.worldConfig = plugin.getConfig(this.entity.world.worldData.getName());
		
		if (this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_MOVEMENT_SPEED_FAST_CHANCE)){
			this.multiplier = (float) worldConfig.getDouble(Config.FEATURE_MOVEMENT_SPEED_FAST_MULTIPLIER);
		}else{
			this.multiplier = (float) worldConfig.getDouble(Config.FEATURE_MOVEMENT_SPEED_MULTIPLIER);
		}
	}
	
	@Override
	public boolean a(PathEntity path, float speed){
		String worldName = this.entity.world.worldData.getName();
		String entityName = this.entity.getBukkitEntity().getType().name().toUpperCase();
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.MOVEMENT_SPEED) && this.worldConfig.getStringList(Config.FEATURE_MOVEMENT_SPEED_MOBS).contains(entityName)){
			speed *= this.multiplier;
		}
		
		return super.a(path, speed);
	}
	
}
