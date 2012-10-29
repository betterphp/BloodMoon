package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import net.minecraft.server.Packet250CustomPayload;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.co.jacekk.bukkit.baseplugin.v4.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;

public class TexturePackListener extends BaseListener<BloodMoon> {
	
	public TexturePackListener(BloodMoon plugin){
		super(plugin);
	}
	
	private void setTexturePack(Player player, String path){
		((CraftPlayer) player).getHandle().netServerHandler.sendPacket(new Packet250CustomPayload("MC|TPack", (path + "\0" + 16).getBytes()));
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBloodMoonStart(BloodMoonStartEvent event){
		for (Player player : event.getWorld().getPlayers()){
			this.setTexturePack(player, plugin.config.getString(Config.FEATURE_TEXTURE_PACK_BLOODMOON));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBloodMoonEnd(BloodMoonEndEvent event){
		for (Player player : event.getWorld().getPlayers()){
			this.setTexturePack(player, plugin.config.getString(Config.FEATURE_TEXTURE_PACK_NORMAL));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		String from = event.getFrom().getName();
		String to = player.getWorld().getName();
		
		if (!plugin.isActive(from) && plugin.isActive(to)){
			this.setTexturePack(player, plugin.config.getString(Config.FEATURE_TEXTURE_PACK_BLOODMOON));
		}else if (plugin.isActive(from) && !plugin.isActive(to)){
			this.setTexturePack(player, plugin.config.getString(Config.FEATURE_TEXTURE_PACK_NORMAL));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String to = player.getWorld().getName();
		
		if (plugin.isActive(to)){
			this.setTexturePack(player, plugin.config.getString(Config.FEATURE_TEXTURE_PACK_BLOODMOON));
		}
	}
	
}
