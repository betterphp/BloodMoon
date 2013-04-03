package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class DungeonListener extends BaseListener<BloodMoon> {
	
	public DungeonListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		
		if (plugin.isEnabled(worldName) && plugin.isFeatureEnabled(worldName, Feature.DUNGEONS)){
			world.getPopulators().add(new DungeonGenerator(plugin, new Random(world.getSeed())));
		}
	}
	
}
