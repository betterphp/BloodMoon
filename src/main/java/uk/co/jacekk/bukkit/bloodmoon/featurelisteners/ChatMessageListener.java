package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;

public class ChatMessageListener extends BaseListener<BloodMoon> {
	
	private String message;
	
	public ChatMessageListener(BloodMoon plugin){
		super(plugin);
		
		this.message = ChatColor.translateAlternateColorCodes('&', plugin.config.getString(Config.FEATURE_CHAT_MESSAGE_MESSAGE));
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBloodMoonStart(BloodMoonStartEvent event){
		for (Player player : event.getWorld().getPlayers()){
			player.sendMessage(this.message);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		String to = player.getWorld().getName();
		
		if (plugin.isActive(to)){
			player.sendMessage(this.message);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String to = player.getWorld().getName();
		
		if (plugin.isActive(to)){
			final String playerName = player.getName();
			
			plugin.scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
				
				public void run(){
					Player player = plugin.server.getPlayer(playerName);
					
					if (player != null){
						player.sendMessage(message);
					}
				}
				
			}, 40L);
		}
	}
	
}
