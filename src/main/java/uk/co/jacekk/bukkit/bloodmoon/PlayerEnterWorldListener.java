package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;

public class PlayerEnterWorldListener extends BaseListener<BloodMoon> {
	
	public PlayerEnterWorldListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		World toWorld = player.getWorld();
		
		if (plugin.isActive(toWorld)){
			final String playerName = player.getName();
			
			plugin.scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
				
				public void run(){
					Player player = plugin.server.getPlayer(playerName);
					
					if (player != null){
						player.sendMessage(ChatColor.RED + "The blood moon is rising !");
					}
				}
				
			}, 40);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		
		if (plugin.isActive(player.getWorld())){
			player.sendMessage(ChatColor.RED + "The blood moon is rising !");
		}
	}
	
}