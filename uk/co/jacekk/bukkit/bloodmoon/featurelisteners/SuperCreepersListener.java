package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class SuperCreepersListener extends BaseListener<BloodMoon> {
	
	public SuperCreepersListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event){
		if (event.isCancelled()) return;
		
		if (event.getEntity() instanceof Creeper){
			Location location = event.getLocation();
			World world = location.getWorld();
			String worldName = world.getName();
			
			if (BloodMoon.bloodMoonWorlds.contains(worldName)){
				event.setCancelled(true);
				
				world.createExplosion(location, (float) BloodMoon.config.getDouble("features.super-creepers.power"), true);
			}
		}
	}
	
}
