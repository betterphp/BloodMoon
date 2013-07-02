package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.List;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.EntityHuman;
import net.minecraft.server.v1_6_R1.EntityOcelot;
import net.minecraft.server.v1_6_R1.PathfinderGoalAvoidPlayer;
import net.minecraft.server.v1_6_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_6_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_6_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_6_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_6_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_6_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_6_R1.PathfinderGoalSwell;
import net.minecraft.server.v1_6_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R1.CraftServer;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityCreeper;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntityCreeper extends net.minecraft.server.v1_6_R1.EntityCreeper {
	
	private BloodMoon plugin;
	private BloodMoonEntityCreeper bloodMoonEntity;
	
	public EntityCreeper(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftCreeper((CraftServer) this.plugin.server, this);
		this.bloodMoonEntity = new BloodMoonEntityCreeper(this.plugin, this, (CraftLivingEntity) this.bukkitEntity, BloodMoonEntityType.CREEPER);
		
		try{
			ReflectionUtils.getFieldValue(this.goalSelector.getClass(), "a", List.class, this.goalSelector).clear();
			ReflectionUtils.getFieldValue(this.targetSelector.getClass(), "a", List.class, this.targetSelector).clear();
			
			this.goalSelector.a(1, new PathfinderGoalFloat(this));
			this.goalSelector.a(2, new PathfinderGoalSwell(this));
			this.goalSelector.a(3, new PathfinderGoalAvoidPlayer(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 0.25F, false));
			this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.2F));
			this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
			
			this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this.plugin, this, EntityHuman.class, 0, true));
			this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
		}catch (Exception e){
			e.printStackTrace();
			this.world.removeEntity(this);
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
		
		double distance = 16.0d;
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.TARGET_DISTANCE) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
			distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
		}
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, distance);
		
		return (entityhuman != null && this.o(entityhuman)) ? entityhuman : null;
	}
	
}