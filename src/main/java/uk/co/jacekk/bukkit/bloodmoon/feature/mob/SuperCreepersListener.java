package uk.co.jacekk.bukkit.bloodmoon.feature.mob;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class SuperCreepersListener extends BaseListener<BloodMoon> {
	
	public SuperCreepersListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onExplosionProme(ExplosionPrimeEvent event){
		if (event.getEntity() != null && event.getEntityType() == EntityType.CREEPER){
			String worldName = event.getEntity().getWorld().getName();
			PluginConfig worldConfig = plugin.getConfig(worldName);
			
			if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.SUPER_CREEPERS)){
				event.setRadius((float) worldConfig.getDouble(Config.FEATURE_SUPER_CREEPERS_POWER));
				event.setFire(worldConfig.getBoolean(Config.FEATURE_SUPER_CREEPERS_FIRE));
			}
		}
	}
	
}
