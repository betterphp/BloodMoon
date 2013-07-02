package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.Collections;
import java.util.List;

import net.minecraft.server.v1_6_R1.EntityCreature;
import net.minecraft.server.v1_6_R1.EntityLiving;
import net.minecraft.server.v1_6_R1.IEntitySelector;
import net.minecraft.server.v1_6_R1.PathfinderGoalTarget;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class PathfinderGoalNearestAttackableTarget extends PathfinderGoalTarget {
	
	private BloodMoon plugin;
	
	private final Class<?> a;
	private final int b;
	private final DistanceComparator e;
	private IEntitySelector f;
	private EntityLiving g;
	
	public PathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityCreature entity, Class<?> class1, int i, boolean flag, boolean flag1, IEntitySelector ientityselector){
		super(entity, flag, flag1);
		
		this.plugin = plugin;
		
		this.a = class1;
		this.b = i;
		this.e = new DistanceComparator(entity);
		this.f = new EntitySelectorNearestAttackableTarget(this, ientityselector);
		
		this.a(1);
	}
	
	public PathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityCreature entity, Class<?> class1, int i, boolean flag, boolean flag1){
		this(plugin, entity, class1, i, flag, flag1, null);
	}
	
	public PathfinderGoalNearestAttackableTarget(BloodMoon plugin, EntityCreature entity, Class<?> class1, int i, boolean flag){
		this(plugin, entity, class1, i, flag, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean a(){
		if (this.b > 0 && this.c.aB().nextInt(this.b) != 0){
			return false;
		}
		
		String worldName = this.c.world.worldData.getName();
		String entityName = this.c.getBukkitEntity().getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		double distance = this.f();
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.TARGET_DISTANCE) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
			distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
		}
		
		@SuppressWarnings("rawtypes")
		List localList = this.c.world.a(this.a, this.c.boundingBox.grow(distance, 4.0D, distance), this.f);
		Collections.sort(localList, this.e);
		
		if (localList.isEmpty()){
			return false;
		}
		
		this.g = ((EntityLiving) localList.get(0));
		
		return true;
	}
	
	@Override
	public void c(){
		this.c.setGoalTarget(this.g);
		super.c();
	}
	
	@Override
	public boolean a(EntityLiving entityliving, boolean flag){
		return super.a(entityliving, flag);
	}
	
}
