package uk.co.jacekk.bukkit.bloodmoon.entities;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;

import uk.co.jacekk.bukkit.bloodmoon.events.CreeperMoveEvent;

import net.minecraft.server.World;

public class BloodMoonEntityCreeper extends net.minecraft.server.EntityCreeper {

	public BloodMoonEntityCreeper(World world) {
		super(world);
	}
	
	@Override
	public void d_(){
		Creeper creeper = (Creeper) this.getBukkitEntity();
		
		Location from = new Location(creeper.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(creeper.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		CreeperMoveEvent event = new CreeperMoveEvent(creeper, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && creeper.isDead() == false){
			return;
		}
		
		super.d_();
	}
	
}
