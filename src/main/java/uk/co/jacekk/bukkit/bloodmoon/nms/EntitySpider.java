package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftSpider;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntitySpider;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntitySpider extends net.minecraft.server.v1_5_R2.EntitySpider {
	
	private BloodMoon plugin;
	private BloodMoonEntitySpider bloodMoonEntity;
	
	public EntitySpider(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftSpider((CraftServer) this.plugin.server, this);
		this.bloodMoonEntity = new BloodMoonEntitySpider(this.plugin, this, (CraftLivingEntity) this.bukkitEntity, BloodMoonEntityType.SPIDER);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new Navigation(this.plugin, this, this.world, this.ay()));
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
		float f = this.c(1.0F);
		
		if (f < 0.5F){
			String worldName = this.world.worldData.getName();
			String entityName = this.getBukkitEntity().getType().name().toUpperCase();
			PluginConfig worldConfig = plugin.getConfig(worldName);
			
			float distance = 16.0f;
			
			if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
				distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
			}
			
			return this.world.findNearbyVulnerablePlayer(this, distance);
		}
		
		return null;
	}
	
}
