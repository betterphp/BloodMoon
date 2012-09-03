package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.v1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class MoreSpawningListener extends BaseListener<BloodMoon> {
	
	private int multiplier;
	
	public MoreSpawningListener(BloodMoon plugin){
		super(plugin);
		
		this.multiplier = Math.max(plugin.config.getInt(Config.FEATURE_MORE_SPAWNING_MULTIPLIER), 1);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() != SpawnReason.NATURAL) return;
		
		EntityType type = event.getEntityType();
		Location location = event.getLocation();
		World world = location.getWorld();
		
		if (plugin.config.getStringList(Config.FEATURE_MORE_SPAWNING_MOBS).contains(type.getName().toUpperCase()) && plugin.isActive(world.getName())){
			for (int i = 0; i < this.multiplier; ++i){
				world.spawnEntity(location, type);
			}
		}
	}
	
}
