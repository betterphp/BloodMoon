package uk.co.jacekk.bukkit.bloodmoon.pathfinders;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySkeleton;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.World;

public class BloodMoonPathfinderGoalArrowAttack extends PathfinderGoal {
	
	private BloodMoon plugin;
	
	private BloodMoonEntitySkeleton skeleton;
	private EntityLiving target;
	private World world;
    
	private int d = 0;
	private float e;
	private int f = 0;
	private int h;
	
	public BloodMoonPathfinderGoalArrowAttack(BloodMoonEntitySkeleton skeleton, BloodMoon plugin, float f, int j){
		this.plugin = plugin;
		
		this.skeleton = skeleton;
		this.world = skeleton.world;
		
		this.e = f;
		this.h = j;
		
		this.a(3);
	}
	
	public boolean a(){
		EntityLiving entityliving = this.skeleton.at();

		if (entityliving == null){
			return false;
		}else{
			this.target = entityliving;
			return true;
		}
	}
	
	public boolean b(){
		return this.a() || !this.skeleton.al().e();
	}
	
	public void d(){
		this.target = null;
	}
	
	public void e(){
		double d0 = 100.0D;
		double d1 = this.skeleton.e(this.target.locX, this.target.boundingBox.b, this.target.locZ);
		boolean flag = this.skeleton.am().canSee(this.target);
		
		if (flag){
			++this.f;
		}else{
			this.f = 0;
		}
		
		if (d1 <= d0 && this.f >= 20){
			this.skeleton.al().f();
		}else{
			this.skeleton.al().a(this.target, this.e);
		}
		
		this.skeleton.getControllerLook().a(this.target, 30.0F, 30.0F);
		this.d = Math.max(this.d - ((plugin.isActive(this.skeleton.world.worldData.name)) ? plugin.config.getInt(Config.FEATURE_ARROW_RATE_MULTIPLIER) : 1), 0);
		
		if (this.d <= 0 && d1 <= d0 && flag){
			this.f();
			this.d = this.h;
		}
	}
	
	private void f(){
		EntityArrow entityarrow = new EntityArrow(this.world, this.skeleton, this.target, 1.6F, 12.0F);
		
		this.world.makeSound(this.skeleton, "random.bow", 1.0F, 1.0F / (this.skeleton.an().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entityarrow);
		
		if (plugin.isActive(this.skeleton.world.worldData.name) && plugin.config.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED)){
			entityarrow.fireTicks = 1200;
		}
	}
	
}
