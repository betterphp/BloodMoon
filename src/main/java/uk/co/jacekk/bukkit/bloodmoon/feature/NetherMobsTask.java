package uk.co.jacekk.bukkit.bloodmoon.feature;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.scheduler.BaseTask;
import uk.co.jacekk.bukkit.baseplugin.v6.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class NetherMobsTask extends BaseTask<BloodMoon> {
	
	private World world;
	private ArrayList<EntityType> netherEntities;
	
	private Random random;
	
	public NetherMobsTask(BloodMoon plugin, World world, ArrayList<EntityType> netherEntities){
		super(plugin);
		
		this.world = world;
		this.netherEntities = netherEntities;
		
		this.random = new Random();
	}
	
	@Override
	public void run(){
		PluginConfig worldConfig = plugin.getConfig(this.world.getName());
		
		spawn: for (Chunk chunk : this.world.getLoadedChunks()){
			EntityType type = EntityType.valueOf(ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_NETHER_MOBS_SPAWN)));
			
			if (type != null && this.netherEntities.contains(type) && this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_NETHER_MOBS_CHANCE)){
				int x = (chunk.getX() * 16) + this.random.nextInt(12) + 2;
				int z = (chunk.getZ() * 16) + this.random.nextInt(12) + 2;
				int y = this.world.getHighestBlockYAt(x, z);
				
				if (type == EntityType.GHAST){
					y += 20;
				}
				
				Location spawnLocation = new Location(world, x, y, z);
				
				for (Entity entity : this.world.getLivingEntities()){
					if (entity.getLocation().distanceSquared(spawnLocation) < 1024){
						continue spawn;
					}
				}
				
				int group = worldConfig.getInt(Config.FEATURE_NETHER_MOBS_GROUP_SIZE) + this.random.nextInt(worldConfig.getInt(Config.FEATURE_NETHER_MOBS_GROUP_VARIATION));
				
				for (int i = 0; i < group; ++i){
					this.world.spawnEntity(spawnLocation.add((this.random.nextDouble() * 3) - 1.5, 0, (this.random.nextDouble() * 3) - 1.5), type);
				}
			}
		}
	}
	
}
