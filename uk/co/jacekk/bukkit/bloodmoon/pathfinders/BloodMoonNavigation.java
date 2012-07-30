package uk.co.jacekk.bukkit.bloodmoon.pathfinders;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.World;

public class BloodMoonNavigation extends Navigation {
	
	private BloodMoon plugin;
	private EntityLiving entity;
	
	public BloodMoonNavigation(BloodMoon plugin, EntityLiving entity, World world, float f){
		super(entity, world, f);
		
		this.plugin = plugin;
		this.entity = entity;
	}
	
	@Override
	public boolean a(double d0, double d1, double d2, float f){
		PathEntity pathentity = this.a((double) MathHelper.floor(d0), (double) ((int) d1), (double) MathHelper.floor(d2));
		
		return this.a(pathentity, f);
	}
	
	@Override
	public boolean a(EntityLiving entityliving, float f){
		PathEntity pathentity = this.a(entityliving);
		
		return pathentity != null ? this.a(pathentity, f) : false;
	}
	
	@Override
	public boolean a(PathEntity path, float speed){
		if (plugin.isActive(this.entity.world.worldData.name)){
			speed *= (float) plugin.config.getDouble(Config.FEATURE_MOVEMENT_SPEED_MULTIPLIER);
		}
		
		return super.a(path, speed);
	}
	
}
