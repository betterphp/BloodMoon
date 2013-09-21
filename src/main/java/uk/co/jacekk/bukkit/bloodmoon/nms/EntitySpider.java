package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_6_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftSpider;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntitySpider;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntitySpider extends net.minecraft.server.v1_6_R3.EntitySpider {
	
	private BloodMoon plugin;
	private BloodMoonEntitySpider bloodMoonEntity;
	
	public EntitySpider(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftSpider((CraftServer) this.plugin.server, this);
		this.bloodMoonEntity = new BloodMoonEntitySpider(this.plugin, this, (CraftLivingEntity) this.bukkitEntity, BloodMoonEntityType.SPIDER);
	}
	
	@Override
	public void bj(){
		try{
			this.bloodMoonEntity.onTick();
			super.bj();
		}catch (Exception e){
			plugin.log.warn("Exception caught while ticking entity");
			e.printStackTrace();
		}
	}
	
}
