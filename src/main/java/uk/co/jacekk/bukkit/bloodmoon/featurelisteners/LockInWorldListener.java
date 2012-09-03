package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import uk.co.jacekk.bukkit.baseplugin.v1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Permission;

public class LockInWorldListener extends BaseListener<BloodMoon> {
	
	public LockInWorldListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();
		World toWorld = event.getTo().getWorld();
		World fromWorld = event.getFrom().getWorld();
		
		if (!Permission.ADMIN_IGNORE_WORLD_LOCK.hasPermission(player) && fromWorld != toWorld && plugin.isActive(fromWorld.getName())){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot leave the world until the bloodmoon has ended.");
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPortal(PlayerPortalEvent event){
		this.onPlayerTeleport(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		World toWorld = event.getRespawnLocation().getWorld();
		World fromWorld = player.getWorld();
		
		if (!Permission.ADMIN_IGNORE_WORLD_LOCK.hasPermission(player) && fromWorld != toWorld && plugin.isActive(fromWorld.getName())){
			event.setRespawnLocation(fromWorld.getSpawnLocation());
			player.sendMessage(ChatColor.RED + "You cannot leave the world until the bloodmoon has ended.");
		}
	}
	
}
