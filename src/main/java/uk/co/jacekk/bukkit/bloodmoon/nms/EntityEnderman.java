package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.EntityHuman;
import net.minecraft.server.v1_6_R1.Vec3D;
import net.minecraft.server.v1_6_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R1.CraftServer;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftEnderman;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityEndermen;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntityEnderman extends net.minecraft.server.v1_6_R1.EntityEnderman {
	
	private BloodMoon plugin;
	private BloodMoonEntityEndermen bloodMoonEntity;
	
	public int bt;
	public boolean bv;
	
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
	
	public boolean f(EntityHuman entityhuman){
		if (entityhuman.inventory.armor[3] != null && entityhuman.inventory.armor[3].id == Material.PUMPKIN.getId()){
			return false;
		}
		
		Vec3D vec3d = entityhuman.j(1.0F).a();
		Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX - entityhuman.locX, this.boundingBox.b + (this.length / 2.0F) - (entityhuman.locY + entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
		double d0 = vec3d1.b();
		vec3d1 = vec3d1.a();
		double d1 = vec3d.b(vec3d1);
		
		return (d1 > 1.0D - 0.025D / d0) ? entityhuman.o(this) : false;
	}
	
	@Override
	protected Entity findTarget(){
		return this.bloodMoonEntity.findTarget();
	}
	
}
