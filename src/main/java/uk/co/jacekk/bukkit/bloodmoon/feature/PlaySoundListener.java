package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.Sound;
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
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class PlaySoundListener extends BaseListener<BloodMoon> {
	
	public PlaySoundListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBloodMoonStart(BloodMoonStartEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_CHAT_MESSAGE_ENABLED)){
			for (Player player : event.getWorld().getPlayers()){
				player.playSound(player.getLocation(), Sound.valueOf(worldConfig.getString(Config.FEATURE_PLAY_SOUND_SOUND)), (float) worldConfig.getDouble(Config.FEATURE_PLAY_SOUND_VOLUME), (float) worldConfig.getDouble(Config.FEATURE_PLAY_SOUND_PITCH));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		String worldName = player.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_CHAT_MESSAGE_ENABLED)){
			player.playSound(player.getLocation(), Sound.valueOf(worldConfig.getString(Config.FEATURE_PLAY_SOUND_SOUND)), (float) worldConfig.getDouble(Config.FEATURE_PLAY_SOUND_VOLUME), (float) worldConfig.getDouble(Config.FEATURE_PLAY_SOUND_PITCH));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String worldName = player.getWorld().getName();
		final PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_CHAT_MESSAGE_ENABLED)){
			final String playerName = player.getName();
			
			plugin.scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
				
				public void run(){
					Player player = plugin.server.getPlayer(playerName);
					
					if (player != null){
						player.playSound(player.getLocation(), Sound.valueOf(worldConfig.getString(Config.FEATURE_PLAY_SOUND_SOUND)), (float) worldConfig.getDouble(Config.FEATURE_PLAY_SOUND_VOLUME), (float) worldConfig.getDouble(Config.FEATURE_PLAY_SOUND_PITCH));
					}
				}
				
			}, 40L);
		}
	}
	
}
