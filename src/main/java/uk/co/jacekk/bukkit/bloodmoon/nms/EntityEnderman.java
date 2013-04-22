package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.EntityHuman;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.Vec3D;
import net.minecraft.server.v1_5_R2.World;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEnderman;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftLivingEntity;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityEndermen;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntityEnderman extends net.minecraft.server.v1_5_R2.EntityEnderman {
	
	private BloodMoon plugin;
	private BloodMoonEntityEndermen bloodMoonEntity;
	
	private int f = 0;
	
	public EntityEnderman(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftEnderman((CraftServer) this.plugin.server, this);
		this.bloodMoonEntity = new BloodMoonEntityEndermen(this.plugin, this, (CraftLivingEntity) this.bukkitEntity, BloodMoonEntityType.ENDERMAN);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new Navigation(this.plugin, this, this.world, this.ay()));
		}catch (Exception e){
			e.printStackTrace();
			this.world.removeEntity(this);
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
	
	private boolean d(EntityHuman entityhuman){
		if (entityhuman.inventory.armor[3] != null && entityhuman.inventory.armor[3].id == Material.PUMPKIN.getId()){
			return false;
		}
		
		Vec3D vec3d = entityhuman.i(1.0F).a();
		Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX - entityhuman.locX, this.boundingBox.b + (this.length / 2.0F) - (entityhuman.locY + entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
		double d0 = vec3d1.b();
		
		vec3d1 = vec3d1.a();
		double d1 = vec3d.b(vec3d1);
		
		return d1 > 1.0D - 0.025D / d0 ? entityhuman.n(this) : false;
	}
	
	@Override
	protected Entity findTarget(){
		String worldName = this.world.worldData.getName();
		String entityName = this.getBukkitEntity().getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		float distance = 64.0f;
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
			distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
		}
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, distance);
		
		if (entityhuman != null){
			if (this.d(entityhuman)){
				if (this.f == 0){
					this.world.makeSound(entityhuman, "mob.endermen.stare", 1.0F, 1.0F);
				}
				
				if (this.f++ == 5){
					this.f = 0;
					this.f(true);
					return entityhuman;
				}
			}else{
				this.f = 0;
			}
		}
		
		return null;
	}
	
}
