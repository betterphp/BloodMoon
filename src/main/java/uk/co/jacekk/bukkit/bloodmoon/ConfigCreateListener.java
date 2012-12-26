package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;

public class ConfigCreateListener extends BaseListener<BloodMoon> {
	
	public ConfigCreateListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		plugin.createConfig(event.getWorld());
	}
	
}
