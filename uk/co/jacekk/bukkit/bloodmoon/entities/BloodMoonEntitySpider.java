package uk.co.jacekk.bukkit.bloodmoon.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.SpiderMoveEvent;

import net.minecraft.server.Entity;
import net.minecraft.server.World;

public class BloodMoonEntitySpider extends net.minecraft.server.EntitySpider {
	
	private BloodMoon plugin;
	
	public BloodMoonEntitySpider(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
	}
	
	public BloodMoonEntitySpider(World world){
		this(world, Bukkit.getPluginManager().getPlugin("BloodMoon"));
	}
	
	@Override
	public void F_(){
		Spider spider = (Spider) this.getBukkitEntity();
		
		Location from = new Location(spider.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(spider.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		SpiderMoveEvent event = new SpiderMoveEvent(spider, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && !spider.isDead()){
			return;
		}
		
		super.F_();
	}
	
	@Override
	protected Entity findTarget(){
		float f = this.b(1.0F);
		
		if (f < 0.5F){
			double distance = (plugin.isActive(this.world.worldData.name) && plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("SPIDER")) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * 16.0d : 16.0d;
			
			return this.world.findNearbyVulnerablePlayer(this, distance);
		}else{
			return null;
		}
	}

}
