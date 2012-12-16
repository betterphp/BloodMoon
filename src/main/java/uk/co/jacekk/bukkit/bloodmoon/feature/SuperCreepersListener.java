package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SuperCreepersListener extends BaseListener<BloodMoon> {
	
	public SuperCreepersListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event){
		if (event.getEntity() != null && event.getEntityType() == EntityType.CREEPER && !event.blockList().isEmpty()){
			Location location = event.getLocation();
			World world = location.getWorld();
			String worldName = world.getName();
			PluginConfig worldConfig = plugin.getConfig(worldName);
			
			if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_SUPER_CREEPERS_ENABLED)){
				event.setCancelled(true);
				
				world.createExplosion(location, (float) worldConfig.getDouble(Config.FEATURE_SUPER_CREEPERS_POWER), worldConfig.getBoolean(Config.FEATURE_SUPER_CREEPERS_FIRE));
			}
		}
	}
	
}
