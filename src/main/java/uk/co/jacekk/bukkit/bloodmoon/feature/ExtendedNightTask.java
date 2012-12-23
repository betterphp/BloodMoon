package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.World;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.scheduler.BaseTask;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class ExtendedNightTask extends BaseTask<BloodMoon> {
	
	protected int taskID;
	
	private World world;
	
	private int maxResets;
	private int minKills;
	private boolean preserveWeather;
	
	protected int kills;
	private int resets;
	
	public ExtendedNightTask(BloodMoon plugin, World world, PluginConfig worldConfig){
		super(plugin);
		
		this.taskID = -1;
		
		this.world = world;
		this.maxResets = worldConfig.getInt(Config.FEATURE_EXTENDED_NIGHT_MAX_RESETS);
		this.minKills = worldConfig.getInt(Config.FEATURE_EXTENDED_NIGHT_MIN_KILLS);
		this.preserveWeather = worldConfig.getBoolean(Config.FEATURE_WEATHER_ENABLED);
		
		this.kills = 0;
		this.resets = 0;
	}
	
	@Override
	public void run(){
		long worldTime = this.world.getTime();
		
		if ((this.minKills > 0 && this.kills > this.minKills) || (this.maxResets > 0 && this.resets > this.maxResets)){
			plugin.scheduler.cancelTask(this.taskID);
			return;
		}
		
		if (worldTime >= 21000 && worldTime <= 21100){
			this.world.setTime(15000);
			++this.resets;
			
			if (this.preserveWeather){
				this.world.setWeatherDuration(8000);
				this.world.setThunderDuration(8000);
			}
		}
	}
	
}
