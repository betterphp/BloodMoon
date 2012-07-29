package uk.co.jacekk.bukkit.bloodmoon.entities;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.UnsafeList;
import org.bukkit.entity.Skeleton;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.SkeletonMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonPathfinderGoalArrowAttack;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonPathfinderGoalNearestAttackableTarget;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.World;

public class BloodMoonEntitySkeleton extends net.minecraft.server.EntitySkeleton {
	
	private BloodMoon plugin;
	
	public BloodMoonEntitySkeleton(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		try{
			Field a = this.goalSelector.getClass().getDeclaredField("a");
			a.setAccessible(true);
			
			@SuppressWarnings("unchecked")
			UnsafeList<PathfinderGoal> goals = (UnsafeList<PathfinderGoal>) a.get(this.goalSelector);
			
			for (Object item : goals){
				Field goal = item.getClass().getDeclaredField("a");
				goal.setAccessible(true);
				
				if (goal.get(item) instanceof PathfinderGoalArrowAttack){
					goal.set(item, new BloodMoonPathfinderGoalArrowAttack(this, this.plugin, this.bb, 60));
				}
			}
			
			a.set(this.goalSelector, goals);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		if (this.plugin.config.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && this.plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("SKELETON")){
			try{
				Field a = this.targetSelector.getClass().getDeclaredField("a");
				a.setAccessible(true);
				
				@SuppressWarnings("unchecked")
				UnsafeList<PathfinderGoal> goals = (UnsafeList<PathfinderGoal>) a.get(this.targetSelector);
				
				for (Object item : goals){
					Field goal = item.getClass().getDeclaredField("a");
					goal.setAccessible(true);
					
					if (goal.get(item) instanceof PathfinderGoalNearestAttackableTarget){
						goal.set(item, new BloodMoonPathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 16.0F, 0, true));
					}
				}
				
				a.set(this.targetSelector, goals);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public BloodMoonEntitySkeleton(World world){
		this(world, Bukkit.getPluginManager().getPlugin("BloodMoon"));
	}
	
	@Override
	public void F_(){
		Skeleton skeleton = (Skeleton) this.getBukkitEntity();
		
		Location from = new Location(skeleton.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(skeleton.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		SkeletonMoveEvent event = new SkeletonMoveEvent(skeleton, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && !skeleton.isDead()){
			return;
		}
		
		super.F_();
	}
	
	@Override
	protected Entity findTarget(){
		float distance = (plugin.isActive(this.world.worldData.name) && plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("SKELETON")) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * 16.0f : 16.0f;
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, (double) distance);
		
		return entityhuman != null && this.h(entityhuman) ? entityhuman : null;
	}

}
