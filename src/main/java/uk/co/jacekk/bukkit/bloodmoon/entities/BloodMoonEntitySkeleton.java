package uk.co.jacekk.bukkit.bloodmoon.entities;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_5.CraftServer;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftSkeleton;
import org.bukkit.entity.Skeleton;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.events.SkeletonMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonNavigation;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonPathfinderGoalArrowAttack;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonPathfinderGoalNearestAttackableTarget;

import net.minecraft.server.v1_4_5.Block;
import net.minecraft.server.v1_4_5.Enchantment;
import net.minecraft.server.v1_4_5.EnchantmentManager;
import net.minecraft.server.v1_4_5.Entity;
import net.minecraft.server.v1_4_5.EntityArrow;
import net.minecraft.server.v1_4_5.EntityHuman;
import net.minecraft.server.v1_4_5.EntityLiving;
import net.minecraft.server.v1_4_5.IRangedEntity;
import net.minecraft.server.v1_4_5.Item;
import net.minecraft.server.v1_4_5.ItemStack;
import net.minecraft.server.v1_4_5.PathfinderGoal;
import net.minecraft.server.v1_4_5.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_4_5.PathfinderGoalFleeSun;
import net.minecraft.server.v1_4_5.PathfinderGoalFloat;
import net.minecraft.server.v1_4_5.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_4_5.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_4_5.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_4_5.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_4_5.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_4_5.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_4_5.PathfinderGoalRestrictSun;
import net.minecraft.server.v1_4_5.World;
import net.minecraft.server.v1_4_5.WorldProviderHell;

public class BloodMoonEntitySkeleton extends net.minecraft.server.v1_4_5.EntitySkeleton implements IRangedEntity {
	
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
		
		if (this.plugin.config.getBoolean(Config.FEATURE_MOVEMENT_SPEED_ENABLED) && this.plugin.config.getStringList(Config.FEATURE_MOVEMENT_SPEED_MOBS).contains("SKELETON")){
			try{
				Field navigation = EntityLiving.class.getDeclaredField("navigation");
				navigation.setAccessible(true);
				navigation.set(this, new BloodMoonNavigation(this.plugin, this, this.world, 16.0f));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		try{
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();
			
			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
			
	        this.goalSelector.a(1, new PathfinderGoalFloat(this));
	        this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
	        this.goalSelector.a(3, new PathfinderGoalFleeSun(this, this.bG));
	        // NOTE: See bD() below
	        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, this.bG));
	        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
	        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
	        
	        if (this.plugin.config.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && this.plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("SKELETON")){
	        	this.targetSelector.a(2, new BloodMoonPathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 16.0F, 0, true));
	        }else{
	        	this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
	        }
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void bG(){
		if ((this.world.worldProvider instanceof WorldProviderHell) && aB().nextInt(5) > 0){
			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityHuman.class, this.bG, false));
			this.setSkeletonType(1);
			this.setEquipment(0, new ItemStack(Item.STONE_SWORD));
		}else{
			if (this.plugin.config.getBoolean(Config.FEATURE_ARROW_RATE_ENABLED)){
	        	this.goalSelector.a(4, new BloodMoonPathfinderGoalArrowAttack(this.plugin, this, this.bG, 60, 10.0f));
	        }else{
	        	this.goalSelector.a(4, new PathfinderGoalArrowAttack(this, this.bG, 60, 10.0f));
	        }
			
			this.bE();
			this.bF();
		}
		
		this.canPickUpLoot = (this.random.nextFloat() < as[this.world.difficulty]);
		
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
		Skeleton skeleton = (Skeleton) this.getBukkitEntity();
		
		Location from = new Location(skeleton.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(skeleton.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		SkeletonMoveEvent event = new SkeletonMoveEvent(skeleton, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && !skeleton.isDead()){
			return;
		}
		
		super.j_();
	}
	
	@Override
	protected Entity findTarget(){
		float distance = (plugin.isActive(this.world.worldData.getName()) && plugin.config.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains("SKELETON")) ? plugin.config.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER) * 16.0f : 16.0f;
		
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
		
		if (EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, bD()) > 0 || getSkeletonType() == 1 || (plugin.isActive(this.world.worldData.getName()) && plugin.config.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED))){
			entityarrow.setOnFire(1024);
		}
		
		this.world.makeSound(this, "random.bow", 1.0F, 1.0F / (aB().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entityarrow);
	}
	
	public void setEquipmentDropChance(int slot, float chance){
		this.dropChances[slot] = chance;
	}
	
}
