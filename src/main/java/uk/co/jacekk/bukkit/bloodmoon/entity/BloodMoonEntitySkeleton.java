package uk.co.jacekk.bukkit.bloodmoon.entity;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_4_6.CraftServer;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftSkeleton;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.v7.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v7.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.nms.BloodMoonNavigation;
import uk.co.jacekk.bukkit.bloodmoon.nms.BloodMoonPathfinderGoalArrowAttack;
import uk.co.jacekk.bukkit.bloodmoon.nms.BloodMoonPathfinderGoalNearestAttackableTarget;

import net.minecraft.server.v1_4_6.Block;
import net.minecraft.server.v1_4_6.Enchantment;
import net.minecraft.server.v1_4_6.EnchantmentManager;
import net.minecraft.server.v1_4_6.Entity;
import net.minecraft.server.v1_4_6.EntityArrow;
import net.minecraft.server.v1_4_6.EntityHuman;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.IRangedEntity;
import net.minecraft.server.v1_4_6.Item;
import net.minecraft.server.v1_4_6.ItemStack;
import net.minecraft.server.v1_4_6.PathfinderGoal;
import net.minecraft.server.v1_4_6.PathfinderGoalFleeSun;
import net.minecraft.server.v1_4_6.PathfinderGoalFloat;
import net.minecraft.server.v1_4_6.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_4_6.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_4_6.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_4_6.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_4_6.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_4_6.PathfinderGoalRestrictSun;
import net.minecraft.server.v1_4_6.World;
import net.minecraft.server.v1_4_6.WorldProviderHell;

public class BloodMoonEntitySkeleton extends net.minecraft.server.v1_4_6.EntitySkeleton implements IRangedEntity {
	
	private BloodMoon plugin;
	
	@SuppressWarnings("unchecked")
	public BloodMoonEntitySkeleton(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftSkeleton((CraftServer) this.plugin.server, this);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new BloodMoonNavigation(this.plugin, this, this.world, 16.0f));
			
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();
			
			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
			
			this.goalSelector.a(1, new PathfinderGoalFloat(this));
			this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
			this.goalSelector.a(3, new PathfinderGoalFleeSun(this, this.bH));
			// NOTE: See bD() below
			this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, this.bH));
			this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
			
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
			this.targetSelector.a(2, new BloodMoonPathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 16.0F, 0, true));
		}catch (Exception e){
			e.printStackTrace();
			this.world.removeEntity(this);
		}
	}
	
	@Override
	public void bG(){
		if ((this.world.worldProvider instanceof WorldProviderHell) && aB().nextInt(5) > 0){
			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityHuman.class, this.bH, false));
			this.setSkeletonType(1);
			this.setEquipment(0, new ItemStack(Item.STONE_SWORD));
		}else{
			this.goalSelector.a(4, new BloodMoonPathfinderGoalArrowAttack(this.plugin, this, this.bH, 60, 10.0f));
			
			this.bE();
			this.bF();
		}
		
		this.canPickUpLoot = (this.random.nextFloat() < at[this.world.difficulty]);
		
		if (getEquipment(4) == null){
			Calendar calendar = this.world.T();
			
			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.random.nextFloat() < 0.25F){
				setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F ? Block.JACK_O_LANTERN : Block.PUMPKIN));
				this.dropChances[4] = 0.0F;
			}
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
			
			org.bukkit.block.Block[] blocks = new org.bukkit.block.Block[2];
			
			blocks[0] = this.world.getWorld().getBlockAt((int) Math.floor(this.locX + bdx), (int) Math.floor(this.locY), (int) Math.floor(this.locZ + bdz));
			blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			
			for (org.bukkit.block.Block block : blocks){
				Material type = block.getType();
				
				if (type != Material.AIR && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_BLOCKS).contains(type.name())){
					Location location = block.getLocation();
					
					if (this.random.nextInt(100) < 80){
						this.world.getWorld().playEffect(location, Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
					}else{
						EntityChangeBlockEvent event = new EntityChangeBlockEvent(bukkitEntity, block, Material.AIR);
						plugin.pluginManager.callEvent(event);
						
						if (!event.isCancelled()){
							this.world.getWorld().playEffect(location, Effect.ZOMBIE_DESTROY_DOOR, 0);
							
							if (worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_REALISTIC_DROP)){
								block.breakNaturally();
							}else{
								block.setType(Material.AIR);
								this.world.getWorld().dropItemNaturally(location, new org.bukkit.inventory.ItemStack(type, 1, block.getData()));
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
	
	@Override
	public void d(EntityLiving entityLiving){
		EntityArrow entityarrow = new EntityArrow(this.world, this, entityLiving, 1.6F, 12.0F);
		
		int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, bD());
		int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, bD());
		
		if (i > 0){
			entityarrow.b(entityarrow.c() + i * 0.5D + 0.5D);
		}
		
		if (j > 0){
			entityarrow.a(j);
		}
		
		String worldName = this.world.worldData.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, bD()) > 0 || getSkeletonType() == 1 || (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED))){
			entityarrow.setOnFire(1024);
		}
		
		this.world.makeSound(this, "random.bow", 1.0F, 1.0F / (aB().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entityarrow);
	}
	
}
