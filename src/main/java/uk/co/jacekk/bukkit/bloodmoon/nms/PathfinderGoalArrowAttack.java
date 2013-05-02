package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_5_R3.Entity;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.IRangedEntity;
import net.minecraft.server.v1_5_R3.MathHelper;
import net.minecraft.server.v1_5_R3.PathfinderGoal;

import org.bukkit.craftbukkit.v1_5_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

public class PathfinderGoalArrowAttack extends PathfinderGoal {
	
	private BloodMoon plugin;
	
	private final EntityLiving entity;
	private final IRangedEntity rangedEntity;
	private EntityLiving target;
	
	private int d;
	private float e;
	private int f;
	private int g;
	private int h;
	private float i;
	private float j;
	
	public PathfinderGoalArrowAttack(BloodMoon plugin, IRangedEntity irangedentity, float f, int i, int j, float f1){
		if (!(irangedentity instanceof EntityLiving)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}
		
		this.plugin = plugin;
		
		this.rangedEntity = irangedentity;
		this.entity = ((EntityLiving) irangedentity);
		
		this.d = -1;
		this.e = f;
		this.f = 0;
        this.g = i;
        this.h = j;
        this.i = f1;
        this.j = f1 * f1;
		
		this.a(3);
	}
	
	public PathfinderGoalArrowAttack(BloodMoon plugin, IRangedEntity irangedentity, float f, int i, float f1){
		this(plugin, irangedentity, f, i, i, f1);
	}
	
	@Override
	public boolean a(){
		EntityLiving entityliving = this.entity.getGoalTarget();
		
		if (entityliving == null){
			return false;
		}
		
		this.target = entityliving;
		
		return true;
	}
	
	@Override
	public boolean b(){
		return (this.a()) || (!this.entity.getNavigation().f());
	}
	
	@Override
	public void d(){
		EntityTargetEvent.TargetReason reason = this.target.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
		CraftEventFactory.callEntityTargetEvent((Entity) this.rangedEntity, null, reason);
		
		this.target = null;
		this.f = 0;
		this.d = -1;
	}
	
	@Override
	public void e(){
		double d0 = this.entity.e(this.target.locX, this.target.boundingBox.b, this.target.locZ);
		boolean flag = this.entity.getEntitySenses().canSee(this.target);
		
		if (flag){
			this.f += 1;
		}else{
			this.f = 0;
		}
		
		if (d0 > this.j || this.f < 20){
			this.entity.getNavigation().a(this.target, this.e);
		}else{
			this.entity.getNavigation().g();
		}
		
		this.entity.getControllerLook().a(this.target, 30.0F, 30.0F);
		
		String worldName = this.entity.world.worldData.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		this.d = Math.max(this.d - ((plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.ARROW_RATE)) ? worldConfig.getInt(Config.FEATURE_ARROW_RATE_MULTIPLIER) : 1), 0);
		
		float f;
		
		if (this.d == 0){
			if (d0 > this.j || !flag){
				return;
			}
			
			f = MathHelper.sqrt(d0) / this.i;
			float f1 = f;
			
			if (f < 0.1F){
				f1 = 0.1F;
			}
			
			if (f1 > 1.0F) {
				f1 = 1.0F;
			}
			
			this.rangedEntity.a(this.target, f1);
			this.d = MathHelper.d(f * (this.h - this.g) + this.g);
		} else if (this.d < 0) {
			f = MathHelper.sqrt(d0) / this.i;
			this.d = MathHelper.d(f * (this.h - this.g) + this.g);
		}
	}
	
}
