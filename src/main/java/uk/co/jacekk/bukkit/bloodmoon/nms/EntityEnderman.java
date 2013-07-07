package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_6_R1.World;

import org.bukkit.Bukkit;
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
	
}
