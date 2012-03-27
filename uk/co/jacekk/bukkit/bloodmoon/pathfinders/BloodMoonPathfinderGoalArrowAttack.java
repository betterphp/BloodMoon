package uk.co.jacekk.bukkit.bloodmoon.pathfinders;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySkeleton;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.World;

public class BloodMoonPathfinderGoalArrowAttack extends PathfinderGoal {
	
	World world;
	BloodMoonEntitySkeleton skeleton;
	EntityLiving c;
	int d = 0;
	float e;
	int f = 0;
	int g;
	int h;
	
	public BloodMoonPathfinderGoalArrowAttack(BloodMoonEntitySkeleton skeleton, float f, int i, int j){
		this.skeleton = skeleton;
		this.world = skeleton.world;
		
		this.e = f;
		this.g = i;
		this.h = j;
		
		this.a(3);
	}
	
	public boolean a(){
		EntityLiving entityliving = this.skeleton.at();

		if (entityliving == null){
			return false;
		}else{
			this.c = entityliving;
			return true;
		}
	}
	
	public boolean b(){
		return this.a() || !this.skeleton.al().e();
	}
	
	public void d(){
		this.c = null;
	}
	
	public void e(){
		double d0 = 100.0D;
		double d1 = this.skeleton.e(this.c.locX, this.c.boundingBox.b, this.c.locZ);
		boolean flag = this.skeleton.am().canSee(this.c);
		
		if (flag){
			++this.f;
		}else{
			this.f = 0;
		}
		
		if (d1 <= d0 && this.f >= 20){
			this.skeleton.al().f();
		}else{
			this.skeleton.al().a(this.c, this.e);
		}
		
		this.skeleton.getControllerLook().a(this.c, 30.0F, 30.0F);
		this.d = Math.max(this.d - ((this.skeleton.bloodMoonState) ? BloodMoon.config.getInt("features.arrow-rate.multiplier") : 1), 0);
		
		if (this.d <= 0 && d1 <= d0 && flag){
			this.f();
			this.d = this.h;
		}
	}
	
	private void f(){
		EntityArrow entityarrow = new EntityArrow(this.world, this.skeleton, this.c, 1.6F, 12.0F);
		
		this.world.makeSound(this.skeleton, "random.bow", 1.0F, 1.0F / (this.skeleton.an().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entityarrow);
		
		if (this.skeleton.bloodMoonState && BloodMoon.config.getBoolean("features.fire-arrows.enabled")){
			entityarrow.fireTicks = 1200;
		}
	}
	
}
