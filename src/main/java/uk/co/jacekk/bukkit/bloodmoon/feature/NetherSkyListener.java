package uk.co.jacekk.bukkit.bloodmoon.feature;

import net.minecraft.server.v1_5_R1.EnumGamemode;
import net.minecraft.server.v1_5_R1.Packet9Respawn;
import net.minecraft.server.v1_5_R1.WorldType;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_5_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class NetherSkyListener extends BaseListener<BloodMoon> {
	
	public NetherSkyListener(BloodMoon plugin){
		super(plugin);
	}
	
	private void sendWorldEnvironment(Player player, Environment environment){
		CraftPlayer craftPlayer = (CraftPlayer) player;
		CraftWorld world = (CraftWorld) player.getWorld();
		Location location = player.getLocation();
		
		Packet9Respawn packet = new Packet9Respawn(environment.getId(), (byte) 1, WorldType.NORMAL, world.getMaxHeight(), EnumGamemode.a(player.getGameMode().getValue()));
		
		craftPlayer.getHandle().playerConnection.sendPacket(packet);
		
		int viewDistance = plugin.server.getViewDistance();
		
		int xMin = location.getChunk().getX() - viewDistance;
		int xMax = location.getChunk().getX() + viewDistance;
		int zMin = location.getChunk().getZ() - viewDistance;
		int zMax = location.getChunk().getZ() + viewDistance;
		
		for (int x = xMin; x < xMax; ++x){
			for (int z = zMin; z < zMax; ++z){
				world.refreshChunk(x, z);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		String worldName = event.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_NETHER_SKY_ENABLED)){
			for (Player player : event.getWorld().getPlayers()){
				this.sendWorldEnvironment(player, Environment.NETHER);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		World world = event.getWorld();
		Environment environment = world.getEnvironment();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_NETHER_SKY_ENABLED)){
			for (Player player : event.getWorld().getPlayers()){
				this.sendWorldEnvironment(player, environment);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent event){
		String worldName = event.getPlayer().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_NETHER_SKY_ENABLED)){
			this.sendWorldEnvironment(event.getPlayer(), Environment.NETHER);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onRespawn(PlayerRespawnEvent event){
		String worldName = event.getPlayer().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_NETHER_SKY_ENABLED)){
			this.sendWorldEnvironment(event.getPlayer(), Environment.NETHER);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChangeWorlds(PlayerChangedWorldEvent event){
		String worldName = event.getPlayer().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_NETHER_SKY_ENABLED)){
			this.sendWorldEnvironment(event.getPlayer(), Environment.NETHER);
		}
	}
	
}
