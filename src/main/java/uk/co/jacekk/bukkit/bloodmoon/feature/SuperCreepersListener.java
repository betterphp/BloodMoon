package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v8.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SuperCreepersListener extends BaseListener<BloodMoon> {
	
	public SuperCreepersListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onExplosionProme(ExplosionPrimeEvent event){
		if (event.getEntity() != null && event.getEntityType() == EntityType.CREEPER){
			String worldName = event.getEntity().getWorld().getName();
			PluginConfig worldConfig = plugin.getConfig(worldName);
			
			if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_SUPER_CREEPERS_ENABLED)){
				event.setRadius((float) worldConfig.getDouble(Config.FEATURE_SUPER_CREEPERS_POWER));
				event.setFire(worldConfig.getBoolean(Config.FEATURE_SUPER_CREEPERS_FIRE));
			}
		}
	}
	
}
