package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.List;

import org.bukkit.Material;
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
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.events.CreeperMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.EndermanMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.SkeletonMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.SpiderMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.ZombieMoveEvent;

public class BreakBlocksListener implements Listener {
	
	private BloodMoon plugin;
	private List<String> mobList;
	
	public BreakBlocksListener(BloodMoon instance){
		this.plugin = instance;
		
		if (BloodMoon.config != null){
			this.mobList = BloodMoon.config.getStringList("features.break-blocks.mobs");
		}
	}
	
	private void mobAttemptBreakBlock(Block block){
		if (block.getWorld().getTime() % 10 == 0 && plugin.rand.nextInt(100) < 50) return;
		
		List<String> blockList = BloodMoon.config.getStringList("features.break-blocks.blocks");
		Material type = block.getType();
		
		if (type != Material.AIR && blockList.contains(type.toString())){
			block.setType(Material.AIR);
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(type, 1));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreeperMoveEvent(CreeperMoveEvent event){
		if (event.isCancelled() || this.mobList == null || mobList.contains("CREEPER") == false) return;
		
		LivingEntity target = event.getTarget();
		Creeper creeper = event.getCreeper();
		
		if (target instanceof Player && BloodMoon.bloodMoonWorlds.contains(creeper.getWorld().getName())){
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSkeletonMoveEvent(SkeletonMoveEvent event){
		if (event.isCancelled() || this.mobList == null || mobList.contains("SKELETON") == false) return;
		
		LivingEntity target = event.getTarget();
		Skeleton skeleton = event.getSkeleton();
		
		if (target instanceof Player && BloodMoon.bloodMoonWorlds.contains(skeleton.getWorld().getName())){
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSpiderMoveEvent(SpiderMoveEvent event){
		if (event.isCancelled() || this.mobList == null || mobList.contains("SPIDER") == false) return;
		
		LivingEntity target = event.getTarget();
		Spider spider = event.getSpider();
		
		if (target instanceof Player && BloodMoon.bloodMoonWorlds.contains(spider.getWorld().getName())){
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onZombieMoveEvent(ZombieMoveEvent event){
		if (event.isCancelled() || this.mobList == null || mobList.contains("ZOMBIE") == false) return;
		
		LivingEntity target = event.getTarget();
		Zombie zombie = event.getZombie();
		
		if (target instanceof Player && BloodMoon.bloodMoonWorlds.contains(zombie.getWorld().getName())){
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEndermanMoveEvent(EndermanMoveEvent event){
		if (event.isCancelled() || this.mobList == null || mobList.contains("ENDERMAN") == false) return;
		
		LivingEntity target = event.getTarget();
		Enderman enderman = event.getEnderman();
		
		if (target instanceof Player && BloodMoon.bloodMoonWorlds.contains(enderman.getWorld().getName())){
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
