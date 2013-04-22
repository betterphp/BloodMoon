package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.Calendar;
import java.util.List;

import net.minecraft.server.v1_5_R2.Block;
import net.minecraft.server.v1_5_R2.Enchantment;
import net.minecraft.server.v1_5_R2.EnchantmentManager;
import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.EntityArrow;
import net.minecraft.server.v1_5_R2.EntityHuman;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.IRangedEntity;
import net.minecraft.server.v1_5_R2.Item;
import net.minecraft.server.v1_5_R2.ItemStack;
import net.minecraft.server.v1_5_R2.PathfinderGoalFleeSun;
import net.minecraft.server.v1_5_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_5_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_5_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_5_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_5_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_5_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_5_R2.PathfinderGoalRestrictSun;
import net.minecraft.server.v1_5_R2.World;
import net.minecraft.server.v1_5_R2.WorldProviderHell;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftSkeleton;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntitySkeleton;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntitySkeleton extends net.minecraft.server.v1_5_R2.EntitySkeleton implements IRangedEntity {
	
	private BloodMoon plugin;
	private BloodMoonEntitySkeleton bloodMoonEntity;
	
	public EntitySkeleton(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftSkeleton((CraftServer) this.plugin.server, this);
		this.bloodMoonEntity = new BloodMoonEntitySkeleton(this.plugin, this, (CraftLivingEntity) this.bukkitEntity, BloodMoonEntityType.SKELETON);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new Navigation(this.plugin, this, this.world, this.ay()));
			
			ReflectionUtils.getFieldValue(this.goalSelector.getClass(), "a", List.class, this.goalSelector).clear();
			ReflectionUtils.getFieldValue(this.targetSelector.getClass(), "a", List.class, this.targetSelector).clear();
			
			this.goalSelector.a(1, new PathfinderGoalFloat(this));
			this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
			this.goalSelector.a(3, new PathfinderGoalFleeSun(this, this.bI));
			// NOTE: See bJ() below
			this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, this.bI));
			this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
			
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 16.0F, 0, true));
		}catch (Exception e){
			e.printStackTrace();
			this.world.removeEntity(this);
		}
	}
	
	@Override
	public void bJ(){
		if (this.world.worldProvider instanceof WorldProviderHell && this.aE().nextInt(5) > 0){
			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityHuman.class, this.bI, false));
			this.setSkeletonType(1);
			this.setEquipment(0, new ItemStack(Item.STONE_SWORD));
		}else{
			this.goalSelector.a(4, new PathfinderGoalArrowAttack(this.plugin, this, this.bI, 60, 10.0f));
			
			this.bH();
			this.bI();
		}
		
		this.h(this.random.nextFloat() < au[this.world.difficulty]);
		
		if (getEquipment(4) == null){
			Calendar calendar = this.world.U();
			
			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.random.nextFloat() < 0.25F){
				setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F ? Block.JACK_O_LANTERN : Block.PUMPKIN));
				this.dropChances[4] = 0.0F;
			}
		}
	}
	
	@Override
	public void l_(){
		this.bloodMoonEntity.onTick();
		
		try{
			super.l_();
		}catch (Exception e){
			plugin.log.warn("Exception caught while ticking entity");
			e.printStackTrace();
		}
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
	public void a(EntityLiving entityLiving, float f){
		EntityArrow entityarrow = new EntityArrow(this.world, this, entityLiving, 1.6F, 14 - this.world.difficulty * 4);
		
		int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, this.bG());
		int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, this.bG());
		
		entityarrow.b(f * 2.0F + this.random.nextGaussian() * 0.25D + this.world.difficulty * 0.11F);
		
		if (i > 0){
			entityarrow.b(entityarrow.c() + i * 0.5D + 0.5D);
		}
		
		if (j > 0){
			entityarrow.a(j);
		}
		
		String worldName = this.world.worldData.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, this.bG()) > 0 || getSkeletonType() == 1 || (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED) && (this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_FIRE_ARROWS_CHANCE)))){
			entityarrow.setOnFire(1024);
		}
		
		this.world.makeSound(this, "random.bow", 1.0F, 1.0F / (this.aE().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entityarrow);
	}
	
}