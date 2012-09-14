package uk.co.jacekk.bukkit.bloodmoon.entities;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftCreeper;
import org.bukkit.entity.Creeper;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.CreeperMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonNavigation;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonPathfinderGoalNearestAttackableTarget;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalAvoidPlayer;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalSwell;
import net.minecraft.server.World;

public class BloodMoonEntityCreeper extends net.minecraft.server.EntityCreeper {
	
	private BloodMoon plugin;
	
	@SuppressWarnings("unchecked")
	public BloodMoonEntityCreeper(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftCreeper((CraftServer) this.plugin.server, this);
		
		if (this.plugin.config.getBoolean(Config.FEATURE_MOVEMENT_SPEED_ENABLED) && this.plugin.config.getStringList(Config.FEATURE_MOVEMENT_SPEED_MOBS).contains("CREEPER")){
			try{
				Field navigation = EntityLiving.class.getDeclaredField("navigation");
				navigation.setAccessible(true);
				navigation.set(this, new BloodMoonNavigation(this.plugin, this, this.world, 16.0f));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		try{
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();
			
			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
			
	        this.goalSelector.a(1, new PathfinderGoalFloat(this));
	        this.goalSelector.a(2, new PathfinderGoalSwell(this));
	        this.goalSelector.a(3, new PathfinderGoalAvoidPlayer(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
	        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 0.25F, false));
	        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.2F));
	        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
	        
	        if (this.plugin.config.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && this.plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("CREEPER")){
	        	this.targetSelector.a(1, new BloodMoonPathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 16.0F, 0, true));
	        }else{
	        	this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
	        }
	        
	        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void h_(){
		Creeper creeper = (Creeper) this.getBukkitEntity();
		
		Location from = new Location(creeper.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(creeper.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		CreeperMoveEvent event = new CreeperMoveEvent(creeper, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && !creeper.isDead()){
			return;
		}
		
		super.h_();
	}
	
	@Override
	protected Entity findTarget(){
		float distance = (plugin.isActive(this.world.worldData.getName()) && plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("CREEPER")) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * 16.0f : 16.0f;
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, distance);
		
		return entityhuman != null && this.l(entityhuman) ? entityhuman : null;
	}
	
}