package uk.co.jacekk.bukkit.bloodmoon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class WorldListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldInit(WorldInitEvent event){
		String worldName = event.getWorld().getName();
		
		if (BloodMoon.config.getStringList("affected-worlds").contains(worldName)){
			BloodMoon.bloodMoonWorlds.add(worldName);
		}
	}
	
}
