package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BloodMoonTimeMonitor implements Runnable {
	
	private BloodMoon plugin;
	
	public BloodMoonTimeMonitor(BloodMoon instance){
		this.plugin = instance;
	}
	
	public void run(){
		for (String worldName : plugin.config.getStringList(Config.AFFECTED_WORLDS)){
			World world = plugin.server.getWorld(worldName);
			
			if (world != null){
				int worldTime = Math.round(world.getTime() / 100);
				
				if (worldTime == 123 && Math.random() < (((double) plugin.config.getInt(Config.CHANCE)) / 100)){
					if (!plugin.bloodMoonActiveWorlds.contains(worldName)){
						for (Player player : world.getPlayers()){
							player.sendMessage(ChatColor.RED + "The blood moon is rising !");
						}
						
						plugin.bloodMoonActiveWorlds.add(worldName);
					}
				}else if (worldTime < 1 && plugin.bloodMoonActiveWorlds.contains(worldName)){
					plugin.bloodMoonActiveWorlds.remove(worldName);
				}
			}
		}
	}
	
}
