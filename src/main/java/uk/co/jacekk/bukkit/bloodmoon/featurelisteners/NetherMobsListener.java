package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import uk.co.jacekk.bukkit.baseplugin.v5.event.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.v5.scheduler.BaseTask;
import uk.co.jacekk.bukkit.baseplugin.v5.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;

public class NetherMobsListener extends BaseListener<BloodMoon> {
	
	private Random random;
	private ArrayList<EntityType> netherEntities;
	
	private BaseTask<BloodMoon> task;
	private int taskID;
	
	public NetherMobsListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
		this.netherEntities = new ArrayList<EntityType>();
		
		this.netherEntities.add(EntityType.GHAST);
		this.netherEntities.add(EntityType.PIG_ZOMBIE);
		this.netherEntities.add(EntityType.BLAZE);
		this.netherEntities.add(EntityType.MAGMA_CUBE);
		
		this.task = new BaseTask<BloodMoon>(plugin){
			
			@Override
			public void run(){
				for (World world : plugin.server.getWorlds()){
					if (plugin.config.getStringList(Config.AFFECTED_WORLDS).contains(world.getName())){
						spawn: for (Chunk chunk : world.getLoadedChunks()){
							EntityType type = EntityType.valueOf(ListUtils.getRandom(plugin.config.getStringList(Config.FEATURE_NETHER_MOBS_SPAWN)));
							
							if (type != null && netherEntities.contains(type) && random.nextInt(100) < plugin.config.getInt(Config.FEATURE_NETHER_MOBS_CHANCE)){
								int x = (chunk.getX() * 16) + random.nextInt(12) + 2;
								int z = (chunk.getZ() * 16) + random.nextInt(12) + 2;
								int y = world.getHighestBlockYAt(x, z);
								
								if (type == EntityType.GHAST){
									y += 20;
								}
								
								Location spawnLocation = new Location(world, x, y, z);
								
								for (Entity entity : world.getLivingEntities()){
									if (entity.getLocation().distanceSquared(spawnLocation) < 1024){
										continue spawn;
									}
								}
								
								int group = plugin.config.getInt(Config.FEATURE_NETHER_MOBS_GROUP_SIZE) + random.nextInt(plugin.config.getInt(Config.FEATURE_NETHER_MOBS_GROUP_VARIATION));
								
								for (int i = 0; i < group; ++i){
									world.spawnEntity(spawnLocation.add((random.nextDouble() * 3) - 1.5, 0, (random.nextDouble() * 3) - 1.5), type);
								}
							}
						}
					}
				}
			}
			
		};
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		this.taskID = plugin.scheduler.scheduleSyncRepeatingTask(plugin, this.task, 0L, 100L);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		plugin.scheduler.cancelTask(this.taskID);
		
		for (LivingEntity entity : event.getWorld().getLivingEntities()){
			if (this.netherEntities.contains(entity.getType())){
				entity.remove();
			}
		}
	}
	
}
