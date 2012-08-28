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
	private int c;
	
	private BloodMoonDistanceComparator g;
	
	public BloodMoonPathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityLiving entity, Class<?> targetType, float distance, int i, boolean flag, boolean flag1){
		super(entity, distance, flag, flag1);
		
		this.plugin = plugin;
		
		this.entity = entity;
		this.targetType = targetType;
		this.e = distance;
		this.c = i;
		this.g = new BloodMoonDistanceComparator(entity);
	}
	
	public BloodMoonPathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityLiving entity, Class<?> oclass, float distance, int i, boolean flag){
		this(plugin, entity, oclass, distance, i, flag, false);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public boolean a(){
		float distance = (plugin.isActive(this.entity.world.worldData.getName())) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * this.e : this.e;
				
		if (this.c > 0 && this.d.au().nextInt(this.c) != 0){
			return false;
		}
		
		if (this.targetType == EntityHuman.class){
			EntityHuman entityhuman = this.d.world.findNearbyVulnerablePlayer(this.d, distance);
			
			if (this.a(entityhuman, false)){
				this.entity = entityhuman;
				return true;
			}
		}else{
			List list = this.d.world.a(this.targetType, this.d.boundingBox.grow( distance, 4.0D, distance));
			
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
	
	public void e(){
		this.d.b(this.entity);
		super.e();
	}
	
}
