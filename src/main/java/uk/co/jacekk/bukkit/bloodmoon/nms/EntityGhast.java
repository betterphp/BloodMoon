package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_6_R1.EntityLiving;
import net.minecraft.server.v1_6_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R1.CraftServer;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftGhast;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityGhast;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntityGhast extends net.minecraft.server.v1_6_R1.EntityGhast {
	
	private BloodMoon plugin;
	private BloodMoonEntityGhast bloodMoonEntity;
	
	public EntityGhast(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftGhast((CraftServer) this.plugin.server, this);
		this.bloodMoonEntity = new BloodMoonEntityGhast(this.plugin, this, (CraftLivingEntity) this.bukkitEntity, BloodMoonEntityType.GHAST);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new Navigation(this.plugin, this, this.world));
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
	
}
