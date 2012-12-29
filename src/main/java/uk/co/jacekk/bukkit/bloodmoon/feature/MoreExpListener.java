package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;

import uk.co.jacekk.bukkit.baseplugin.v7.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v7.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class MoreExpListener extends BaseListener<BloodMoon> {
	
	public MoreExpListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		String worldName = entity.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (entity instanceof Creature && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_MORE_EXP_ENABLED)){
			if (!worldConfig.getBoolean(Config.FEATURE_MORE_EXP_IGNORE_SPAWNERS) || plugin.getSpawnReason(entity) != SpawnReason.SPAWNER){
				event.setDroppedExp(event.getDroppedExp() * Math.max(worldConfig.getInt(Config.FEATURE_MORE_EXP_MULTIPLIER), 1));
			}
		}
	}
	
}
