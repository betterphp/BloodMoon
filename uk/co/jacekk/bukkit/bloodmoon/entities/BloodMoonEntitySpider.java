package uk.co.jacekk.bukkit.bloodmoon.entities;

import org.bukkit.Location;
import org.bukkit.entity.Spider;

import uk.co.jacekk.bukkit.bloodmoon.events.SpiderMoveEvent;

import net.minecraft.server.World;

public class BloodMoonEntitySpider extends net.minecraft.server.EntitySpider {
	
	public BloodMoonEntitySpider(World world){
		super(world);
	}
	
	@Override
	public void d_(){
		Spider spider = (Spider) this.getBukkitEntity();
		
		Location from = new Location(spider.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(spider.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		SpiderMoveEvent event = new SpiderMoveEvent(spider, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && spider.isDead() == false){
			return;
		}
		
		super.d_();
	}
	
}
