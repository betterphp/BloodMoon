package uk.co.jacekk.bukkit.bloodmoon.entities;

import org.bukkit.Location;
import org.bukkit.entity.Enderman;

import uk.co.jacekk.bukkit.bloodmoon.events.EndermanMoveEvent;

import net.minecraft.server.World;

public class BloodMoonEntityEnderman extends net.minecraft.server.EntityEnderman {

	public BloodMoonEntityEnderman(World world) {
		super(world);
	}
	
	@Override
	public void d_(){
		Enderman enderman = (Enderman) this.getBukkitEntity();
		
		Location from = new Location(enderman.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(enderman.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		EndermanMoveEvent event = new EndermanMoveEvent(enderman, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && enderman.isDead() == false){
			return;
		}
		
		super.d_();
	}
	
}
