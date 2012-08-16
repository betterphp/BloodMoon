package uk.co.jacekk.bukkit.bloodmoon;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.BaseTask;

public class TimeMonitorTask extends BaseTask<BloodMoon> {
	
	private Random random;
	
	public TimeMonitorTask(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
	}
	
	public void run(){
		for (String worldName : plugin.config.getStringList(Config.AFFECTED_WORLDS)){
			World world = plugin.server.getWorld(worldName);
			
			if (world != null){
				long worldTime = world.getTime();
				
				if (worldTime >= 13000 && worldTime <= 13100 && this.random.nextInt(100) < plugin.config.getInt(Config.CHANCE)){
					if (!plugin.isActive(worldName)){
						for (Player player : world.getPlayers()){
							player.sendMessage(ChatColor.RED + "The blood moon is rising !");
						}
						
						plugin.activate(worldName);
					}
				}else if (worldTime >= 23000 && worldTime <= 23100 && plugin.isActive(worldName)){
					plugin.deactivate(worldName);
				}
			}
		}
	}
	
}
