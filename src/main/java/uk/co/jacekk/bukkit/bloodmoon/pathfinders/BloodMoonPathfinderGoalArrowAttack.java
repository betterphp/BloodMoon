package uk.co.jacekk.bukkit.bloodmoon.pathfinders;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IRangedEntity;
import net.minecraft.server.PathfinderGoal;

public class BloodMoonPathfinderGoalArrowAttack extends PathfinderGoal {
	
	private BloodMoon plugin;
	
	private final EntityLiving entity;
	private final IRangedEntity rangedEntity;
	private EntityLiving target;
	
	private int d = 0;
	private float e;
	private int f = 0;
	private int g;
	private float h;
	
	public BloodMoonPathfinderGoalArrowAttack(BloodMoon plugin, IRangedEntity irangedentity, float f, int i, float f1){
		if (!(irangedentity instanceof EntityLiving)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}
		
		this.plugin = plugin;
		
		this.rangedEntity = irangedentity;
		this.entity = ((EntityLiving) irangedentity);
		
		this.e = f;
		this.g = i;
		this.h = (f1 * f1);
		
		a(3);
	}
	
	@Override
	public boolean a(){
		EntityLiving entityliving = this.entity.aF();
		
		if (entityliving == null){
			return false;
		}
		
		this.target = entityliving;
		
		return true;
	}
	
	public boolean b(){
		return (a()) || (!this.entity.getNavigation().f());
	}
	
	public void d(){
		EntityTargetEvent.TargetReason reason = this.target.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
		CraftEventFactory.callEntityTargetEvent((Entity) this.rangedEntity, null, reason);
		
		this.target = null;
		this.f = 0;
	}
	
	public void e(){
		double d0 = this.entity.e(this.target.locX, this.target.boundingBox.b, this.target.locZ);
		boolean flag = this.entity.az().canSee(this.target);
		
		if (flag){
			this.f += 1;
		}else{
			this.f = 0;
		}
		
		if ((d0 <= this.h) && (this.f >= 20)){
			this.entity.getNavigation().g();
		}else{
			this.entity.getNavigation().a(this.target, this.e);
		}
		
		this.entity.getControllerLook().a(this.target, 30.0F, 30.0F);
		this.d = Math.max(this.d - ((plugin.isActive(this.entity.world.worldData.getName()) && plugin.config.getBoolean(Config.FEATURE_ARROW_RATE_ENABLED)) ? plugin.config.getInt(Config.FEATURE_ARROW_RATE_MULTIPLIER) : 1), 0);
		
		if ((this.d <= 0) && (d0 <= this.h) && (flag)){
			this.rangedEntity.d(this.target);
			this.d = this.g;
		}
	}
	
}
