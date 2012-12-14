package uk.co.jacekk.bukkit.bloodmoon.entities;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_5.CraftServer;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftEnderman;
import org.bukkit.entity.Enderman;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.EndermanMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonNavigation;

import net.minecraft.server.v1_4_5.Block;
import net.minecraft.server.v1_4_5.Entity;
import net.minecraft.server.v1_4_5.EntityHuman;
import net.minecraft.server.v1_4_5.EntityLiving;
import net.minecraft.server.v1_4_5.ItemStack;
import net.minecraft.server.v1_4_5.Vec3D;
import net.minecraft.server.v1_4_5.World;

public class BloodMoonEntityEnderman extends net.minecraft.server.v1_4_5.EntityEnderman {
	
	private BloodMoon plugin;
	private int h = 0;
	
	public BloodMoonEntityEnderman(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftEnderman((CraftServer) this.plugin.server, this);
		
		if (this.plugin.config.getBoolean(Config.FEATURE_MOVEMENT_SPEED_ENABLED) && this.plugin.config.getStringList(Config.FEATURE_MOVEMENT_SPEED_MOBS).contains("ENDERMAN")){
			try{
				Field navigation = EntityLiving.class.getDeclaredField("navigation");
				navigation.setAccessible(true);
				navigation.set(this, new BloodMoonNavigation(this.plugin, this, this.world, 16.0f));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void j_(){
		Enderman enderman = (Enderman) this.getBukkitEntity();
		
		Location from = new Location(enderman.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(enderman.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		EndermanMoveEvent event = new EndermanMoveEvent(enderman, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && !enderman.isDead()){
			return;
		}
		
		super.j_();
	}
	
	private boolean d(EntityHuman entityhuman){
		ItemStack itemstack = entityhuman.inventory.armor[3];
		
		if (itemstack != null && itemstack.id == Block.PUMPKIN.id){
			return false;
		}
		
		Vec3D vec3d = entityhuman.i(1.0F).a();
		Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX - entityhuman.locX, this.boundingBox.b + (this.length / 2.0F) - (entityhuman.locY + entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
		double d0 = vec3d1.b();
		
		vec3d1 = vec3d1.a();
		double d1 = vec3d.b(vec3d1);
		
		return d1 > 1.0D - 0.025D / d0 ? entityhuman.m(this) : false;
	}
	
	@Override
	protected Entity findTarget(){
		double distance = (plugin.isActive(this.world.worldData.getName()) && plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("ENDERMAN")) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * 64.0d : 64.0d;
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, distance);
		
		if (entityhuman != null){
			if (this.d(entityhuman)){
				if (this.h++ == 5){
					this.h = 0;
					return entityhuman;
				}
			}else{
				this.h = 0;
			}
		}
		
		return null;
	}
	
}
