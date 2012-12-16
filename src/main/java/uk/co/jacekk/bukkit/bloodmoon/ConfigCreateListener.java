package uk.co.jacekk.bukkit.bloodmoon;

import java.io.File;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;

public class ConfigCreateListener extends BaseListener<BloodMoon> {
	
	public ConfigCreateListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		String worldName = event.getWorld().getName();
		
		if (!plugin.worldConfig.containsKey(worldName)){
			PluginConfig worldConfig = new PluginConfig(new File(plugin.getBaseDirPath() + File.separator + worldName + ".yml"), Config.class, plugin.log);
			
			plugin.worldConfig.put(worldName, worldConfig);
			
			if (worldConfig.getBoolean(Config.ALWAYS_ON)){
				plugin.activate(worldName);
			}
		}
	}
	
}
