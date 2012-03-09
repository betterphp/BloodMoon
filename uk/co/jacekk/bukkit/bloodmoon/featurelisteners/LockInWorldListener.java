package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class LockInWorldListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if (event.isCancelled()) return;
		
		Player player = event.getPlayer();
		World toWorld = event.getTo().getWorld();
		World fromWorld = event.getFrom().getWorld();
		
		if (player.hasPermission("bloodmoon.feature.ignore-world-lock") == false && fromWorld != toWorld && BloodMoon.bloodMoonWorlds.contains(fromWorld.getName())){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot leave the world until the bloodmoon has ended.");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPortal(PlayerPortalEvent event){
		this.onPlayerTeleport(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		World toWorld = event.getRespawnLocation().getWorld();
		World fromWorld = player.getWorld();
		
		if (player.hasPermission("bloodmoon.feature.ignore-world-lock") == false && fromWorld != toWorld && BloodMoon.bloodMoonWorlds.contains(fromWorld.getName())){
			event.setRespawnLocation(fromWorld.getSpawnLocation());
			player.sendMessage(ChatColor.RED + "You cannot leave the world until the bloodmoon has ended.");
		}
	}
	
}
