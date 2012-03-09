package uk.co.jacekk.bukkit.bloodmoon;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;

public class BloodMoonLogger {
	
	private BloodMoon plugin;
	private Logger logger;
	
	public BloodMoonLogger(BloodMoon instance){
		this.plugin = instance;
		this.logger = Logger.getLogger("Minecraft");
		
		final Filter currentFilter = this.logger.getFilter();
		
		this.logger.setFilter(new Filter(){
			
			@Override
			public boolean isLoggable(LogRecord record){
				if (currentFilter != null && currentFilter.isLoggable(record) == false){
					return false;
				}
				
				if (record.getMessage().contains("Fetching addPacket for removed entity")){
					return false;
				}
				
				return true;
			}
			
		});
		
	}
	
	private String buildString(String msg){
		PluginDescriptionFile pdFile = plugin.getDescription();
		
		return pdFile.getName() + " " + pdFile.getVersion() + ": " + msg;
	}
	
	public void info(String msg){
		this.logger.info(this.buildString(msg));
	}
	
	public void warn(String msg){
		this.logger.warning(this.buildString(msg));
	}
	
	public void fatal(String msg){
		this.logger.severe(this.buildString(msg));
	}
	
}
