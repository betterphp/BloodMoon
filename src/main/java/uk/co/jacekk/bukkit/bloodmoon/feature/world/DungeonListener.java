package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class DungeonListener extends BaseListener<BloodMoon> {
	
	public DungeonListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		World world = event.getWorld();
		String worldName = world.getName();
		
		if (plugin.isEnabled(worldName) && plugin.isFeatureEnabled(worldName, Feature.DUNGEONS)){
			world.getPopulators().add(new DungeonGenerator(plugin));
		}
	}
	
	private boolean isProtected(Block block){
		Chunk chunk = block.getChunk();
		World world = block.getWorld();
		
		PluginConfig worldConfig = plugin.getConfig(world.getName());
		
		int gridX = (int) (Math.floor(chunk.getX() / 10.0d) * 10);
		int gridZ = (int) (Math.floor(chunk.getZ() / 10.0d) * 10);
		
		DungeonProperties properties = new DungeonProperties(block.getWorld(), worldConfig, gridX, gridZ);
		
		return (properties.isInChunk(chunk));
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		World world = block.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isEnabled(worldName) && worldConfig.getBoolean(Config.FEATURE_DUNGEONS_PROTECTED) && block.getType() != Material.IRON_FENCE && this.isProtected(block)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Block block = event.getBlock();
		World world = block.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isEnabled(worldName) && worldConfig.getBoolean(Config.FEATURE_DUNGEONS_PROTECTED) && this.isProtected(block)){
			event.setCancelled(true);
		}
	}
	
}
