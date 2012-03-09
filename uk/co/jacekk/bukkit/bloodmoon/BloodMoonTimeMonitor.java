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
		for (String worldName : BloodMoon.config.getStringList("affected-worlds")){
			World world = plugin.server.getWorld(worldName);
			
			if (world != null){
				int worldTime = Math.round(world.getTime() / 100);
				
				if (worldTime == 123 && Math.random() < (((double) BloodMoon.config.getInt("bloodmoon-chance")) / 100) && BloodMoon.bloodMoonWorlds.contains(worldName) == false){
					for (Player player : world.getPlayers()){
						player.sendMessage(ChatColor.RED + "The blood moon is rising !");
					}
					
					BloodMoon.bloodMoonWorlds.add(worldName);
				}else if (worldTime < 1 && BloodMoon.bloodMoonWorlds.contains(worldName)){
					BloodMoon.bloodMoonWorlds.remove(worldName);
				}
			}
		}
	}
	
}
