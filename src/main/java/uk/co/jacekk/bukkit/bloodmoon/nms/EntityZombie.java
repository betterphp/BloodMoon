package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.List;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.EntityHuman;
import net.minecraft.server.v1_6_R1.EntityLiving;
import net.minecraft.server.v1_6_R1.EntityVillager;
import net.minecraft.server.v1_6_R1.PathfinderGoalBreakDoor;
import net.minecraft.server.v1_6_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_6_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_6_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_6_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_6_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_6_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_6_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_6_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_6_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R1.CraftServer;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftZombie;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityZombie;

public class EntityZombie extends net.minecraft.server.v1_6_R1.EntityZombie {
	
	private BloodMoon plugin;
	private BloodMoonEntityZombie bloodMoonEntity;
	
	public EntityZombie(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftZombie((CraftServer) this.plugin.server, this);
		this.bloodMoonEntity = new BloodMoonEntityZombie(this.plugin, this, (CraftLivingEntity) this.bukkitEntity, BloodMoonEntityType.ZOMBIE);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new Navigation(this.plugin, this, this.world));
			
			this.getNavigation().b(true);
			
			ReflectionUtils.getFieldValue(this.goalSelector.getClass(), "a", List.class, this.goalSelector).clear();
			ReflectionUtils.getFieldValue(this.targetSelector.getClass(), "a", List.class, this.targetSelector).clear();
			
			this.goalSelector.a(0, new PathfinderGoalFloat(this));
			this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
			this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0d, false));
			this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0d, true));
			this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0d));
			this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, 1.0d, false));
			this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0d));
			this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
			
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 0, true));
			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this.plugin, this, EntityVillager.class, 0, false));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void l_(){
		try{
			this.bloodMoonEntity.onTick();
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
		
		return (entityhuman != null && this.o(entityhuman)) ? entityhuman : null;
	}
	
}
