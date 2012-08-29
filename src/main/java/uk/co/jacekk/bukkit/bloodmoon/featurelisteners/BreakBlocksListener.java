package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.CreeperMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.EndermanMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.SkeletonMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.SpiderMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.ZombieMoveEvent;

public class BreakBlocksListener extends BaseListener<BloodMoon> {
	
	private Random rand;
	
	public BreakBlocksListener(BloodMoon plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	private void mobAttemptBreakBlock(LivingEntity entity, Block block){
		World world = block.getWorld();
		Material type = block.getType();
		
		if (plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_BLOCKS).contains(type.name().toUpperCase())){
			Location location = block.getLocation();
			
			if (this.rand.nextInt(100) < 80){
				world.playEffect(location, Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
			}else{
				EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity, block, Material.AIR);
				plugin.pluginManager.callEvent(event);
				
				if (!event.isCancelled()){
					world.playEffect(location, Effect.ZOMBIE_DESTROY_DOOR, 0);
					
					if (plugin.config.getBoolean(Config.FEATURE_BREAK_BLOCKS_REALISTIC_DROP)){
						block.breakNaturally();
					}else{
						if (type != Material.AIR){
							block.setType(Material.AIR);
							
							world.dropItemNaturally(location, new ItemStack(type, 1, block.getData()));
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreeperMoveEvent(CreeperMoveEvent event){
		Creeper creeper = event.getCreeper();
		World world = creeper.getWorld();
		
		if (plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains("CREEPER") && world.getTime() % 20 == 0){
			LivingEntity target = event.getTarget();
			
			if (target instanceof Player && plugin.isActive(world.getName())){
				Block[] blocks = new Block[2];
				
				blocks[0] = creeper.getTargetBlock(null, 1);
				blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
				
				for (Block block : blocks){
					this.mobAttemptBreakBlock(creeper, block);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSkeletonMoveEvent(SkeletonMoveEvent event){
		Skeleton skeleton = event.getSkeleton();
		World world = skeleton.getWorld();
		
		if (plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains("SKELETON") && world.getTime() % 20 == 0){
			LivingEntity target = event.getTarget();
			
			if (target instanceof Player && plugin.isActive(world.getName())){
				Block[] blocks = new Block[2];
				
				blocks[0] = skeleton.getTargetBlock(null, 1);
				
				if (blocks[0] != null){
					blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
					
					for (Block block : blocks){
						this.mobAttemptBreakBlock(skeleton, block);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSpiderMoveEvent(SpiderMoveEvent event){
		Spider spider = event.getSpider();
		World world = spider.getWorld();
		
		if (plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains("SPIDER") && world.getTime() % 20 == 0){
			LivingEntity target = event.getTarget();
			
			if (target instanceof Player && plugin.isActive(world.getName())){
				Block block = spider.getTargetBlock(null, 1);
				
				if (block != null){
					this.mobAttemptBreakBlock(spider, block);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onZombieMoveEvent(ZombieMoveEvent event){
		Zombie zombie = event.getZombie();
		World world = zombie.getWorld();
		
		if (plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains("ZOMBIE") && world.getTime() % 20 == 0){
			LivingEntity target = event.getTarget();
			
			if (target instanceof Player && plugin.isActive(world.getName())){
				Block[] blocks = new Block[2];
				
				blocks[0] = zombie.getTargetBlock(null, 1);
				
				if (blocks[0] != null){
					blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
					
					for (Block block : blocks){
						this.mobAttemptBreakBlock(zombie, block);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEndermanMoveEvent(EndermanMoveEvent event){
		Enderman enderman = event.getEnderman();
		World world = enderman.getWorld();
		
		if (plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains("ENDERMAN") && world.getTime() % 20 == 0){
			LivingEntity target = event.getTarget();
			
			if (target instanceof Player && plugin.isActive(world.getName())){
				Block[] blocks = new Block[3];
				
				blocks[0] = enderman.getTargetBlock(null, 1);
				
				if (blocks[0] != null){
					blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
					blocks[2] = blocks[1].getRelative(BlockFace.DOWN);
					
					for (Block block : blocks){
						this.mobAttemptBreakBlock(enderman, block);
					}
				}
			}
		}
	}
	
}
