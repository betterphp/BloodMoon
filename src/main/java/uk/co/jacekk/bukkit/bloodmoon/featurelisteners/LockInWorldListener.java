package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Permission;

public class LockInWorldListener extends BaseListener<BloodMoon> {
	
	public LockInWorldListener(BloodMoon plugin){
		super(plugin);
	}
	
	private boolean canTeleport(Player player, World from, World to){
		String worldName = from.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		return (Permission.ADMIN_IGNORE_WORLD_LOCK.has(player) || from.equals(to) || !plugin.isActive(worldName) || !worldConfig.getBoolean(Config.FEATURE_LOCK_IN_WORLD_ENABLED));
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();
		
		if (!this.canTeleport(player, event.getFrom().getWorld(), event.getTo().getWorld())){
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
		World fromWorld = player.getWorld();
		World toWorld = event.getRespawnLocation().getWorld();
		
		if (!this.canTeleport(player, fromWorld, toWorld)){
			event.setRespawnLocation(fromWorld.getSpawnLocation());
			player.sendMessage(ChatColor.RED + "You cannot leave the world until the bloodmoon has ended.");
		}
	}
	
}
