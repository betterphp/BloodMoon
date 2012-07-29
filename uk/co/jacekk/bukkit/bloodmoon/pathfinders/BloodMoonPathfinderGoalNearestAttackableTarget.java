package uk.co.jacekk.bukkit.bloodmoon.pathfinders;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoalTarget;

public class BloodMoonPathfinderGoalNearestAttackableTarget extends PathfinderGoalTarget {
	
	private BloodMoon plugin;
	
	private EntityLiving entity;
	@SuppressWarnings("rawtypes")
	private Class targetType;
	private int f;
	
	private BloodMoonDistanceComparator g;
	
	public BloodMoonPathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityLiving entity, Class<?> targetType, float distance, int i, boolean flag, boolean flag1){
		super(entity, distance, flag, flag1);
		
		this.plugin = plugin;
		
		this.entity = entity;
		this.targetType = targetType;
		this.d = distance;
		this.f = i;
		this.g = new BloodMoonDistanceComparator(entity);
	}
	
	public BloodMoonPathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityLiving entity, Class<?> oclass, float distance, int i, boolean flag){
		this(plugin, entity, oclass, distance, i, flag, false);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public boolean a(){
		float distance = (plugin.isActive(this.entity.world.worldData.name)) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * this.d : this.d;
				
		if (this.f > 0 && this.c.an().nextInt(this.f) != 0){
			return false;
		}else{
			if (this.targetType == EntityHuman.class){
				EntityHuman entityhuman = this.c.world.findNearbyVulnerablePlayer(this.c, (double) distance);
				
				if (this.a(entityhuman, false)){
					this.entity = entityhuman;
					return true;
				}
			}else{
				List list = this.c.world.a(this.targetType, this.c.boundingBox.grow((double) distance, 4.0D, (double) distance));
				
				Collections.sort(list, this.g);
				Iterator iterator = list.iterator();
				
				while (iterator.hasNext()){
					Entity entity = (Entity) iterator.next();
					EntityLiving entityliving = (EntityLiving) entity;
					
					if (this.a(entityliving, false)){
						this.entity = entityliving;
						return true;
					}
				}
			}
			
			return false;
		}
	}
	
	public void c(){
		this.c.b(this.entity);
		super.c();
	}
	
}
