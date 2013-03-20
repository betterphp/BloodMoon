package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.v1_5_R1.Entity;
import net.minecraft.server.v1_5_R1.EntityHuman;
import net.minecraft.server.v1_5_R1.EntityLiving;
import net.minecraft.server.v1_5_R1.IEntitySelector;
import net.minecraft.server.v1_5_R1.PathfinderGoalTarget;
import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class PathfinderGoalNearestAttackableTarget extends PathfinderGoalTarget {
	
	private BloodMoon plugin;
	
	private EntityLiving a;
	private Class<?> b;
	private int c;
	private final IEntitySelector g;
	private DistanceComparator h;
	
	public PathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityLiving entityliving, Class<?> class1, float f, int i, boolean flag, boolean flag1, IEntitySelector ientityselector){
		super(entityliving, f, flag, flag1);
		
		this.plugin = plugin;
		
		this.b = class1;
		this.e = f;
		this.c = i;
		this.h = new DistanceComparator(entityliving);
		this.g = ientityselector;
		
		this.a(1);
	}
	
	public PathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityLiving entityliving, Class<?> class1, float f, int i, boolean flag, boolean flag1){
		this(plugin, entityliving, class1, f, i, flag, flag1, null);
	}
	
	public PathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityLiving entityliving, Class<?> class1, float f, int i, boolean flag){
		this(plugin, entityliving, class1, f, i, flag, false);
	}
	
	@Override
	public boolean a(){
		String worldName = this.d.world.worldData.getName();
		String entityName = this.d.getBukkitEntity().getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		float e = this.e;
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.TARGET_DISTANCE) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
			e *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
		}
		
		if (this.c > 0 && this.d.aE().nextInt(this.c) != 0){
			return false;
		}
		
		if (this.b == EntityHuman.class){
			EntityHuman entityhuman = this.d.world.findNearbyVulnerablePlayer(this.d, e);
			
			if (this.a(entityhuman, false)){
				this.a = entityhuman;
				return true;
			}
		}else{
			@SuppressWarnings("unchecked")
			List<Entity> list = this.d.world.a(this.b, this.d.boundingBox.grow(e, 4.0D, e), this.g);
			
			Collections.sort(list, this.h);
			Iterator<Entity> iterator = list.iterator();
			
			while (iterator.hasNext()){
				Entity entity = iterator.next();
				EntityLiving entityliving = (EntityLiving) entity;
				
				if (this.a(entityliving, false)){
					this.a = entityliving;
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void c(){
		this.d.setGoalTarget(this.a);
		super.c();
	}
	
}
