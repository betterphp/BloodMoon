package uk.co.jacekk.bukkit.bloodmoon.feature.spawning;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class MoreSpawningListener extends BaseListener<BloodMoon> {
	
	private Random random;
	
	public MoreSpawningListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() != SpawnReason.NATURAL) return;
		
		EntityType type = event.getEntityType();
		Location location = event.getLocation();
		World world = location.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.MORE_SPAWNING) && worldConfig.getStringList(Config.FEATURE_MORE_SPAWNING_MOBS).contains(type.getName().toUpperCase())){
			for (int i = 0; i < Math.max(worldConfig.getInt(Config.FEATURE_MORE_SPAWNING_MULTIPLIER), 1); ++i){
				world.spawnEntity(location.add((this.random.nextDouble() * 3) - 1.5, 0, (this.random.nextDouble() * 3) - 1.5), type);
			}
		}
	}
	
}
