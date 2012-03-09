package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class MoreSpawningListener implements Listener {
	
	private int multiplier;
	
	public MoreSpawningListener(){
		this.multiplier = BloodMoon.config.getInt("features.more-spawning.multiplier");
		
		if (this.multiplier == 0 || this.multiplier > 100){
			this.multiplier = 1;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (event.isCancelled() || event.getSpawnReason() != SpawnReason.NATURAL) return;
		
		CreatureType type = event.getCreatureType();
		Location location = event.getLocation();
		World world = location.getWorld();
		
		List<CreatureType> types = Arrays.asList(CreatureType.CREEPER, CreatureType.ENDERMAN, CreatureType.SKELETON, CreatureType.ZOMBIE, CreatureType.SPIDER);
		
		if (BloodMoon.bloodMoonWorlds.contains(world.getName()) && types.contains(type)){
			for (int i = 0; i < this.multiplier; ++i){
				world.spawnCreature(location, type);
			}
		}
	}
	
}
