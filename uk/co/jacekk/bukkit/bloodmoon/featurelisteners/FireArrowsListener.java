package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySkeleton;

public class FireArrowsListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onProjectileHit(ProjectileHitEvent event){
		Entity entity = event.getEntity();
		
		if (entity instanceof Projectile && BloodMoon.bloodMoonWorlds.contains(entity.getWorld().getName())){
			Projectile projectile = (Projectile) entity;
			
			LivingEntity shooter = projectile.getShooter();
			
			if (shooter != null){
				if (((CraftEntity) shooter).getHandle() instanceof BloodMoonEntitySkeleton){
					Block block = projectile.getWorld().getBlockAt(projectile.getLocation());
					
					if (block.getType() != Material.AIR){
						block.setType(Material.FIRE);
					}
				}
			}
		}
	}
	
}
