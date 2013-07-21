package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;

public class WorldInitListener extends BaseListener<BloodMoon> {
	
	public WorldInitListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		World world = event.getWorld();
		PluginConfig config = plugin.createConfig(world);
	}
	
}
