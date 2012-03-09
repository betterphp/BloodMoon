package uk.co.jacekk.bukkit.bloodmoon.listeners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class PlayerListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		World toWorld = player.getWorld();
		
		if (BloodMoon.bloodMoonWorlds.contains(toWorld.getName())){
			player.sendMessage(ChatColor.RED + "The blood moon is rising !");
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		
		if (BloodMoon.bloodMoonWorlds.contains(player.getWorld().getName())){
			player.sendMessage(ChatColor.RED + "The blood moon is rising !");
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if (event.isCancelled()) return;
		
		if (event.getMessage().equalsIgnoreCase("/reload")){
			event.getPlayer().sendMessage(ChatColor.RED + "Due to the way BloodMoon works, /reload can cause a crash.");
			event.getPlayer().sendMessage(ChatColor.RED + "A fix is being worked on, but for now you will have to restart.");
			event.getPlayer().sendMessage(ChatColor.RED + "To ignore this and crash the server use /reload force.");
			
			event.setCancelled(true);
		}
	}
	
}