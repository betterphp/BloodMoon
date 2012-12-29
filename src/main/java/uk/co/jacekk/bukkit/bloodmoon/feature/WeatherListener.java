package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import uk.co.jacekk.bukkit.baseplugin.v7.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v7.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class WeatherListener extends BaseListener<BloodMoon> {
	
	public WeatherListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_WEATHER_ENABLED)){
			world.setStorm(worldConfig.getBoolean(Config.FEATURE_WEATHER_RAIN));
			world.setThundering(worldConfig.getBoolean(Config.FEATURE_WEATHER_THUNDER));
			
			world.setWeatherDuration(10000);
			world.setThunderDuration(10000);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_WEATHER_ENABLED)){
			world.setWeatherDuration(10);
			world.setThunderDuration(10);
		}
	}
	
}
