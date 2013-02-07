package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.Random;

import uk.co.jacekk.bukkit.baseplugin.v9.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.MathHelper;
import net.minecraft.server.v1_4_R1.Navigation;
import net.minecraft.server.v1_4_R1.PathEntity;
import net.minecraft.server.v1_4_R1.World;

public class BloodMoonNavigation extends Navigation {
	
	private BloodMoon plugin;
	private EntityLiving entity;
	private Random random;
	
	private PluginConfig worldConfig;
	private float multiplier;
	
	public BloodMoonNavigation(BloodMoon plugin, EntityLiving entity, World world, float f){
		super(entity, world, f);
		
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
	public boolean a(double d0, double d1, double d2, float f){
		PathEntity pathentity = this.a(MathHelper.floor(d0), ((int) d1), MathHelper.floor(d2));
		
		return this.a(pathentity, f);
	}
	
	@Override
	public boolean a(EntityLiving entityliving, float f){
		PathEntity pathentity = this.a(entityliving);
		
		return pathentity != null ? this.a(pathentity, f) : false;
	}
	
	@Override
	public boolean a(PathEntity path, float speed){
		String worldName = this.entity.world.worldData.getName();
		String entityName = this.entity.getBukkitEntity().getType().name().toUpperCase();
		
		if (plugin.isActive(worldName) && this.worldConfig.getBoolean(Config.FEATURE_MOVEMENT_SPEED_ENABLED) && this.worldConfig.getStringList(Config.FEATURE_MOVEMENT_SPEED_MOBS).contains(entityName)){
			speed *= this.multiplier;
		}
		
		return super.a(path, speed);
	}
	
}
