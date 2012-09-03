package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.v1.event.BaseListener;

public class ActivateWorldListener extends BaseListener<BloodMoon> {
	
	public ActivateWorldListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldInit(WorldInitEvent event){
		String worldName = event.getWorld().getName();
		
		if (plugin.config.getStringList(Config.AFFECTED_WORLDS).contains(worldName)){
			plugin.activate(worldName);
		}
	}
	
}
