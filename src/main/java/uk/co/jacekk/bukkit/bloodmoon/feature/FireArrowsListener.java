package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;

import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v8.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntitySkeleton;

public class FireArrowsListener extends BaseListener<BloodMoon> {
	
	public FireArrowsListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onProjectileHit(ProjectileHitEvent event){
		Entity entity = event.getEntity();
		String worldName = entity.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (entity instanceof Projectile && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED) && worldConfig.getBoolean(Config.FEATURE_FIRE_ARROWS_IGNITE_TARGET)){
			Projectile projectile = (Projectile) entity;
			LivingEntity shooter = projectile.getShooter();
			
			if (shooter != null && ((CraftEntity) shooter).getHandle() instanceof BloodMoonEntitySkeleton){
				Block block = projectile.getWorld().getBlockAt(projectile.getLocation());
				
				if (block.getType() != Material.AIR){
					block.setType(Material.FIRE);
				}
			}
		}
	}
	
}
