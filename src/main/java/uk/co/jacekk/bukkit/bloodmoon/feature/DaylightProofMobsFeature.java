package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityCombustEvent;

import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v8.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class DaylightProofMobsFeature extends BaseListener<BloodMoon> {
	
	public DaylightProofMobsFeature(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityCombust(EntityCombustEvent event){
		String worldName = event.getEntity().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		EntityType type = event.getEntityType();
		
		if ((type == EntityType.ZOMBIE || type == EntityType.SKELETON) && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_DAYLIGHT_PROOF_MOBS_ENABLED)){
			event.setCancelled(true);
		}
	}
	
}
