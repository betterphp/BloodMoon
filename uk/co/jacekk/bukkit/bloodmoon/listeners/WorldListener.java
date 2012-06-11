package uk.co.jacekk.bukkit.bloodmoon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class WorldListener extends BaseListener<BloodMoon> {
	
	public WorldListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldInit(WorldInitEvent event){
		String worldName = event.getWorld().getName();
		
		if (plugin.config.getStringList(Config.AFFECTED_WORLDS).contains(worldName)){
			plugin.bloodMoonActiveWorlds.add(worldName);
		}
	}
	
}
