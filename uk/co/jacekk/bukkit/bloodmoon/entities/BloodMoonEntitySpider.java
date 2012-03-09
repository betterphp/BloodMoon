package uk.co.jacekk.bukkit.bloodmoon.entities;

import org.bukkit.Location;
import org.bukkit.entity.Spider;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.events.SpiderMoveEvent;

import net.minecraft.server.Entity;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

public class BloodMoonEntitySpider extends net.minecraft.server.EntitySpider {
	
	private boolean bloodMoonState;
	
	public BloodMoonEntitySpider(World world){
		super(world);
		
		this.bloodMoonState = BloodMoon.bloodMoonWorlds.contains(world.worldData.name);
	}
/*	
	protected void a(Entity entity, float f){
		float f1 = this.a(1.0F);
		
		if (f1 > 0.5F && this.random.nextInt(100) == 0){
			this.target = null;
		}else{
			double multiplier = BloodMoon.config.getDouble("features.spider-jump.multiplier");
			
			if (f > 2.0F * multiplier && f < 6.0D * multiplier && this.random.nextInt(10) == 0){
				if (this.onGround){
					double d0 = entity.locX - this.locX;
					double d1 = entity.locZ - this.locZ;
					float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
					
					if (this.bloodMoonState && BloodMoon.config.getBoolean("features.spider-jump.enabled")){
						this.motX = multiplier * (d0 / (double) f2 * 0.5D * 0.800000011920929D + this.motX * 0.20000000298023224D);
						this.motZ = multiplier * (d1 / (double) f2 * 0.5D * 0.800000011920929D + this.motZ * 0.20000000298023224D);
						this.motY = multiplier * 0.5 * 0.4000000059604645D;
					}else{
						this.motX = d0 / (double) f2 * 0.5D * 0.800000011920929D + this.motX * 0.20000000298023224D;
						this.motZ = d1 / (double) f2 * 0.5D * 0.800000011920929D + this.motZ * 0.20000000298023224D;
						this.motY = 0.4000000059604645D;
					}
				}
			}else{
				super.a(entity, f);
			}
		}
	}
*/	
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
