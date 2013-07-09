package uk.co.jacekk.bukkit.bloodmoon.feature.spawning;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.scheduler.BaseTask;
import uk.co.jacekk.bukkit.baseplugin.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class MoreMobsTask extends BaseTask<BloodMoon> {
	
	private CraftWorld world;
	private PluginConfig worldConfig;
	private ArrayList<EntityType> types;
	
	private Random random;
	
	public MoreMobsTask(BloodMoon plugin, World world){
		super(plugin);
		
		this.world = (CraftWorld) world;
		this.worldConfig = plugin.getConfig(world.getName());
		this.types = new ArrayList<EntityType>();
		
		for (String name : this.worldConfig.getStringList(Config.FEATURE_MORE_MOBS_SPAWN)){
			EntityType type = EntityType.valueOf(name);
			
			if (type != null && type.isAlive()){
				this.types.add(type);
			}
		}
		
		this.random = new Random();
	}
	
	@Override
	public void run(){
		long worldTime = this.world.getTime();
		
		if (worldTime < 13000 || worldTime > 23000){
			return;
		}
		
		spawn: for (Chunk chunk : this.world.getLoadedChunks()){
			EntityType type = ListUtils.getRandom(this.types);
			
			if (this.random.nextInt(100) < this.worldConfig.getInt(Config.FEATURE_MORE_MOBS_CHANCE)){
				int x = (chunk.getX() * 16) + this.random.nextInt(12) + 2;
				int z = (chunk.getZ() * 16) + this.random.nextInt(12) + 2;
				int y = this.world.getHighestBlockYAt(x, z);
				
				if (type == EntityType.GHAST){
					y += 20;
				}else if (type == EntityType.BAT){
					y += 4;
				}
				
				Location spawnLocation = new Location(world, x, y, z);
				
				for (Entity entity : this.world.getLivingEntities()){
					if (!entity.isDead() && entity.getLocation().distanceSquared(spawnLocation) < 1024){
						continue spawn;
					}
				}
				
				int group = this.worldConfig.getInt(Config.FEATURE_MORE_MOBS_GROUP_SIZE) + this.random.nextInt(this.worldConfig.getInt(Config.FEATURE_MORE_MOBS_GROUP_VARIATION));
				
				for (int i = 0; i < group; ++i){
					spawnLocation.add((this.random.nextDouble() * 3) - 1.5, 0, (this.random.nextDouble() * 3) - 1.5);
					this.world.spawn(spawnLocation, type.getEntityClass(), SpawnReason.NATURAL);
				}
			}
		}
	}
	
}
