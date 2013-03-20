package uk.co.jacekk.bukkit.bloodmoon.feature.client;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class ChatMessageListener extends BaseListener<BloodMoon> {
	
	public ChatMessageListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBloodMoonStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isFeatureEnabled(worldName, Feature.CHAT_MESSAGE)){
			for (Player player : world.getPlayers()){
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', worldConfig.getString(Config.FEATURE_CHAT_MESSAGE_MESSAGE)));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		String worldName = player.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isFeatureEnabled(worldName, Feature.CHAT_MESSAGE)){
			if (plugin.isActive(worldName)){
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', worldConfig.getString(Config.FEATURE_CHAT_MESSAGE_MESSAGE)));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String worldName = player.getWorld().getName();
		final PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.CHAT_MESSAGE)){
			final String playerName = player.getName();
			
			plugin.scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
				
				public void run(){
					Player player = plugin.server.getPlayer(playerName);
					
					if (player != null){
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', worldConfig.getString(Config.FEATURE_CHAT_MESSAGE_MESSAGE)));
					}
				}
				
			}, 40L);
		}
	}
	
}
