package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.BaseTask;

public class TimeMonitorTask extends BaseTask<BloodMoon> {
	
	public TimeMonitorTask(BloodMoon plugin){
		super(plugin);
	}
	
	public void run(){
		for (String worldName : plugin.config.getStringList(Config.AFFECTED_WORLDS)){
			World world = plugin.server.getWorld(worldName);
			
			if (world != null){
				int worldTime = Math.round(world.getTime() / 100);
				
				if (worldTime == 123 && Math.random() < (((double) plugin.config.getInt(Config.CHANCE)) / 100)){
					if (!plugin.isActive(worldName)){
						for (Player player : world.getPlayers()){
							player.sendMessage(ChatColor.RED + "The blood moon is rising !");
						}
						
						plugin.activate(worldName);
					}
				}else if (worldTime < 1 && plugin.isActive(worldName)){
					plugin.deactivate(worldName);
				}
			}
		}
	}
	
}
