package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.EntityLiving;
import net.minecraft.server.v1_6_R1.IEntitySelector;

public class EntitySelectorNearestAttackableTarget implements IEntitySelector {
	
	final IEntitySelector c;
	final PathfinderGoalNearestAttackableTarget d;
	
	public EntitySelectorNearestAttackableTarget(PathfinderGoalNearestAttackableTarget pathfindergoalnearestattackabletarget, IEntitySelector ientityselector){
		this.d = pathfindergoalnearestattackabletarget;
		this.c = ientityselector;
	}
	
	public boolean a(Entity entity){
		return !(entity instanceof EntityLiving) ? false : (this.c != null && !this.c.a(entity) ? false : this.d.a((EntityLiving) entity, false));
	}
	
}