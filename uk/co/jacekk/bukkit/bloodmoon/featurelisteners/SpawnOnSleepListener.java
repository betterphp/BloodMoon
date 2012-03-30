package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class SpawnOnSleepListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedEnter(PlayerBedEnterEvent event){
		if (event.isCancelled()) return;
		
		Player player = event.getPlayer();
		Location location = player.getLocation();
		World world = location.getWorld();
		
		if (BloodMoon.bloodMoonWorlds.contains(world.getName())){
			String mobName = BloodMoon.config.getRandomStringFromList("features.spawn-on-sleep.spawn");
			EntityType creatureType = EntityType.fromName(Character.toUpperCase(mobName.charAt(0)) + mobName.toLowerCase().substring(1));
			
			if (creatureType != null){
				world.spawnCreature(location, creatureType);
			}
		}
	}
	
}
