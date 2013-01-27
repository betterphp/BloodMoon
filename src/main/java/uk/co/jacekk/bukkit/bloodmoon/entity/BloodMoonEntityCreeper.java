package uk.co.jacekk.bukkit.bloodmoon.entity;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_4_R1.CraftServer;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftCreeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v8.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.nms.BloodMoonNavigation;
import uk.co.jacekk.bukkit.bloodmoon.nms.BloodMoonPathfinderGoalNearestAttackableTarget;

import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.EntityOcelot;
import net.minecraft.server.v1_4_R1.PathfinderGoal;
import net.minecraft.server.v1_4_R1.PathfinderGoalAvoidPlayer;
import net.minecraft.server.v1_4_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_4_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_4_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_4_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_4_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_4_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_4_R1.PathfinderGoalSwell;
import net.minecraft.server.v1_4_R1.World;

public class BloodMoonEntityCreeper extends net.minecraft.server.v1_4_R1.EntityCreeper {
	
	private BloodMoon plugin;
	
	@SuppressWarnings("unchecked")
	public BloodMoonEntityCreeper(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftCreeper((CraftServer) this.plugin.server, this);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new BloodMoonNavigation(this.plugin, this, this.world, 16.0f));
			
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();
			
			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
			
			this.goalSelector.a(1, new PathfinderGoalFloat(this));
			this.goalSelector.a(2, new PathfinderGoalSwell(this));
			this.goalSelector.a(3, new PathfinderGoalAvoidPlayer(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 0.25F, false));
			this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.2F));
			this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
			
			this.targetSelector.a(1, new BloodMoonPathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 16.0F, 0, true));
			this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
		}catch (Exception e){
			e.printStackTrace();
			this.world.removeEntity(this);
		}
	}
	
	@Override
	public void j_(){
		String worldName = this.world.worldData.getName();
		String entityName = this.getBukkitEntity().getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (this.target instanceof EntityHuman && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_ENABLED) && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains(entityName) && world.getTime() % 20 == 0 && this.world.worldData.getName().equals(this.target.world.worldData.getName())){
			LivingEntity bukkitEntity = ((LivingEntity) this.getBukkitEntity());
			Location direction = this.target.getBukkitEntity().getLocation().subtract(bukkitEntity.getLocation());
			
			double dx = direction.getX();
			double dz = direction.getY();
			
			int bdx = 0;
			int bdz = 0;
			
			if (Math.abs(dx) > Math.abs(dz)){
				bdx = (dx > 0) ? 1 : -1;
			}else{
				bdz = (dx > 0) ? 1 : -1;
			}
			
			Block[] blocks = new Block[2];
			
			blocks[0] = this.world.getWorld().getBlockAt((int) Math.floor(this.locX + bdx), (int) Math.floor(this.locY), (int) Math.floor(this.locZ + bdz));
			blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			
			for (Block block : blocks){
				Material type = block.getType();
				
				if (type != Material.AIR && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_BLOCKS).contains(type.name())){
					Location location = block.getLocation();
					
					if (this.random.nextInt(100) < 80){
						this.world.getWorld().playEffect(location, Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
					}else{
						EntityChangeBlockEvent event = new EntityChangeBlockEvent(bukkitEntity, block, Material.AIR, (byte) 0);
						plugin.pluginManager.callEvent(event);
						
						if (!event.isCancelled()){
							this.world.getWorld().playEffect(location, Effect.ZOMBIE_DESTROY_DOOR, 0);
							
							if (worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_REALISTIC_DROP)){
								block.breakNaturally();
							}else{
								block.setType(Material.AIR);
								
								if (worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_DROP_ITEMS)){
									this.world.getWorld().dropItemNaturally(location, new ItemStack(type, 1, block.getData()));
								}
							}
						}
					}
				}
			}
		}
		
		super.j_();
	}
	
	@Override
	protected Entity findTarget(){
		String worldName = this.world.worldData.getName();
		String entityName = this.getBukkitEntity().getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		float distance = 16.0f;
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
			distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
		}
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, distance);
		
		return entityhuman != null && this.n(entityhuman) ? entityhuman : null;
	}
	
}