package uk.co.jacekk.bukkit.bloodmoon.entities;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;

import uk.co.jacekk.bukkit.bloodmoon.events.ZombieMoveEvent;

import net.minecraft.server.World;

public class BloodMoonEntityZombie extends net.minecraft.server.EntityZombie {
	
	public BloodMoonEntityZombie(World world){
		super(world);
	}
	
	@Override
	public void d_(){
		Zombie zombie = (Zombie) this.getBukkitEntity();
		
		Location from = new Location(zombie.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(zombie.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		ZombieMoveEvent event = new ZombieMoveEvent(zombie, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && zombie.isDead() == false){
			return;
		}
		
		super.d_();
	}
	
}
