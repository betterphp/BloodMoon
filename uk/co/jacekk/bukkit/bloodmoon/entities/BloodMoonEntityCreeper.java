package uk.co.jacekk.bukkit.bloodmoon.entities;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.UnsafeList;
import org.bukkit.entity.Creeper;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.CreeperMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonPathfinderGoalNearestAttackableTarget;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.World;

public class BloodMoonEntityCreeper extends net.minecraft.server.EntityCreeper {
	
	private BloodMoon plugin;
	
	public BloodMoonEntityCreeper(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		if (this.plugin.config.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && this.plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("CREEPER")){
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
	
	public BloodMoonEntityCreeper(World world){
		this(world, Bukkit.getPluginManager().getPlugin("BloodMoon"));
	}
	
	@Override
	public void F_(){
		Creeper creeper = (Creeper) this.getBukkitEntity();
		
		Location from = new Location(creeper.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(creeper.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		CreeperMoveEvent event = new CreeperMoveEvent(creeper, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && !creeper.isDead()){
			return;
		}
		
		super.F_();
	}
	
	@Override
	protected Entity findTarget(){
		float distance = (plugin.isActive(this.world.worldData.name) && plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("CREEPER")) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * 16.0f : 16.0f;
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, (double) distance);
		
		return entityhuman != null && this.h(entityhuman) ? entityhuman : null;
	}
	
}