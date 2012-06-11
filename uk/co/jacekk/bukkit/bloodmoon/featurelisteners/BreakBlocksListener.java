package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.List;
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
	private List<String> mobList;
	
	public BreakBlocksListener(BloodMoon plugin){
		super(plugin);
		
		this.rand = new Random();
		this.mobList = plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS);
	}
	
	private void mobAttemptBreakBlock(Block block){
		World world = block.getWorld();
		Material type = block.getType();
		
		if (plugin.config.getStringList(Config.FEATURE_BREAK_BLOCKS_BLOCKS).contains(type.name().toUpperCase()) && block.getWorld().getTime() % 10 == 0){
			Location location = block.getLocation();
			
			if (this.rand.nextInt(100) < 75){
				world.playEffect(location, Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
			}else{
				world.playEffect(location, Effect.ZOMBIE_DESTROY_DOOR, 0);
				
				if (plugin.config.getBoolean(Config.FEATURE_BREAK_BLOCKS_REALISTIC_DROP)){
					block.breakNaturally();
				}else{
					if (type != Material.AIR){
						block.setType(Material.AIR);
						world.dropItemNaturally(location, new ItemStack(type, 1));
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreeperMoveEvent(CreeperMoveEvent event){
		if (this.mobList == null || mobList.contains("CREEPER") == false) return;
		
		LivingEntity target = event.getTarget();
		Creeper creeper = event.getCreeper();
		
		if (target instanceof Player && plugin.isActive(creeper.getWorld())){
			Block[] blocks = new Block[2];
			
			try{
				blocks[0] = creeper.getTargetBlock(null, 1);
				blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			}catch (Exception e){
				return;
			}
			
			for (Block block : blocks){
				this.mobAttemptBreakBlock(block);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSkeletonMoveEvent(SkeletonMoveEvent event){
		if (this.mobList == null || mobList.contains("SKELETON") == false) return;
		
		LivingEntity target = event.getTarget();
		Skeleton skeleton = event.getSkeleton();
		
		if (target instanceof Player && plugin.isActive(skeleton.getWorld())){
			Block[] blocks = new Block[2];
			
			try{
				blocks[0] = skeleton.getTargetBlock(null, 1);
				blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			}catch (Exception e){
				return;
			}
			
			for (Block block : blocks){
				this.mobAttemptBreakBlock(block);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSpiderMoveEvent(SpiderMoveEvent event){
		if (this.mobList == null || mobList.contains("SPIDER") == false) return;
		
		LivingEntity target = event.getTarget();
		Spider spider = event.getSpider();
		
		if (target instanceof Player && plugin.isActive(spider.getWorld())){
			Block[] blocks = new Block[2];
			
			try{
				blocks[0] = spider.getTargetBlock(null, 1);
				blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			}catch (Exception e){
				return;
			}
			
			for (Block block : blocks){
				this.mobAttemptBreakBlock(block);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onZombieMoveEvent(ZombieMoveEvent event){
		if (this.mobList == null || mobList.contains("ZOMBIE") == false) return;
		
		LivingEntity target = event.getTarget();
		Zombie zombie = event.getZombie();
		
		if (target instanceof Player && plugin.isActive(zombie.getWorld())){
			Block[] blocks = new Block[2];
			
			try{
				blocks[0] = zombie.getTargetBlock(null, 1);
				blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			}catch (Exception e){
				return;
			}
			
			for (Block block : blocks){
				this.mobAttemptBreakBlock(block);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEndermanMoveEvent(EndermanMoveEvent event){
		if (this.mobList == null || mobList.contains("ENDERMAN") == false) return;
		
		LivingEntity target = event.getTarget();
		Enderman enderman = event.getEnderman();
		
		if (target instanceof Player && plugin.isActive(enderman.getWorld())){
			Block[] blocks = new Block[3];
			
			try{
				blocks[0] = enderman.getTargetBlock(null, 1);
				blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
				blocks[2] = blocks[1].getRelative(BlockFace.DOWN);
			}catch (Exception e){
				return;
			}
			
			for (Block block : blocks){
				this.mobAttemptBreakBlock(block);
			}
		}
	}
	
}
